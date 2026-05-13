package rent_cars.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // <-- Новий імпорт
import org.springframework.stereotype.Repository;
import rent_cars.entities.Car;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long>, JpaSpecificationExecutor<Car> {

    List<Car> findByStatus(String status);
    List<Car> findByCategoryId(Long categoryId);
    long countByStatus(String status);
}