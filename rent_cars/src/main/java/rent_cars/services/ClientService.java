package rent_cars.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rent_cars.entities.Client;
import rent_cars.repositories.ClientRepository;
import rent_cars.repositories.RentalRepository;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final RentalRepository rentalRepository;

    @Transactional
    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Клієнта не знайдено"));


        client.getRentals().forEach(rental -> {
            if ("active".equals(rental.getStatus()) || "paid".equals(rental.getStatus())) {
                rental.getCar().setStatus("available");
            }
        });

        clientRepository.delete(client);
    }
}