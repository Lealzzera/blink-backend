package com.blink.backend.domain.service;

import com.blink.backend.domain.integration.WahaClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WhatsAppService {

    private final WahaClient wahaClient;
    public byte[] getWahaQrCode(){
        return wahaClient.getWahaQrCode();
    }
}
