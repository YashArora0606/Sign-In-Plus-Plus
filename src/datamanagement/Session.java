package datamanagement;

import java.sql.Timestamp;
import java.util.Date;

public class Session {
    public final Student student;
    public final Timestamp startTime;
    public final Timestamp endTime;
    public final String reason;
    public final String courseWork;
    public final String courseMissed;

    public Session(Student student, Timestamp startTime, Timestamp endTime, String reason, String courseWork, String courseMissed) {
        this.student = student;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
        this.courseWork = courseWork;
        this.courseMissed = courseMissed;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }
}
