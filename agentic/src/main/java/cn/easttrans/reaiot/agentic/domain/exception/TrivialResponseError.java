package cn.easttrans.reaiot.agentic.domain.exception;

public class TrivialResponseError extends RuntimeException {
    public TrivialResponseError(String message) {
        super(message);
    }

    public TrivialResponseError(String message, Throwable cause) {
        super(message, cause);
    }
}

