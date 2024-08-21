package com.swmarastro.mykkumiserver.hashtag;

import com.swmarastro.mykkumiserver.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Hashtag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    public static Hashtag of(String name) {
        return Hashtag.builder()
                .name(name).build();
    }

}
