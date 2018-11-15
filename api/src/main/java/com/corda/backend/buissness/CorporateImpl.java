package com.corda.backend.buissness;

import com.corda.backend.api.Corporate;
import com.corda.backend.config.CordaServiceConfiguration;
import com.corda.backend.model.ClientCorporate;
import com.corda.backend.web.rest.Controller;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.Response;
import java.util.Arrays;


@Component
public class CorporateImpl implements Corporate {
    @Autowired
    private CordaServiceConfiguration cordaServiceConfiguration;

    @Autowired
    private RestTemplate restTemplate;

    private final Logger log = LoggerFactory.getLogger(CorporateImpl.class);
    final String uri = cordaServiceConfiguration.getNodeServiceAddress() + "api/kyc/createCorporateTranscation";

    @Override
    public void createCordaTranscation(ClientCorporate clientCorporate) throws  RestClientException{

        try {
            HttpEntity<ClientCorporate> entity = getHTTPHeader(clientCorporate);
            log.debug("call cordapp");
            String response = restTemplate.exchange(
                uri, HttpMethod.POST, entity, String.class).getBody();
             log.debug("response from corda +",response);
        } catch (RestClientException e) {
            log.error("error occur in corda transcation"+e);
            throw new RestClientException(e.getMessage());

        }
    }

    private HttpEntity getHTTPHeader(ClientCorporate clientCorporate) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ClientCorporate> entity = new HttpEntity<ClientCorporate>(clientCorporate, headers);
        return entity;

    }
}
