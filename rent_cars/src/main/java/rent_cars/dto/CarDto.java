package rent_cars.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CarDto {
    @NotNull(message = "ID категорії не може бути порожнім")
    private Long categoryId;

    @NotBlank(message = "Марка авто є обов'язковою")
    private String brand;

    @NotBlank(message = "Модель авто є обов'язковою")
    private String model;

    @NotBlank(message = "Номерний знак є обов'язковим")
    private String plateNo;

    @NotNull(message = "Рік випуску є обов'язковим")
    @Min(value = 1900, message = "Рік випуску не може бути меншим за 1900")
    private Integer year;
}