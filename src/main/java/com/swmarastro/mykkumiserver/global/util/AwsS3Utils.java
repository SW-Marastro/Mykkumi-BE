package com.swmarastro.mykkumiserver.global.util;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.swmarastro.mykkumiserver.global.config.S3properties;
import com.swmarastro.mykkumiserver.global.exception.CommonException;
import com.swmarastro.mykkumiserver.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
@Slf4j
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

    /**
     * S3에서 이미지 삭제
     */
    public void deleteImageByUrl(String url) {
        try {
            String objectKey = getObjectKeyFromUrl(url);
            amazonS3.deleteObject(s3properties.getBucket(), objectKey);
        } catch (Exception e) {
            log.info("이미지 삭제 중 에러 발생 "+e.getMessage());
        }
    }

    /**
     * s3 주소 -> CDN 주소로 변환
     */
    public String s3AddressToCdnAddress(String s3Address) {
        // 정규 표현식을 정의
        Pattern pattern = Pattern.compile(s3properties.getS3UrlRegex());
        Matcher matcher = pattern.matcher(s3Address);

        // URL이 정규 표현식과 일치하면 변환
        if (matcher.matches()) {
            // image/ 이후 url 추출
            String extractedPath = matcher.group(1);
            // cdn URL 생성
            return s3properties.getCdnUrlPrefix() + extractedPath;
        }

        // 변환되지 않은 경우 예외
        throw new CommonException(ErrorCode.INVALID_VALUE, "이미지 업로드 실패했습니다.", "s3 url이 올바르지 않습니다.");
    }

    public Boolean isValidUrl(String url, String hashCode) {
        try {
            String objectKey = getObjectKeyFromUrl(url);

            // 업로드된 파일의 메타데이터를 가져옴
            GetObjectMetadataRequest metadataRequest = new GetObjectMetadataRequest(s3properties.getBucket(), objectKey);
            ObjectMetadata metadata = amazonS3.getObjectMetadata(metadataRequest);

            // S3에서 반환된 ETag와 클라이언트가 제공한 MD5 해시를 비교
            String uploadedETag = metadata.getETag();
            return uploadedETag.equals(hashCode);
        } catch (Exception e) {
            //TODO 무슨 에러가 어떻게 생길 줄 알고 에러를 내릴까
            return false;
        }
    }

    private String getObjectKeyFromUrl(String url) {
        try {
            URL urlObj = new URL(url);
            // URL에서 경로 부분을 가져오고 첫 번째 슬래시를 제거하여 반환
            return urlObj.getPath().substring(1);
        } catch (MalformedURLException e) {
            throw new CommonException(ErrorCode.INVALID_VALUE, "url 형식이 올바르지 않습니다.", "url 형식이 올바르지 않습니다.");
        }
    }
}
