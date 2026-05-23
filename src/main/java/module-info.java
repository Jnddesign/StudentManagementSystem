module com.student {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.student to javafx.fxml;
    exports com.student;
}