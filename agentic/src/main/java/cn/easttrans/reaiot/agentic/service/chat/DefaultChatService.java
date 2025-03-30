package cn.easttrans.reaiot.agentic.service.chat;

import cn.easttrans.reaiot.agentic.domain.dto.beamconstruction.MtlStoragePageRequest;
import cn.easttrans.reaiot.agentic.domain.exception.TrivialResponseError;
import cn.easttrans.reaiot.agentic.service.beamconstruction.AbstractBeamConstructionService;
import cn.easttrans.reaiot.agentic.service.beamconstruction.BeamMtlService;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
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
import org.springframework.core.io.Resource;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static cn.easttrans.reaiot.agentic.EnvironmentalConstants.KEY_SPACE_ENV;
import static cn.easttrans.reaiot.agentic.EnvironmentalConstants.OPEN_AI.BASE_URL_ENV;
import static cn.easttrans.reaiot.agentic.EnvironmentalConstants.OPEN_AI.NOMEN_PROMPT_ENV;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;
import static org.springframework.ai.chat.memory.cassandra.CassandraChatMemoryConfig.DEFAULT_KEYSPACE_NAME;
import static org.springframework.ai.chat.memory.cassandra.CassandraChatMemoryConfig.DEFAULT_TABLE_NAME;

@Service
@Slf4j
public class DefaultChatService implements ChatService {
    private final String urlLLM;
    private final Resource nameCreatorPrompt;
    private final String keySpace;
    private final ChatMemory chatMemory;
    private final ChatClient chatClient;
    private final CqlSession cqlSession;
    private final Cache<String, Set<String>> cache;
    private final BeamMtlService beamMtlService;
    private static final String ningyanPromptEnhanced =
            "You engage in conversation with your interlocutor on \"宁盐梁场\",\n" +
                    "a program, whose front end in vue and back end in springboot, on smart construction of beams for highways between 南京 and 盐城.\n" +
                    "\"宁盐梁场\" is about order placement, concrete pouring, curing, tensioning, storage, grouting, inspection, transportation, and erection of beams.\n" +
                    "Your reply shall always be in Mandarin Chinese." +
                    "Below is more info of \"宁盐梁场\":\n" +
                    "南京至盐城高速扬州段NY-YZ5标位于扬州市仪征、高邮境内，起点桩号为K35+000，终点桩号为K71+800，线路长39.729km。\n" +
                    "本标段主线桥梁共计32座，总长15745.44m，特大桥10054.48m/2座、大桥4263.04m/8座、中小桥1427.92m/22座。\n" +
                    "建设箱梁预制场一座，占地330亩，负责本标段及YZ6-11标箱梁预制并运输至相应标段临时存梁场。";

    @Autowired
    public DefaultChatService(@Value(BASE_URL_ENV) String urlLLM,
                              @Value(NOMEN_PROMPT_ENV) Resource nameCreatorPrompt,
                              @Value(KEY_SPACE_ENV) String keySpace,
                              BeamMtlService beamMtlService,
                              ChatMemory chatMemory,
                              ChatModel chatModel,
                              CqlSession cqlSession,
                              @Qualifier("userSessionsCache") Cache<String, Set<String>> cache) {
        this.urlLLM = urlLLM;
        this.nameCreatorPrompt = nameCreatorPrompt;
        this.keySpace = keySpace;
        this.chatMemory = chatMemory;
        this.beamMtlService = beamMtlService;
        this.chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory), new SimpleLoggerAdvisor())
//                .defaultTools(beamMtlService)
                .build();
        this.cqlSession = cqlSession;
        this.cache = cache;

