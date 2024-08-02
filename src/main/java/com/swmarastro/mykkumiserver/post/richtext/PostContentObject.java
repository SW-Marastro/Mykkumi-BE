package com.swmarastro.mykkumiserver.post.richtext;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PostContentHashtagText.class, name = "hashtag"),
        @JsonSubTypes.Type(value = PostContentPlainText.class, name = "plain")
})
public abstract class PostContentObject {

    protected String text;
}