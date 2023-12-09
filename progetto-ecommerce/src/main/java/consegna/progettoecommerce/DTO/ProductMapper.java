package consegna.progettoecommerce.DTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import consegna.progettoecommerce.entities.Product;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
    @Mapping(source = "product.name", target = "name")
    @Mapping(source = "product.type", target = "type")
    @Mapping(source = "product.model", target = "model")
    @Mapping(source = "product.price", target = "price")
    ProductDTO productToProductDTO(Product product);
    Product productDTOToProduct(ProductDTO productDTO);
}
