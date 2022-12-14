module open.api.aidre.cce109_final {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;
    requires com.jfoenix;
    requires nv.i18n;
    requires java.desktop;
    requires AnimateFX;

    opens open.api.aidre.UMCoV to javafx.fxml;
    exports open.api.aidre.UMCoV;
}

