-- blink_be_dev.clinic definição

CREATE TABLE blink_be_dev.clinic
(
    id           serial       NOT NULL PRIMARY KEY,
    clinic_name  varchar(255) NOT NULL,
    waha_session varchar(100) NOT NULL,
    clinic_code  varchar(50)  NOT NULL
);

-- blink_be_dev.user_entity definição

CREATE TABLE blink_be_dev.user_entity
(
    id        serial       NOT NULL PRIMARY KEY,
    "name"    varchar(150) NOT NULL,
    clinic_id int          NOT NULL REFERENCES blink_be_dev.clinic (id),
    "role"    varchar(50)  NOT NULL,
    user_id   uuid         NOT NULL REFERENCES auth.users (id)
);

-- blink_be_dev.clinic_availability_exception definição

CREATE TABLE blink_be_dev.clinic_availability_exception
(
    id               serial NOT NULL PRIMARY KEY,
    clinic_id        int    NOT NULL REFERENCES blink_be_dev.clinic (id),
    exception_day    date   NOT NULL,
    is_working_day   bool   NOT NULL,
    open_time        time   NULL,
    close_time       time   NULL,
    lunch_start_time time   NULL,
    lunch_end_time   time   NULL
);

-- blink_be_dev.patient definição

CREATE TABLE blink_be_dev.patient
(
    id           serial                  NOT NULL PRIMARY KEY,
    code         uuid                    NOT NULL UNIQUE,
    "name"       varchar(150)            NOT NULL,
    phone_number varchar(20)             NOT NULL,
    created_at   timestamp DEFAULT now() NULL,
    clinic_id    int                     NOT NULL REFERENCES blink_be_dev.clinic (id),
    ai_answer    bool      DEFAULT true  NULL
);

-- blink_be_dev.clinic_availability definição

CREATE TABLE blink_be_dev.clinic_availability
(
    id                 serial                  NOT NULL PRIMARY KEY,
    clinic_id          int                     NOT NULL REFERENCES blink_be_dev.clinic (id),
    week_day           varchar(20)             NOT NULL,
    open_time          time                    NULL,
    close_time         time                    NULL,
    lunch_start_time   time                    NULL,
    lunch_end_time     time                    NULL,
    updated_by_user_id int                     NOT NULL REFERENCES blink_be_dev.user_entity (id),
    created_at         timestamp DEFAULT now() NOT NULL,
    updated_at         timestamp DEFAULT now() NOT NULL,
    is_working_day     bool      DEFAULT true  NULL
);

-- blink_be_dev.clinic_configuration definição

CREATE TABLE blink_be_dev.clinic_configuration
(
    id                   serial                  NOT NULL PRIMARY KEY,
    clinic_id            int                     NOT NULL REFERENCES blink_be_dev.clinic (id),
    appointment_duration int       DEFAULT 30    NULL,
    allow_overbooking    bool      DEFAULT false NULL,
    updated_by_user_id   int                     NOT NULL REFERENCES blink_be_dev.user_entity (id),
    created_at           timestamp DEFAULT now() NULL,
    updated_at           timestamp DEFAULT now() NULL,
    default_ai_answer    bool      DEFAULT true  NULL,
    ai_name              varchar(100)            NULL
);

-- blink_be_dev.appointment definição

CREATE TABLE blink_be_dev.appointment
(
    id                  serial      NOT NULL PRIMARY KEY,
    patient_id          int         NOT NULL REFERENCES blink_be_dev.patient (id),
    scheduled_time      timestamp   NOT NULL,
    clinic_id           int         NOT NULL REFERENCES blink_be_dev.clinic (id),
    duration            int         NOT NULL,
    status              varchar(50) NOT NULL,
    notes               text        NULL,
    attended_by_user_id int         NULL REFERENCES blink_be_dev.user_entity (id),
    attended_at         timestamp   NULL,
    created_at          timestamp DEFAULT now() NULL,
    updated_at          timestamp DEFAULT now() NULL
);

-- blink_be_dev.sale definição

CREATE TABLE blink_be_dev.sale
(
    id                    serial                          NOT NULL PRIMARY KEY,
    patient_id            int                             NOT NULL REFERENCES blink_be_dev.patient (id),
    appointment_id        int                             NULL REFERENCES blink_be_dev.appointment (id),
    value                 numeric(10, 2)                  NOT NULL,
    registered_by_user_id int                             NOT NULL REFERENCES blink_be_dev.user_entity (id),
    registered_at         timestamp    DEFAULT now()      NOT NULL,
    status                varchar(255) DEFAULT 'NAO_PAGO' NOT NULL
);
