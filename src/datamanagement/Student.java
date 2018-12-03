package datamanagement;

public class Student {
    public final int id;
    public final String firstName;
    public final String lastName;
    public final int grade;

    public Student(int id, String firstName, String lastName, int grade) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.grade = grade;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Student) {
            return id == ((Student)obj).id;
        }
        return super.equals(obj);
    }
}
