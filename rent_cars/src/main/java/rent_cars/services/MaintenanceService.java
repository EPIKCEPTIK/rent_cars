package rent_cars.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rent_cars.dto.MaintenanceDto;
import rent_cars.entities.Car;
import rent_cars.entities.Maintenance;
import rent_cars.repositories.CarRepository;
import rent_cars.repositories.MaintenanceRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final CarRepository carRepository;

    @Transactional
    public Maintenance addMaintenance(MaintenanceDto dto) {
        log.info("Додавання запису про ТО для авто ID: {}", dto.getCarId());

        Car car = carRepository.findById(dto.getCarId())
                .orElseThrow(() -> new RuntimeException("Авто не знайдено"));

        Maintenance maintenance = new Maintenance();
        maintenance.setCar(car);
        maintenance.setServDate(dto.getServDate());
        maintenance.setType(dto.getType());
        maintenance.setCost(dto.getCost());
        maintenance.setDescription(dto.getDescription());

        return maintenanceRepository.save(maintenance);
    }

    public List<Maintenance> getCarMaintenanceHistory(Long carId) {
        if (!carRepository.existsById(carId)) {
            throw new RuntimeException("Авто не знайдено");
        }
        return maintenanceRepository.findByCarIdOrderByServDateDesc(carId);
    }
    public void deleteMaintenance(Long id) {
        if (!maintenanceRepository.existsById(id)) {
            throw new RuntimeException("Запис про ТО не знайдено");
        }
        maintenanceRepository.deleteById(id);
    }
}