package consegna.progettoecommerce.models;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import consegna.progettoecommerce.entities.Product;
import consegna.progettoecommerce.entities.User;
import consegna.progettoecommerce.models.dots.ProductDTO;
import consegna.progettoecommerce.models.dots.UserDTO;

@Mapper
public interface DtosMapper {
    
    DtosMapper INSTANCE = Mappers.getMapper(DtosMapper.class);
    UserDTO userToUserDTO(User user);
    ProductDTO productToProductDTO(Product product);

}
