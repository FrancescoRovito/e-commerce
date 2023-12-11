package consegna.progettoecommerce.entities;

import jakarta.persistence.Entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="purchase")
public class Purchase {   
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id", nullable = false)
    private Integer id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="consumer", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="product", nullable = false)
    private Product product;

    @Column(name="quantity_to_purchase", nullable=false)
    private Integer quantityToPurchase;

    @Column(name="total_cost", nullable=false)
    private Double totalCost;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="purchase_data", nullable=false)
    private Date data;
}