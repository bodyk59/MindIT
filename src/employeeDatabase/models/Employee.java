//This model for getting and setting information about employee
package employeeDatabase.models;

public class Employee {
    private String EmployeeID,
            Surname,
            Name,
            FathersName,
            DateOfBirth,
            JobPosition,
            Department,
            RoomNumber,
            WorkNumber,
            WorkEmail,
            Salary,
            ApprovalDay,
            Note;

    public Employee() {
    }

    public Employee(String employeeID,
                    String surname,
                    String name,
                    String fathersName,
                    String dateOfBirth,
                    String jobPosition,
                    String department,
                    String roomNumber,
                    String workNumber,
                    String workEmail,
                    String salary,
                    String approvalDay,
                    String note) {
        EmployeeID = employeeID;
        Surname = surname;
        Name = name;
        FathersName = fathersName;
        DateOfBirth = dateOfBirth;
        JobPosition = jobPosition;
        Department = department;
        RoomNumber = roomNumber;
        WorkNumber = workNumber;
        WorkEmail = workEmail;
        Salary = salary;
        ApprovalDay = approvalDay;
        Note = note;
    }

    public String  getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(String employeeID) {
        EmployeeID = employeeID;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFathersName() {
        return FathersName;
    }

    public void setFathersName(String fathersName) {
        FathersName = fathersName;
    }

    public String  getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String  dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getJobPosition() {
        return JobPosition;
    }

    public void setJobPosition(String jobPosition) {
        JobPosition = jobPosition;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getRoomNumber() {
        return RoomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        RoomNumber = roomNumber;
    }

    public String getWorkNumber() {
        return WorkNumber;
    }

    public void setWorkNumber(String workNumber) {
        WorkNumber = workNumber;
    }

    public String getWorkEmail() {
        return WorkEmail;
    }

    public void setWorkEmail(String workEmail) {
        WorkEmail = workEmail;
    }

    public String getSalary() {
        return Salary;
    }

    public void setSalary(String salary) {
        Salary = salary;
    }

    public String  getApprovalDay() {
        return ApprovalDay;
    }

    public void setApprovalDay(String  approvalDay) {
        ApprovalDay = approvalDay;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }
}