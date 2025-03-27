package cn.easttrans.reaiot.agentic;

/**
 * 环境常量
 */
public final class EnvironmentalConstants {
    public static final String CASSANDRA_CONTACT_ENV = "${spring.cassandra.contact-point}";
    public static final String KEY_SPACE_ENV = "${spring.ai.vectorstore.cassandra.keyspace}";

    /**
     * 梁场
     */
    public static class BEAM {
        public static final String BASE_URL_ENV = "${easttrans.beam-construction.base-url}";
        public static final String USERNAME_ENV = "${easttrans.beam-construction.username}";
        public static final String PASSWORD_ENV = "${easttrans.beam-construction.password}";
        public static final String SUFFIX_ENV = "${easttrans.beam-construction.suffix}";
    }

    /**
     * OpenAI
     */
    public static class OPEN_AI {
        public static final String BASE_URL_ENV = "${spring.ai.openai.base-url}";
        public static final String SYS_PROMPT_ENV = "classpath:/prompts/system-message.st";
        public static final String NOMEN_PROMPT_ENV = "classpath:/prompts/system-name-creator.st";

    }
}
