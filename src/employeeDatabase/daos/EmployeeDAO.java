package employeeDatabase.daos;

import employeeDatabase.helpers.ConnectionManager;
import employeeDatabase.models.Employee;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EmployeeDAO {
    //method for adding a new employee into database
    public static void create(Employee employee) {
        Connection connection = ConnectionManager.getConnection();

        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO employee.employee VALUES (" + employee.getEmployeeID() +
                    ", '" + employee.getSurname() +
                    "', '" + employee.getName() +
                    "', '" + employee.getFathersName() +
                    "', '" + employee.getDateOfBirth() +
                    "', '" + employee.getJobPosition() +
                    "', '" + employee.getDepartment()+
                    "', '" + employee.getRoomNumber() +
                    "', '" + employee.getWorkNumber() +
                    "', '" + employee.getWorkEmail()+
                    "', " + employee.getSalary() +
                    ", '" + employee.getApprovalDay() +
                    "', '" + employee.getNote() +
                    "')");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    //method for retrieving the employee from database
    public static Employee retrieve(String id) {
        Connection connection = ConnectionManager.getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM employee.employee WHERE id_pracivnyka = " + id + ";");

            if(resultSet.next())
            {
                Employee employee = new Employee();

                employee.setEmployeeID(resultSet.getString("id_pracivnyka"));
                employee.setSurname(resultSet.getString("prizvyshche"));
                employee.setName(resultSet.getString("imia"));
                employee.setFathersName(resultSet.getString("po_batkovi"));
                employee.setDateOfBirth(resultSet.getString("data_narodjennia"));
                employee.setJobPosition(resultSet.getString("posada"));
                employee.setDepartment(resultSet.getString("viddil"));
                employee.setRoomNumber(resultSet.getString("nomer_kimnaty"));
                employee.setWorkNumber(resultSet.getString("slujbovyi_nomer"));
                employee.setWorkEmail(resultSet.getString("slujbovyi_email"));
                employee.setSalary(resultSet.getString("zarplata"));
                employee.setApprovalDay(resultSet.getString("data_pryiniattia"));
                employee.setNote(resultSet.getString("prymitky"));

                return employee;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //method for updating the employee from database
    public static void update(Employee employee, String id) {
        Connection connection = ConnectionManager.getConnection();

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE employee.employee SET " +
                    "id_pracivnyka = " + employee.getEmployeeID() +
                    ", prizvyshche = '" + employee.getSurname() +
                    "', imia = '" + employee.getName() +
                    "', po_batkovi = '" + employee.getFathersName() +
                    "', data_narodjennia = '" +  employee.getDateOfBirth() +
                    "', posada = '" + employee.getJobPosition() +
                    "', viddil = '" + employee.getDepartment() +
                    "',nomer_kimnaty = '" + employee.getRoomNumber() +
                    "', slujbovyi_nomer = '" + employee.getWorkNumber() +
                    "', slujbovyi_email = '" + employee.getWorkEmail() +
                    "', zarplata = " + employee.getSalary() +
                    ", data_pryiniattia = '" + employee.getApprovalDay() +
                    "', prymitky = '" + employee.getNote() +
                    "' WHERE id_pracivnyka = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //method for deleting the employee from database
    public static void delete(String id) {
        Connection connection = ConnectionManager.getConnection();

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE  FROM employee.employee WHERE id_pracivnyka = " + id + ";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}