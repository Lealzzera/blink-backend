package com.blink.backend.persistence.entity.auth;

import com.blink.backend.domain.model.auth.AuthenticatedUser;
import com.blink.backend.domain.model.auth.Authorities;
import com.blink.backend.persistence.entity.clinic.Clinic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "user_entity")
public class UserEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "name")
    private String name;

    @OneToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    @Column(name = "role")
    private String role;

    public AuthenticatedUser toAuthUser() {
        return new AuthenticatedUser(name, List.of(Authorities.valueOf(role.toUpperCase())), clinic);
    }
}
