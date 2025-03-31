package cn.easttrans.reaiot.agentic;

/**
 * 环境常量
 */
public final class EnvironmentalConstants {
    public static final String CASSANDRA_CONTACT_ENV = "${spring.cassandra.contact-points}";
    public static final String KEY_SPACE_ENV = "${spring.ai.vectorstore.cassandra.keyspace}";
    private static final String USER_SESSION_TABLE_NAME = "ai_chat_user";

    /**
     * OpenAI
     */
    public static class OPEN_AI {
        public static final String BASE_URL_ENV = "${spring.ai.openai.base-url}";
        public static final String SYS_PROMPT_ENV = "classpath:/prompts/system-message.st";
        public static final String NOMEN_PROMPT_ENV = "classpath:/prompts/system-name-creator.st";

    }
}
