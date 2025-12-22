package com.blink.backend.controller.message.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WahaMessagePayload {
    @JsonProperty("body")
    private String message;
    @JsonProperty("_data")
    @Getter(AccessLevel.NONE)
    private JsonNode from;
    private String to;

    public String getFrom() {
        String remoteJid = from.at("/Info/Sender").toString();
        String remoteJidAlt = from.at("/Info/SenderAlt").toString();
        String from = remoteJid.contains("@s.whatsapp.net") ? remoteJid : remoteJidAlt;
        return from.substring(0, from.lastIndexOf('@')).replace("\"", "");
    }
}
