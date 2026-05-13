package rent_cars.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import rent_cars.dto.RentalDto;
import rent_cars.entities.Car;
import rent_cars.entities.CarCategory;
import rent_cars.entities.Client;
import rent_cars.entities.Rental;
import rent_cars.repositories.CarRepository;
import rent_cars.repositories.ClientRepository;
import rent_cars.repositories.RentalRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock
    private CarRepository carRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks
    private RentalService rentalService;


    @Test
    void createRental_Success() {
        String userEmail = "test@gmail.com";
        CarCategory category = new CarCategory();
        category.setBaseRate(BigDecimal.valueOf(1000));

        Car car = new Car();
        car.setId(1L);
        car.setStatus("available");
        car.setCategory(category);

        Client client = new Client();
        client.setEmail(userEmail);

        RentalDto dto = new RentalDto();
        dto.setCarId(1L);
        dto.setStartDate(LocalDateTime.now().plusDays(1));
        dto.setEndDate(LocalDateTime.now().plusDays(4));

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(clientRepository.findByEmail(userEmail)).thenReturn(Optional.of(client));
        when(rentalRepository.save(any(Rental.class))).thenAnswer(i -> i.getArgument(0));

        Rental result = rentalService.createRental(dto, userEmail);

        assertNotNull(result);
        assertEquals("active", result.getStatus());
        assertEquals(BigDecimal.valueOf(3000), result.getTotalPrice());
        assertEquals("rented", car.getStatus());
        verify(carRepository, times(1)).save(car);
    }

    @Test
    void createRental_InvalidDates_ThrowsException() {
        RentalDto dto = new RentalDto();
        dto.setCarId(1L);
        dto.setStartDate(LocalDateTime.now().plusDays(5));
        dto.setEndDate(LocalDateTime.now().plusDays(1));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> rentalService.createRental(dto, "test@gmail.com"));

        assertEquals("Дата завершення не може бути раніше дати початку", ex.getMessage());
        verify(carRepository, never()).findById(any());
    }

    @Test
    void createRental_CarNotFound_ThrowsException() {
        RentalDto dto = new RentalDto();
        dto.setCarId(99L);
        dto.setStartDate(LocalDateTime.now().plusDays(1));
        dto.setEndDate(LocalDateTime.now().plusDays(2));

        when(carRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> rentalService.createRental(dto, "test@gmail.com"));

        assertEquals("Авто не знайдено", ex.getMessage());
    }

    @Test
    void createRental_CarNotAvailable_ThrowsException() {
        Car car = new Car();
        car.setStatus("maintenance");

        RentalDto dto = new RentalDto();
        dto.setCarId(1L);
        dto.setStartDate(LocalDateTime.now().plusDays(1));
        dto.setEndDate(LocalDateTime.now().plusDays(2));

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> rentalService.createRental(dto, "test@gmail.com"));

        assertEquals("Авто недоступне для оренди", ex.getMessage());
    }

    @Test
    void createRental_ClientNotFound_ThrowsException() {
        Car car = new Car();
        car.setStatus("available");

        RentalDto dto = new RentalDto();
        dto.setCarId(1L);
        dto.setStartDate(LocalDateTime.now().plusDays(1));
        dto.setEndDate(LocalDateTime.now().plusDays(2));

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(clientRepository.findByEmail("ghost@gmail.com")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> rentalService.createRental(dto, "ghost@gmail.com"));

        assertEquals("Клієнта не знайдено", ex.getMessage());
    }


    @Test
    void completeRental_Success() {
        Car car = new Car();
        car.setStatus("rented");

        Rental rental = new Rental();
        rental.setId(1L);
        rental.setStatus("paid");
        rental.setCar(car);

        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
        when(rentalRepository.save(any(Rental.class))).thenAnswer(i -> i.getArgument(0));

        Rental result = rentalService.completeRental(1L);

        assertEquals("closed", result.getStatus());
        assertEquals("available", car.getStatus());
        verify(carRepository, times(1)).save(car);
    }

    @Test
    void completeRental_NotPaid_ThrowsException() {
        Rental rental = new Rental();
        rental.setId(1L);
        rental.setStatus("active");

        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> rentalService.completeRental(1L));

        assertEquals("Неможливо завершити: оренда ще не оплачена або вже закрита!", ex.getMessage());
        verify(carRepository, never()).save(any());
    }

    @Test
    void completeRental_NotFound_ThrowsException() {
        when(rentalRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> rentalService.completeRental(99L));

        assertEquals("Оренду не знайдено", ex.getMessage());
    }



    @Test
    void getMyRentals_ReturnsPage() {
        String email = "test@gmail.com";
        Page<Rental> expectedPage = new PageImpl<>(List.of(new Rental(), new Rental()));

        when(rentalRepository.findByClientEmail(eq(email), any(PageRequest.class))).thenReturn(expectedPage);

        Page<Rental> result = rentalService.getMyRentals(email, 0, 10);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(rentalRepository, times(1)).findByClientEmail(eq(email), any(PageRequest.class));
    }
}