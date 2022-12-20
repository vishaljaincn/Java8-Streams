package java8.ocp.template.after;

public class PermanentEmployee extends Employee {
    public PermanentEmployee(String name, int age, double salary) {
        super(name, age, salary, "permanent");
    }

    @Override
    double calculateBonus() {
        return salary * 0.1;
    }
}
