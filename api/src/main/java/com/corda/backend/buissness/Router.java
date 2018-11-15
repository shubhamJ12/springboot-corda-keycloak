package com.corda.backend.buissness;

import com.corda.backend.amazon.S3Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.security.MessageDigest;

@Component
public class Router {

    @Autowired
    S3Application s3Applications;

    public void saveDocToS3Bucket(String filenamePath, String key) {
        s3Applications.putDocumentS3("G:\\sanjeev\\file\\test.txt", "QnKSUiYVKDSvPUnRLKmuxk9diJ6yS96r1TrAXzjTiBcCLA");

    }


    public String generateHash(String fileNamePath) {
        byte[] buffer = new byte[8192];
        int count;
        BufferedInputStream bis = null;
        byte[] hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            bis = new BufferedInputStream(new FileInputStream(fileNamePath));
            while ((count = bis.read(buffer)) > 0) {
                digest.update(buffer, 0, count);
            }
            bis.close();

            hash = digest.digest();
            System.out.println(new BASE64Encoder().encode(hash));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return new BASE64Encoder().encode(hash);
    }

    public void callCordaApplicationLayer() {

    }

    public void getHashForClientDatabase() {

    }

}
