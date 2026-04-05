package rent_cars.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "car_categories")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "base_rate")
    private BigDecimal baseRate;

    private String description;

    @OneToMany(mappedBy = "category")
    @JsonIgnoreProperties("category") // <--- РОЗРИВАЄМО ЦИКЛ З ІНШОГО БОКУ
    private List<Car> cars;
}
