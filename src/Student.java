public class Student implements Comparable<Student> {
    public final String name;
    public final int id;
    public final int grade; 

    public Student(String name, int id, int grade) {
        this.name =  name;
        this.id = id;
        this.grade = grade; 
    }
    
    @Override 
    public int compareTo(Student student) {
        if (this.id < student.id) {
            return -1;
        } else {
            return 1;
        }
    }

}