//        this.initializeTable();
    }

    private static final String USER_SESSION_TABLE_NAME = "ai_chat_user";

    private void initializeTable() {
        try {
            boolean tableExists = cqlSession.execute(
                    String.format("SELECT table_name FROM system_schema.tables WHERE keyspace_name = '%s' AND table_name = '" + USER_SESSION_TABLE_NAME + "';", keySpace)
            ).one() != null;
            if (!tableExists) {
                log.warn("Table {} was not found, creating it ...", USER_SESSION_TABLE_NAME);
                cqlSession.execute(String.format(
                        "CREATE TABLE %s.ai_chat_user (" +
                                "   user_id text," +
                                "   session_id text," +
                                "   PRIMARY KEY (user_id, session_id)" +
                                ") WITH CLUSTERING ORDER BY (session_id DESC);",
                        keySpace));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize ai_chat_user table", e);
        }
    }

    public Flux<ServerSentEvent<String>> dialog(String userId, String dialogId, String systemMsg, String userMsg) {
        String conversationId = userId + ":" + dialogId;
        ChatClient.ChatClientRequestSpec chatClientRequestSpec = chatClient.prompt();
        if (!systemMsg.isEmpty()) {
            chatClientRequestSpec.system(systemMsg);
        }
        if (!userMsg.isEmpty()) {
            var refineUserMsg = userMsg.contains("宁盐梁场") ? "一些宁盐梁场的背景信息(如果对话历史中你已经给出过背景信息了，那就不要再给出了): " + ningyanPromptEnhanced : userMsg;
            refineUserMsg = userMsg.contains("物料入库") ? "根据API返回的数据，回答问题。API返回: \n" + Arrays.toString(beamMtlService.materialStorage(150)) + "\n 问题: \n" + refineUserMsg : refineUserMsg;
            chatClientRequestSpec.user(refineUserMsg);
            log.info("Dialog {} asked: {}", conversationId, refineUserMsg);
        }

        return getServerSentEventFlux(conversationId, "Fail to connect to " + urlLLM + "!!", chatClientRequestSpec);
    }

    public Flux<ServerSentEvent<String>> dialog(String userId, String dialogId, Resource systemMsg, String userMsg) {
        String conversationId = userId + ":" + dialogId;

        String tempSystemMsg = "You engage in conversation with your interlocutor on \"宁盐梁场\". Your reply shall always be in Mandarin Chinese.";
        Mono<ChatClient.ChatClientRequestSpec> chatClientRequestSpecMono = Mono.defer(() -> {
            if (userMsg.isEmpty()) {
                return Mono.just(chatClient.prompt().system(tempSystemMsg));
            } else {
                if (userMsg.contains("宁盐梁场")) {
                    String refinedUserMsg = "宁盐梁场背景信息..." + ningyanPromptEnhanced;
                    log.info("Dialog {} asked: {}", conversationId, refinedUserMsg);
                    return Mono.just(chatClient.prompt().system(tempSystemMsg).user(refinedUserMsg));
                } else if (userMsg.contains("物料入库")) {
                    return beamMtlService.materialStorage(new MtlStoragePageRequest(150))
                            .map(materials -> {
                                String refinedTempSystemMsg = tempSystemMsg + "根据API返回的数据(不用提及出处)，回答问题。\nAPI的返回: \n" + Arrays.toString(materials);
                                log.info("Dialog {} asked: {}", conversationId, userMsg);
                                return chatClient.prompt().system(refinedTempSystemMsg).user(userMsg);
                            });
                } else {
                    return Mono.just(chatClient.prompt().system(tempSystemMsg).user(userMsg));
                }
            }
        });

        return getServerSentEventFlux(conversationId, "Fail to connect to " + urlLLM + "!!", chatClientRequestSpecMono);
    }


    private Flux<ServerSentEvent<String>> getServerSentEventFlux(String conversationId, String llmCallError, ChatClient.ChatClientRequestSpec chatClientRequestSpec) {
        String[] parts = conversationId.split(":", 2);
        if (parts.length != 2) {
            return Flux.error(new IllegalArgumentException("Invalid conversationId format: " + conversationId));
        }
        String userId = parts[0];
        String sessionId = parts[1];

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
                        updatedSessions.add(sessionId);
                        return updatedSessions;
                    });
                    log.debug("Updated cache for userId={} with sessionId={}", userId, sessionId);
                })
                .onErrorResume(e -> {
                    log.error("Error while processing LLM stream", e);
                    return Flux.just(
                            ServerSentEvent.builder("Error: " + e.getMessage()).event("error").build(),
                            ServerSentEvent.builder("[DONE]").build()
                    );
                });
    }

    private Flux<ServerSentEvent<String>> getServerSentEventFlux(String conversationId, String llmCallError, Mono<ChatClient.ChatClientRequestSpec> chatClientRequestSpecMono) {
        String[] parts = conversationId.split(":", 2);
        if (parts.length != 2) {
            return Flux.error(new IllegalArgumentException("Invalid conversationId format: " + conversationId));
        }
        String userId = parts[0];
        String sessionId = parts[1];

        return chatClientRequestSpecMono.flatMapMany(chatClientRequestSpec ->
                chatClientRequestSpec
                        .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, conversationId)
                                .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
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
                                updatedSessions.add(sessionId);
                                return updatedSessions;
                            });
                            log.debug("Updated cache for userId={} with sessionId={}", userId, sessionId);
                        })
                        .onErrorResume(e -> {
                            log.error("Error while processing LLM stream", e);
                            return Flux.just(
                                    ServerSentEvent.builder("Error: " + e.getMessage()).event("error").build(),
                                    ServerSentEvent.builder("[DONE]").build()
                            );
                        })
        );
    }


    public void clearMemory(String sessionId) {
        this.chatMemory.clear(sessionId);
    }

    public List<Message> getMemory(String userId, String dialogId, int lastN) {
        String sessionId = userId + ":" + dialogId;
        return this.chatMemory.get(sessionId, lastN);
    }

    public String getConversationName(String userMsg) {
        if (null == userMsg || userMsg.isEmpty()) {
            return "";
        } else {
            return chatClient.prompt()
                    .system(this.nameCreatorPrompt)
                    .user(userMsg)
                    .call()
                    .content();
        }
    }

    public Set<String> getUserSessions(String userId) { // ToDo: do it reactively
        Set<String> cachedSessions = cache.getIfPresent(userId);
        if (cachedSessions != null) {
            log.trace("Current cache state: {}", cache.asMap());
            return cachedSessions;
        } else {
            PreparedStatement stmt = cqlSession.prepare("SELECT DISTINCT session_id FROM " + DEFAULT_KEYSPACE_NAME + "." + DEFAULT_TABLE_NAME);

            ResultSet rs = this.cqlSession.execute(stmt.bind());

            Map<String, Set<String>> sessionsByUser = new HashMap<>();
            rs.forEach(row -> {
                String sessionId = row.getString("session_id");
                if (null != sessionId) {
                    String[] parts = sessionId.split(":", 2);
                    if (parts.length == 2) {
                        sessionsByUser
                                .computeIfAbsent(parts[0], k -> new HashSet<>())
                                .add(parts[1]);
                    }

                }
            });
            sessionsByUser.forEach(cache::put);
            log.trace("Current cache state: {}", cache.asMap());
            return sessionsByUser.getOrDefault(userId, Collections.emptySet());
        }
    }
}
