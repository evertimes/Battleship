module org.evertimes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;


    opens org.evertimes to javafx.fxml;
    exports org.evertimes;
    exports org.evertimes.ships;
    opens org.evertimes.ships to javafx.fxml;
}