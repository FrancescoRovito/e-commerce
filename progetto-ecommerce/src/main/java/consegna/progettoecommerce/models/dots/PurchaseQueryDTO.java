package consegna.progettoecommerce.models.dots;

import java.util.Date;

public interface PurchaseQueryDTO {
    public Integer getConsumer();
    public Date getPurchaseDate();
    public Double getTotalCost();
}
