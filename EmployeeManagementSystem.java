import java.io.*;
import java.util.*;

public class EmployeeManagementSystem {

    static final String FILE_NAME = "employees.txt";

    // Employee class
    static class Employee {
        String id;
        String name;
        String department;
        double salary;

        Employee(String id, String name,
                 String department, double salary) {
            this.id = id;
            this.name = name;
            this.department = department;
            this.salary = salary;
        }

        public String toString() {
            return id + " | " + name + " | "
                    + department + " | " + salary;
        }
    }

    // Node class
    static class Node {
        Employee employee;
        Node next;

        Node(Employee employee) {
            this.employee = employee;
            this.next = null;
        }
    }

    // Custom Singly Linked List
    static class EmployeeList {

        Node head;
        int nextId = 101;

        // HashMap for searching employee by ID
        HashMap<String, Employee> employeeMap =
                new HashMap<>();

        // Add employee
        void addEmployee(String name, String department,
                         double salary) {

            String id = "E" + nextId++;

            Employee emp = new Employee(
                    id, name, department, salary);

            Node newNode = new Node(emp);

            if (head == null) {
                head = newNode;
            } else {
                Node temp = head;

                while (temp.next != null) {
                    temp = temp.next;
                }

                temp.next = newNode;
            }

            employeeMap.put(id, emp);

            System.out.println(
                    "Employee added. ID: " + emp);
        }

        // Display all employees
        void displayEmployees() {

            if (head == null) {
                System.out.println("No employees found.");
                return;
            }

            Node temp = head;

            System.out.println("\n--- Employee List ---");

            while (temp != null) {
                System.out.println(temp.employee);
                temp = temp.next;
            }
        }

        // Search employee by ID
        void searchById(String id) {

            Employee emp = employeeMap.get(id);

            if (emp == null) {
                System.out.println(
                        "Employee " + id + " not found.");
            } else {
                System.out.println(emp);
            }
        }

        // Search employee by Name
        void searchByName(String name) {

            Node temp = head;
            boolean found = false;

            while (temp != null) {

                if (temp.employee.name.toLowerCase()
                        .contains(name.toLowerCase())) {

                    System.out.println(temp.employee);
                    found = true;
                }

                temp = temp.next;
            }

            if (!found) {
                System.out.println(
                        "Employee " + name + " not found.");
            }
        }

        // Delete employee
        void deleteEmployee(String id) {

            if (head == null) {
                System.out.println("Employee list is empty.");
                return;
            }

            if (head.employee.id.equalsIgnoreCase(id)) {

                head = head.next;
                employeeMap.remove(id);

                System.out.println(
                        "Employee " + id + " deleted.");
                System.out.println(
                        "Total employees: " + countEmployees());
                return;
            }

            Node temp = head;

            while (temp.next != null &&
                    !temp.next.employee.id.equalsIgnoreCase(id)) {

                temp = temp.next;
            }

            if (temp.next == null) {
                System.out.println(
                        "Employee " + id + " not found.");
                return;
            }

            temp.next = temp.next.next;
            employeeMap.remove(id);

            System.out.println(
                    "Employee " + id + " deleted.");
            System.out.println(
                    "Total employees: " + countEmployees());
        }

        // Count employees
        int countEmployees() {

            int count = 0;
            Node temp = head;

            while (temp != null) {
                count++;
                temp = temp.next;
            }

            return count;
        }

        // Sort salary from high to low
        void sortBySalary() {

            if (head == null || head.next == null) {
                System.out.println("Not enough employees to sort.");
                return;
            }

            boolean swapped;

            do {
                swapped = false;
                Node temp = head;

                while (temp.next != null) {

                    if (temp.employee.salary <
                            temp.next.employee.salary) {

                        Employee t = temp.employee;
                        temp.employee = temp.next.employee;
                        temp.next.employee = t;

                        swapped = true;
                    }

                    temp = temp.next;
                }

            } while (swapped);

            System.out.println(
                    "Employees sorted by salary (High to Low).");

            displayEmployees();
        }

