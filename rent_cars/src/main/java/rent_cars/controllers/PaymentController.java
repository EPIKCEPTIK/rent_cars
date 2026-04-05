package rent_cars.controllers;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import rent_cars.entities.Payment;
import rent_cars.repositories.PaymentRepository;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentRepository paymentRepository;

    @PostMapping
    public Payment processPayment(@RequestBody Payment payment) {
        // В ідеалі тут має бути перевірка, чи не перевищує сума заборгованість
        return paymentRepository.save(payment);
    }

    @GetMapping("/rental/{rentalId}")
    public List<Payment> getPaymentsByRental(@PathVariable Long rentalId) {
        return paymentRepository.findByRentalId(rentalId);
    }
}
