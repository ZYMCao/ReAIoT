package cn.easttrans.reaiot.agentic.service.chat;

import org.springframework.ai.chat.messages.Message;
import org.springframework.core.io.Resource;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

public interface ChatService {

    Flux<ServerSentEvent<String>> dialog(String conversationId, String systemMsg, String user);
    Flux<ServerSentEvent<String>> dialog(String conversationId, Resource systemMsg, String userMsg);

    List<Message> getMemory(String dialogId, int lastN);
}
