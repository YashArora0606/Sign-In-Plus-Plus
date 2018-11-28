package datamanagement;

import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;

public interface Database {

    boolean addStudent(Student student);

    Student findStudent (int id) throws IOException;

    boolean removeStudent(int id);

    boolean signIn(Session session);

    boolean signOut(int id);

    List<Session> findSessions(HashMap<String, Object> criterion) throws IOException, InputMismatchException;

    boolean isStudentSignedIn(int id) throws IOException;

    void close();

}
