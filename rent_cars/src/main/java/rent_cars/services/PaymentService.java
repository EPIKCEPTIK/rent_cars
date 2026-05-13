package rent_cars.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rent_cars.dto.PaymentDto;
import rent_cars.entities.Payment;
import rent_cars.entities.Rental;
import rent_cars.repositories.PaymentRepository;
import rent_cars.repositories.RentalRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RentalRepository rentalRepository;

    @Transactional
    public Payment processPayment(PaymentDto dto, String userEmail) {
        log.info("Спроба оплати оренди ID: {} на суму {}", dto.getRentalId(), dto.getAmount());

        Rental rental = rentalRepository.findById(dto.getRentalId())
                .orElseThrow(() -> new RuntimeException("Оренду не знайдено"));

        if (!rental.getClient().getEmail().equals(userEmail)) {
            throw new RuntimeException("Ви не можете оплатити чужу оренду!");
        }

        if ("paid".equals(rental.getStatus()) || "closed".equals(rental.getStatus())) {
            throw new RuntimeException("Ця оренда вже була оплачена або закрита");
        }

        if (dto.getAmount().compareTo(rental.getTotalPrice()) != 0) {
            throw new RuntimeException("Невірна сума до сплати. Очікується: " + rental.getTotalPrice());
        }

        Payment payment = new Payment();
        payment.setRental(rental);
        payment.setAmount(dto.getAmount());
        payment.setMethod(dto.getMethod());

        rental.setStatus("paid");
        rentalRepository.save(rental);

        return paymentRepository.save(payment);
    }
}