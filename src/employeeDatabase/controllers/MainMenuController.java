package employeeDatabase.controllers;

import employeeDatabase.daos.EmployeeDAO;
import employeeDatabase.helpers.ConnectionManager;
import employeeDatabase.helpers.ReportManager;
import employeeDatabase.models.ChangesTracker;
import employeeDatabase.models.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.ResourceBundle;

//For initializing this program we need to implement interface "Initializable"
public class MainMenuController implements Initializable {

    //Defining all elements from MainMenu.fxml file
    public AnchorPane MainMenu;
    public TextField EmployeeID;
    public TextField Name;
    public TextField Surname;
    public TextField FathersName;
    public DatePicker DateOfBirth;
    public TextField JobPosition;
    public TextField Department;
    public TextField RoomNumber;
    public TextField WorkNumber;
    public TextField WorkEmail;
    public TextField Salary;
    public DatePicker ApprovalDay;
    public TextField Note;
    public TextField Search;
    public TextArea ResultConsole;
    public TextField SearchTracker;
    public TextField ReportNameField;

    //Defining table and columns(for storing employee table information from database) from MainMenu.fxml file
    @FXML
    private TableView<Employee> TableOfDatabase;
    @FXML
    private TableColumn<Employee, String> columnEmployeeID;
    @FXML
    private TableColumn<Employee, String> columnSurname;
    @FXML
    private TableColumn<Employee, String> columnName;
    @FXML
    private TableColumn<Employee, String> columnFathersName;
    @FXML
    private TableColumn<Employee, String> columnDateOfBirth;
    @FXML
    private TableColumn<Employee, String> columnJobPosition;
    @FXML
    private TableColumn<Employee, String> columnDepartment;
    @FXML
    private TableColumn<Employee, String> columnRoomNumber;
    @FXML
    private TableColumn<Employee, String> columnWorkNumber;
    @FXML
    private TableColumn<Employee, String> columnWorkEmail;
    @FXML
    private TableColumn<Employee, String> columnSalary;
    @FXML
    private TableColumn<Employee, String> columnApprovalDay;
    @FXML
    private TableColumn<Employee, String> columnNote;

    //Defining table and columns(for storing logs table information from database) from MainMenu.fxml file
    @FXML
    private TableView<ChangesTracker> TableChangesTracker;
    @FXML
    private TableColumn<ChangesTracker, String> columnEvent;
    @FXML
    private TableColumn<ChangesTracker, Date> columnDate;

    //Defining ObservableList for storing information in TableView from database tables
    private ObservableList<Employee> observableList = FXCollections.observableArrayList();
    private ObservableList<ChangesTracker> observableListCT = FXCollections.observableArrayList();

