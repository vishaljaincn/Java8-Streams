package java8.employee.queries;


import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public class EmployeeQueries {
    public static void main(String[] args) {
        List<Employee> employees = createEmployees();

        // 1. Male and Female employees
        maleAndFemale(employees);

        //2. Print the name of all the dept in organization
        departmentNames(employees);

        //3. What is the average age of male and female employees
        avarageAgeOfMaleFemale(employees);

        //4. Get the details of highest paid employee in the organization
        highestPaid(employees);

        //5. Get names of all employees who have joined after 2015
        joinedAfter2015(employees);

        //6. Count the number of employees in each dept
        noOfEmployeesInEachDept(employees);

        //7. What is the average salary of each department?
        avgSalaryOfEachDept(employees);

        //8. Details of the youngest male employee in the product development department
        youngestInProdDevDept(employees);

        //9. Who has the most working experience in the organization
        mostWorkingExperience(employees);

        //10. How many male and female employees are there in the sales and marketing team
        maleFamaleCountInSalesTeam(employees);

        //11. What is the avg salary of male and female employees?
        avgSalaryOfMaleFemale(employees);

        //12. List down the names of all employees in each department
        namesOfEmployeesInEachDept(employees);

        //13. What is the average salary and total salary of the whole organization?
        avgAndTotalSalaryAndStats(employees);

        //14. Separate the employees who are younger or equal to 25 years from those employees who are older than 25 years.
        partitionYoundAndOld(employees);

        //15. Who is the oldest employee in the organization? What is his age and which department he belongs to?
        oldestEmployee(employees);
    }

    private static void oldestEmployee(List<Employee> employees) {
        System.out.println("15----Who is the oldest employee in the organization? What is his age and which department he belongs to?");
        //        employees.stream().mapToInt(Employee::age).max().ifPresent(); // Doesn't work because we need employee object
        Optional<Employee> oldestEmp = employees.stream().max(Comparator.comparing(Employee::age));
        oldestEmp.ifPresent(employee -> {
            System.out.println("name:" + employee.name());
            System.out.println("age:" + employee.age());
            System.out.println("dept:" + employee.department());
        });
    }

    private static void partitionYoundAndOld(List<Employee> employees) {
        System.out.println("14----Separate the employees who are younger or equal to 25 years from those employees who are older than 25 years.");
        Map<Boolean, List<Employee>> youngOldPartitions = employees.stream()
                .collect(Collectors.partitioningBy(employee -> employee.age() < 25));

        youngOldPartitions.forEach((k, v) -> {
            System.out.println(k ? "Young:" : "Old:");
            System.out.println(v.stream().map(Employee::name).collect(Collectors.joining(", ")));
        });
    }

    private static void avgAndTotalSalaryAndStats(List<Employee> employees) {
        System.out.println("13----What is the average salary and total salary of the whole organization");
        DecimalFormat df = new DecimalFormat("0.00");
//        Double avgSalary = employees.stream().collect(Collectors.averagingDouble(Employee::salary)); //WORKS too
        OptionalDouble averageOptional = employees.stream().mapToDouble(Employee::salary).average();
        System.out.println(df.format(averageOptional.getAsDouble()));

        double totalSalaryOfOrg = employees.stream().mapToDouble(Employee::salary).sum(); //<----
        System.out.println(totalSalaryOfOrg);

        DoubleSummaryStatistics stats = employees.stream().collect(Collectors.summarizingDouble(Employee::salary));
        System.out.printf("""
                        Average: %S,\
                        Max: %s,\
                        Min: %s,
                        Sum: %S
                        """,
                df.format(stats.getAverage()),
                df.format(stats.getMax()),
                df.format(stats.getMin()),
                df.format(stats.getSum()));
    }

    private static void namesOfEmployeesInEachDept(List<Employee> employees) {
        System.out.println("12----List down the names of all employees in each department");
        employees.stream()
                .collect(Collectors.groupingBy(Employee::department))
                .forEach((String dept, List<Employee> employeeList) ->
//                                System.out.println(dept+":"+ employeeList.stream().map(Employee::name).collect(toSet()))); // WORKS
                        System.out.println(dept + ": " + employeeList.stream().map(Employee::name).collect(Collectors.joining(", "))));
    }

    private static void avgSalaryOfMaleFemale(List<Employee> employees) {
        System.out.println("11----What is the avg salary of male and female employees");
        employees.stream()
                .collect(Collectors.groupingBy(Employee::gender, Collectors.averagingDouble(Employee::salary)))
                .forEach((gender, avgSalary) -> System.out.println(gender + ":" + avgSalary.intValue()));
    }

    private static void maleFamaleCountInSalesTeam(List<Employee> employees) {
        System.out.println("10----How many male and female employees are there in the sales and marketing team");
        employees.stream()
//                .filter(employee -> Stream.of("Sales And Marketing").anyMatch(s->s.equals(employee.department())))
                .filter(employee -> "Sales And Marketing".equals(employee.department()))
                .collect(Collectors.groupingBy(Employee::gender, Collectors.counting()))
                .forEach((gender, count) -> System.out.println(gender + ":" + count));
    }

    private static void mostWorkingExperience(List<Employee> employees) {
        System.out.println("9----Who has the most working experience in the organization");
        employees.stream()
                .filter(employee -> employee.gender().equals("Male"))
                .min(Comparator.comparing(Employee::yearOfJoining))
                .ifPresentOrElse(System.out::println, () -> System.out.println("Did not find the experienced male employee"));
    }

    private static void youngestInProdDevDept(List<Employee> employees) {
        System.out.println("8----Youngest male employee in the product development department");
        employees.stream()
                .filter(employee -> employee.department().equalsIgnoreCase("Product Development"))
                .min(Comparator.comparing(Employee::age))
                .ifPresentOrElse(System.out::println, () -> System.out.println("Did not find youngest male employee"));
    }

    private static void avgSalaryOfEachDept(List<Employee> employees) {
        System.out.println("7----What is the average salary of each department");

        employees.stream()
                .collect(Collectors.groupingBy(Employee::department, Collectors.averagingDouble(Employee::salary)))
                .forEach((dept, avgSalary) -> {
                    DecimalFormat df = new DecimalFormat("0.00");
                    System.out.println(dept + ":" + df.format(avgSalary));
                });
    }

    private static void noOfEmployeesInEachDept(List<Employee> employees) {
        System.out.println("6----Number of employees in each dept");
        employees.stream()
                .collect(Collectors.groupingBy(Employee::department, Collectors.counting()))
                .forEach((dept, count) -> System.out.println(dept + ":" + count));
    }

    private static void joinedAfter2015(List<Employee> employees) {
        System.out.println("5----Names of all employees who have joined after 2015");
        employees.stream()
                .filter(employee -> employee.yearOfJoining() > 2015)
                .map(Employee::name)
                .collect(toSet())
                .forEach(System.out::println);
    }

    private static void highestPaid(List<Employee> employees) {
        System.out.println("4----Highest paid employee in the organization----");
        Optional<Employee> highestPaid = employees.stream().max(Comparator.comparing(Employee::salary));
        highestPaid.ifPresentOrElse(System.out::println, () -> System.out.println("Highest paid doesn't exist"));
    }

    private static void avarageAgeOfMaleFemale(List<Employee> employees) {
        System.out.println("3----Average age of male and female employees------");
        Map<String, List<Employee>> groupByGender = employees.stream().collect(Collectors.groupingBy(Employee::gender));
        for (Map.Entry<String, List<Employee>> group : groupByGender.entrySet()) {
            Double age = group.getValue().stream().collect(Collectors.averagingInt(Employee::age));
            System.out.println(group.getKey() + ":" + age.intValue());
        }

        System.out.println("3----Average age of male and female employees------");
        Map<String, Double> ageGroupByGender =
                employees.stream()
                        .collect(Collectors.groupingBy(Employee::gender, Collectors.averagingDouble(Employee::age)));
        ageGroupByGender.forEach((k, v) -> System.out.println(k + ":" + v.intValue()));

        // Note: Collectors.groupingBy we get List of elements; Use second argument to change it to something else like count, average,..

        // Note:
        /*
        Different ways of iterating map:
        ageGroupByGender.entrySet().stream().forEach(Map.Entry)
        ageGroupByGender.entrySet().forEach(Map.Entry)
        ageGroupByGender.forEach(k,v)
         */
    }

    private static void departmentNames(List<Employee> employees) {
        System.out.println("2----Departments----");
        Set<String> departments =
                employees.stream()
                        .map(Employee::department)
                        .collect(toSet());
        System.out.println(departments);
    }

    private static void maleAndFemale(List<Employee> employees) {
        System.out.println("1----Male employees----");
        Set<Employee> maleEmployees =
                employees.stream()
                        .filter(employee -> employee.gender().equalsIgnoreCase("male"))
                        .collect(toSet());
        maleEmployees.forEach(System.out::println);

        System.out.println("1----Female employees----");
        Set<Employee> feMaleEmployees =
                employees.stream()
                        .filter(employee -> employee.gender().equalsIgnoreCase("female"))
                        .collect(toSet());
        feMaleEmployees.forEach(System.out::println);

        System.out.println("1----Male&Female employees----");
        Map<String, List<Employee>> genderGroups = getCollect(employees);
        Set<String> genders = genderGroups.keySet();
        System.out.println(genders);
        for (Map.Entry<String, List<Employee>> group : genderGroups.entrySet()) { // Note: Instead we can also use genderGroups.forEach(...)

            System.out.println(group.getKey() + ":");
            group.getValue().stream().forEach(System.out::println);
        }

        System.out.println("1----Male&Female employees: Count only----");
        Map<String, Long> groupByGendercount =
                employees.stream()
                        .collect(Collectors.groupingBy(Employee::gender, Collectors.counting()));
        System.out.println(groupByGendercount);
    }

    private static Map<String, List<Employee>> getCollect(List<Employee> employees) {
        return employees.stream()
                .collect(Collectors.groupingBy(Employee::gender));
    }

    private static List<Employee> createEmployees() {
        List<Employee> employeeList = new ArrayList<Employee>();

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
