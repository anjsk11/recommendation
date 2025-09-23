package com.sensingbros.recommendation.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Place {

    @Id
    @Column(nullable = false, updatable = false)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer scoreCount;

    @Column(precision = 2, scale = 1)
    private BigDecimal avgScore;

    @Column(columnDefinition = "text")
    private String category;

    @Column(nullable = false, unique = true, columnDefinition = "text")
    private String url;

    @Column(nullable = false, columnDefinition = "text")
    private String address;

    @Column(nullable = false)
    private Double x;

    @Column(nullable = false)
    private Double y;

    @OneToMany(mappedBy = "place")
    private Set<Review> reviews = new HashSet<>();

    @Column(nullable = false)
    private Integer heatmapx;

    @Column(nullable = false)
    private Integer heatmapy;

}
