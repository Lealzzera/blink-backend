-- Rollback for V0__init_db.sql
-- Drop tables in reverse order to respect foreign key constraints

DROP TABLE IF EXISTS blink_be_dev.sale;
DROP TABLE IF EXISTS blink_be_dev.appointment;
DROP TABLE IF EXISTS blink_be_dev.clinic_configuration;
DROP TABLE IF EXISTS blink_be_dev.clinic_availability;
DROP TABLE IF EXISTS blink_be_dev.user_entity;
DROP TABLE IF EXISTS blink_be_dev.patient;
DROP TABLE IF EXISTS blink_be_dev.clinic_availability_exception;
DROP TABLE IF EXISTS blink_be_dev.clinic;
