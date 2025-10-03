package com.blink.backend.domain.integration.n8n.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class N8nMessageReceived {
    private String message;
    private String sender;
    private String senderName;
    private List<AppointmentsData> appointmentsData;
    private Integer clinicId;
    private String clinicName;

}
