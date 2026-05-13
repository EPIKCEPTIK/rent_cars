package rent_cars.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rent_cars.dto.PaymentDto;
import rent_cars.entities.Payment;
import rent_cars.services.PaymentService;

import java.security.Principal;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "5.Платежі", description = "Безпечна обробка транзакцій за оренду авто")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @Operation(summary = "Оплатити оренду", description = "Здійснює платіж. Сума повинна точно збігатися з totalPrice оренди поточного користувача.")
    public ResponseEntity<Payment> processPayment(
            @Valid @RequestBody PaymentDto paymentDto,
            Principal principal
    ) {
        return ResponseEntity.ok(paymentService.processPayment(paymentDto, principal.getName()));
    }
}