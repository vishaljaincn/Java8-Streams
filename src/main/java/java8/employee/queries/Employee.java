package java8.employee.queries;

public record Employee(int id, String name, int age, String gender, String department, int yearOfJoining, double salary) {
    Employee(int id, String name){
        this(id, name, 0, null, null, 0, 0);
    }

}