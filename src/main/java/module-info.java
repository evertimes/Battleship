module org.evertimes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.evertimes to javafx.fxml;
    exports org.evertimes;
}