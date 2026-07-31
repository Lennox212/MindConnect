package com.example.MindConnect.Service;

import com.example.MindConnect.CustomExceptions.InvalidPictureException;
import com.example.MindConnect.CustomExceptions.PictureNotFoundException;
import com.example.MindConnect.CustomExceptions.PictureUploadException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import java.time.Duration;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor


public class S3Service {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;


    public String uploadFile(MultipartFile picture) {

        if (picture == null || picture.isEmpty()) {
            throw new PictureNotFoundException("Picture cannot be empty");
        }

        String contentType = picture.getContentType();

        String extension = getFileExtension(contentType);

        String objectKey = UUID.randomUUID().toString() + extension;

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .contentType(contentType)
                .build();

        try {
            s3Client.putObject(request, RequestBody.
                    fromInputStream(picture.getInputStream(),
                            picture.getSize()));

        } catch (IOException exception) {
            throw new PictureUploadException("Failed to upload picture", exception);
        }

        return objectKey;

    }

    private String getFileExtension(String contentType) {
        return switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> throw new InvalidPictureException("Unsupported image type");

        };
    }

    public void deleteFile(String objectKey) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        try {
            s3Client.deleteObject(request);

        } catch (SdkException exception) {
            throw new PictureUploadException("Failed to delete picture", exception);
        }

    }

    public String generatePresignedUrl(String objectKey) {
        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(20))
                    .getObjectRequest(request)
                    .build();

            PresignedGetObjectRequest presignedGetObjectRequest =
                    s3Presigner.presignGetObject(presignRequest);

            return presignedGetObjectRequest.url().toString();


        } catch (SdkException exception) {
            throw new PictureUploadException("Failed to generate profile picture URL.", exception);
        }
    }
}