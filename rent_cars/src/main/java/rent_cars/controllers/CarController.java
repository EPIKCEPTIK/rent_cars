package rent_cars.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rent_cars.entities.Car;
import rent_cars.repositories.CarRepository;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarRepository carRepository;

    @GetMapping
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @GetMapping("/available")
    public List<Car> getAvailableCars() {
        return carRepository.findByStatus("available");
    }

    @PostMapping
    public Car addCar(@RequestBody Car car) {
        return carRepository.save(car);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        return carRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
