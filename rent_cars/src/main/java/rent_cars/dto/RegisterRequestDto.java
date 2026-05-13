package rent_cars.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequestDto {
    @NotBlank(message = "Ім'я обов'язкове")
    private String fullName;

    @NotBlank(message = "Номер ліцензії обов'язковий")
    private String licenseNo;

    @NotBlank(message = "Телефон обов'язковий")
    private String phone;

    @Email(message = "Неправильний формат email")
    private String email;

    @NotBlank(message = "Пароль обов'язковий")
    private String password;
}