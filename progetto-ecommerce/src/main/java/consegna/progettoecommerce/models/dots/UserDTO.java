package consegna.progettoecommerce.models.dots;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    
    private String firstName;
    private String lastName;
    private String email;
    private boolean hasModified;
    private double budget;
    private String token;
}
