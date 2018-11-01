
package com.corda.backend.amazon;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class AWSS3Service {
    private  AmazonS3 s3client;

   /* public AWSS3Service() {
        this.s3client =  AmazonS3ClientBuilder.defaultClient();
    }

    public AWSS3Service(AmazonS3 s3client) {
        this.s3client = s3client;
    }

    public AmazonS3 getS3client() {
        return s3client;
    }*/

    public AmazonS3 getS3client() {
        return s3client;
    }

    public void setS3client(AmazonS3 s3client) {
        this.s3client = s3client;
    }

    //is bucket exist?
    public boolean doesBucketExist(String bucketName) {
        return s3client.doesBucketExist(bucketName);
    }

    //create a bucket
    public Bucket createBucket(String bucketName) {
        return s3client.createBucket(bucketName);
    }

    //list all buckets
    public List<Bucket> listBuckets() {
        return s3client.listBuckets();
    }

    //delete a bucket
    public void deleteBucket(String bucketName) {
        s3client.deleteBucket(bucketName);
    }

    //uploading object
    public PutObjectResult putObject(String bucketName, String key, File file) {
        return s3client.putObject(bucketName, key, file);
    }

    //listing objects
    public ObjectListing listObjects(String bucketName) {
        return s3client.listObjects(bucketName);
    }

    //get an object
    public S3Object getObject(String bucketName, String objectKey) {
        return s3client.getObject(bucketName, objectKey);
    }

    //copying an object
    public CopyObjectResult copyObject(
        String sourceBucketName,
        String sourceKey,
        String destinationBucketName,
        String destinationKey
    ) {
        return s3client.copyObject(
            sourceBucketName,
            sourceKey,
            destinationBucketName,
            destinationKey
        );
    }

    //deleting an object
    public void deleteObject(String bucketName, String objectKey) {
        s3client.deleteObject(bucketName, objectKey);
    }

    //deleting multiple Objects
    public DeleteObjectsResult deleteObjects(DeleteObjectsRequest delObjReq) {
        return s3client.deleteObjects(delObjReq);
    }
}

