package rent_cars.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rent_cars.dto.DashboardDto;
import rent_cars.repositories.CarRepository;
import rent_cars.repositories.PaymentRepository;
import rent_cars.repositories.RentalRepository;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final CarRepository carRepository;
    private final RentalRepository rentalRepository;
    private final PaymentRepository paymentRepository;

    public DashboardDto getDashboardStats() {
        DashboardDto dto = new DashboardDto();

        dto.setTotalRevenue(paymentRepository.getTotalRevenue());
        dto.setAvailableCars(carRepository.countByStatus("available"));
        dto.setRentedCars(carRepository.countByStatus("rented"));
        dto.setMaintenanceCars(carRepository.countByStatus("maintenance"));
        dto.setActiveRentals(rentalRepository.countByStatus("active"));

        return dto;
    }
}