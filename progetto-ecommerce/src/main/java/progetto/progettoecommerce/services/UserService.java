package progetto.progettoecommerce.services;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import progetto.progettoecommerce.config.JwtService;
import progetto.progettoecommerce.entities.User;
import progetto.progettoecommerce.models.AuthenticationRequest;
import progetto.progettoecommerce.models.AuthenticationResponse;
import progetto.progettoecommerce.models.RegisterRequest;
import progetto.progettoecommerce.models.Role;
import progetto.progettoecommerce.repositories.UsersRepository;
import progetto.progettoecommerce.utility.DataNotCorrectException;
import progetto.progettoecommerce.utility.Support;

@Service
@RequiredArgsConstructor
public class UserService{
    
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
   
    public AuthenticationResponse registerUser(RegisterRequest request) throws RuntimeException{
         User user= User.builder() 
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .budget(request.getBudget())
                    .role(Role.USER)
                    .build();   
        if(!Support.validUser(user))
            throw new DataNotCorrectException();
        usersRepository.save(user); 
        String jwtToken=jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse registerAdmin(RegisterRequest request) throws RuntimeException{
         User user= User.builder() 
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .budget(request.getBudget())
                    .role(Role.ADMIN)
                    .build();   
        if(!Support.validUser(user))
            throw new DataNotCorrectException();
        usersRepository.save(user); 
        String jwtToken=jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }
    
    public AuthenticationResponse login(AuthenticationRequest request) throws RuntimeException {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        System.out.println(request.getEmail());
        User user=usersRepository.findByEmail(request.getEmail()); 
        String jwtToken=jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }
}