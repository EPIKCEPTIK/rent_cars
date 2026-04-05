package rent_cars.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "maintenance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    @JsonBackReference(value = "car-maintenance")
    private Car car;

    @Column(name = "serv_date")
    private LocalDate servDate;

    private String type;
    private BigDecimal cost;
    private String description;
}
