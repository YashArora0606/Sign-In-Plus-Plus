package datamanagement;

import utilities.SinglyLinkedList;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.InputMismatchException;

public interface Database {

    boolean addStudent(Student student);

    Student findStudentById(int id) throws IOException;

    boolean isStudentSignedIn(int id) throws IOException;

    boolean removeStudentById(int id);


    boolean addSession(Session session);

    SinglyLinkedList<Session> findSessions(HashMap<String, Object> criterion) throws IOException, InputMismatchException;

    boolean resolveOpenSessions(int id, Timestamp time);


    void close();

}
