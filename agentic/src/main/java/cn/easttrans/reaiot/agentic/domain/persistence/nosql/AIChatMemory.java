package cn.easttrans.reaiot.agentic.domain.persistence.nosql;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.Column;

import java.time.Instant;

import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.CLUSTERED;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.PARTITIONED;


@Table("ai_chat_memory")
public record AIChatMemory(
        @PrimaryKey AIChatMemory.PK pk,
        @Column("assistant")
        String assistant,
        @Column("user")
        String user
) {
    @PrimaryKeyClass
    public record PK(
            @PrimaryKeyColumn(name = "session_id", type = PARTITIONED) String sessionId,
            @PrimaryKeyColumn(name = "message_timestamp", type = CLUSTERED, ordinal = 1) Instant messageTimestamp
    ) {
    }
}
