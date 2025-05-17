module startSkate {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires java.desktop;
    requires java.management;
    requires transitive mysql.connector.j;

    opens view to javafx.fxml;
    opens viewBasic to javafx.fxml;
    exports startskate;

}