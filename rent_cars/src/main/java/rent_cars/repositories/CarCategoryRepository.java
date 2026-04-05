package rent_cars.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rent_cars.entities.CarCategory;

import java.util.Optional;

@Repository
public interface CarCategoryRepository extends JpaRepository<CarCategory, Long> {
    Optional<CarCategory> findByName(String name);
}
