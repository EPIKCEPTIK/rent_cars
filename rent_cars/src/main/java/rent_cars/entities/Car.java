package rent_cars.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "cars")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnoreProperties({"cars", "hibernateLazyInitializer", "handler"})

    private CarCategory category;

    @Column(nullable = false, length = 50)
    private String brand;
    private String model;

    @Column(name = "plate_no", unique = true, nullable = false)
    private String plateNo;

    private Integer year;
    private String status = "available";

    @JsonManagedReference(value = "car-rentals")
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private List<Rental> rentals;

    @JsonManagedReference(value = "car-maintenance")
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private List<Maintenance> maintenances;

    @Override
    public String toString() {
        return "Car{id=" + id + ", brand='" + brand + "'}";
    }
}