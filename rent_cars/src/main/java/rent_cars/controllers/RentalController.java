package rent_cars.controllers;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rent_cars.entities.Car;
import rent_cars.entities.Rental;
import rent_cars.repositories.CarRepository;
import rent_cars.repositories.RentalRepository;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;

    @PostMapping
    public ResponseEntity<Rental> createRental(@RequestBody Rental rental) {
        // Знаходимо машину, щоб перевірити статус
        return carRepository.findById(rental.getCar().getId()).map(car -> {
            if (!"available".equals(car.getStatus())) {
                return ResponseEntity.badRequest().<Rental>build();
            }

            // Змінюємо статус авто на "rented"
            car.setStatus("rented");
            carRepository.save(car);

            return ResponseEntity.ok(rentalRepository.save(rental));
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/client/{clientId}")
    public List<Rental> getClientRentals(@PathVariable Long clientId) {
        return rentalRepository.findByClientId(clientId);
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<Rental> closeRental(@PathVariable Long id) {
        return rentalRepository.findById(id).map(rental -> {
            rental.setStatus("closed");

            // Повертаємо авто в доступні
            Car car = rental.getCar();
            car.setStatus("available");
            carRepository.save(car);

            return ResponseEntity.ok(rentalRepository.save(rental));
        }).orElse(ResponseEntity.notFound().build());
    }
}
