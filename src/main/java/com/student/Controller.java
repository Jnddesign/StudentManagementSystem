package com.student;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class Controller {

    // ── FXML-injected controls ───────────────────────────────────────────────
    @FXML private TextField              txtName;
    @FXML private TextField              txtCourse;
    @FXML private ChoiceBox<YearLevel>   cbYear;

    @FXML private TableView<Student>              table;
    @FXML private TableColumn<Student, Integer>   colId;
    @FXML private TableColumn<Student, String>    colName;
    @FXML private TableColumn<Student, String>    colCourse;
    @FXML private TableColumn<Student, String>    colYear;

    @FXML private Label lblStatus;   // optional — shows feedback messages
    // ─────────────────────────────────────────────────────────────────────────

    private final ObservableList<Student> studentList = FXCollections.observableArrayList();
    private Connection conn;
    private int selectedId = -1;   // -1 means "no row selected"

    // ── Lifecycle ────────────────────────────────────────────────────────────

    @FXML
    public void initialize() {
        conn = DBConnection.connect();
        if (conn == null) {
            showStatus("❌ Could not connect to database. Check DBConnection.java.", true);
            return;
        }


        cbYear.getItems().setAll(YearLevel.values());


        colId.setCellValueFactory(    data -> data.getValue().idProperty().asObject());
        colName.setCellValueFactory(  data -> data.getValue().nameProperty());
        colCourse.setCellValueFactory(data -> data.getValue().courseProperty());
        colYear.setCellValueFactory(  data -> data.getValue().yearLevelProperty());

        table.setItems(studentList);

        table.setOnMouseClicked(e -> {
            Student selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selectedId = selected.getId();
                txtName.setText(selected.getName());
                txtCourse.setText(selected.getCourse());
                cbYear.setValue(YearLevel.fromString(selected.getYearLevel()));
                showStatus("Row selected — edit fields then click Update.", false);
            }
        });

        loadData();
    }


    private void loadData() {
        studentList.clear();
        try {
            String    query = "SELECT * FROM students ORDER BY id";
            ResultSet rs    = conn.createStatement().executeQuery(query);
            while (rs.next()) {
                studentList.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("course"),
                        rs.getString("year_level")
                ));
            }
            rs.close();
        } catch (Exception e) {
            showStatus("❌ Failed to load data: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }


    @FXML
    private void addStudent() {
        if (!validateInputs()) return;

        try {
            String sql = "INSERT INTO students (name, course, year_level) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, txtName.getText().trim());
            pst.setString(2, txtCourse.getText().trim());
            pst.setString(3, cbYear.getValue().toString());
            pst.executeUpdate();
            pst.close();

            showStatus("✅ Student added successfully.", false);
            loadData();
            clearFields();
        } catch (Exception e) {
            showStatus("❌ Add failed: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    @FXML
    private void updateStudent() {
        if (selectedId == -1) {
            showStatus("⚠️ Please select a row in the table first.", true);
            return;
        }
        if (!validateInputs()) return;

        try {
            String sql = "UPDATE students SET name=?, course=?, year_level=? WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, txtName.getText().trim());
            pst.setString(2, txtCourse.getText().trim());
            pst.setString(3, cbYear.getValue().toString());
            pst.setInt(4, selectedId);
            pst.executeUpdate();
            pst.close();

            showStatus("✅ Student updated successfully.", false);
            loadData();
            clearFields();
        } catch (Exception e) {
            showStatus("❌ Update failed: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteStudent() {
        if (selectedId == -1) {
            showStatus("⚠️ Please select a row in the table first.", true);
            return;
        }

        // Confirmation dialog
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this student?",
                ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                try {
                    String sql = "DELETE FROM students WHERE id=?";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setInt(1, selectedId);
                    pst.executeUpdate();
                    pst.close();

                    showStatus("✅ Student deleted.", false);
                    loadData();
                    clearFields();
                } catch (Exception e) {
                    showStatus("❌ Delete failed: " + e.getMessage(), true);
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void clearFields() {
        txtName.clear();
        txtCourse.clear();
        cbYear.setValue(null);
        selectedId = -1;
        table.getSelectionModel().clearSelection();
        showStatus("Fields cleared.", false);
    }



    private boolean validateInputs() {
        if (txtName.getText().trim().isEmpty() ||
                txtCourse.getText().trim().isEmpty() ||
                cbYear.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Validation");
            alert.setHeaderText("All fields are required.");
            alert.setContentText("Please fill in Name, Course, and Year Level before continuing.");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private void showStatus(String message, boolean isError) {
        if (lblStatus != null) {
            lblStatus.setText(message);
            lblStatus.setStyle(isError
                    ? "-fx-text-fill: #d32f2f;"
                    : "-fx-text-fill: #388e3c;");
        }
        System.out.println(message);
    }
}