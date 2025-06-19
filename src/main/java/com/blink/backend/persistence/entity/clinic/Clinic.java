package com.blink.backend.persistence.entity.clinic;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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

    public String getTokenizedName() { //TODO descomentar code quando tiver waha plus
        return "default";
        /*return stripAccents(normalizeSpace(clinicName))
                .toLowerCase()
                .replace("\s", "_");*/
    }
}
