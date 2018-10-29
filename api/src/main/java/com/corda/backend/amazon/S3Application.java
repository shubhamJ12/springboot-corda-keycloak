
package com.corda.backend.amazon;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.corda.backend.web.rest.UserResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class S3Application {

    //  private  static final AWSCredentials credentials;
    private static String bucketName;


    @Autowired
    AWSS3Service awsService;

    private final Logger log = LoggerFactory.getLogger(S3Application.class);
    private static AmazonS3 s3client;

    static {
        //put your accesskey and secretkey here
      /*  credentials = new BasicAWSCredentials(
            "AKIAJSFO3V7JKK6A43HA",
            "dP64QEUbpx62TGkr1vCpRiarvrSnf6mxX98dAuIN"
        )*/
        ;


       /* s3client= AmazonS3ClientBuilder
            .standard().withRegion(Regions.US_EAST_1).build();

      */   /*s3client = AmazonS3ClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.US_EAST_1) // N.virginia // https://docs.aws.amazon.com/general/latest/gr/rande.html
            .build();*/


    }


    public void putDocumentS3(String filePath, String key) {
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider().getCredentials();

            log.debug("ss  credentials  "+credentials);
        } catch (Exception e) {
            throw new AmazonClientException(
                "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (~/.aws/credentials), and is in valid format.",
                e);
        }
        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.US_EAST_1)
            .build();


        awsService.setS3client(s3);

        //  AWSS3Service awsService = new AWSS3Service();
        // setting up default client taking key from environment variable
        // https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-credentials.html
        bucketName = "sanjeevbuckettest";


        //list all the buckets
        for (Bucket s : awsService.listBuckets()) {
            System.out.println(s.getName());
        }

        // uploading object
        awsService.putObject(
            bucketName,
            key,
            new File(filePath)
        );

        //listing objects
        ObjectListing objectListing = awsService.listObjects(bucketName);
        for (S3ObjectSummary os : objectListing.getObjectSummaries()) {
            log.debug("save os.getKey( : {}", os.getKey());
            System.out.println(os.getKey());
        }

        //downloading an object
//        S3Object s3object = awsService.getObject(bucketName, "Document/hello.txt");
//        S3ObjectInputStream inputStream = s3object.getObjectContent();
//       // FileUtils.copyInputStreamToFile(inputStream, new File("/Users/user/Desktop/hello.txt"));
//        Files.copy(inputStream,"/Users/user/Desktop/hello.txt", StandardCopyOption.REPLACE_EXISTING);


    }

   /* public static void main(String[] args) throws IOException {
        //set-up the client
        AmazonS3 s3client = AmazonS3ClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.US_EAST_1) // N.virginia // https://docs.aws.amazon.com/general/latest/gr/rande.html
            .build();
        AWSS3Service awsService = new AWSS3Service(s3client);

        //  AWSS3Service awsService = new AWSS3Service();
        // setting up default client taking key from environment variable
        // https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-credentials.html
        bucketName = "sanjeevbuckettest";


        //list all the buckets
        for(Bucket s : awsService.listBuckets() ) {
            System.out.println(s.getName());
        }

       // uploading object
        awsService.putObject(
                bucketName,
                "Document/hello.txt",
                new File("/Users/user/Document/hello.txt")
        );

        //listing objects
        ObjectListing objectListing = awsService.listObjects(bucketName);
        for(S3ObjectSummary os : objectListing.getObjectSummaries()) {
            System.out.println(os.getKey());
        }

        //downloading an object
//        S3Object s3object = awsService.getObject(bucketName, "Document/hello.txt");
//        S3ObjectInputStream inputStream = s3object.getObjectContent();
//       // FileUtils.copyInputStreamToFile(inputStream, new File("/Users/user/Desktop/hello.txt"));
//        Files.copy(inputStream,"/Users/user/Desktop/hello.txt", StandardCopyOption.REPLACE_EXISTING);

    }*/
}

