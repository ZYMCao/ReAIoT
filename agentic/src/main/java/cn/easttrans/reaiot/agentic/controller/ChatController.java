package cn.easttrans.reaiot.agentic.controller;

import cn.easttrans.reaiot.agentic.domain.dto.chat.Question;
import cn.easttrans.reaiot.agentic.service.chat.DefaultChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

/**
 * @Description:
 * @Author: ZhanyingCao
 * @CreateDate: 2025/3/19 14:10
 **/
@RequestMapping("/ai/chat")
@RestController
@Slf4j
public class ChatController {
    private final DefaultChatService chatService;

    @Autowired
    public ChatController(DefaultChatService chatService) {
        this.chatService = chatService;
    }
    @PostMapping(value = "/dialog/{userId}/{sessionId}", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> dialog(@PathVariable String userId, @PathVariable String sessionId, @RequestBody Question question) {
        if (null == question || question.user().isEmpty() || question.user().isBlank()) {
            throw new IllegalArgumentException("用户的提问不能为空！");
        }
        return chatService.dialog(userId, sessionId, question.user());
    }

    @GetMapping(value = "/getMemory/{userId}/{dialogId}")
    public Mono<Message[]> getMemory(@PathVariable String userId, @PathVariable String dialogId) {
        return chatService.getMemory(dialogId, 200);
    }

    @GetMapping(value = "/getConversationName")
    public String getConversationName(@RequestBody Question question) {
        return chatService.getConversationName(question.user());
    }

    @GetMapping(value = "/getSessions/{userId}")
    public Mono<Set<String>> getSessions(@PathVariable String userId) {
        return chatService.getSessions(userId);
    }
}
