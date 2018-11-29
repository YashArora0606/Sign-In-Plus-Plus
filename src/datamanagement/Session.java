package datamanagement;

import java.sql.Timestamp;

public class Session {
    public final Student student;
    public final Timestamp startTime;
    public final Timestamp endTime;
    public final String reason;
    public final String cert;
    public final String course;

    public Session(Student student, Timestamp startTime, Timestamp endTime, String reason, String cert, String course) {
        this.student = student;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
        this.cert = cert;
        this.course = course;
    }
}
