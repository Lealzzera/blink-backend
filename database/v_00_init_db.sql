-- USER
CREATE TABLE users
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL
);

CREATE TABLE clinic
(
    id           SERIAL PRIMARY KEY,
    clinic_name  VARCHAR(255) NOT NULL,
    waha_session VARCHAR(100) NOT NULL
);

CREATE TABLE user_clinic
(
    user_id   INT NOT NULL,
    clinic_id INT NOT NULL,
    role      VARCHAR(150),
    PRIMARY KEY (user_id, clinic_id),
    FOREIGN KEY (clinic_id) REFERENCES clinic (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE clinic_configuration
(
    id                   SERIAL PRIMARY KEY,
    clinic_id            INT NOT NULL,
    whats_number         VARCHAR(15),
    appointment_duration INT       DEFAULT 30,
    allow_overbooking    BOOLEAN   DEFAULT FALSE,
    updated_by_user_id   INT NOT NULL,
    created_at           TIMESTAMP DEFAULT NOW(),
    updated_at           TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (updated_by_user_id) REFERENCES users (id),
    FOREIGN KEY (clinic_id) REFERENCES clinic (id)
);

-- APPOINTMENT

CREATE TABLE clinic_availability
(
    id                 SERIAL PRIMARY KEY,
    clinic_id          INT         NOT NULL,
    week_day           VARCHAR(20) NOT NULL,
    open_time          TIME,
    close_time         TIME,
    lunch_start_time   TIME,
    lunch_end_time     TIME,
    updated_by_user_id INT         NOT NULL,
    created_at         TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMP   NOT NULL DEFAULT NOW(),
    is_working_day     BOOLEAN              DEFAULT TRUE,
    FOREIGN KEY (clinic_id) REFERENCES clinic (id),
    FOREIGN KEY (updated_by_user_id) REFERENCES users (id)
);

CREATE TABLE clinic_availability_exception
(
    id               SERIAL PRIMARY KEY,
    clinic_id        INT     NOT NULL,
    exception_day    DATE    NOT NULL,
    is_working_day   BOOLEAN NOT NULL,
    open_time        TIME,
    close_time       TIME,
    lunch_start_time TIME,
    lunch_end_time   TIME,
    FOREIGN KEY (clinic_id) REFERENCES clinic (id)
);

CREATE TABLE patient
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(150) NOT NULL,
    phone_number VARCHAR(20)  NOT NULL,
    created_at   TIMESTAMP DEFAULT NOW(),
    clinic_id    INT          NOT NULL,
    FOREIGN KEY (clinic_id) REFERENCES clinic (id)
);


CREATE TABLE service_type
(
    id           SERIAL PRIMARY KEY,
    service_type VARCHAR(50) NOT NULL
);

CREATE TABLE appointment
(
    id                  SERIAL PRIMARY KEY,
    patient_id          INT         NOT NULL,
    scheduled_time      TIMESTAMP   NOT NULL,
    clinic_id           INT         NOT NULL,
    duration            INT         NOT NULL,
    status              VARCHAR(50) NOT NULL,
    service_type_id     INT         NOT NULL,
    notes               TEXT,
    attended_by_user_id INT,
    attended_at         TIMESTAMP,
    created_at          TIMESTAMP DEFAULT NOW(),
    updated_at          TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (clinic_id) REFERENCES clinic (id),
    FOREIGN KEY (patient_id) REFERENCES patient (id),
    FOREIGN KEY (service_type_id) REFERENCES service_type (id),
    FOREIGN KEY (attended_by_user_id) REFERENCES users (id)
);

CREATE TABLE sale
(
    id                    SERIAL PRIMARY KEY,
    patient_id            INT                     NOT NULL,
    appointment_id        INT,
    value                 NUMERIC(10, 2)          NOT NULL,
    status                VARCHAR(255)            NOT NULL,
    registered_by_user_id INT                     NOT NULL,
    registered_at         TIMESTAMP DEFAULT NOW() NOT NULL,
    service_type_id       INT                     NOT NULL,
    FOREIGN KEY (service_type_id) REFERENCES service_type (id),
    FOREIGN KEY (appointment_id) REFERENCES appointment (id),
    FOREIGN KEY (patient_id) REFERENCES patient (id),
    FOREIGN KEY (registered_by_user_id) REFERENCES users (id)
);

-- MESSAGE

CREATE TABLE chat
(
    id           SERIAL PRIMARY KEY,
    patient_id   INT,
    clinic_id    INT,
    ai_answer BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (patient_id) REFERENCES patient (id),
    FOREIGN KEY (clinic_id) REFERENCES clinic (id)
);