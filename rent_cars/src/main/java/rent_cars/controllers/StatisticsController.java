package rent_cars.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rent_cars.dto.DashboardDto;
import rent_cars.services.StatisticsService;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
@Tag(name = "7.Аналітика", description = "Статистичні дані для керівництва компанії")
@PreAuthorize("hasRole('ADMIN')")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/dashboard")
    @Operation(summary = "Отримати зведення (Дашборд)", description = "Повертає ключові показники: дохід, стан автопарку та кількість активних оренд.")
    public ResponseEntity<DashboardDto> getDashboard() {
        return ResponseEntity.ok(statisticsService.getDashboardStats());
    }
}