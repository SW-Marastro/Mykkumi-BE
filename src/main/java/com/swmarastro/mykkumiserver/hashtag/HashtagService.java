package com.swmarastro.mykkumiserver.hashtag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HashtagService {

    private final HashtagRepository hashtagRepository;
    private final PostHashtagRepository postHashtagRepository;

    public Hashtag saveHashtag(Hashtag hashtag) {
        return hashtagRepository.save(hashtag);
    }

    public Hashtag findByName(String hashtagName) {
        return hashtagRepository.findByName(hashtagName).orElse(null);
    }

    public PostHashtag savePostHashtag(PostHashtag postHashtag) {
        return postHashtagRepository.save(postHashtag);
    }
}
