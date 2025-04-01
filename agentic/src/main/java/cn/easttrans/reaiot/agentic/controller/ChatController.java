package cn.easttrans.reaiot.agentic.controller;

import cn.easttrans.reaiot.agentic.domain.chat.Question;
import cn.easttrans.reaiot.agentic.service.chat.DefaultChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

import static cn.easttrans.reaiot.agentic.EnvironmentalConstants.OPEN_AI.SYS_PROMPT_ENV;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
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
    private final Resource systemPrompt;
    private final DefaultChatService chatService;

    @Autowired
    public ChatController(@Value(SYS_PROMPT_ENV) Resource systemPrompt,
                          DefaultChatService chatService) {
        this.systemPrompt = systemPrompt;
        this.chatService = chatService;
    }

    @PostMapping(value = "/dialog/{userId}/{sessionId}", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> dialog(@PathVariable String userId, @PathVariable String sessionId, @RequestBody Question question) {
        if (null == question || question.user().isEmpty() || question.user().isBlank()) {
            throw new IllegalArgumentException("用户的提问(字段user)不能为空！");
        }
        return chatService.dialog(userId, sessionId, question.system(), question.user());
    }

    @GetMapping(value = "/getMemory/{userId}/{sessionId}")
    public List<Message> getMemory(@PathVariable String userId, @PathVariable String sessionId) {
        return chatService.getMemory(sessionId, 200);
    }

    @GetMapping(value = "/getSessions/{userId}", produces = APPLICATION_JSON_VALUE)
    public Flux<String> getSessions(@PathVariable String userId) {
        return chatService.getUserSessions(userId);
    }
}
