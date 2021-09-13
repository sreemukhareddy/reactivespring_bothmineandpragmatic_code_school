package com.learnreactivespring.modal;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document   // @Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Item {
    @Id
    private String id;
    private String description;
    private Double price;
}
