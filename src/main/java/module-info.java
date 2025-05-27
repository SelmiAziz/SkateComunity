module startSkate {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires java.desktop;
    requires java.management;
    requires transitive mysql.connector.j;
    requires java.xml.crypto;

    opens view to javafx.fxml;
    opens viewbasic to javafx.fxml;
    exports startskate;

}