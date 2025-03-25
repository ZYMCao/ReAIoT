package cn.easttrans.reaiot.agentic.service.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static cn.easttrans.reaiot.agentic.EnvironmentalConstants.OPEN_AI.BASE_URL_ENV;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Service
@Slf4j
public class DefaultChatService implements ChatService {
    private final String urlLLM;
    private final ChatClient chatClient;

    @Autowired
    public DefaultChatService(@Value(BASE_URL_ENV) String urlLLM,
                              ChatModel chatModel,
                              ChatMemory chatMemory) {
        this.urlLLM = urlLLM;
        this.chatClient = ChatClient.builder(chatModel).defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory)).build();
    }

    @Override
    public Flux<ServerSentEvent<String>> dialog(String conversationId, String userMsg) {
        String llmCallError = "Fail to connect to " + urlLLM + "!!";
        log.info("Dialog {} asked: {}", conversationId, userMsg);

        return chatClient.prompt()
                .user(userMsg)
                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, conversationId).param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                .stream()
                .content()
                .switchIfEmpty(Mono.fromRunnable(() -> log.warn(llmCallError)).then(Mono.error(new RuntimeException(llmCallError))))
                .doOnNext(log::debug)
                .map(content -> ServerSentEvent.builder(content).event("message").build())
                .concatWithValues(ServerSentEvent.builder("[DONE]").build())
                .onErrorResume(e -> Flux.just(ServerSentEvent.builder("Error: " + e.getMessage()).event("error").build()));
    }
}
