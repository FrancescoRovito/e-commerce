package consegna.progettoecommerce.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import consegna.progettoecommerce.config.JwtService;
import consegna.progettoecommerce.models.requests.PageRequestAttributes;
import consegna.progettoecommerce.services.PurchaseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/purch")
public class PurchaseController {
    
    private final PurchaseService purchaseService;
    private final JwtService jwtService;

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    @PostMapping("/findByIdAndYear")
    public ResponseEntity findByIdAndYear(HttpServletRequest httpRequest, @RequestParam("year") int year,
    @RequestBody PageRequestAttributes pageRequestAttributes){
        try{
            String email=jwtService.extractUsernameFromRequest(httpRequest);
            return ResponseEntity.ok(purchaseService.findByEmailAndYear(email, year, pageRequestAttributes));
        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getClass().getSimpleName());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    @GetMapping("/findByConsumer")
    public ResponseEntity findByConsumer(@RequestParam("idConsumer") int idConsumer){
        try{
            return ResponseEntity.ok(purchaseService.findByConsumer(idConsumer));
        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getClass().getSimpleName());
        }
    }
}
