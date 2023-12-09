package consegna.progettoecommerce.DTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import consegna.progettoecommerce.entities.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(target = "hasModified", constant = "true")
    @Mapping(source = "user.budget", target = "budget")
    UserDTO userToUserDTO(User user);
    User UserDTOToUser(UserDTO userDTO);
    
}
