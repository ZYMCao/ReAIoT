package cn.easttrans.reaiot.domain.persistence.sql.view;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

import static cn.easttrans.reaiot.domain.ModelConstants.*;

/**
 * @Description:
 * @Author: ZhanyingCao
 * @CreateDate: 2025/1/24 18:50
 **/
@Table(ALARM_VIEW_NAME)
public record AlarmInfoEntity(
        @Id UUID id,
        @Column(CREATED_TIME) long createdTime,
        @Column(ACK_TS) Long ackTs,
        @Column(CLEAR_TS) Long clearTs,
        @Column(ADDITIONAL_INFO) String additionalInfo,
        @Column(ORIGINATOR_NAME) String originatorName
) {}
