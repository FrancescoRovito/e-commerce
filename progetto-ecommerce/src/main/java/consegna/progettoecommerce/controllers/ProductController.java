package consegna.progettoecommerce.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import consegna.progettoecommerce.models.requests.ModifyProductRequest;
import consegna.progettoecommerce.models.requests.PageRequestAttributes;
import consegna.progettoecommerce.models.requests.ProductRequest;
import consegna.progettoecommerce.services.ProductService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/prod")
public class ProductController {
    
    private final ProductService productService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/infoDb")
    public ResponseEntity infoDb(){
        try{
            return ResponseEntity.ok(productService.infoDb());
        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getClass().getSimpleName());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity addProduct(@RequestBody ProductRequest request){
        try{
            return ResponseEntity.ok(productService.addProduct(request));
        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getClass().getSimpleName());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/modify")
    public ResponseEntity modifyProduct(@RequestBody ModifyProductRequest request){
        try{
            return ResponseEntity.ok(productService.modifyProduct(request));
        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getClass().getSimpleName());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity deleteProduct(@RequestParam("code") String code){
        try{
            productService.deleteProduct(code);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getClass().getSimpleName());
        }
    }

    //NON FUNZIONA LA PAGINAZIONE, RIMANE SEMPRE A 0 CON BODY, CON PARAM SI INVECE
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    @GetMapping("/findAllByNameAdmin")
    public ResponseEntity findAllByNameAdmin(@RequestParam("name") String name,
    @RequestBody PageRequestAttributes pageRequestAttributes){
        try{
            return ResponseEntity.ok(productService.findAllByName(name,pageRequestAttributes));
        }
         catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getClass().getSimpleName());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/findAllByTypeAdmin or hasAuthority('USER')")
    public ResponseEntity findAllByTypeAdmin(@RequestParam("type") String type,
    @RequestBody PageRequestAttributes pageRequestAttributes){
        try{
            System.out.println(">>> "+pageRequestAttributes);
            return ResponseEntity.ok(productService.findAllByType(type,pageRequestAttributes));
        }
         catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getClass().getSimpleName());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/findByCode")
    public ResponseEntity findByCode(@RequestParam("code") String code){
        try{
            return ResponseEntity.ok(productService.findByCode(code));
        }
         catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getClass().getSimpleName());
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/findByModel")
    public ResponseEntity findByModel(@RequestParam("model") String model){
        try{
            return ResponseEntity.ok(productService.findByModel(model));
        }
         catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getClass().getSimpleName());
        }
    }

    @PreAuthorize("hasAuthority('USER') OR hasAuthority('ADMIN')")
    @GetMapping("/order")
    public ResponseEntity orderBy(@RequestParam("name") String name, @RequestParam("typology") String typology,
    @RequestParam("nPage") int nPage, @RequestParam("dimPage") int dimPage){
        try{
            return ResponseEntity.ok(productService.orderBy(name,typology,nPage,dimPage));
        }
         catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getClass().getSimpleName());
        }
    }
}
