package java8.employee.queries;

public record Employee(int id, String name, int age, String gender, String department,
                       int yearOfJoining,
                       double salary) {
    static String address = "chua";

    Employee(int id, String name) {
        this(id, name, 0, null, null, 0, 0);
    }

    void meow() {
        System.out.println("Meow");
    }

}