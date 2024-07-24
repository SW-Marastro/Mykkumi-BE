package com.swmarastro.mykkumiserver.global.util;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.swmarastro.mykkumiserver.global.config.S3properties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AwsS3Utils {

    private final AmazonS3 amazonS3;
    private final S3properties s3properties;

    /**
     * upload 메서드에서 올린 파일 url 리턴
     */
    public String getImageUrl(String uploadString) {
        URL url = amazonS3.getUrl(s3properties.getBucket(), uploadString);
        return ""+url;
    }

    /**
     * S3에 파일 저장
     */
    public String upload(MultipartFile file, String folderPath) throws IOException {
        String s3FileName = folderPath + UUID.randomUUID() + "-" + file.getOriginalFilename();
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(file.getInputStream().available());
        amazonS3.putObject(s3properties.getBucket(), s3FileName, file.getInputStream(), objMeta);
        return s3FileName;
    }

    /**
     * preSignedUrl 발급 _ 10분간 유효
     */
    public String generatePreSignedUrl(String filePath, String bucketName) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 10); //10분간 유효함
        return amazonS3.generatePresignedUrl(bucketName, filePath, calendar.getTime(), HttpMethod.PUT).toString();
    }
}
