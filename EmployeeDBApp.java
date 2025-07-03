import java.sql.*;
import java.util.Scanner;

public class EmployeeDBApp {

private static final String URL = "jdbc:mysql://localhost:3306/company";
private static final String USER = "root";
private static final String PASSWORD = "Harsh112183";


    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found: " + e.getMessage());
            return;
        }
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Employee Management ===");
            System.out.println("1. Add Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Update Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Exit");
            System.out.print("Choose option: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> addEmployee(sc);
                case 2 -> viewEmployees();
                case 3 -> updateEmployee(sc);
                case 4 -> deleteEmployee(sc);
                case 5 -> {
                    running = false;
                    System.out.println("Exiting.");
                }
                default -> System.out.println("Invalid choice.");
            }
        }
        sc.close();
    }

    private static void addEmployee(Scanner sc) {
        try (Connection conn = connect()) {
            System.out.print("Name: ");
            String name = sc.nextLine();
            System.out.print("Department: ");
            String dept = sc.nextLine();
            System.out.print("Salary: ");
            double salary = Double.parseDouble(sc.nextLine());

            String sql = "INSERT INTO employees (name, department, salary) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, dept);
            ps.setDouble(3, salary);

            int rows = ps.executeUpdate();
            System.out.println(rows + " employee(s) added.");
        } catch (Exception e) {
            System.out.println("Error adding employee: " + e.getMessage());
        }
    }

    private static void viewEmployees() {
        try (Connection conn = connect()) {
            String sql = "SELECT * FROM employees";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("\nID\tName\t\tDepartment\tSalary");
            while (rs.next()) {
                System.out.printf("%d\t%s\t\t%s\t\t%.2f%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getDouble("salary"));
            }
        } catch (Exception e) {
            System.out.println("Error viewing employees: " + e.getMessage());
        }
    }

    private static void updateEmployee(Scanner sc) {
        try (Connection conn = connect()) {
            System.out.print("Enter Employee ID to update: ");
            int id = Integer.parseInt(sc.nextLine());

            System.out.print("New Name: ");
            String name = sc.nextLine();
            System.out.print("New Department: ");
            String dept = sc.nextLine();
            System.out.print("New Salary: ");
            double salary = Double.parseDouble(sc.nextLine());

            String sql = "UPDATE employees SET name=?, department=?, salary=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, dept);
            ps.setDouble(3, salary);
            ps.setInt(4, id);

            int rows = ps.executeUpdate();
            System.out.println(rows + " employee(s) updated.");
        } catch (Exception e) {
            System.out.println("Error updating employee: " + e.getMessage());
        }
    }

    private static void deleteEmployee(Scanner sc) {
        try (Connection conn = connect()) {
            System.out.print("Enter Employee ID to delete: ");
            int id = Integer.parseInt(sc.nextLine());

            String sql = "DELETE FROM employees WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            System.out.println(rows + " employee(s) deleted.");
        } catch (Exception e) {
            System.out.println("Error deleting employee: " + e.getMessage());
        }
    }
}
