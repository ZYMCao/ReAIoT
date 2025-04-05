package cn.easttrans.reaiot.agentic.dao.nosql;

import cn.easttrans.reaiot.agentic.domain.persistence.nosql.AIChatMemory;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AIChatMemoryRepository extends ReactiveCassandraRepository<AIChatMemory, AIChatMemory.PK> {
}

