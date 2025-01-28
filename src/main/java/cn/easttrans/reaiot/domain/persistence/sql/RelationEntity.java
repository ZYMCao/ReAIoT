package cn.easttrans.reaiot.domain.persistence.sql;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

import static cn.easttrans.reaiot.domain.ModelConstants.*;

/**
 * @Description: 联合主键: primary key (from_id, from_type, relation_type_group, relation_type, to_id, to_type)
 * @ToDo: R2DBC似乎仍没能解决联合主键的问题：
 * <a href="https://github.com/spring-projects/spring-data-relational/issues/1276">R2DBC Composite Primary Key</a>
 * @Author: ZhanyingCao
 * @CreateDate: 2025/1/24 17:51
 **/
@Table(RELATION_TABLE_NAME)
public record RelationEntity( // ToDO
        @Column(FROM_ID) UUID fromId,
        @Column(FROM_TYPE) String fromType,
        @Column(TO_ID) UUID toId,
        @Column(TO_TYPE) String toType,
        @Column(RELATION_TYPE_GROUP) String relationTypeGroup,
        @Column(RELATION_TYPE) String relationType,
        @Column(ADDITIONAL_INFO) String additionalInfo
) {}
