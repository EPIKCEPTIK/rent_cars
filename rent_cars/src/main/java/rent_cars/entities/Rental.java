package rent_cars.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "rentals")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    @JsonBackReference(value = "car-rentals") // Розірвання циклу з Car
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @JsonBackReference(value = "client-rentals") // Розірвання циклу з Client
    private Client client;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(length = 20)
    private String status = "active";

    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "rental-payments")
    private List<Payment> payments;

    @Override
    public String toString() {
        return "Rental{id=" + id + ", status='" + status + "'}";
    }
}
