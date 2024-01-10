package consegna.progettoecommerce.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModifyProductRequest {
    
    private ProductRequest newProductRequest;
    private String oldCode;
}
