package cn.easttrans.reaiot.domain.persistence.sql;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

import static cn.easttrans.reaiot.domain.ModelConstants.*;

/**
 * @Description:
 * @Author: ZhanyingCao
 * @CreateDate: 2025/1/24 18:40
 **/
@Table(AUDIT_LOG_TABLE_NAME)
public record AuditLogEntity(
        @Id UUID id,
        @Column(CREATED_TIME) long createdTime,
        @Column(TENANT_ID) UUID tenantId,
        @Column(CUSTOMER_ID) UUID customerId,
        @Column(ENTITY_ID) UUID entityId,
        @Column(ENTITY_TYPE) String entityType,
        @Column(ENTITY_NAME) String entityName,
        @Column(USER_ID) UUID userId,
        @Column(USER_NAME) String userName,
        @Column(ACTION_TYPE) String actionType,
        @Column(ACTION_DATA) String actionData,
        @Column(ACTION_STATUS) String actionStatus,
        @Column(ACTION_FAILURE_DETAILS) String actionFailureDetails
) {}
