package co.pla.portfoliomanagement.portfolio.domain.entity;

import co.pla.portfoliomanagement.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "predictions")
public class Prediction extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PredictionStatus status = PredictionStatus.PENDING;

    @OneToMany(mappedBy = "prediction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PredictionAction> actions = new ArrayList<>();

    public void addAction(PredictionAction action) {
        actions.add(action);
        action.setPrediction(this);
    }

    public enum PredictionStatus {
        PENDING,
        COMPLETED,
        FAILED
    }
}
