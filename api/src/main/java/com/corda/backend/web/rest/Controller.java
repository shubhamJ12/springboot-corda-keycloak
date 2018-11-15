package com.corda.backend.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.corda.backend.buissness.Router;
import com.corda.backend.config.CordaServiceConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/api")
public class Controller {
    @Autowired
    private CordaServiceConfiguration cordaServiceConfiguration;

    @Autowired
    private Router router;

    private final Logger log = LoggerFactory.getLogger(Controller.class);
    @GetMapping("/create-iou")
    @Timed
    public void createIOUS() throws Exception {
       String urls= cordaServiceConfiguration.getNodeServiceAddress()+"api/example/create-iou?iouValue=22&attachmentHashValue=QnKSUikVKDSvPUnRLKmIxk9diJ6yS96r1TrAXzjTiBcCLA&partyName=O=IBFS,L=New%20York,C=US&partyName2=O=Algeria,L=Paris,C=FR";
        log.debug("REST request to get User : {}", urls);
        System.out.print(urls);
        URL url = new URL(urls);
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        OutputStreamWriter out = new OutputStreamWriter(
            httpCon.getOutputStream());
        out.write("Resource content");
        out.close();
        httpCon.getInputStream();



    }
    @GetMapping("/save-document")
    @Timed
    public void saveDocument() throws Exception {
        router.saveDocToS3Bucket("","");
        log.debug("save doc : {}","done");


    }

}
