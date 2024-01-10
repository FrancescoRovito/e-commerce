package consegna.progettoecommerce.controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import consegna.progettoecommerce.config.JwtService;
import consegna.progettoecommerce.models.requests.AuthenticationRequest;
import consegna.progettoecommerce.models.requests.ModifyUserRequest;
import consegna.progettoecommerce.models.requests.PageRequestAttributes;
import consegna.progettoecommerce.models.requests.RegisterRequest;
import consegna.progettoecommerce.services.UserService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    
    @PostMapping("/registerUser")
    public ResponseEntity registerUser(@RequestBody RegisterRequest request){ 
        try{
            return new ResponseEntity(userService.registerUser(request), HttpStatus.OK);
        }
        catch(RuntimeException e){
            return new ResponseEntity(e.getClass().getSimpleName(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity registerAdmin(@RequestBody RegisterRequest request){ 
        try{
            return new ResponseEntity(userService.registerAdmin(request), HttpStatus.OK);
        }
        catch(RuntimeException e){
            return new ResponseEntity(e.getClass().getSimpleName(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity authenticate (@RequestBody AuthenticationRequest request){
        try{
            return ResponseEntity.ok(userService.login(request));
        }
        catch(RuntimeException e){
            return new ResponseEntity(e.getClass().getSimpleName(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/modify")
    public ResponseEntity modifyUser (HttpServletRequest httpRequest, @RequestBody ModifyUserRequest request){
        try{
            String oldEmail=jwtService.extractUsernameFromRequest(httpRequest);
            return ResponseEntity.ok(userService.modifyUser(oldEmail, request));
        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getClass().getSimpleName());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN') OR hasAuthority('USER')")
    @GetMapping("/findByEmail")
    public ResponseEntity findByEmail(HttpServletRequest httpRequest){
        try{
            String email=jwtService.extractUsernameFromRequest(httpRequest);
            return ResponseEntity.ok(userService.findByEmail(email));
        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getClass().getSimpleName());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/findAll")
    public ResponseEntity findAll(@RequestBody PageRequestAttributes pageRequestAttributes){
        try{
            return ResponseEntity.ok(userService.findAll(pageRequestAttributes));
        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getClass().getSimpleName());
        }
    }

    /*@PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/findAll")
    public ResponseEntity findAll(){
        try{
            return ResponseEntity.ok(userService.findAll());
        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getClass().getSimpleName());
        }
    }*/

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    @DeleteMapping("/deleteItSelf")
    public ResponseEntity deleteItSelf(HttpServletRequest httpRequest){
        try{
            String email=jwtService.extractUsernameFromRequest(httpRequest);
            userService.deleteUser(email);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getClass().getSimpleName());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/deleteUser")
    public ResponseEntity deleteItSelf(@RequestParam("email") String email){
        try{
            userService.deleteUser(email);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getClass().getSimpleName());
        }
    }
}
