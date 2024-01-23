package com.drugbox.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ImageService {

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;
    private final Storage storage;

    public String uploadImage(MultipartFile image) throws IOException {
        String uuid = UUID.randomUUID().toString(); // Google Cloud Storage에 저장될 파일 이름
        String ext = image.getContentType();

        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, uuid)
                        .setContentType(ext)
                        .build(),
                image.getInputStream()
        );

        return uuid;
    }

    public String processImage(String image) {
        if (image == null) {
            return null;
        }
        if (image.startsWith("https://")) { // 구글 프로필 이미지 처리
            return image;
        }
        return "https://storage.googleapis.com/" + bucketName + "/" + image;
    }
}
