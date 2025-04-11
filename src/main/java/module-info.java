module com.melliott.gtdfx.gtdclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires org.json;
    requires java.sql;


    opens com.melliott.gtdfx.gtdclient to javafx.fxml;
    exports com.melliott.gtdfx.gtdclient;
    exports com.melliott.gtdfx.gtdclient.model;
    opens com.melliott.gtdfx.gtdclient.model to javafx.fxml;
}