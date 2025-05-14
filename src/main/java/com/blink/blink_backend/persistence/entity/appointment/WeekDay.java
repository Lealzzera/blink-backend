package com.blink.blink_backend.persistence.entity.appointment;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "week_day")
public class WeekDay {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "week_day_name")
    private String name;
}
