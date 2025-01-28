package cn.easttrans.reaiot.domain.persistence.sql;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

import static cn.easttrans.reaiot.domain.ModelConstants.*;

/**
 * @Description:
 * @Author: ZhanyingCao
 * @CreateDate: 2025/1/24 17:13
 **/
@Table(CUSTOMER_TABLE_NAME)
public record CustomerEntity(
        @Id UUID id,
        @Column(CREATED_TIME) long createdTime,
        @Column(ADDITIONAL_INFO) String additionalInfo,
        @Column(ADDRESS) String address,
        @Column(ADDRESS2) String address2,
        @Column(CITY) String city,
        @Column(COUNTRY) String country,
        @Column(EMAIL) String email,
        @Column(PHONE) String phone,
        @Column(STATE) String state,
        @Column(TENANT_ID) UUID tenantId,
        @Column(PARENT_CUSTOMER_ID) UUID parentCustomerId,
        @Column(TITLE) String title,
        @Column(ZIP) String zip,
        @Column(EXTERNAL_ID) UUID externalId
) {}
