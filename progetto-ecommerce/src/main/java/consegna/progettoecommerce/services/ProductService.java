package consegna.progettoecommerce.services;

import org.springframework.stereotype.Service;

import consegna.progettoecommerce.DTO.ProductDTO;
import consegna.progettoecommerce.DTO.ProductMapper;
import consegna.progettoecommerce.entities.Product;
import consegna.progettoecommerce.models.ProductRequest;
import consegna.progettoecommerce.repositories.ProductRepository;
import consegna.progettoecommerce.utility.exceptions.DataNotCorrectException;
import consegna.progettoecommerce.utility.support.Support;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;
    //Cannot find implementation for progetto.progettoecommerce.DTO.ProductMapper
    //però aggiungeva lo stesso, nonostante lanciava un'eccezione come mai?
    public ProductDTO addProduct (ProductRequest request) throws RuntimeException{
        if(!Support.validProduct(request))
            throw new DataNotCorrectException();
        Product product= Product.builder()
        .name(request.getName())
        .type(request.getType())
        .model(request.getModel())
        .code(request.getCode())
        .price(request.getPrice())
        .quantity(request.getQuantity())
        .build();
        productRepository.save(product);
        ProductDTO productDTO = ProductMapper.INSTANCE.productToProductDTO(product);
        return productDTO;
    }



}
