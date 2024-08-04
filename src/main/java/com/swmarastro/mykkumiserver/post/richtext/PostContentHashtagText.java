package com.swmarastro.mykkumiserver.post.richtext;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostContentHashtagText extends PostContentObject {

    private String color;
    private String url;

    public PostContentHashtagText(String text, String color, String url) {
        super(text);
        this.color = color;
        this.url = url+text.substring(1);
    }
}
