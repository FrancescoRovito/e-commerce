package consegna.progettoecommerce.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import consegna.progettoecommerce.models.ProductRequest;
import consegna.progettoecommerce.services.ProductService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/prod")
public class ProductController {
    
    private final ProductService productService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity addProduct(@RequestBody ProductRequest request){
        try{
            return ResponseEntity.ok(productService.addProduct(request));
        }
        catch(RuntimeException e){
            System.out.println("QUI "+e.getClass().getSimpleName());
            return ResponseEntity.badRequest().body(e.getClass().getSimpleName());
        }
    }
}
