package com.hirehub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "companies", schema = "jobportal")
public class Company extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 500)
    @Column(name = "logo", length = 500)
    private String logo;

    @Size(max = 100)
    @NotNull
    @Column(name = "industry", nullable = false, length = 100)
    private String industry;

    @Size(max = 50)
    @NotNull
    @Column(name = "size", nullable = false, length = 50)
    private String size;

    @NotNull
    @Column(name = "rating", nullable = false, precision = 3, scale = 2)
    private BigDecimal rating;

    @Size(max = 1000)
    @Column(name = "locations", length = 1000)
    private String locations;

    @NotNull
    @Column(name = "founded", nullable = false)
    private Integer founded;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "employees")
    private Integer employees;

    @Size(max = 500)
    @Column(name = "website", length = 500)
    private String website;
}


