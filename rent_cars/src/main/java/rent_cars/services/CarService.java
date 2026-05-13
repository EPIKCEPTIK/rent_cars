package rent_cars.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rent_cars.entities.Car;
import rent_cars.entities.CarCategory;
import rent_cars.dto.CarDto;
import rent_cars.repositories.CarCategoryRepository;
import rent_cars.repositories.CarRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import rent_cars.repositories.CarSpecs;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final CarCategoryRepository categoryRepository;

    public Page<Car> getAllCars(String brand, String status, Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Specification<Car> spec = Specification.where(CarSpecs.hasBrand(brand))
                .and(CarSpecs.hasStatus(status))
                .and(CarSpecs.hasCategory(categoryId));

        return carRepository.findAll(spec, pageable);
    }

    public List<Car> getAvailableCars() {
        return carRepository.findByStatus("available");
    }

    public Car getCarById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Авто з ID " + id + " не знайдено"));
    }

    public Car addCar(CarDto carDto) {
        CarCategory category = categoryRepository.findById(carDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Категорію не знайдено"));

        Car car = new Car();
        car.setCategory(category);
        car.setBrand(carDto.getBrand());
        car.setModel(carDto.getModel());
        car.setPlateNo(carDto.getPlateNo());
        car.setYear(carDto.getYear());
        car.setStatus("available");

        return carRepository.save(car);
    }
    @Transactional
    public Car updateCarStatus(Long id, String newStatus) {
        Car car = getCarById(id);

        if (!newStatus.equals("available") && !newStatus.equals("maintenance")) {
            throw new IllegalArgumentException("Неприпустимий статус. Дозволено: available або maintenance.");
        }

        if ("rented".equals(car.getStatus())) {
            throw new RuntimeException("Неможливо змінити статус! Авто зараз знаходиться в оренді. Спочатку завершіть оренду.");
        }

        car.setStatus(newStatus);
        return carRepository.save(car);
    }
    public void deleteCar(Long id) {
        if (!carRepository.existsById(id)) {
            throw new RuntimeException("Авто не знайдено");
        }
        carRepository.deleteById(id);
    }
}