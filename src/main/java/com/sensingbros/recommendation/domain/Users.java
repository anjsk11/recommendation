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
    @Column(name = "heatmap", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Integer[][] heatmap;

    @OneToMany(mappedBy = "user")
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Gps> Gps = new HashSet<>();

    public Users() {
        if (this.heatmap == null)
            initHeatmap();  // 생성자에서 초기화
    }

    private void initHeatmap() {
        heatmap = new Integer[30][30];
        for (int i = 0; i < 30; i++) {
            Arrays.fill(heatmap[i], 0);  // 각 행을 0으로 채움
        }
    }
}
