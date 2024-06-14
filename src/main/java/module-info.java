module org.example.datavid_cake_tracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    requires org.controlsfx.controls;

    opens org.example.datavid_cake_tracker to javafx.fxml;
    opens org.example.datavid_cake_tracker.Model to javafx.base;
    exports org.example.datavid_cake_tracker;
}