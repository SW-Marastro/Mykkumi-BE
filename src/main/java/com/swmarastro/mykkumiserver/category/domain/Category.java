package com.swmarastro.mykkumiserver.category.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer listOrder;

    @OneToMany(mappedBy = "category")
    private List<SubCategory> subCategories = new ArrayList<>();
}
