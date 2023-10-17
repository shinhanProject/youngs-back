package com.youngs.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@RequiredArgsConstructor
@Service
public class S3Service {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    /**
     * S3에 해당하는 키의 값으로 데이터 반환
     * @param key 조회할 파일의 키 값
     * @return 조회한 데이터 값
     */
    public InputStream downloadObject(String key) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);

        S3Object s3Object = amazonS3Client.getObject(getObjectRequest);
        return s3Object.getObjectContent();
    }

}
