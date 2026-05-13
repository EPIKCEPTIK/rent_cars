package rent_cars.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentDto {
    @NotNull(message = "ID оренди є обов'язковим")
    private Long rentalId;

    @NotNull(message = "Сума платежу є обов'язковою")
    @Positive(message = "Сума має бути більшою за нуль")
    private BigDecimal amount;

    @NotNull(message = "Метод оплати обов'язковий (напр. card, cash)")
    private String method;
}