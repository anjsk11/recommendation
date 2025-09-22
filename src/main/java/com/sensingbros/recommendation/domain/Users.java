package com.sensingbros.recommendation.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Convert;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import com.sensingbros.recommendation.converter.IntArray2DConverter;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Setter
@DynamicUpdate
public class Users {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;
    
    @Column(length = 100)
    private String name;

    @Column(insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    @Convert(converter = IntArray2DConverter.class)
    @Column(name = "morning_heatmap", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Integer[][] morningHeatmap;

    @Convert(converter = IntArray2DConverter.class)
    @Column(name = "noon_heatmap", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Integer[][] noonHeatmap;

    @Convert(converter = IntArray2DConverter.class)
    @Column(name = "night_heatmap", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Integer[][] nightHeatmap;

    @OneToMany(mappedBy = "user")
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Gps> Gps = new HashSet<>();

    public Users() {
        if (this.morningHeatmap == null
                || this.noonHeatmap == null
                || this.nightHeatmap == null) {
            initHeatmap();  // 3개 모두 초기화
        }
    }

    private void initHeatmap() {
        morningHeatmap = new Integer[30][30];
        noonHeatmap = new Integer[30][30];
        nightHeatmap = new Integer[30][30];

        for (int i = 0; i < 30; i++) {
            Arrays.fill(morningHeatmap[i], 0);
            Arrays.fill(noonHeatmap[i], 0);
            Arrays.fill(nightHeatmap[i], 0);// 각 행을 0으로 채움
        }
    }
}
