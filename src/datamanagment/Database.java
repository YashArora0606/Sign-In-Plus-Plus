package datamanagment;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface Database {

    void close();

    boolean signIn(Session session);

    boolean signOut(String id);

    List<Session> findSessions(HashMap<String, Object> criterion) throws IOException;

    Student findStudent (String id) throws IOException;

    boolean addStudent(Student student);

}
