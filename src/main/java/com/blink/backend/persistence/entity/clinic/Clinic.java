package com.blink.backend.persistence.entity.clinic;


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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "clinic_name")
    private String clinicName;

    @OneToOne(mappedBy = "clinic")
    private ClinicConfiguration clinicConfiguration;
}
