package rent_cars.entities;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "license_no", unique = true, nullable = false)
    private String licenseNo;

    @Column(nullable = false)
    private String phone;

    private String email;
    private String address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "client-rentals")
    private List<Rental> rentals;

    @Override
    public String toString() {
        return "Client{id=" + id + ", name='" + fullName + "'}";
    }
}

