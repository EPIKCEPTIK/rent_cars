package rent_cars.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_id", nullable = false)
    @JsonBackReference(value = "rental-payments")
    private Rental rental;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "pay_date")
    private LocalDateTime payDate = LocalDateTime.now();

    private String method;
}
