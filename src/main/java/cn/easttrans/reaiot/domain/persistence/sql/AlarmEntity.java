package cn.easttrans.reaiot.domain.persistence.sql;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

import static cn.easttrans.reaiot.domain.ModelConstants.*;

/**
 * @Description:
 * @Author: ZhanyingCao
 * @CreateDate: 2025/1/24 16:21
 **/
@Table(ALARM_TABLE_NAME)
public record AlarmEntity(
        @Id UUID id,
        @Column(CREATED_TIME) long createdTime,
        @Column(ACK_TS) Long ackTs,
        @Column(CLEAR_TS) Long clearTs,
        @Column(ADDITIONAL_INFO) String additionalInfo,
        @Column(END_TS) Long endTs,
        @Column(ORIGINATOR_ID) UUID originatorId,
        @Column(ORIGINATOR_TYPE) Integer originatorType,
        @Column(PROPAGATE) boolean propagate,
        @Column(SEVERITY) String severity,
        @Column(START_TS) Long startTs,
        @Column(ASSIGN_TS) long assignTs,
        @Column(ASSIGNEE_ID) UUID assigneeId,
        @Column(TENANT_ID) UUID tenantId,
        @Column(CUSTOMER_ID) UUID customerId,
        @Column(PROPAGATE_RELATION_TYPES) String propagateRelationTypes,
        @Column(TYPE) String type,
        @Column(PROPAGATE_TO_OWNER) boolean propagateToOwner,
        @Column(PROPAGATE_TO_OWNER_HIERARCHY) boolean propagateToOwnerHierarchy,
        @Column(PROPAGATE_TO_TENANT) boolean propagateToTenant,
        @Column(ACKNOWLEDGED) boolean acknowledged,
        @Column(CLEARED) boolean cleared
) {}
