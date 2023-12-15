package consegna.progettoecommerce.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @NoArgsConstructor  
    @AllArgsConstructor 
    @Builder
    @Entity
    @Table(name="products")
    public class Product {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name="id", nullable=false)
        private Integer id;

        @Column(name="code", nullable=false, unique=true)
        private String code;

        @Column(name="name")
        private String name;

        @Column(name="type")
        private String type;

        @Column(name="model", unique=true)
        private String model;

        @Column(name="price", nullable=false)
        private Double price;

        @Column(name="quantity")
        private Integer quantity;

        @Column(name="image")
        private String image;

        @Version
        @Column (name="version")
        private Long version;
    }
