package consegna.progettoecommerce.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import consegna.progettoecommerce.entities.Product;
import consegna.progettoecommerce.models.DtosMapper;
import consegna.progettoecommerce.models.ProductTypology;
import consegna.progettoecommerce.models.dots.ProductDTO;
import consegna.progettoecommerce.models.requests.ModifyProductRequest;
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
    //Cannot find implementation for progetto.progettoecommerce.DTO.ProductMapper
    //però aggiungeva lo stesso, nonostante lanciava un'eccezione come mai?
    
    public String infoDb(){
        return url;
    }
    
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
        ProductDTO productDTO = DtosMapper.INSTANCE.productToProductDTO(product);
        return productDTO;
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

    public Page<Product> findAllByNameAdmin(String name, PageRequestAttributes pageRequestAttributes){
        PageRequest pageRequest=PageRequest.of(pageRequestAttributes.getNPage(), pageRequestAttributes.getDimPage());
        return productRepository.findByName(name, pageRequest);
    }

    public Page<Product> findAllByTypeAdmin(String type, PageRequestAttributes pageRequestAttributes){
        PageRequest pageRequest=PageRequest.of(pageRequestAttributes.getNPage(), pageRequestAttributes.getDimPage());
        return productRepository.findByType(type, pageRequest);
    }

    public Page<ProductDTO> findAllByNameUser(String name, PageRequestAttributes pageRequestAttributes){
        PageRequest pageRequest=PageRequest.of(pageRequestAttributes.getNPage(), pageRequestAttributes.getDimPage());
        Page<Product> pages=productRepository.findByName(name, pageRequest);
        List<ProductDTO> listDto = pages.stream()
        .map(DtosMapper.INSTANCE::productToProductDTO)
        .collect(Collectors.toList());
        return new PageImpl<>(listDto, pageRequest, pages.getTotalElements());
    }

    public Page<ProductDTO> findAllByTypeUser(String type, PageRequestAttributes pageRequestAttributes){
        PageRequest pageRequest=PageRequest.of(pageRequestAttributes.getNPage(), pageRequestAttributes.getDimPage());
        Page<Product> pages=productRepository.findByType(type, pageRequest);
        List<ProductDTO> listDto = pages.stream()
        .map(DtosMapper.INSTANCE::productToProductDTO)
        .collect(Collectors.toList());
        return new PageImpl<>(listDto, pageRequest, pages.getTotalElements());
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

    public Page<Product> orderBy (String name, String typology, int nPage, int dimPage) throws RuntimeException{
        Sort sort;
        if(typology.equalsIgnoreCase("asc"))
            sort=Sort.by(Sort.Order.asc("price"));
        else if(typology.equalsIgnoreCase("desc"))
            sort=Sort.by(Sort.Order.desc("price"));
        else
            throw new DataNotCorrectException();
        PageRequest pageRequest=PageRequest.of(nPage, dimPage, sort);
        return productRepository.findByName(name, pageRequest);
        
    }

    /*public Page<Product> orderBy (String sortBy, String typology, int nPage, int dimPage) throws RuntimeException{
        Sort sort;
        if(!isExistsTypology(sortBy))
            throw new DataNotCorrectException();
        if(typology.equalsIgnoreCase("asc"))
            sort=Sort.by(Sort.Order.asc("price"));
        else if(typology.equalsIgnoreCase("desc"))
            sort=Sort.by(Sort.Order.desc("price"));
        else
            throw new DataNotCorrectException();
        PageRequest pageRequest=PageRequest.of(nPage, dimPage, sort);
        switch (sortBy) {
            case "name":
                return productRepository.findByName(sortBy, pageRequest);
            case "type":
                return productRepository.findByType(sortBy, pageRequest);
            default:
                throw new DataNotCorrectException();
        }
    }*/

    private boolean isExistsTypology(String sortBy) {
        for (ProductTypology t : ProductTypology.values()) {
            if (t.name().equalsIgnoreCase(sortBy)) {
                return true;
            }
        }
        return false;
    }

}
