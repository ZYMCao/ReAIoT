package cn.easttrans.reaiot.agentic.service.chat;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static cn.easttrans.reaiot.agentic.EnvironmentalConstants.OPEN_AI.BASE_URL_ENV;
import static cn.easttrans.reaiot.agentic.EnvironmentalConstants.OPEN_AI.NOMEN_PROMPT_ENV;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Service
@Slf4j
public class DefaultChatService implements ChatService {
    private final String urlLLM;
    private final Resource nameCreatorPrompt;
    private final ChatMemory chatMemory;
    private final ChatClient chatClient;
    private final CqlSession cqlSession;

    @Autowired
    public DefaultChatService(@Value(BASE_URL_ENV) String urlLLM,
                              @Value(NOMEN_PROMPT_ENV) Resource nameCreatorPrompt,
                              ChatMemory chatMemory,
                              ChatModel chatModel,
                              CqlSession cqlSession) {
        this.urlLLM = urlLLM;
        this.nameCreatorPrompt = nameCreatorPrompt;
        this.chatMemory = chatMemory;
        this.chatClient = ChatClient.builder(chatModel).defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory)).build();
        this.cqlSession = cqlSession;
    }

    @Override
    public Flux<ServerSentEvent<String>> dialog(String conversationId, String systemMsg, String userMsg) {
        String llmCallError = "Fail to connect to " + urlLLM + "!!";
        log.info("Dialog {} asked: {}", conversationId, userMsg);
        ChatClient.ChatClientRequestSpec chatClientRequestSpec = chatClient.prompt();
        if (!systemMsg.isEmpty()) {
            chatClientRequestSpec.system(systemMsg);
        }
        if (!userMsg.isEmpty()) {
            chatClientRequestSpec.user(userMsg);
        }

        return getServerSentEventFlux(conversationId, llmCallError, chatClientRequestSpec);
    }

    @Override
    public Flux<ServerSentEvent<String>> dialog(String conversationId, Resource systemMsg, String userMsg) {
        String llmCallError = "Fail to connect to " + urlLLM + "!!";
        log.info("Dialog {} asked: {}", conversationId, userMsg);
        ChatClient.ChatClientRequestSpec chatClientRequestSpec = userMsg.isEmpty() ?
                chatClient.prompt().system(systemMsg) :
                chatClient.prompt().system(systemMsg).user(userMsg);

        return getServerSentEventFlux(conversationId, llmCallError, chatClientRequestSpec);
    }

    private Flux<ServerSentEvent<String>> getServerSentEventFlux(String conversationId, String llmCallError, ChatClient.ChatClientRequestSpec chatClientRequestSpec) {
        return chatClientRequestSpec
                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, conversationId).param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                .stream()
                .content()
                .switchIfEmpty(Mono.fromRunnable(() -> log.warn(llmCallError)).then(Mono.error(new RuntimeException(llmCallError))))
                .doOnNext(log::debug)
                .map(content -> ServerSentEvent.builder(content).event("message").build())
                .concatWithValues(ServerSentEvent.builder("[DONE]").build())
                .onErrorResume(e -> Flux.just(ServerSentEvent.builder("Error: " + e.getMessage()).event("error").build()));
    }

    @Override
    public List<Message> getMemory(String dialogId, int lastN) {
        return this.chatMemory.get(dialogId, lastN);
    }

    public String getConversationName(String userMsg) {
        if (userMsg.isEmpty()) {
            return "";
        } else {
            return chatClient.prompt()
                    .system(this.nameCreatorPrompt)
                    .user(userMsg)
                    .call()
                    .content();
        }
    }

    public Map<String, List<Message>> getAllUserMemories(String userId) {
        Map<String, List<Message>> result = new HashMap<>();
        Set<String> sessions = getUserSessions(userId);

        for (String sessionId : sessions) {
            result.put(sessionId, chatMemory.get(sessionId, 50));
        }
        return result;
    }

    public Set<String> getUserSessions(String userId) {
        PreparedStatement stmt = this.cqlSession.prepare(
                "SELECT DISTINCT session_id " +
                        "FROM springframework.ai_chat_memory " + // ToDo: 不能写死
                        "WHERE session_id LIKE ? " +
                        "PER PARTITION LIMIT 1000"
        );

        ResultSet rs = this.cqlSession.execute(stmt.bind(userId + ":%"));

        Set<String> sessionIds = new HashSet<>();
        rs.forEach(row -> sessionIds.add(row.getString("session_id")));
        return sessionIds;
    }
}
