package cn.easttrans.reaiot.domain.persistence.sql;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

import static cn.easttrans.reaiot.domain.persistence.ModelConstants.*;

@Table(DEVICE_TABLE_NAME)
public record Device(@Id UUID id,
                     @Column(CREATED_TIME)
                     Long createdTime,
                     @Column(ADDITIONAL_INFO)
                     String additionalInfo,
                     @Column(CUSTOMER_ID)
                     UUID customerId,
                     @Column(DEVICE_DEVICE_PROFILE_ID)
                     UUID deviceProfileId,
                     @Column(DEVICE_DEVICE_DATA)
                     String deviceData,
                     String type,
                     String name,
                     String label,
                     @Column(TENANT_ID)
                     UUID tenantId,
                     @Column(DEVICE_FIRMWARE_ID)
                     UUID firmwareId,
                     @Column(DEVICE_SOFTWARE_ID)
                     UUID softwareId,
                     @Column(EXTERNAL_ID)
                     UUID externalId
) {
}

