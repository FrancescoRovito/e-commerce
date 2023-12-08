package progetto.progettoecommerce.controllers;

import lombok.RequiredArgsConstructor;
import progetto.progettoecommerce.models.AuthenticationRequest;
import progetto.progettoecommerce.models.RegisterRequest;
import progetto.progettoecommerce.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService usersService;
    
    
    @PostMapping("/registerUser")
    public ResponseEntity saveUser(@RequestBody RegisterRequest request){ 
        try{
            return new ResponseEntity(usersService.registerUser(request), HttpStatus.OK);
        }
        catch(RuntimeException e){
            return new ResponseEntity(e.getClass().getSimpleName(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity saveAdmin(@RequestBody RegisterRequest request){ 
        try{
            return new ResponseEntity(usersService.registerAdmin(request), HttpStatus.OK);
        }
        catch(RuntimeException e){
            return new ResponseEntity(e.getClass().getSimpleName(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity authenticate (@RequestBody AuthenticationRequest request){
        try{
            return ResponseEntity.ok(usersService.login(request));
        }
        catch(RuntimeException e){
            System.out.println("QUI "+e.getClass().getSimpleName());
            return new ResponseEntity(e.getClass().getSimpleName(), HttpStatus.BAD_REQUEST);
        }
    }
}