    //Upload information from database after the program starts
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UploadNewDataAndSearch();
        UploadNewDataAndSearchCT();
    }

    //Defining button "Add"
    public void buttonAdd() throws SQLException {

        //Age verification
        if (DateOfBirth.getValue().compareTo(LocalDate.now().minus(216, ChronoUnit.MONTHS)) > 0) {
            ResultConsole.clear();
            ResultConsole.setText("Error occurred while adding a new employee:\nno under-aged employee allowed!");
            //We can not change anything into the result console
            ResultConsole.setEditable(false);
        //Checking for NULL fields
        } else if (EmployeeID.getText() != null &&
                (Surname.getText() != null) &&
                (Name.getText() != null) &&
                (FathersName.getText() != null) &&
                (DateOfBirth.getValue() != null) &&
                (JobPosition.getText() != null) &&
                (Department.getText() != null) &&
                (RoomNumber.getText() != null) &&
                (WorkNumber.getText() != null) &&
                (WorkEmail.getText() != null) &&
                (Salary.getText() != null) &&
                (ApprovalDay.getValue() != null)) {

            //Checking for the same ID
            Connection connection = ConnectionManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSetEmployeeID = statement.executeQuery("SELECT * FROM employee.employee WHERE id_pracivnyka = " + EmployeeID.getText() + ";");

            //If we have the same ID
            if (resultSetEmployeeID.next()) {
                ResultConsole.clear();
                ResultConsole.setText("Error occurred while adding a new employee:\nthis ID already exists!");
                //We can not change anything into the result console
                ResultConsole.setEditable(false);
            //If we do not have the same ID, check for the same surname, name and father`s name
            } else {
                ResultSet resultSetSNF = statement.executeQuery("SELECT * FROM employee.employee WHERE prizvyshche = '" + Surname.getText() +
                        "' AND imia = '" + Name.getText() +
                        "' AND po_batkovi = '" + FathersName.getText() + "';");
                //If we have the same surname, name and father`s name
                if (resultSetSNF.next()){
                    ResultConsole.clear();
                    ResultConsole.setText("Error occurred while adding a new employee:\nthis employee already exists!");
                    //We can not change anything into the result console
                    ResultConsole.setEditable(false);
                //If we do not have the same surname, name and father`s name, we create new employee
                } else {
                    EmployeeDAO.create(new Employee(EmployeeID.getText(),
                            Surname.getText(),
                            Name.getText(),
                            FathersName.getText(),
                            String.valueOf(DateOfBirth.getValue()),
                            JobPosition.getText(),
                            Department.getText(),
                            RoomNumber.getText(),
                            WorkNumber.getText(),
                            WorkEmail.getText(),
                            Salary.getText(),
                            String.valueOf(ApprovalDay.getValue()),
                            Note.getText()));
                    //Printing message into the result console if operation was successful
                    ResultConsole.clear();
                    ResultConsole.setText("You have successfully added a new employee:\n\n" + Surname.getText() + " " + Name.getText());
                    //We can not change anything into the result console
                    ResultConsole.setEditable(false);
                }
            }
        } else {
            //Printing message into the result console if operation was not successful
            ResultConsole.clear();
            ResultConsole.setText("Error occurred while adding a new employee!");
            //We can not change anything into the result console
            ResultConsole.setEditable(false);
        }

        //Set value into employee TableView, delete all records and upload all employee table records from database
        ValuesForModelOfTable();
        TableOfDatabase.getItems().clear();
        UploadNewDataAndSearch();

        //Set value into changes tracker TableView, delete all records and upload all logs table records from database
        ValuesForModelOfTableCT();
        TableChangesTracker.getItems().clear();
        UploadNewDataAndSearchCT();

        //Delete values from text fields
        ClearField();
    }

    //Defining button "Update"
    public void buttonUpdate() throws SQLException {
        //Checking records for certain ID
        Connection connection = ConnectionManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSetEmployeeID = statement.executeQuery("SELECT * FROM employee.employee WHERE id_pracivnyka = " + EmployeeID.getText() + ";");

        //If we have this ID
        if (resultSetEmployeeID.next()) {
            EmployeeDAO.update(new Employee(EmployeeID.getText(),
                    Surname.getText(),
                    Name.getText(),
                    FathersName.getText(),
                    String.valueOf(DateOfBirth.getValue()),
                    JobPosition.getText(),
                    Department.getText(),
                    RoomNumber.getText(),
                    WorkNumber.getText(),
                    WorkEmail.getText(),
                    Salary.getText(),
                    String.valueOf(ApprovalDay.getValue()),
                    Note.getText()), EmployeeID.getText());

            //Printing message into the result console if operation was successful
            ResultConsole.clear();
            ResultConsole.setText("You have successfully updated the employee:\n\n" + Surname.getText() + " " + Name.getText());
            //We can not change anything into the result console
            ResultConsole.setEditable(false);

        } else {
            //Printing message into the result console if operation was not successful
            ResultConsole.clear();
            ResultConsole.setText("Error occurred while updating the employee!");
            //We can not change anything into the result console
            ResultConsole.setEditable(false);
        }

        //Set value into employee TableView, delete all records and upload all employee table records from database
        ValuesForModelOfTable();
        TableOfDatabase.getItems().clear();
        UploadNewDataAndSearch();

        //Set value into changes tracker TableView, delete all records and upload all logs table records from database
        ValuesForModelOfTableCT();
        TableChangesTracker.getItems().clear();
        UploadNewDataAndSearchCT();

        //Delete values from text fields
        ClearField();
    }

    //Defining button "Delete"
    public void buttonDelete() throws SQLException {
        //Checking records for certain ID
        Connection connection = ConnectionManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSetEmployeeID = statement.executeQuery("SELECT * FROM employee.employee WHERE id_pracivnyka = " + EmployeeID.getText() + ";");

        //If we have this ID
        if (resultSetEmployeeID.next()) {
            EmployeeDAO.delete(EmployeeID.getText());

            //Printing message into the result console if operation was successful
            ResultConsole.clear();
            ResultConsole.setText("You have successfully deleted the employee:\n\n" + Surname.getText() + " " + Name.getText());
            //We can not change anything into the result console
            ResultConsole.setEditable(false);

        } else {
            //Printing message into the result console if operation was not successful
            ResultConsole.clear();
            ResultConsole.setText("Error occurred while deleting the employee!");
            //We can not change anything into the result console
            ResultConsole.setEditable(false);
        }

        //Set value into employee TableView, delete all records and upload all employee table records from database
        ValuesForModelOfTable();
        TableOfDatabase.getItems().clear();
        UploadNewDataAndSearch();

        //Set value into changes tracker TableView, delete all records and upload all logs table records from database
        ValuesForModelOfTableCT();
        TableChangesTracker.getItems().clear();
        UploadNewDataAndSearchCT();

        //Delete values from text fields
        ClearField();
    }

    //Defining button "Clear"
    public void buttonClear() {
        //Delete values from text fields
        ClearField();
    }

    //Defining button "Create report"
    public void buttonCreateReport() {
        //Create new report about employee with following values
        ReportManager.getReport(MainMenu,
                 ReportNameField.getText(),
                 "Current Affairs\nOn The Company Employees",
                 "employee",
                 "employee",
                 new String[] {"ID",
                         "Surname",
                         "Name",
                         "Father`s name",
                         "Date of birth",
                         "Job position",
                         "Department",
                         "Room number",
                         "Work number",
                         "Work email",
                         "Salary",
                         "Approval day",
                         "Note"},
                 new String[] {"id_pracivnyka",
                         "prizvyshche",
                         "imia",
                         "po_batkovi",
                         "data_narodjennia",
                         "posada",
                         "viddil",
                         "nomer_kimnaty",
                         "slujbovyi_nomer",
                         "slujbovyi_email",
                         "zarplata",
                         "data_pryiniattia",
                         "prymitky"});

        //Clear all text fields and print message into the result console if operation was successful
        ReportNameField.clear();
        ResultConsole.clear();
        ResultConsole.setText("You have successfully created the report about the employees!");
        //We can not change anything into the result console
        ResultConsole.setEditable(false);
    }

    //Defining button "Create changes tracker report"
    public void buttonCreateReportCT() {
         //Create new changes tracker report with following values
         ReportManager.getReport(MainMenu,
                 ReportNameField.getText(),
                 "Changes tracker",
                 "employee",
                 "logs",
                 new String[] {"Event",
                         "Date"},
                 new String[] {"podia",
                         "dodano"});

        //Clear all text fields and print message into the result console if operation was successful
        ReportNameField.clear();
         ResultConsole.clear();
         ResultConsole.setText("You have successfully created the report about changes!");
         //We can not change anything into the result console
         ResultConsole.setEditable(false);
    }

    //Defining button "Log out"
    public void buttonLogOut() {
        try{
            //Change current stage on Login stage
            Stage stage = (Stage) MainMenu.getScene().getWindow();
            stage.close();
            Parent root = FXMLLoader.load(getClass().getResource("/employeeDatabase/views/Login.fxml"));
            stage.setTitle("MindIT");
            stage.setScene(new Scene(root, 300, 200));
            stage.setFullScreen(false);
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    //Defining method for upload and search information for TableOfDatabase
    private void UploadNewDataAndSearch() {
        try {
            //Connection to database
            Connection connection = ConnectionManager.getConnection();
            //Execute SQl query for selecting all records from employee table
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM employee.employee;");

            //Add all existing records to ObservableList from employee table columns
            while (resultSet.next()){
                observableList.add(new Employee(resultSet.getString("id_pracivnyka"),
                        resultSet.getString("prizvyshche"),
                        resultSet.getString("imia"),
                        resultSet.getString("po_batkovi"),
                        resultSet.getString("data_narodjennia"),
                        resultSet.getString("posada"),
                        resultSet.getString("viddil"),
                        resultSet.getString("nomer_kimnaty"),
                        resultSet.getString("slujbovyi_nomer"),
                        resultSet.getString("slujbovyi_email"),
                        resultSet.getString("zarplata"),
                        resultSet.getString("data_pryiniattia"),
                        resultSet.getString("prymitky")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Set to TableOfDatabase columns values from all text fields
        ValuesForModelOfTable();

        //Create employee FilteredList
        FilteredList<Employee> filteredList = new FilteredList<>(observableList, b -> true);

        //Take value from Search field
        Search.textProperty().addListener((observable, oldValue, newValue ) -> filteredList.setPredicate(employee -> {

            //If we do not have anything in Search field
            if (newValue == null || newValue.isEmpty()){
                return true;
            }

            //We can search something starting with small letter
            String lowerCaseFilter = newValue.toLowerCase();

            //Return record when we have coincidence in Search field and in TableView
            if (employee.getEmployeeID().toLowerCase().contains(lowerCaseFilter)){
                return true;
            } else if (employee.getSurname().toLowerCase().contains(lowerCaseFilter)){
                return true;
            } else if (employee.getName().toLowerCase().contains(lowerCaseFilter)){
                return true;
            } else if (employee.getFathersName().toLowerCase().contains(lowerCaseFilter)){
                return true;
            } else if (employee.getDateOfBirth().toLowerCase().contains(lowerCaseFilter)){
                return true;
            } else if (employee.getJobPosition().toLowerCase().contains(lowerCaseFilter)){
                return true;
            } else if (employee.getDepartment().toLowerCase().contains(lowerCaseFilter)){
                return true;
            } else if (employee.getRoomNumber().toLowerCase().contains(lowerCaseFilter)){
                return true;
            } else if (employee.getWorkNumber().toLowerCase().contains(lowerCaseFilter)){
                return true;
            } else if (employee.getWorkEmail().toLowerCase().contains(lowerCaseFilter)){
                return true;
            } else if (employee.getSalary().toLowerCase().contains(lowerCaseFilter)){
                return true;
            } else if (employee.getApprovalDay().toLowerCase().contains(lowerCaseFilter)){
                return true;
            } else if (employee.getNote().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else {
                return false;
            }
        }));

        //Create SortedList for needed record
        SortedList<Employee> sortedList = new SortedList<>(filteredList);

        sortedList.comparatorProperty().bind(TableOfDatabase.comparatorProperty());

        //Set that value into TableOfDatabase
        TableOfDatabase.setItems(sortedList);
    }

    //Defining method for upload and search information for TableChangesTracker
    private void UploadNewDataAndSearchCT() {
        try {
            //Connection to database
            Connection connection = ConnectionManager.getConnection();
            //Execute SQl query for selecting all records from employee table
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM employee.logs;");

            //Add all existing records to ObservableList from logs table columns
            while (resultSet.next()){
                observableListCT.add(new ChangesTracker(resultSet.getString("podia"),
                        resultSet.getString("dodano")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Set to TableOfDatabase columns values from all text fields
        ValuesForModelOfTableCT();

        //Create logs FilteredList
        FilteredList<ChangesTracker> filteredList = new FilteredList<>(observableListCT, b -> true);

        //Take value from Search field
        SearchTracker.textProperty().addListener((observable, oldValue, newValue ) -> filteredList.setPredicate(changesTracker -> {

            //If we do not have anything in Search field
            if (newValue == null || newValue.isEmpty()){
                return true;
            }

            //We can search something starting with small letter
            String lowerCaseFilter = newValue.toLowerCase();

            //Return record when we have coincidence in Search field and in TableView
            if (changesTracker.getEvent().toLowerCase().contains(lowerCaseFilter)){
                return true;
            } else if (changesTracker.getAdded().toLowerCase().contains(lowerCaseFilter)){
                return true;
            }  else {
                return false;
            }
        }));

        //Create SortedList for needed record
        SortedList<ChangesTracker> sortedList = new SortedList<>(filteredList);

        sortedList.comparatorProperty().bind(TableChangesTracker.comparatorProperty());

        //Set that value into TableChangesTracker
        TableChangesTracker.setItems(sortedList);
    }

    //Set to TableOfDatabase columns values from all text fields
    private void ValuesForModelOfTable() {
        columnEmployeeID.setCellValueFactory(new PropertyValueFactory<>("EmployeeID"));
        columnSurname.setCellValueFactory(new PropertyValueFactory<>("Surname"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        columnFathersName.setCellValueFactory(new PropertyValueFactory<>("FathersName"));
        columnDateOfBirth.setCellValueFactory(new PropertyValueFactory<>("DateOfBirth"));
        columnJobPosition.setCellValueFactory(new PropertyValueFactory<>("JobPosition"));
        columnDepartment.setCellValueFactory(new PropertyValueFactory<>("Department"));
        columnRoomNumber.setCellValueFactory(new PropertyValueFactory<>("RoomNumber"));
        columnWorkNumber.setCellValueFactory(new PropertyValueFactory<>("WorkNumber"));
        columnWorkEmail.setCellValueFactory(new PropertyValueFactory<>("WorkEmail"));
        columnSalary.setCellValueFactory(new PropertyValueFactory<>("Salary"));
        columnApprovalDay.setCellValueFactory(new PropertyValueFactory<>("ApprovalDay"));
        columnNote.setCellValueFactory(new PropertyValueFactory<>("Note"));

        TableOfDatabase.setItems(observableList);
    }

    //Set to TableChangesTracker columns values from all text fields
    private void ValuesForModelOfTableCT() {
        columnEvent.setCellValueFactory(new PropertyValueFactory<>("Event"));
        columnDate.setCellValueFactory(new PropertyValueFactory<>("Added"));

        TableChangesTracker.setItems(observableListCT);
    }

    //Deleting values from all text fields
    private void ClearField() {
        EmployeeID.clear();
        Surname.clear();
        Name.clear();
        FathersName.clear();
        DateOfBirth.setValue(null);
        JobPosition.clear();
        Department.clear();
        RoomNumber.clear();
        WorkNumber.clear();
        WorkEmail.clear();
        Salary.clear();
        ApprovalDay.setValue(null);
        Note.clear();
    }

    //Defining "Show on click" method
    public void ShowOnClick() {

        //Get selected item from TableOfDatabase
        Employee employee = TableOfDatabase.getSelectionModel().getSelectedItem();

        //Set values to text fields from selected item
        EmployeeID.setText(employee.getEmployeeID());
        Surname.setText(employee.getSurname());
        Name.setText(employee.getName());
        FathersName.setText(employee.getFathersName());

        //Transfer type String from database to type LocalDate into DatePicker field
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String DOB = employee.getDateOfBirth();
        LocalDate localDateDOB = LocalDate.parse(DOB, dateTimeFormatter);
        DateOfBirth.setValue(localDateDOB);

        JobPosition.setText(employee.getJobPosition());
        Department.setText(employee.getDepartment());
        RoomNumber.setText(employee.getRoomNumber());
        WorkNumber.setText(employee.getWorkNumber());
        WorkEmail.setText(employee.getWorkEmail());
        Salary.setText(employee.getSalary());

        //Transfer type String from database to type LocalDate into DatePicker field
        String AD = employee.getApprovalDay();
        LocalDate localDateAD = LocalDate.parse(AD, dateTimeFormatter);
        ApprovalDay.setValue(localDateAD);

        Note.setText(employee.getNote());
    }
}