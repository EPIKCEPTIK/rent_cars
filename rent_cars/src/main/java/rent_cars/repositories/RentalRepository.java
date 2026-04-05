package rent_cars.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rent_cars.entities.Rental;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByStatus(String status);
    List<Rental> findByClientId(Long clientId);
    List<Rental> findByCarIdAndStatus(Long carId, String status);
}
