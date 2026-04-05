package rent_cars.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rent_cars.entities.Maintenance;

import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    List<Maintenance> findByCarIdOrderByServDateDesc(Long carId);
}