package cn.easttrans.reaiot.agentic.service.chat;

import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

public interface ChatService {
    Flux<ServerSentEvent<String>> dialog(String conversationId, String userMsg);
}
