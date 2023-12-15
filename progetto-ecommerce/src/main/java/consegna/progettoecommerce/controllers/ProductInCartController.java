package consegna.progettoecommerce.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import consegna.progettoecommerce.config.JwtService;
import consegna.progettoecommerce.models.requests.PageRequestAttributes;
import consegna.progettoecommerce.models.requests.ProductInCartRequest;
import consegna.progettoecommerce.services.ProductInCartService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;


@RequiredArgsConstructor
@RestController
@RequestMapping("/prodInCart")
public class ProductInCartController {

    private final JwtService jwtService;
    private final ProductInCartService productInCartService;
    
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/add")
    public ResponseEntity addProductInCart(HttpServletRequest httpRequest, @RequestBody ProductInCartRequest productInCartRequest){
        try{
            String email=jwtService.extractUsernameFromRequest(httpRequest);
            return ResponseEntity.ok(productInCartService.addProductInCart(email, productInCartRequest));
        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getClass().getSimpleName());
        }
    }

    // /delete/{productCode}/user/{userid}
    //prendere la mail dal secondo path
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/{email}/delete/{productCode}")
    public ResponseEntity deleteProductInCart(@PathVariable("email") String email, @PathVariable("productCode") String productCode){
        try{
            productInCartService.deleteProductInCart(email, productCode);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getClass().getSimpleName());
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/getProductsInCart")
    public ResponseEntity getProductsInCart(HttpServletRequest httpRequest, @RequestBody PageRequestAttributes pageRequestAttributes){
        try{
            String email=jwtService.extractUsernameFromRequest(httpRequest);
            return ResponseEntity.ok(productInCartService.getProductsInCart(email, pageRequestAttributes));
        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getClass().getSimpleName());
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/buyAll")
    public ResponseEntity buyAllProductsInCart(HttpServletRequest httpRequest){
        try{
            String email=jwtService.extractUsernameFromRequest(httpRequest);
            return ResponseEntity.ok(productInCartService.buyAllProductsInCart(email));
        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getClass().getSimpleName());
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/buySingleProduct/{productCode}")
    public ResponseEntity buySingleProductInCart(HttpServletRequest httpRequest, @PathVariable("productCode") String productCode){
        try{
            String email=jwtService.extractUsernameFromRequest(httpRequest);
            return ResponseEntity.ok(productInCartService.buySingleProductInCart(email, productCode));
        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getClass().getSimpleName());
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/getTotalCost")
    public ResponseEntity getTotalCost(HttpServletRequest httpRequest){
        try{
            String email=jwtService.extractUsernameFromRequest(httpRequest);
            return ResponseEntity.ok(productInCartService.getTotalCost(email));
        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getClass().getSimpleName());
        }
    }
    
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/modify")
    public ResponseEntity modifyProductInCart(HttpServletRequest httpRequest, @RequestBody ProductInCartRequest productInCartRequest){
        try{
            String email=jwtService.extractUsernameFromRequest(httpRequest);
            productInCartService.modifyQuantityProductInCart(email, productInCartRequest);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getClass().getSimpleName());
        }
    }
}
