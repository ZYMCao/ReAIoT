package cn.easttrans.reaiot.agentic.service.chat;

import cn.easttrans.reaiot.agentic.dao.nosql.ChatUserRepository;
import cn.easttrans.reaiot.agentic.domain.exception.TrivialResponseError;
import cn.easttrans.reaiot.agentic.domain.persistence.nosql.AIChatUser;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.easttrans.reaiot.agentic.EnvironmentalConstants.OPEN_AI.BASE_URL_ENV;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Service
@Slf4j
public class DefaultChatService implements ChatService {
    private final String urlLLM;
    private final ChatMemory chatMemory;
    private final ChatClient chatClient;
    private final Cache<String, Set<String>> cache;
    private final ChatUserRepository chatUserRepository;

    @Autowired
    public DefaultChatService(@Value(BASE_URL_ENV) String urlLLM,
                              ChatMemory chatMemory,
                              ChatModel chatModel,
                              @Qualifier("userSessionsCache") Cache<String, Set<String>> cache,
                              ChatUserRepository chatUserRepository) {
        this.urlLLM = urlLLM;
        this.chatMemory = chatMemory;
        this.chatClient = ChatClient.builder(chatModel).defaultAdvisors(
                new MessageChatMemoryAdvisor(chatMemory),
                new SimpleLoggerAdvisor()
        ).build();
        this.cache = cache;
        this.chatUserRepository = chatUserRepository;
    }

    public Flux<ServerSentEvent<String>> dialog(String userId, String dialogId, String systemMsg, String userMsg) {
        ChatClient.ChatClientRequestSpec chatClientRequestSpec = chatClient.prompt();
        chatClientRequestSpec.user(userMsg);
        if (null != systemMsg && !systemMsg.isEmpty() && !systemMsg.isBlank()) {
            chatClientRequestSpec.system(systemMsg);
        }

        return getServerSentEventFlux(userId, dialogId, "Fail to connect to " + urlLLM + "!!", chatClientRequestSpec);
    }

    private Flux<ServerSentEvent<String>> getServerSentEventFlux(String userId, String conversationId, String llmCallError, ChatClient.ChatClientRequestSpec chatClientRequestSpec) {
        return chatClientRequestSpec
                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, conversationId).param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 2))
                .stream()
                .content()
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Empty response from LLM - {}", llmCallError);
                    return Mono.error(new TrivialResponseError(llmCallError));
                }))
                .doOnNext(log::debug)
                .map(content -> ServerSentEvent.builder(content).event("message").build())
                .concatWithValues(ServerSentEvent.builder("[DONE]").build())
                .doOnComplete(() -> {
                    cache.asMap().compute(userId, (key, existingSessions) -> {
                        Set<String> updatedSessions = existingSessions != null ?
                                new HashSet<>(existingSessions) : new HashSet<>();
                        updatedSessions.add(conversationId);
                        return updatedSessions;
                    });
                    log.debug("Updated cache for userId={} with sessionId={}", userId, conversationId);

                    saveUserSession(userId, conversationId);
                })
                .onErrorResume(e -> {
                    log.error("Error while processing LLM stream", e);
                    return Flux.just(
                            ServerSentEvent.builder("Error: " + e.getMessage()).event("error").build(),
                            ServerSentEvent.builder("[DONE]").build()
                    );
                });
    }

    void saveUserSession(String userId, String conversationId) {
        chatUserRepository.save(new AIChatUser(userId, conversationId))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(
                        saved -> log.debug("Saved session to Cassandra: userId={}, sessionId={}", userId, conversationId),
                        error -> log.error("Failed to save session to Cassandra", error)
                );
    }

    public List<Message> getMemory(String sessionId, int lastN) {
        return this.chatMemory.get(sessionId, lastN);
    }

    public Mono<Set<String>> getUserSessions(String userId) {
        return Mono.just(userId)
                .mapNotNull(cache::getIfPresent) // 从缓存中拿
                .switchIfEmpty(getUserSessionsAbCassandra(userId)) // 从数据库中拿
                .subscribeOn(Schedulers.boundedElastic())
//                .flatMapMany(Flux::fromIterable)
                .doOnSubscribe(__ -> log.trace("Fetching sessions for user {}", userId))
//                .doOnComplete(() -> log.trace("Current cache state: {}", cache.asMap()))
                .onErrorResume(e -> {
                    log.error("Error fetching sessions for user {}", userId, e);
                    return Mono.empty();
                });
    }

    Mono<Set<String>> getUserSessionsAbCassandra(String userId) {
        return chatUserRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new TrivialResponseError("No session datum was fetched from Cassandra!!"))) // ToDO: properly handle trivial return
                .doOnNext(fetched -> log.trace("Cache miss for user {}, querying repository", userId))
                .map(AIChatUser::sessionId)
                .collect(Collectors.toSet())
                .doOnNext(sessions -> {
                    cache.put(userId, sessions);
                    log.trace("Updated cache for user {} with {}", userId, sessions);
                });
    }
}
