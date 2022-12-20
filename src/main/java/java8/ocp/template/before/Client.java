package java8.ocp.template.before;

public class Client {

        public static void main(String[] args) {
            Employee permanentEmployee =  new Employee("John", 30, 10000, "permanent");
            System.out.println(permanentEmployee.income());

            Employee contractEmployee =  new Employee("John", 30, 10000, "contract");
            System.out.println(contractEmployee.income());

        }
}
