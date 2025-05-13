package com.blink.blink_backend.persistence.entity.clinic;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "clinic")
public class Clinic {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "clinic_name")
    private String clinicName;
}
