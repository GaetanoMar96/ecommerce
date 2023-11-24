package com.project.ecommerce.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {

    @Id
    private String id;
    private String name;
    private String brand;
    private String description;
    private double price;
    private String gender;
    private List<ImageData> images;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ImageData {
        private String image; //initial value
        private byte[] imageBin; //post processed value
        private String color;
    }
}
