package consegna.progettoecommerce.services;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import consegna.progettoecommerce.config.JwtService;
import consegna.progettoecommerce.entities.User;
import consegna.progettoecommerce.models.requests.AuthenticationRequest;
import consegna.progettoecommerce.models.AuthenticationResponse;
import consegna.progettoecommerce.models.DtosMapper;
import consegna.progettoecommerce.models.requests.ModifyUserRequest;
import consegna.progettoecommerce.models.requests.PageRequestAttributes;
import consegna.progettoecommerce.models.requests.RegisterRequest;
import consegna.progettoecommerce.models.Role;
import consegna.progettoecommerce.models.dots.UserDTO;
import consegna.progettoecommerce.repositories.UsersRepository;
import consegna.progettoecommerce.utility.exceptions.DataNotCorrectException;
import consegna.progettoecommerce.utility.exceptions.PasswordNotCorrectException;
import consegna.progettoecommerce.utility.exceptions.UserNotExistsException;
import consegna.progettoecommerce.utility.support.Support;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService{
    
    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
   
    public AuthenticationResponse registerUser(RegisterRequest request) throws RuntimeException{
        if(!Support.validUser(request))
            throw new DataNotCorrectException();
        if(!Support.validPassword(request.getPassword()))
            throw new PasswordNotCorrectException();
         User user= User.builder() 
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .budget(request.getBudget())
                    .role(Role.USER)
                    .build();   
        userRepository.save(user); 
        String jwtToken=jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse registerAdmin(RegisterRequest request) throws RuntimeException{
         if(!Support.validUser(request))
            throw new DataNotCorrectException();
        if(!Support.validPassword(request.getPassword()))
            throw new PasswordNotCorrectException();
         User user= User.builder() 
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .budget(request.getBudget())
                    .role(Role.ADMIN)
                    .build();   
        userRepository.save(user); 
        String jwtToken=jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }
    
    public AuthenticationResponse login(AuthenticationRequest request) throws RuntimeException {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user=userRepository.findByEmail(request.getEmail());
        if(user == null)
            throw new UserNotExistsException();
        String jwtToken=jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }

    public UserDTO modifyUser (String oldmail, ModifyUserRequest request) throws RuntimeException{
        User user=userRepository.findByEmail(oldmail);
        if(user==null)
            throw new UserNotExistsException();
        if(!Support.validUser(request))
            throw new DataNotCorrectException();
        if(!Support.validPassword(request.getPassword()))
            throw new PasswordNotCorrectException();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setBudget(request.getBudget());
        userRepository.save(user);

        String token=jwtService.generateToken(user);
        UserDTO userDTO = DtosMapper.INSTANCE.userToUserDTO(user);  //è il pattern singleton
        userDTO.setToken(token);
        //UserDTO userDTO=new UserDTO(user);
        return userDTO;
    }

    public Page<User> findAll(PageRequestAttributes pageRequestAttributes){
        PageRequest pageRequest=PageRequest.of(pageRequestAttributes.getNPage(),pageRequestAttributes.getDimPage());
        return userRepository.findAll(pageRequest);
    }

    public UserDTO findByEmail(String email)throws RuntimeException{
        User user=userRepository.findByEmail(email);
        if(user == null)
            throw new UserNotExistsException();
        UserDTO userDTO =DtosMapper.INSTANCE.userToUserDTO(user);
        return userDTO;
    }

    public void deleteUser(String email) throws RuntimeException{
        User user=userRepository.findByEmail(email);
        if(user==null)
            throw new UserNotExistsException();
        userRepository.delete(user);
    }

    /*public List<User> findAll(){
        return userRepository.findAll();
    }*/
}