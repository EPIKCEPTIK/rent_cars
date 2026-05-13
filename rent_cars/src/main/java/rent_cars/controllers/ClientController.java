package rent_cars.controllers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rent_cars.entities.Client;
import rent_cars.repositories.ClientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import rent_cars.services.ClientService;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Tag(name = "3.Клієнти")
public class ClientController {

    private final ClientRepository clientRepository;
    private final ClientService clientService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Отримати всіх клієнтів", description = "Повертає список всіх зареєстрованих клієнтів. Вимагає прав адміністратора.")
    public ResponseEntity<Page<Client>> getAllClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Client> clientPage = clientRepository.findAll(
                PageRequest.of(page, size, Sort.by("id").descending())
        );
        return ResponseEntity.ok(clientPage);
    }
    @GetMapping("/{licenseNo}")
    @Operation(summary = "Знайти клієнта за ліцензією", description = "Повертає дані клієнта за його номером водійського посвідчення.")
    public ResponseEntity<Client> getByLicense(@PathVariable String licenseNo) {
        return clientRepository.findByLicenseNo(licenseNo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Видалити клієнта", description = "Видаляє клієнта та всі його записи. Тільки для Адміна.")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}