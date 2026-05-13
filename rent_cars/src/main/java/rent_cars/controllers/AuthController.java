package rent_cars.controllers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import rent_cars.dto.AuthRequestDto;
import rent_cars.dto.AuthResponseDto;
import rent_cars.dto.RegisterRequestDto;
import rent_cars.entities.Client;
import rent_cars.repositories.ClientRepository;
import rent_cars.security.JwtService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "1.Аутентифікація", description = "Ендпоінти для реєстрації та входу користувачів у систему")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @Operation(summary = "Вхід в систему", description = "Аутентифікація користувача за email та паролем для отримання JWT токена.")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        UserDetails user = userDetailsService.loadUserByUsername(request.getEmail());
        String jwtToken = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponseDto(jwtToken));
    }

    @PostMapping("/register")
    @Operation(summary = "Реєстрація", description = "Створення нового облікового запису клієнта.")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {

        if (clientRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Користувач з таким email вже існує!");
        }

        Client client = new Client();
        client.setFullName(request.getFullName());
        client.setLicenseNo(request.getLicenseNo());
        client.setPhone(request.getPhone());
        client.setEmail(request.getEmail());

        client.setPassword(passwordEncoder.encode(request.getPassword()));
        client.setRole("USER");

        clientRepository.save(client);

        String jwtToken = jwtService.generateToken(client);
        return ResponseEntity.ok(new AuthResponseDto(jwtToken));
    }
}