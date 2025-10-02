package co.pla.portfoliomanagement.portfolio.domain.entity;


import co.pla.portfoliomanagement.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "prediction_actions")
public class PredictionAction extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prediction_id", nullable = false)
    private Prediction prediction;

    @Column(nullable = false, length = 20)
    private String symbol;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ActionType action;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double weight;

    public enum ActionType {
        BUY,
        SELL,
        HOLD
    }
}
