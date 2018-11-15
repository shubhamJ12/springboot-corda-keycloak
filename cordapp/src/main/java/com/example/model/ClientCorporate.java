package com.example.model;

import java.util.Date;

public class ClientCorporate {
     String corporate_id;
     String corporate_name;

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

 /*   @java.lang.Override
    public java.lang.String toString() {
        return "ClientCorporate{" +
                "corporate_id='" + corporate_id + '\'' +
                ", corporate_name='" + corporate_name + '\'' +
                '}';
    }*/
}