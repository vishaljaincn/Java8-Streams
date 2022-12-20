package java8.ocp.template.after;

public class ContractEmployee extends Employee {
    public ContractEmployee(String name, int age, double salary) {
        super(name, age, salary, "contract");
    }

    @Override
    double calculateBonus() {
        return salary * 0.05;
    }
}
