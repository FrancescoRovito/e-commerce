package progetto.progettoecommerce.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @NoArgsConstructor  
    @AllArgsConstructor 
    @Entity
    @Table(name="products")
    public class Product {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name="id", nullable=false)
        private Integer id;
    }
