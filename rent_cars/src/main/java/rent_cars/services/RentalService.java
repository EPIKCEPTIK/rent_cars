package rent_cars.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rent_cars.dto.RentalDto;
import rent_cars.entities.Car;
import rent_cars.entities.Client;
import rent_cars.entities.Rental;
import rent_cars.repositories.CarRepository;
import rent_cars.repositories.ClientRepository;
import rent_cars.repositories.RentalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.math.BigDecimal;
import java.time.Duration;

@Slf4j // Автоматично додає логер для класу
@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;
    private final ClientRepository clientRepository;

    public Page<Rental> getAllRentals(int page, int size) {
        return rentalRepository.findAll(
                PageRequest.of(page, size, Sort.by("id").descending())
        );
    }


    @Transactional
    public Rental createRental(RentalDto dto, String userEmail) {
        log.info("Оренда авто ID: {} користувачем (email): {}", dto.getCarId(), userEmail);

        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new IllegalArgumentException("Дата завершення не може бути раніше дати початку");
        }

        Car car = carRepository.findById(dto.getCarId())
                .orElseThrow(() -> new RuntimeException("Авто не знайдено"));

        if (!"available".equals(car.getStatus())) {
            log.warn("Авто {} вже зайняте!", car.getBrand());
            throw new RuntimeException("Авто недоступне для оренди");
        }

        Client client = clientRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Клієнта не знайдено"));

        long days = Duration.between(dto.getStartDate(), dto.getEndDate()).toDays();
        if (days == 0) days = 1;

        BigDecimal totalPrice = car.getCategory().getBaseRate().multiply(BigDecimal.valueOf(days));

        Rental rental = new Rental();
        rental.setCar(car);
        rental.setClient(client);
        rental.setStartDate(dto.getStartDate());
        rental.setEndDate(dto.getEndDate());
        rental.setTotalPrice(totalPrice);
        rental.setStatus("active");

        car.setStatus("rented");
        carRepository.save(car);

        Rental savedRental = rentalRepository.save(rental);
        log.info("Оренду створено. ID оренди: {}, До сплати: {}", savedRental.getId(), totalPrice);

        return savedRental;
    }
    @Transactional
    public Rental completeRental(Long rentalId) {
        log.info("Спроба завершення оренди ID: {}", rentalId);

        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("Оренду не знайдено"));

        if (!"paid".equals(rental.getStatus())) {
            throw new RuntimeException("Неможливо завершити: оренда ще не оплачена або вже закрита!");
        }

        rental.setStatus("closed");

        Car car = rental.getCar();
        car.setStatus("available");
        carRepository.save(car);

        Rental savedRental = rentalRepository.save(rental);
        log.info("Оренду ID: {} успішно закрито. Авто {} знову доступне.", savedRental.getId(), car.getBrand());

        return savedRental;
    }
    public Page<Rental> getMyRentals(String userEmail, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startDate").descending());
        return rentalRepository.findByClientEmail(userEmail, pageable);
    }
    public void deleteRental(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Оренду не знайдено"));

        if ("active".equals(rental.getStatus()) || "paid".equals(rental.getStatus())) {
            Car car = rental.getCar();
            car.setStatus("available");
            carRepository.save(car);
        }
        rentalRepository.deleteById(id);
    }
}