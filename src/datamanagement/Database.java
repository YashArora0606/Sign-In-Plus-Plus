package datamanagement;

import utilities.SinglyLinkedList;

import java.io.IOException;
import java.sql.Timestamp;

public interface Database {

    boolean addStudent(Student newStudent);

    Student findStudentById(int id) throws IOException;

    SinglyLinkedList<Student> getStudents();

    boolean isStudentSignedIn(int id) throws IOException;

    boolean updateStudent(Student updatedStudent);

    boolean removeStudentById(int id);


    boolean addSession(Session newSession);

    SinglyLinkedList<Session> findSessions(Query query) throws IOException;

    boolean resolveOpenSessions(int id, Timestamp time);


    void close();

}
