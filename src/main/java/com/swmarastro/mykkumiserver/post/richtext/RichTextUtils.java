package com.swmarastro.mykkumiserver.post.richtext;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class RichTextUtils {

    private static final String POST_CONTENT_HASHTAG_COLOR = "#0062FF";
    private static final String POST_HASHTAG_URL_PREFIX = "mykkumi://search?hashtag=";

    /**
     * 문자열에서 plain text와 hashtag를 구분하여 잘라주는 메서드
     */
    public static List<String> extractPlainTextAndHashtags(String input) {
        List<String> result = new ArrayList<>();

        // 정규 표현식 패턴 정의
        Pattern hashtagPattern = Pattern.compile("#\\S+");
        Matcher matcher = hashtagPattern.matcher(input);

        int lastIndex = 0;
        while (matcher.find()) {
            // 해시태그 이전의 일반 텍스트 부분 추출
            if (matcher.start() > lastIndex) {
                String plainText = input.substring(lastIndex, matcher.start());
                result.add(plainText);
            }

            // 해시태그 부분 추출
            String hashtagText = matcher.group();
            result.add(hashtagText);

            lastIndex = matcher.end();
        }

        // 마지막 해시태그 이후의 일반 텍스트 부분 추출
        if (lastIndex < input.length()) {
            String plainText = input.substring(lastIndex);
            result.add(plainText);
        }

        return result;
    }

    /**
     * 포스트 본문 일반 문자열 -> rich text 변환
     */
    public static List<PostContentObject> makePostContentStringToRichText(List<String> tokens) {

        List<PostContentObject> richTexts = new ArrayList<>();
        for (String token : tokens) {
            PostContentObject text;
            if (token.startsWith("#")) {
                text = new PostContentHashtagText(token, POST_CONTENT_HASHTAG_COLOR, POST_HASHTAG_URL_PREFIX);
            } else {
                text = new PostContentPlainText(token);
            }
            richTexts.add(text);
        }
        return richTexts;
    }

}
