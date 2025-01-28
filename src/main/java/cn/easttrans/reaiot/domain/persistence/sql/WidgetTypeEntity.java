package cn.easttrans.reaiot.domain.persistence.sql;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

import static cn.easttrans.reaiot.domain.ModelConstants.*;

/**
 * @Description:
 * @Author: ZhanyingCao
 * @CreateDate: 2025/1/24 18:35
 **/
@Table(WIDGET_TYPE_TABLE_NAME)
public record WidgetTypeEntity(
        @Id UUID id,
        @Column(CREATED_TIME) long createdTime,
        @Column(FQN) String fqn,
        @Column(DESCRIPTOR) String descriptor,
        @Column(NAME) String name,
        @Column(TENANT_ID) UUID tenantId,
        @Column(IMAGE) String image,
        @Column(DEPRECATED) boolean deprecated,
        @Column(DESCRIPTION) String description,
        @Column(TAGS) String[] tags,
        @Column(EXTERNAL_ID) UUID externalId
) {}
