package consegna.progettoecommerce.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseRequest {
    
    private int year;
    private int page;
    private int dimPage;
}
