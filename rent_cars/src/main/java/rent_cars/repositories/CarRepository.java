package rent_cars.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rent_cars.entities.Car;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByStatus(String status); // наприклад, findByStatus("available")
    List<Car> findByCategoryId(Long categoryId);
    List<Car> findByBrandContainingIgnoreCase(String brand);
}
