package datamanagement;

import java.sql.Timestamp;

public class Session {
    public final Student student;
    public final Timestamp startTime;
    public final Timestamp endTime;
    public final String reason;
    public final String sert;
    public final String course;

    public Session(Student student, Timestamp startTime, Timestamp endTime, String reason, String sert, String course) {
        this.student = student;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
        this.sert = sert;
        this.course = course;
    }
}
