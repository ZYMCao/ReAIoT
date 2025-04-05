package cn.easttrans.reaiot.agentic.service.chat;

import org.springframework.ai.chat.messages.Message;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface ChatService {
    Flux<ServerSentEvent<String>> dialog(String userId, String dialogId, String userMsg);
    Flux<ServerSentEvent<String>> dialog(String userId, String dialogId, String systemMsg, String userMsg);

    Mono<Message[]> getMemory(String sessionId, int lastN);

    Mono<Void> clearMemory(String userId, String sessionId);

    Mono<Set<String>> getSessions(String userId);
}
