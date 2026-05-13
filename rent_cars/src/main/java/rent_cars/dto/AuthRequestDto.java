package rent_cars.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequestDto {
    @NotBlank(message = "Email не може бути порожнім")
    @Email(message = "Некоректний формат email")
    private String email;

    @NotBlank(message = "Пароль не може бути порожнім")
    private String password;
}