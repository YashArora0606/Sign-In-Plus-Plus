public class Student implements Comparable<Student> {
    public final String firstName;
    public final String lastName;
    public final String id;
    public final String grade;

    public Student(String firstName, String lastName, String id, String grade) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.grade = grade;
    }

    @Override
    public int compareTo(Student student) {
//        if (this.id < student.id) {
//            return -1;
//        } else {
//            return 1;
//        }
//    }
        return this.id.compareTo(student.id);

    }
}
