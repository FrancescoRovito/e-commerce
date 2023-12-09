package consegna.progettoecommerce.entities;

import jakarta.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="purchase")
public class Purchase {     //VA INSERITO QUA L' @ONDELETE
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id", nullable = false)
    private Integer id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="user", nullable=false)
    private User user;
}