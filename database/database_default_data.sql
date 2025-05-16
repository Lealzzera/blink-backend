-- USERS
INSERT INTO users (name) VALUES ('João da Silva');

-- CLINIC
INSERT INTO clinic (clinic_name) VALUES ('Clínica Saúde Total');

-- USER_CLINIC
INSERT INTO user_clinic (user_id, clinic_id, role) VALUES (1, 1, 'Administrador');

-- CLINIC_CONFIGURATION
INSERT INTO clinic_configuration (
    clinic_id, whats_number, appointment_duration, default_appointment_duration, allow_overbooking, updated_by_user_id
) VALUES (
    1, '11999999999', '30 minutos', 30, FALSE, 1
);

-- WEEK_DAY
INSERT INTO week_day (week_day_name) VALUES
('MONDAY'),
('TUESDAY'),
('WEDNESDAY'),
('THURSDAY'),
('FRIDAY'),
('SATURDAY'),
('SUNDAY');

-- clinic_availability
INSERT INTO clinic_availability (
    clinic_id,
    week_day_id,
    open_time,
    close_time,
    lunch_start_time,
    lunch_end_time,
    updated_by_user_id,
    is_working_day
)
SELECT
    1 AS clinic_id,          -- ID da clínica existente
    wd.id AS week_day_id,
    '08:00:00' AS open_time,
    '18:00:00' AS close_time,
    '12:00:00' AS lunch_start_time,
    '13:00:00' AS lunch_end_time,
    1 AS updated_by_user_id, -- ID do usuário que atualizou
    CASE
        WHEN wd.week_day_name IN ('SATURDAY', 'SUNDAY') THEN FALSE
        ELSE TRUE
    END AS is_working_day
FROM week_day wd;

-- PATIENT
INSERT INTO patient (name, phone_number) VALUES ('Maria Oliveira', '11988887777');

-- APPOINTMENT_STATUS
INSERT INTO appointment_status (status) VALUES ('Agendado');

-- SERVICE_TYPE
INSERT INTO service_type (service_type) VALUES ('Consulta');

-- APPOINTMENT
INSERT INTO appointment (
    patient_id, scheduled_time, clinic_id, duration, appointment_status_id, service_type_id, notes, attended_by_user_id, attended_at
) VALUES (
    1, NOW() + interval '3 day', 1, 30, 1, 1, 'Paciente prefere atendimento pela manhã', 1, NOW()
);

-- SALE
INSERT INTO sale (
    patient_id, appointment_id, sale_value, sale_registered_by_user_id, sale_registered_at, service_type_id
) VALUES (
    1, 1, 200.00, 1, NOW(), 1
);

-- CHAT
INSERT INTO chat (patient_id, clinic_id, is_ai_answer) VALUES (1, 1, TRUE);
