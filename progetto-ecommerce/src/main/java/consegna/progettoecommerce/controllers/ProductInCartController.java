package consegna.progettoecommerce.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import consegna.progettoecommerce.config.JwtService;
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
}
