package rent_cars.controllers;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rent_cars.dto.CarDto;
import rent_cars.entities.Car;
import rent_cars.services.CarService;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
@Tag(name = "2.Автомобілі", description = "Управління автопарком: перегляд, пошук та додавання транспортних засобів")
public class CarController {

    private final CarService carService;

    @GetMapping
    @Operation(summary = "Отримати список автомобілів", description = "Повертає сторінку автомобілів. Клієнти бачать тільки вільні авто, Адміністратори - всі (якщо не вказано інше).")
    public ResponseEntity<Page<Car>> getAllCars(
            @Parameter(description = "Назва бренду для пошуку") @RequestParam(required = false) String brand,

            @Parameter(description = "Статус авто (тільки для Адміна)") @RequestParam(required = false) String status,

            @Parameter(description = "Ідентифікатор категорії") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "Номер сторінки") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Кількість записів") @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            status = "available";
        }

        return ResponseEntity.ok(carService.getAllCars(brand, status, categoryId, page, size));
    }
    @GetMapping("/{id}")
    @Operation(summary = "Отримати автомобіль за ID", description = "Повертає детальну інформацію про конкретний транспортний засіб.")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        return ResponseEntity.ok(carService.getCarById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Додати новий автомобіль", description = "Створює новий запис автомобіля в системі. Вимагає прав адміністратора.")
    public ResponseEntity<Car> addCar(@Valid @RequestBody CarDto carDto) {
        return ResponseEntity.ok(carService.addCar(carDto));
    }
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Змінити статус авто (Тільки Адмін)", description = "Переводить машину в ремонт (maintenance) або повертає в стрій (available).")
    public ResponseEntity<Car> updateCarStatus(
            @PathVariable Long id,
            @Parameter(description = "Новий статус (available або maintenance)") @RequestParam String status
    ) {
        return ResponseEntity.ok(carService.updateCarStatus(id, status));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Видалити авто", description = "Видаляє автомобіль з бази даних. Тільки для Адміна.")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }
}