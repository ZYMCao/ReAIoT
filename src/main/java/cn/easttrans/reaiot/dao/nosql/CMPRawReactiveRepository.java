package cn.easttrans.reaiot.dao.nosql;

/**
 * @Description:
 * @Author: ZhanyingCao
 * @CreateDate: 2025/3/13 14:46
 **/
import cn.easttrans.reaiot.domain.dto.CMPRaw;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CMPRawReactiveRepository extends ReactiveCassandraRepository<CMPRaw, Integer> {
}

