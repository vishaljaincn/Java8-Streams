package java8.employee.queries;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public class EmployeeQueries {
    public static void main(String[] args) {
        Employee employee = new Employee(9, "vishal");
        employee.meow();
        System.out.println(Employee.address);
        Employee.address="oooaaa";
        System.out.println(Employee.address);

        List<Employee> employees = createEmployees();

        // 1. Male and Female employees
        maleAndFemale(employees);

        // 2. Print the name of all the departments in the organization
        departmentNames(employees);

        // 3. What is the average age of male and female employees?
        averageAgeOfMaleFemale(employees);

        // 4. Get the details of the highest-paid employee in the organization
        highestPaid(employees);

        // 5. Get names of all employees who have joined after 2015
        joinedAfter2015(employees);

        // 6. Count the number of employees in each department
        noOfEmployeesInEachDept(employees);

        // 7. What is the average salary of each department?
        avgSalaryOfEachDept(employees);

        // 8. Details of the youngest male employee in the product development department
        youngestInProdDevDept(employees);

        // 9. Who has the most working experience in the organization?
        mostWorkingExperience(employees);

        // 10. How many male and female employees are there in the Sales and Marketing team?
        maleFemaleCountInSalesTeam(employees);

        // 11. What is the average salary of male and female employees?
        avgSalaryOfMaleFemale(employees);

        // 12. List down the names of all employees in each department
        namesOfEmployeesInEachDept(employees);

        // 13. What is the average salary and total salary of the whole organization?
        avgAndTotalSalaryAndStats(employees);

        // 14a. Separate the employees who are younger or equal to 25 years from those employees who are older than 25 years
        partitionYoungAndOld(employees);

        // 14b. Separate the employees who are younger or equal to 25 years from those employees who are older than 25 years
        partitionYoungAndOldCount(employees);

        // 15. Who is the oldest employee in the organization? What is his age, and which department does he belong to?
        oldestEmployee(employees);
    }

    // 1. Male and Female employees
    private static void maleAndFemale(List<Employee> employees) {
        System.out.println("1----Male employees----");
        Set<Employee> maleEmployees = employees.stream()
                .filter(employee -> employee.gender().equalsIgnoreCase("male"))
                .collect(toSet());
        maleEmployees.forEach(System.out::println);

        System.out.println("1----Female employees----");
        Set<Employee> femaleEmployees = employees.stream()
                .filter(employee -> employee.gender().equalsIgnoreCase("female"))
                .collect(toSet());
        femaleEmployees.forEach(System.out::println);

        System.out.println("1----Male & Female employees----");
        Map<String, List<Employee>> genderGroups = getCollect(employees);
        genderGroups.forEach((gender, employeeList) -> {
            System.out.println(gender + ":");
            employeeList.forEach(System.out::println);
        });

        System.out.println("1----Male & Female employees: Count only----");
        Map<String, Long> groupByGenderCount = employees.stream()
                .collect(Collectors.groupingBy(Employee::gender, Collectors.counting()));
        System.out.println(groupByGenderCount);
    }

    // 2. Print the name of all the departments in the organization
    private static void departmentNames(List<Employee> employees) {
        System.out.println("2----Departments----");
        Set<String> departments = employees.stream()
                .map(Employee::department)
                .collect(toSet());
        System.out.println(departments);
    }

    // 3. What is the average age of male and female employees?
    private static void averageAgeOfMaleFemale(List<Employee> employees) {
        System.out.println("3----Average age of male and female employees------");
        Map<String, Double> ageGroupByGender = employees.stream()
                .collect(Collectors.groupingBy(Employee::gender, Collectors.averagingDouble(Employee::age)));
        ageGroupByGender.forEach((gender, avgAge) -> System.out.println(gender + ":" + avgAge.intValue()));
    }

    // 4. Get the details of the highest-paid employee in the organization
    private static void highestPaid(List<Employee> employees) {
        System.out.println("4----Highest-paid employee in the organization----");
        employees.stream().max(Comparator.comparing(Employee::salary))
                .ifPresentOrElse(System.out::println, () -> System.out.println("Highest-paid employee doesn't exist"));
    }

    // 5. Get names of all employees who have joined after 2015
    private static void joinedAfter2015(List<Employee> employees) {
        System.out.println("5----Names of all employees who have joined after 2015");
        employees.stream()
                .filter(employee -> employee.yearOfJoining() > 2015)
                .map(Employee::name)
                .collect(toSet())
                .forEach(System.out::println);
    }

    // 6. Count the number of employees in each department
    private static void noOfEmployeesInEachDept(List<Employee> employees) {
        System.out.println("6----Number of employees in each department");
        employees.stream()
                .collect(Collectors.groupingBy(Employee::department, Collectors.counting()))
                .forEach((dept, count) -> System.out.println(dept + ":" + count));
    }

    // 7. What is the average salary of each department?
    private static void avgSalaryOfEachDept(List<Employee> employees) {
        System.out.println("7----What is the average salary of each department?");
        employees.stream()
                .collect(Collectors.groupingBy(Employee::department, Collectors.averagingDouble(Employee::salary)))
                .forEach((dept, avgSalary) -> {
                    DecimalFormat df = new DecimalFormat("0.00");
                    System.out.println(dept + ":" + df.format(avgSalary));
                });
    }

    // 8. Details of the youngest male employee in the product development department
    private static void youngestInProdDevDept(List<Employee> employees) {
        System.out.println("8----Youngest male employee in the Product Development department");
        employees.stream()
                .filter(employee -> employee.department().equalsIgnoreCase("Product Development"))
                .filter(employee -> employee.gender().equalsIgnoreCase("Male"))
                .min(Comparator.comparing(Employee::age))
                .ifPresentOrElse(System.out::println, () -> System.out.println("No youngest male employee found"));
    }


    // 9. Who has the most working experience in the organization?
    private static void mostWorkingExperience(List<Employee> employees) {
        System.out.println("9----Who has the most working experience in the organization?");
        employees.stream()
                .min(Comparator.comparing(Employee::yearOfJoining))
                .ifPresentOrElse(System.out::println, () -> System.out.println("No experienced employee found"));
    }

    // 10. How many male and female employees are there in the Sales and Marketing team?
    private static void maleFemaleCountInSalesTeam(List<Employee> employees) {
        System.out.println("10----How many male and female employees are there in the Sales and Marketing team?");
        employees.stream()
                .filter(employee -> (employee.department().equalsIgnoreCase("Sales And Marketing")))
                .collect(Collectors.groupingBy(Employee::gender, Collectors.counting()))
                .forEach((gender, count) -> System.out.println(gender + ":" + count));
    }

    // 11. What is the average salary of male and female employees?
    private static void avgSalaryOfMaleFemale(List<Employee> employees) {
        System.out.println("11----What is the average salary of male and female employees?");
        employees.stream()
                .collect(Collectors.groupingBy(Employee::gender, Collectors.averagingDouble(Employee::salary)))
                .forEach((gender, avgSalary) -> System.out.println(gender + ":" + avgSalary.intValue()));
    }

    // 12. List down the names of all employees in each department
    private static void namesOfEmployeesInEachDept(List<Employee> employees) {
        System.out.println("12----List down the names of all employees in each department");
        employees.stream()
                .collect(Collectors.groupingBy(Employee::department))
                .forEach((dept, employeeList) -> System.out.println(dept + ": " + employeeList.stream()
                        .map(Employee::name)
                        .collect(Collectors.joining(", "))));
    }

    // 13. What is the average salary and total salary of the whole organization?
    private static void avgAndTotalSalaryAndStats(List<Employee> employees) {
        System.out.println("13----What is the average salary and total salary of the whole organization?");
        DecimalFormat df = new DecimalFormat("0.00");
        employees.stream().mapToDouble(Employee::salary).average()
                .ifPresent(avgSalary -> System.out.println("Average Salary: " + df.format(avgSalary)));

        double totalSalaryOfOrg = employees.stream().mapToDouble(Employee::salary).sum();
        System.out.println("Total Salary: " + totalSalaryOfOrg);

        DoubleSummaryStatistics stats = employees.stream()
                .collect(Collectors.summarizingDouble(Employee::salary));
        System.out.printf("""
                Stats -> Average: %s, Max: %s, Min: %s, Sum: %s
                """, df.format(stats.getAverage()), df.format(stats.getMax()), df.format(stats.getMin()), df.format(stats.getSum()));
    }

    // 14a. Separate the employees who are younger or equal to 25 years from those employees who are older than 25 years
    private static void partitionYoungAndOld(List<Employee> employees) {
        System.out.println("14b----Separate the employees who are younger or equal to 25 years from those who are older");

        // Partition employees by age (<= 25 and > 25)
        Map<Boolean, List<Employee>> partitionedByAge = employees.stream()
                .collect(Collectors.partitioningBy(employee -> employee.age() > 25));

        // Employees older than 25
        System.out.println("Employees older than 25:");
        partitionedByAge.get(true).forEach(System.out::println);

        // Employees younger than or equal to 25
        System.out.println("Employees younger than or equal to 25:");
        partitionedByAge.get(false).forEach(System.out::println);
    }

    // 14b.  How can we separate and count the number of employees who are older than 25 years and those who are younger than or equal to 25 years using Java Streams
    private static void partitionYoungAndOldCount(List<Employee> employees) {
        System.out.println("14a----Separate the employees who are younger or equal to 25 years from those who are older");

        // Partition employees by age (<= 25 and > 25) and get counts
        Map<Boolean, Long> countByAgeGroup = employees.stream()
                .collect(Collectors.partitioningBy(employee -> employee.age() > 25, Collectors.counting()));

        // Print the count of employees older than 25
        System.out.println("Employees older than 25: " + countByAgeGroup.get(true));

        // Print the count of employees younger than or equal to 25
        System.out.println("Employees younger than or equal to 25: " + countByAgeGroup.get(false));
    }

    // 15. Who is the oldest employee in the organization? What is his age, and which department does he belong to?
    private static void oldestEmployee(List<Employee> employees) {
        System.out.println("15----Oldest employee in the organization, his age, and department");
        employees.stream().max(Comparator.comparing(Employee::age))
                .ifPresentOrElse(emp -> System.out.println("Name: " + emp.name() + ", Age: " + emp.age() + ", Department: " + emp.department()),
                        () -> System.out.println("Oldest employee doesn't exist"));
    }

    private static Map<String, List<Employee>> getCollect(List<Employee> employees) {
        return employees.stream()
                .collect(Collectors.groupingBy(Employee::gender));
    }

    private static List<Employee> createEmployees() {
        List<Employee> employeeList = new ArrayList<>();

        employeeList.add(new Employee(111, "Jiya Brein", 32, "Female", "HR", 2011, 25000.0));
        employeeList.add(new Employee(122, "Paul Niksui", 25, "Male", "Sales And Marketing", 2015, 13500.0));
        employeeList.add(new Employee(133, "Martin Theron", 29, "Male", "Infrastructure", 2012, 18000.0));
        employeeList.add(new Employee(144, "Murali Gowda", 28, "Male", "Product Development", 2014, 32500.0));
        employeeList.add(new Employee(155, "Nima Roy", 27, "Female", "HR", 2013, 22700.0));
        employeeList.add(new Employee(166, "Iqbal Hussain", 43, "Male", "Security And Transport", 2016, 10500.0));
        employeeList.add(new Employee(177, "Manu Sharma", 35, "Male", "Account And Finance", 2010, 27000.0));
        employeeList.add(new Employee(188, "Wang Liu", 31, "Male", "Product Development", 2015, 34500.0));
        employeeList.add(new Employee(199, "Amelia Zoe", 24, "Female", "Sales And Marketing", 2016, 11500.0));
        employeeList.add(new Employee(200, "Jaden Dough", 38, "Male", "Security And Transport", 2015, 11000.5));
        employeeList.add(new Employee(211, "Jasna Kaur", 27, "Female", "Infrastructure", 2014, 15700.0));
        employeeList.add(new Employee(222, "Nitin Joshi", 25, "Male", "Product Development", 2016, 28200.0));
        employeeList.add(new Employee(233, "Jyothi Reddy", 27, "Female", "Account And Finance", 2013, 21300.0));
        employeeList.add(new Employee(244, "Nicolus Den", 24, "Male", "Sales And Marketing", 2017, 10700.5));
        employeeList.add(new Employee(255, "Ali Baig", 23, "Male", "Infrastructure", 2018, 12700.0));
        employeeList.add(new Employee(266, "Sanvi Pandey", 26, "Female", "Product Development", 2015, 28900.0));
        employeeList.add(new Employee(277, "Anuj Chettiar", 31, "Male", "Product Development", 2012, 35700.0));
        return employeeList;
    }
}

