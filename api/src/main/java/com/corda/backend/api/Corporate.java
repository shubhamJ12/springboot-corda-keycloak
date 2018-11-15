package com.corda.backend.api;

import com.corda.backend.model.ClientCorporate;
import org.json.JSONException;
import org.springframework.web.client.RestClientException;

public interface Corporate {
    public void createCordaTranscation(ClientCorporate clientCorporate)throws RestClientException;
}
