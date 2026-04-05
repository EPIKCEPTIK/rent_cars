package rent_cars.controllers;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rent_cars.entities.Client;
import rent_cars.repositories.ClientRepository;


@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientRepository clientRepository;

    @PostMapping
    public Client registerClient(@RequestBody Client client) {
        return clientRepository.save(client);
    }

    @GetMapping("/{licenseNo}")
    public ResponseEntity<Client> getByLicense(@PathVariable String licenseNo) {
        return clientRepository.findByLicenseNo(licenseNo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
