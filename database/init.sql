-- USER
CREATE TABLE users(
    id SERIAL PRIMARY KEY,
    name VARCHAR (150) NOT NULL,
);

CREATE TABLE user_clinic(
    user_id INT NOT NULL PRIMARY KEY,
    clinic_id INT NOT NULL PRIMARY KEY,
    role VARCHAR(150),
    FOREIGN KEY (clinic_id) REFERENCES clinic(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
)

---  CLINIC
CREATE TABLE clinic (
    id SERIAL PRIMARY KEY,
    clinic_name VARCHAR(255) NOT NULL
);

CREATE TABLE clinic_configuration(
    id SERIAL PRIMARY KEY,
    whats_number VARCHAR(15)
    appointment_duration VARCHAR(100),
    default_appointment_duration INT DEFAULT 30,
    allow_overbooking BOOLEAN DEFAULT FALSE,
    updated_by_user_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (updated_by_user_id) REFERENCES user(id)
);

-- APPOINTMENT

CREATE TABLE week_day (
    id SERIAL PRIMARY KEY,
    week_day_name VARCHAR(20) NOT NULL
);

CREATE TABLE clinic_availability (
    id SERIAL PRIMARY KEY,
    clinic_id INT NOT NULL,
    week_day_id INT NOT NULL,
    open_time TIME NOT NULL,
    close_time TIME NOT NULL,
    lunch_start_time TIME,
    lunch_end_time TIME,
    updated_by_user_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    is_working_day BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (clinic_id) REFERENCES clinic(id),
    FOREIGN KEY (week_day_id) REFERENCES week_day(id),
    FOREIGN KEY (updated_by_user_id) REFERENCES user(id)
);

CREATE TABLE patient (
    id SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()
);


CREATE TABLE appointment_status (
    id SERIAL PRIMARY KEY,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE service_type (
    id SERIAL PRIMARY KEY,
    service_type VARCHAR(50) NOT NULL
);

CREATE TABLE appointment (
    id SERIAL PRIMARY KEY,
    patient_id INT NOT NULL,
    scheduled_time TIMESTAMP NOT NULL,
    clinic_id INT NOT NULL,
    duration INT NOT NULL, -- duração em minutos?
    appointment_status_id INT NOT NULL,
    service_type_id INT NOT NULL,
    notes TEXT,
    attended_by_user_id INT,
    attended_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (clinic_id) REFERENCES clinic(id),
    FOREIGN KEY (patient_id) REFERENCES patient(id),
    FOREIGN KEY (appointment_status_id) REFERENCES appointment_status(id),
    FOREIGN KEY (service_type_id) REFERENCES service_type(id),
    FOREIGN KEY (attended_by_user_id) REFERENCES user(id)
);

CREATE TABLE sale(
    id SERIAL PRIMARY KEY,
    patient_id INT NOT NULL,
    appointment_id INT,
    sale_value NUMERIC(10,2) NOT NULL,
    sale_registered_by_user_id INT NOT NULL,
    sale_registered_at TIMESTAMP NOT NULL,
    service_type_ID INT NOT NULL,
    FOREIGN KEY (service_type_id) REFERENCES service_type(id),
    FOREIGN KEY (appointment_id) REFERENCES appointment(id),
    FOREIGN KEY (patient_id) REFERENCES patient(id)
);

-- message

CREATE TABLE chat (
    id SERIAL PRIMARY KEY,
    patient_id INT,
    clinic_id INT,
    is_ai_answer BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (patient_id) REFERENCES patient(id),
    FOREIGN KEY (clinic_id) REFERENCES clinic(id)
);