-- USERS
INSERT INTO users (name)
VALUES ('João da Silva');

-- CLINIC
INSERT INTO clinic (clinic_name, waha_session)
VALUES ('Clínica Saúde Total', 'default');

-- USER_CLINIC
INSERT INTO user_clinic (user_id, clinic_id, role)
VALUES (1, 1, 'Administrador');

-- CLINIC_CONFIGURATION
INSERT INTO clinic_configuration (clinic_id, whats_number, appointment_duration, allow_overbooking, updated_by_user_id)
VALUES (1, '11999999999', 30, FALSE, 1);

-- clinic_availability
INSERT INTO clinic_availability (clinic_id,
                                 week_day,
                                 open_time,
                                 close_time,
                                 lunch_start_time,
                                 lunch_end_time,
                                 updated_by_user_id,
                                 is_working_day)
VALUES (1, 'SEGUNDA', '08:00:00', '18:00:00', '12:00:00', '13:00:00', 1, TRUE),
       (1, 'TERCA', '08:00:00', '18:00:00', '12:00:00', '13:00:00', 1, TRUE),
       (1, 'QUARTA', '08:00:00', '18:00:00', '12:00:00', '13:00:00', 1, TRUE),
       (1, 'QUINTA', '08:00:00', '18:00:00', '12:00:00', '13:00:00', 1, TRUE),
       (1, 'SEXTA', '08:00:00', '18:00:00', '12:00:00', '13:00:00', 1, TRUE),
       (1, 'SABADO', '08:00:00', '12:00:00', NULL, NULL, 1, FALSE),
       (1, 'DOMINGO', NULL, NULL, NULL, NULL, 1, FALSE);

-- PATIENT
INSERT INTO patient (name, phone_number, clinic_id, ai_answer)
VALUES ('João do Brás', '11911112222', 1, true);

-- SERVICE_TYPE
INSERT INTO service_type (service_type)
VALUES ('Consulta');

-- APPOINTMENT
INSERT INTO appointment (patient_id, scheduled_time, clinic_id, duration, service_type_id, notes, attended_by_user_id,
                         attended_at, status)
VALUES (1, NOW() + interval '3 day', 1, 30, 1, 'Paciente prefere atendimento pela manhã', 1, NOW(), 'AGENDADO');

-- SALE
INSERT INTO sale (patient_id, appointment_id, value, status, registered_by_user_id, registered_at, service_type_id)
VALUES (1, 1, 200.00, 'PAGO', 1, NOW(), 1);