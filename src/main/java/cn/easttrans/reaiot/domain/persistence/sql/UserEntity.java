package cn.easttrans.reaiot.domain.persistence.sql;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

import static cn.easttrans.reaiot.domain.ModelConstants.*;

/**
 * @Description:
 * @Author: ZhanyingCao
 * @CreateDate: 2025/1/24 18:09
 **/
@Table(USER_TABLE_NAME)
public record UserEntity(
        @Id UUID id,
        @Column(CREATED_TIME) long createdTime,
        @Column(ADDITIONAL_INFO) String additionalInfo,
        @Column(AUTHORITY) String authority,
        @Column(CUSTOMER_ID) UUID customerId,
        @Column(EMAIL) String email,
        @Column(FIRST_NAME) String firstName,
        @Column(LAST_NAME) String lastName,
        @Column(PHONE) String phone,
        @Column(TENANT_ID) UUID tenantId
) {}
