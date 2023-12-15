package consegna.progettoecommerce;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import consegna.progettoecommerce.models.requests.ProductInCartRequest;
import consegna.progettoecommerce.models.requests.ProductRequest;
import consegna.progettoecommerce.models.requests.RegisterRequest;
import consegna.progettoecommerce.services.ProductInCartService;
import consegna.progettoecommerce.services.ProductService;
import consegna.progettoecommerce.services.PurchaseService;
import consegna.progettoecommerce.services.UserService;
import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class ProgettoEcommerceApplication {

	private final UserService userService;
	private final ProductService productService;
	private final ProductInCartService productInCartService;
	private final PurchaseService purchaseService;
	public static void main(String[] args) {
		SpringApplication.run(ProgettoEcommerceApplication.class, args);
	}
		@Bean
		CommandLineRunner run(){
			return args -> {
			System.out.println();
			System.out.println("USER "+userService.registerUser(new RegisterRequest("Giovanni", "Rossi", "giovanni@ciao.it", "Ciao1!", 1450.0)));
			System.out.println();
			System.out.println("USER "+userService.registerUser(new RegisterRequest("Gino", "Verde", "gino@ciao.it", "Arrivederci1!", 1955.0)));
			System.out.println();
			System.out.println("ADMIN "+userService.registerAdmin(new RegisterRequest("Gianni", "Giallo", "gianni@ciao.it", "Prova1!", 955.0)));
			System.out.println();

			System.out.println(productService.addProduct(new ProductRequest("Asus","Tv","F550","110",200.0,5)));
			System.out.println();
			System.out.println(productService.addProduct(new ProductRequest("Asus","Smarthwatch","F5","111",50.0,7)));
			System.out.println();
			System.out.println(productService.addProduct(new ProductRequest("Acer","Notebook","A550","112",320.0,5)));
			System.out.println();

			// se metto qua la stampa è come se si andasse in stack overflow
			System.out.println(productInCartService.addProductInCart("giovanni@ciao.it", new ProductInCartRequest("110",3)));
			System.out.println();
			System.out.println(productInCartService.addProductInCart("giovanni@ciao.it", new ProductInCartRequest("111",2)));
			System.out.println();
			System.out.println(productInCartService.addProductInCart("gino@ciao.it", new ProductInCartRequest("110",3)));
			System.out.println();

			System.out.println(productInCartService.buyAllProductsInCart("giovanni@ciao.it"));
			//productInCartService.buyAllProductsInCart("gino@ciao.it"); 
			System.out.println();
			//System.out.println(purchaseService.findByEmailAndYear("giovanni@ciao.it",2023));

		};
		}
	}

