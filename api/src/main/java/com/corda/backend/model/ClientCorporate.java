package com.corda.backend.model;

import java.util.Date;

public class ClientCorporate {
    private String corporate_id;
    private String corporate_name;

    public ClientCorporate(String corporate_id, String corporate_name) {
        this.corporate_id = corporate_id;
        this.corporate_name = corporate_name;
    }

    public String getCorporate_id() {
        return corporate_id;
    }

    public void setCorporate_id(String corporate_id) {
        this.corporate_id = corporate_id;
    }

    public String getCorporate_name() {
        return corporate_name;
    }

    public void setCorporate_name(String corporate_name) {
        this.corporate_name = corporate_name;
    }


}
