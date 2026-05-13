package rent_cars.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rent_cars.dto.RentalDto;
import rent_cars.entities.Rental;
import rent_cars.services.RentalService;

import java.security.Principal;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
@Tag(name = "4.Оренда", description = "Обробка процесів створення та завершення оренди автомобілів")
public class RentalController {

    private final RentalService rentalService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Отримати всі записи про оренду (Тільки Адмін)", description = "Повертає повну історію всіх оренд у системі.")
    public ResponseEntity<Page<Rental>> getAllRentals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(rentalService.getAllRentals(page, size));
    }

    @GetMapping("/my")
    @Operation(summary = "Мої оренди", description = "Повертає історію оренд поточного авторизованого користувача.")
    public ResponseEntity<Page<Rental>> getMyRentals(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(rentalService.getMyRentals(principal.getName(), page, size));
    }

    @PostMapping
    @Operation(summary = "Створити оренду", description = "Оформлює новий договір оренди для поточного авторизованого клієнта.")
    public ResponseEntity<Rental> createRental(@Valid @RequestBody RentalDto rentalDto, Principal principal) {
        return ResponseEntity.ok(rentalService.createRental(rentalDto, principal.getName()));
    }

    @PutMapping("/{id}/close")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Завершити оренду", description = "Закриває існуючу оренду та змінює статус автомобіля на доступний. Оренда має бути попередньо оплачена (статус paid). Вимагає прав адміністратора.")
    public ResponseEntity<Rental> closeRental(@PathVariable Long id) {
        return ResponseEntity.ok(rentalService.completeRental(id));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Видалити оренду", description = "Видаляє запис про оренду. Якщо вона була активна, авто знову стане вільним.")
    public ResponseEntity<Void> deleteRental(@PathVariable Long id) {
        rentalService.deleteRental(id);
        return ResponseEntity.noContent().build();
    }
}