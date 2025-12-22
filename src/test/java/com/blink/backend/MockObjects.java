package com.blink.backend;

import com.blink.backend.controller.appointment.dto.UpdateAppointmentStatusDTO;
import com.blink.backend.persistence.entity.appointment.AppointmentEntity;

public class MockObjects {

    public static UpdateAppointmentStatusDTO getUpdateAppointmentStatus(int id, String status) {
        UpdateAppointmentStatusDTO object = new UpdateAppointmentStatusDTO();
        object.setAppointmentId(id);
        object.setNewStatus(status);
        return object;
    }

    public static AppointmentEntity getAppointment(){
        return new AppointmentEntity();
    }
}
