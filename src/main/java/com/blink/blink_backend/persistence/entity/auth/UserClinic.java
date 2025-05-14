package com.blink.blink_backend.persistence.entity.auth;


import com.blink.blink_backend.persistence.entity.clinic.Clinic;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "user_clinic")
public class UserClinic {

    @Id
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

    @Id
    @JoinColumn(name = "clinic_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Clinic clinic;

    @Column(name = "role")
    private String role;


}
