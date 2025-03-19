package cn.easttrans.reaiot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @Description:
 * @Author: ZhanyingCao
 * @CreateDate: 2025/1/24 13:26
 **/
@RequestMapping("/api/open-ai")
@RequiredArgsConstructor
@RestController
@Slf4j
public class OpenAIController {
    private final OpenAiChatModel openAiChatModel;

    @GetMapping("/userTextChat")
    public Mono<String> textChat(@RequestParam(value = "userText") String userText) {
        return Mono.fromCallable(() -> openAiChatModel.call(userText))
                .doOnNext(log::info)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/systemAndUserTextChat")
    public String textChatWithSystemMessage(@RequestParam(value = "systemText") String systemText,
                                            @RequestParam(value = "userText") String userText) {
        Message systemMessage = new SystemMessage(systemText);
        Message userMessage = new UserMessage(userText);
        return openAiChatModel.call(systemMessage, userMessage);
    }

    @PostMapping("/chat")
    public ChatDto sendMessage(@RequestBody ChatDto request) {
        return new ChatDto(this.openAiChatModel.call(request.message()));
    }

    public record ChatDto(String message) {
    }
}
