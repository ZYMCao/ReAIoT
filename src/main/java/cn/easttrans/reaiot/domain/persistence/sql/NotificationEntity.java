package cn.easttrans.reaiot.domain.persistence.sql;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

import static cn.easttrans.reaiot.domain.ModelConstants.*;

/**
 * @Description:
 * @Author: ZhanyingCao
 * @CreateDate: 2025/1/24 17:48
 **/
@Table(NOTIFICATION_TABLE_NAME)
public record NotificationEntity(
        @Id UUID id,
        @Column(CREATED_TIME) long createdTime,
        @Column(REQUEST_ID) UUID requestId,
        @Column(RECIPIENT_ID) UUID recipientId,
        @Column(TYPE) String type,
        @Column(DELIVERY_METHOD) String deliveryMethod,
        @Column(SUBJECT) String subject,
        @Column(BODY) String body,
        @Column(ADDITIONAL_CONFIG) String additionalConfig,
        @Column(STATUS) String status
) {}
