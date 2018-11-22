package datamanagment;

import utilities.SinglyLinkedList;

public class Student implements Comparable<Student> {
    public final String firstName;
    public final String lastName;
    public final String id;
    public final String grade;
    private SinglyLinkedList<Session> list;

    public Student(String firstName, String lastName, String id, String grade) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.grade = grade;
    }

    public Student(String firstName, String lastName, String id, String grade, SinglyLinkedList<Session> list) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.grade = grade;
        this.list = list;
    }

    @Override
    public int compareTo(Student student) {
        return id.compareTo(student.id);
    }

    public void addSession(Session session){
        this.list.add(session);
    }
}
