module com.melliott.gtdfx.gtdclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires org.json;
    requires java.sql;
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires static lombok;
    requires spring.jcl;


    opens com.melliott.gtdfx.gtdclient;
    exports com.melliott.gtdfx.gtdclient;
    exports com.melliott.gtdfx.gtdclient.model;
    exports com.melliott.gtdfx.gtdclient.service;
    opens com.melliott.gtdfx.gtdclient.model;
    opens com.melliott.gtdfx.gtdclient.service;
}