package cn.easttrans.reaiot.domain.persistence.sql;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

import static cn.easttrans.reaiot.domain.ModelConstants.*;

/**
 * @Description:
 * @Author: ZhanyingCao
 * @CreateDate: 2025/1/24 17:25
 **/
@Table(DASHBOARD_TABLE_NAME)
public record DashboardEntity(
        @Id UUID id,
        @Column(CREATED_TIME) long createdTime,
        @Column(CONFIGURATION) String configuration,
        @Column(ASSIGNED_CUSTOMERS) String assignedCustomers,
        @Column(TENANT_ID) UUID tenantId,
        @Column(CUSTOMER_ID) UUID customerId,
        @Column(TITLE) String title,
        @Column(MOBILE_HIDE) boolean mobileHide,
        @Column(MOBILE_ORDER) Integer mobileOrder,
        @Column(IMAGE) String image,
        @Column(EXTERNAL_ID) UUID externalId
) {}
