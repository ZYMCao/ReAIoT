package cn.easttrans.reaiot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

/**
 * @Description:
 * @Author: ZhanyingCao
 * @CreateDate: 2025/3/19 14:10
 **/
@RequestMapping("/api/chat")
@RestController
@Slf4j
public class ChatController {
    private final Resource systemPrompt;
    private final ChatModel chatModel;
    private final String urlLLM;
    private final Map<String, ChatMemory> chatMemories = new ConcurrentHashMap<>();

    @Autowired
    public ChatController(@Value("classpath:/prompts/system-message.st") Resource systemPrompt,
                          @Value("${spring.ai.openai.base-url}") String urlLLM,
                          ChatModel chatModel) {
        this.systemPrompt = systemPrompt;
        this.chatModel = chatModel;
        this.urlLLM = urlLLM;
    }

    @PostMapping(value = "/dialog/{dialogId}", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> dialog(@PathVariable String dialogId, @RequestParam String question) {
        ChatMemory chatMemory = chatMemories.computeIfAbsent(dialogId, id -> new InMemoryChatMemory());
        ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultSystem(systemPrompt)
                .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory))
                .build();

        String LLM_CALL_ERROR = "Fail to connect to " + urlLLM + " ...";
        return chatClient.prompt()
                .user(question)
                .system(systemPrompt)
                .stream()
                .content()
                .switchIfEmpty(Mono.fromRunnable(() -> log.warn(LLM_CALL_ERROR)).then(Mono.error(new RuntimeException(LLM_CALL_ERROR))))
                .doOnNext(log::debug)
                .map(content -> ServerSentEvent.builder(content).event("message").build())
                .concatWithValues(ServerSentEvent.builder("[DONE]").build())
                .onErrorResume(e -> Flux.just(ServerSentEvent.builder("Error: " + e.getMessage()).event("error").build()));
    }
}
