package datamanagement;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface Database {

    void close();

    boolean signIn(Session session);

    boolean signOut(int id);

    List<Session> findSessions(HashMap<String, Object> criterion) throws IOException;

    Student findStudent (int id) throws IOException;

    boolean addStudent(Student student);

    boolean removeStudent(int id);

}
