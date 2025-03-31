package cn.easttrans.reaiot.agentic.domain.persistence.nosql;

import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.CLUSTERED;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.PARTITIONED;


@Table("ai_chat_user")
public record AIChatUser(
        @PrimaryKeyColumn(name = "user_id", type = PARTITIONED) String userId,
        @PrimaryKeyColumn(name = "session_id", type = CLUSTERED) String sessionId
) {
}
