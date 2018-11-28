package datamanagement;

import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;

public interface Database {

    boolean addStudent(Student student);

    Student findStudentById(int id) throws IOException;

    boolean isStudentSignedIn(int id) throws IOException;

    boolean removeStudentById(int id);

    boolean addSession(Session session);

    List<Session> findSessions(HashMap<String, Object> criterion) throws IOException, InputMismatchException;

    boolean signOut(int id);

    void close();

}
