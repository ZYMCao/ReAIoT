package cn.easttrans.reaiot.agentic.controller;

import cn.easttrans.reaiot.agentic.domain.dto.chat.Question;
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
import java.util.Set;

import static cn.easttrans.reaiot.agentic.EnvironmentalConstants.OPEN_AI.SYS_PROMPT_ENV;
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

    @PostMapping(value = "/dialog/{userId}/{dialogId}", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> dialog(@PathVariable String userId, @PathVariable String dialogId, @RequestBody Question question) {
        return question.system() == null ?
                chatService.dialog(userId, dialogId, this.systemPrompt, question.user()) :
                chatService.dialog(userId, dialogId, question.system(), question.user());
    }

    @GetMapping(value = "/getMemory/{userId}/{dialogId}")
    public List<Message> getMemory(@PathVariable String userId, @PathVariable String dialogId) {
        return chatService.getMemory(userId, dialogId, 200);
    }

    @GetMapping(value = "/getConversationName")
    public String getConversationName(@RequestBody Question question) {
        return chatService.getConversationName(question.user());
    }

    @GetMapping(value = "/getSessions/{userId}")
    public Set<String> getUserSessions(@PathVariable String userId) {
        return chatService.getUserSessions(userId);
    }
}
