package cn.easttrans.reaiot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: ZhanyingCao
 * @CreateDate: 2025/3/19 14:10
 **/
@RequestMapping("/api/chat")
@RestController
@Slf4j
public class ChatController {
    private final ChatClient chatClient;

    @Autowired
    public ChatController(@Value("classpath:/prompts/system-message.st") Resource systemResource,
                          ChatModel chatModel,
                          ChatMemory chatMemory) {
        this.chatClient = ChatClient.builder(chatModel)
                .defaultSystem(systemResource)
                .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory))
                .build();
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam String userMessage) {
        return chatClient.prompt()
                .user(userMessage)
                .stream()
                .content()
                .doOnNext(log::info)
                .blockLast();
    }
}
