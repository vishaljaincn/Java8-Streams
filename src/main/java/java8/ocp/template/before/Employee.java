package java8.ocp.template.before;

public class Employee {
    String name;
    int age;
    double salary;
    String type;

    public Employee(String name, int age, double salary, String type) {
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.type = type;
    }

    public double income() {
        return salary + calculateBonus();
    }

    private double calculateBonus() {
        if (type.equals("permanent")) {
            return salary * 0.1;
        } else if (type.equals("contract")) {
            return salary * 0.05;
        } else if (type.equals("intern")) {
            return 0;
        }
        return 0;
        // OCP VIOLATION:
        // For new employee types, we need to modify this class and add new if-else statements here.
    }
}
