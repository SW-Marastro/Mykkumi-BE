package com.swmarastro.mykkumiserver.category.domain;

import com.swmarastro.mykkumiserver.global.BaseEntity;
import com.swmarastro.mykkumiserver.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSubCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_sub_category")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String subCategory;

    public static String listToString(List<Long> categoryIds) {
        return categoryIds.stream().map(Object::toString).collect(Collectors.joining(","));
    }

    public static List<Long> stringToList(String categoryString) {
        return Arrays.stream(categoryString.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    public String updateSubCategory(List<Long> categoryIds) {
        String categoryStr = listToString(categoryIds);
        this.subCategory = categoryStr;
        return categoryStr;
    }

    public static UserSubCategory of(User user) {
        return UserSubCategory.builder()
                .user(user)
                .subCategory(null)
                .build();
    }
}
