package rent_cars.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DashboardDto {
    private BigDecimal totalRevenue;
    private long availableCars;
    private long rentedCars;
    private long maintenanceCars;
    private long activeRentals;
}