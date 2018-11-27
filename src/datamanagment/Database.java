package datamanagment;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface Database {

    void close();

    Student findStudent (String id);

    boolean signIn(Session session);

    boolean signOut(String id);

    List<Session> querySessions(HashMap<String, Object> criterion) throws SQLException;

}
