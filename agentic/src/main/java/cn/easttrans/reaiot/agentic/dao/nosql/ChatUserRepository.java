package cn.easttrans.reaiot.agentic.dao.nosql;

import cn.easttrans.reaiot.agentic.domain.persistence.nosql.AIChatUser;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Flux;

public interface ChatUserRepository extends ReactiveCassandraRepository<AIChatUser, AIChatUser> {
    Flux<AIChatUser> findByUserId(String userId);
}
