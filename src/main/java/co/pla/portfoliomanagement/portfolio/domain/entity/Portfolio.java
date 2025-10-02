package co.pla.portfoliomanagement.portfolio.domain.entity;

import co.pla.portfoliomanagement.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "portfolios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Portfolio extends BaseEntity {

    @Column(nullable = false,unique = true)
    private String name;

    @Column(name = "user_id",nullable = false)
    private UUID userUid;

    @Column(nullable = false)
    private Double balance;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("portfolio")
    private Set<StockPosition> stockPositions = new HashSet<>();
}