        // Load employees from file
        void loadFromFile() {

            File file = new File(FILE_NAME);

            if (!file.exists()) {
                return;
            }

            try (BufferedReader br =
                    new BufferedReader(new FileReader(file))) {

                String line;

                while ((line = br.readLine()) != null) {

                    String[] data = line.split("\\|", -1);

                    if (data.length != 4) {
                        continue;
                    }

                    String id = data[0].trim();
                    String name = data[1].trim();
                    String department = data[2].trim();

                    try {
                        double salary =
                                Double.parseDouble(data[3].trim());

                        if (salary <= 0 ||
                                employeeMap.containsKey(id)) {
                            continue;
                        }

                        Employee emp = new Employee(
                                id, name, department, salary);

                        Node newNode = new Node(emp);

                        if (head == null) {
                            head = newNode;
                        } else {
                            Node temp = head;

                            while (temp.next != null) {
                                temp = temp.next;
                            }

                            temp.next = newNode;
                        }

                        employeeMap.put(id, emp);

                        if (id.matches("E\\d+")) {
                            int number = Integer.parseInt(
                                    id.substring(1));

                            if (number >= nextId) {
                                nextId = number + 1;
                            }
                        }

                    } catch (NumberFormatException e) {
                        System.out.println(
                                "Skipping invalid employee record.");
                    }
                }

                System.out.println(
                        "Employee records loaded successfully.");

            } catch (IOException e) {
                System.out.println(
                        "Error loading file: " + e.getMessage());
            }
        }

        // Save employees to file
        void saveToFile() {

            try (BufferedWriter bw =
                    new BufferedWriter(new FileWriter(FILE_NAME))) {

                Node temp = head;

                while (temp != null) {

                    Employee emp = temp.employee;

                    bw.write(emp.id + "|" +
                            emp.name + "|" +
                            emp.department + "|" +
                            emp.salary);

                    bw.newLine();

                    temp = temp.next;
                }

                System.out.println(
                        "Employee records saved successfully.");

            } catch (IOException e) {
                System.out.println(
                        "Error saving file: " + e.getMessage());
            }
        }
    }

    // Main method
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        EmployeeList list = new EmployeeList();

        // Load existing data
        list.loadFromFile();

        int choice = 0;

        do {
            System.out.println("\n================================");
            System.out.println("   EMPLOYEE MANAGEMENT SYSTEM");
            System.out.println("================================");
            System.out.println("1. Add Employee");
            System.out.println("2. Display All Employees");
            System.out.println("3. Search Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Sort by Salary (High to Low)");
            System.out.println("6. Exit");
            System.out.println("================================");

            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid menu choice.");
                continue;
            }

            switch (choice) {

                case 1:

                    System.out.print("Enter Name: ");
                    String name = sc.nextLine().trim();

                    System.out.print("Enter Department: ");
                    String department = sc.nextLine().trim();

                    if (name.isEmpty() || department.isEmpty()
                            || name.contains("|")
                            || department.contains("|")) {
                        System.out.println("Invalid employee details.");
                        break;
                    }

                    System.out.print("Enter Salary: ");

                    try {
                        double salary =
                                Double.parseDouble(sc.nextLine().trim());

                        if (salary <= 0) {
                            System.out.println(
                                    "Salary must be greater than zero.");
                            break;
                        }

                        list.addEmployee(
                                name, department, salary);

                    } catch (NumberFormatException e) {
                        System.out.println("Invalid salary.");
                    }

                    break;

                case 2:

                    list.displayEmployees();
                    break;

                case 3:

                    System.out.println("1. Search by ID");
                    System.out.println("2. Search by Name");
                    System.out.print("Enter choice: ");

                    String searchChoice = sc.nextLine().trim();

                    if (searchChoice.equals("1")) {

                        System.out.print("Enter Employee ID: ");
                        String id = sc.nextLine().trim();

                        list.searchById(id);

                    } else if (searchChoice.equals("2")) {

                        System.out.print("Enter Employee Name: ");
                        String empName = sc.nextLine().trim();

                        list.searchByName(empName);

                    } else {
                        System.out.println("Invalid search choice.");
                    }

                    break;

                case 4:

                    System.out.print("Enter Employee ID to delete: ");
                    String deleteId = sc.nextLine().trim();

                    list.deleteEmployee(deleteId);
                    break;

                case 5:

                    list.sortBySalary();
                    break;

                case 6:

                    list.saveToFile();

                    System.out.println(
                            "Thank you for using Employee Management System.");

                    break;

                default:

                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 6);

        sc.close();
    }
}


// ================================
//    EMPLOYEE MANAGEMENT SYSTEM
// ================================
// 1. Add Employee
// 2. Display All Employees
// 3. Search Employee
// 4. Delete Employee
// 5. Sort by Salary (High to Low)
// 6. Exit
// ================================
// Enter your choice: 1
// Enter Name: Aditya
// Enter Department: abc
// Enter Salary: 10000
// Employee added. ID: E102 | Aditya | abc | 10000.0
