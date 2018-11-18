public class Student implements Comparable<Student> {
    public final String name;
    public final int id;

    public Student(String name, int id) {
        this.name =  name;
        this.id = id;
    }

    public int compareTo(Student student) {
        if (this.id < student.id) {
            return -1;
        } else {
            return 1;
        }
    }

}
