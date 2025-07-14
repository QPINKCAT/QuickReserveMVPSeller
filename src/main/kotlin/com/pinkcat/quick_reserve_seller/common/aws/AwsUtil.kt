package com.pinkcat.quick_reserve_seller.common.aws

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.net.URL
import java.time.Duration

@Component
class AwsUtil(
    @Value("\${aws.region}")
    val region: String,
    @Value("\${aws.s3.bucket.product.image}")
    val productImageBucket: String,
    @Value("\${aws.access-key}")
    val accessKey: String,
    @Value("\${aws.secret-key}")
    val secretKey: String
) {
    private val presigner: S3Presigner

    init {
        val credential = AwsBasicCredentials.create(accessKey, secretKey)
        presigner = S3Presigner.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(credential))
            .build()
    }

    fun generateUploadUrl(
        objectKey: String,
        contentType: String,
        validFor: Duration = Duration.ofMinutes(15)
    ): URL {
        val putReq = PutObjectRequest.builder()
            .bucket(productImageBucket)
            .key(objectKey)
            .contentType(contentType)
            .build()

        val presignReq = PutObjectPresignRequest.builder()
            .signatureDuration(validFor)
            .putObjectRequest(putReq)
            .build()

        return presigner.presignPutObject(presignReq).url()
    }
}