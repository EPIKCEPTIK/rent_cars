package rent_cars.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MaintenanceDto {

    @NotNull(message = "ID авто є обов'язковим")
    private Long carId;

    @NotNull(message = "Дата обслуговування є обов'язковою")
    private LocalDate servDate;

    @NotBlank(message = "Тип робіт обов'язковий (напр., Заміна масла, Ремонт ДТП)")
    private String type;

    @NotNull(message = "Вартість ремонту є обов'язковою")
    @PositiveOrZero(message = "Вартість не може бути від'ємною")
    private BigDecimal cost;

    private String description;
}