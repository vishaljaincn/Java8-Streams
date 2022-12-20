package java8.ocp.template.after;

public class Client {
    public static void main(String[] args) {
        Employee permanentEmployee =  new PermanentEmployee("John", 30, 10000);
        System.out.println(permanentEmployee.income());

        Employee contractEmployee =  new ContractEmployee("John", 30, 10000);
        System.out.println(contractEmployee.income());

    }
}
