package com.learnreactiveprogramming.itemclient.com.learnreactiveprogramming.itemclient.domain;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Item {
    private String id;
    private String description;
    private Double price;
}
