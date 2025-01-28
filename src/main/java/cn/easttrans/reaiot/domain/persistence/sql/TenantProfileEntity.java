package cn.easttrans.reaiot.domain.persistence.sql;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

import static cn.easttrans.reaiot.domain.ModelConstants.*;

/**
 * @Description:
 * @Author: ZhanyingCao
 * @CreateDate: 2025/1/24 18:16
 **/
@Table(TENANT_PROFILE_TABLE_NAME)
public record TenantProfileEntity(
        @Id UUID id,
        @Column(CREATED_TIME) long createdTime,
        @Column(NAME) String name,
        @Column(PROFILE_DATA) String profileData,
        @Column(DESCRIPTION) String description,
        @Column(IS_DEFAULT) boolean isDefault,
        @Column(ISOLATED_TB_RULE_ENGINE) boolean isolatedTbRuleEngine
) {}
