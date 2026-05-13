package rent_cars.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RentalDto {

    @NotNull(message = "ID авто є обов'язковим")
    private Long carId;


    @NotNull(message = "Дата початку є обов'язковою")
    @FutureOrPresent(message = "Дата початку не може бути в минулому")
    private LocalDateTime startDate;

    @NotNull(message = "Дата завершення є обов'язковою")
    @Future(message = "Дата завершення має бути в майбутньому")
    private LocalDateTime endDate;
}