package consegna.progettoecommerce.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModifyUserRequest {
    
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Double budget;
}
