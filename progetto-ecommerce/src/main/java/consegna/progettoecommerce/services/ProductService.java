package consegna.progettoecommerce.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import consegna.progettoecommerce.entities.Product;
import consegna.progettoecommerce.models.DtosMapper;
import consegna.progettoecommerce.models.dots.ProductDTO;
import consegna.progettoecommerce.models.requests.ModifyProductRequest;
import consegna.progettoecommerce.models.requests.OrderRequest;
import consegna.progettoecommerce.models.requests.PageRequestAttributes;
import consegna.progettoecommerce.models.requests.ProductRequest;
import consegna.progettoecommerce.repositories.ProductRepository;
import consegna.progettoecommerce.utility.exceptions.DataNotCorrectException;
import consegna.progettoecommerce.utility.exceptions.ProductNotExistsException;
import consegna.progettoecommerce.utility.support.Support;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;
    
    @Value("${spring.datasource.url}")
    private String url;
    
    public String infoDb(){
        return url;
    }
    
    public Product addProduct (ProductRequest request) throws RuntimeException{
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
        return product;
        /* in questo modo quando aggiungo dinamicamente in grafica mi mostra solo i campi
           del dto mentre dal db li prende tutti
        ProductDTO productDTO = DtosMapper.INSTANCE.productToProductDTO(product);
        return productDTO; */
    }

    public ProductDTO modifyProduct (ModifyProductRequest modifyProductRequest ) throws RuntimeException{
        Product product=productRepository.findByCode(modifyProductRequest.getOldCode());
        if(product==null)
            throw new ProductNotExistsException();
        if(!Support.validProduct(modifyProductRequest.getNewProductRequest()))
            throw new DataNotCorrectException();
        product.setName(modifyProductRequest.getNewProductRequest().getName());
        product.setType(modifyProductRequest.getNewProductRequest().getType());
        product.setModel(modifyProductRequest.getNewProductRequest().getModel());
        product.setCode(modifyProductRequest.getNewProductRequest().getCode());
        product.setPrice(modifyProductRequest.getNewProductRequest().getPrice());
        product.setQuantity(modifyProductRequest.getNewProductRequest().getQuantity());
        productRepository.save(product);
        ProductDTO productDTO=DtosMapper.INSTANCE.productToProductDTO(product);
        return productDTO;
    }

    public void deleteProduct (String code) throws RuntimeException{
        Product product=productRepository.findByCode(code);
        if(product==null)
            throw new ProductNotExistsException();
        productRepository.delete(product);
    }

    public Page<Product> findAllByName(String name, PageRequestAttributes pageRequestAttributes) throws RuntimeException{
        PageRequest pageRequest=PageRequest.of(pageRequestAttributes.getPage(), pageRequestAttributes.getDimPage());
        return productRepository.findByName(name, pageRequest);
    }

    public Page<Product> findAllByType(String type, PageRequestAttributes pageRequestAttributes) throws RuntimeException{
        PageRequest pageRequest=PageRequest.of(pageRequestAttributes.getPage(), pageRequestAttributes.getDimPage());
        return productRepository.findByType(type, pageRequest);
    }


    public ProductDTO findByModel (String model) throws RuntimeException{
        Product product=productRepository.findByModel(model);
        if(product==null)
            throw new ProductNotExistsException();
        ProductDTO productDTO=DtosMapper.INSTANCE.productToProductDTO(product);
        return productDTO;
    } 

    public Product findByCode (String code) throws RuntimeException{
        Product product=productRepository.findByCode(code);
        if(product==null)
            throw new ProductNotExistsException();
        return product;
    } 

    public Page<Product> orderBy(OrderRequest orderRequest) throws RuntimeException{
        Sort sort;
        if(orderRequest.getSortBy()==null || orderRequest.getTypology()==null)
            throw new DataNotCorrectException();
        if(orderRequest.getTypology().equals("asc"))
            sort=Sort.by(Sort.Order.asc(orderRequest.getSortBy()));
        else if(orderRequest.getTypology().equals("desc"))
            sort=Sort.by(Sort.Order.desc(orderRequest.getSortBy()));
        else
            throw new DataNotCorrectException();
        PageRequest pageRequest=PageRequest.of(orderRequest.getPage(), orderRequest.getDimPage(), sort);
        return productRepository.findAll(pageRequest);
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }
}

