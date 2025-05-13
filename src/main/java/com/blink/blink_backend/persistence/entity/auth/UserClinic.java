package com.blink.blink_backend.persistence.entity.auth;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "user_clinic")
public class UserClinic {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long userId;

    @Id
    @Column(name = "clinic_id")
    private Long clinicId;

    @Column(name = "role")
    private String role;


}
