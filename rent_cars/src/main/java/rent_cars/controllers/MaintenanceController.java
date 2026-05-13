package rent_cars.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rent_cars.dto.MaintenanceDto;
import rent_cars.entities.Maintenance;
import rent_cars.services.MaintenanceService;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
@RequiredArgsConstructor
@Tag(name = "6.Технічне обслуговування", description = "Управління ремонтами та ТО автомобілів (тільки Адмін)")
@PreAuthorize("hasRole('ADMIN')") // Усі методи в цьому контролері тільки для Адміна!
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @PostMapping
    @Operation(summary = "Додати запис про ТО", description = "Додає інформацію про ремонт або технічне обслуговування авто.")
    public ResponseEntity<Maintenance> addMaintenance(@Valid @RequestBody MaintenanceDto dto) {
        return ResponseEntity.ok(maintenanceService.addMaintenance(dto));
    }

    @GetMapping("/car/{carId}")
    @Operation(summary = "Історія ремонтів авто", description = "Повертає всі записи про ТО для конкретного автомобіля (сортування від нових до старих).")
    public ResponseEntity<List<Maintenance>> getCarMaintenanceHistory(@PathVariable Long carId) {
        return ResponseEntity.ok(maintenanceService.getCarMaintenanceHistory(carId));
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Видалити запис про ТО", description = "Видаляє запис з історії обслуговування авто.")
    public ResponseEntity<Void> deleteMaintenance(@PathVariable Long id) {
        maintenanceService.deleteMaintenance(id);
        return ResponseEntity.noContent().build();
    }
}