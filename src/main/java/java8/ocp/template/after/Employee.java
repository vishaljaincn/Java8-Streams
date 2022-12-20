package java8.ocp.template.after;

public abstract class Employee {
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

    abstract double calculateBonus();
}
