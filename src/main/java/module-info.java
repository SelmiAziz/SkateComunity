module startSkate {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires java.desktop;

    opens view to javafx.fxml;
    exports startSkate;
}