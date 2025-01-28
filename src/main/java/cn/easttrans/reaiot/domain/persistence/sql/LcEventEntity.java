package cn.easttrans.reaiot.domain.persistence.sql;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

import static cn.easttrans.reaiot.domain.ModelConstants.*;

/**
 * @Description:
 * @Author: ZhanyingCao
 * @CreateDate: 2025/1/24 17:33
 **/
@Table(LC_EVENT_TABLE_NAME)
public record LcEventEntity(
        @Id UUID id,
        @Column(TENANT_ID) UUID tenantId,
        @Column(TS) long ts,
        @Column(ENTITY_ID) UUID entityId,
        @Column(SERVICE_ID) String serviceId,
        @Column(E_TYPE) String eType,
        @Column(E_SUCCESS) boolean eSuccess,
        @Column(E_ERROR) String eError
) {}
