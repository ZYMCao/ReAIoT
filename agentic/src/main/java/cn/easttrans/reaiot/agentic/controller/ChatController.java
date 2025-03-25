package cn.easttrans.reaiot.agentic.controller;

import cn.easttrans.reaiot.agentic.service.beamconstruction.BeamMtlService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static cn.easttrans.reaiot.agentic.EnvironmentalConstants.OPEN_AI.BASE_URL_ENV;
import static cn.easttrans.reaiot.agentic.EnvironmentalConstants.OPEN_AI.SYS_PROMPT_ENV;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;
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
    private final String urlLLM;
    private final String LLM_CALL_ERROR;
    private final ChatClient chatClient;

    @Autowired
    public ChatController(@Value(SYS_PROMPT_ENV) Resource systemPrompt,
                          @Value(BASE_URL_ENV) String urlLLM,
                          ChatModel chatModel,
                          ChatMemory chatMemory) {
        this.systemPrompt = systemPrompt;
        this.urlLLM = urlLLM;
        this.LLM_CALL_ERROR = "Fail to connect to " + urlLLM + "!!";
        this.chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory))
//                .defaultTools(beamMtlService)
                .build();
    }

    private record Question(String user) {
    }

    @PostMapping(value = "/dialog/{dialogId}", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> dialog(@PathVariable String dialogId, @RequestBody Question question) {
        log.info("Dialog {} asked: {}", dialogId, question.user());

        return chatClient.prompt()
                .user(question.user())
                .system(systemPrompt)
                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, dialogId).param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                .stream()
                .content()
                .switchIfEmpty(Mono.fromRunnable(() -> log.warn(LLM_CALL_ERROR)).then(Mono.error(new RuntimeException(LLM_CALL_ERROR))))
                .doOnNext(log::debug)
                .map(content -> ServerSentEvent.builder(content).event("message").build())
                .concatWithValues(ServerSentEvent.builder("[DONE]").build())
                .onErrorResume(e -> Flux.just(ServerSentEvent.builder("Error: " + e.getMessage()).event("error").build()));
    }
}
