package datamanagment;

import java.util.Date;

public class Session {
    public final Student student;
    public final Date startTime;
    public final Date endTime;
    public final String reason;
    public final String courseWork;
    public final String courseMissed;

    public Session(Student student, Date startTime, String reason, String courseWork, String courseMissed) {
        this.student = student;
        this.startTime = startTime;
        this.endTime = null;
        this.reason = reason;
        this.courseWork = courseWork;
        this.courseMissed = courseMissed;
    }

    public Session(Student student, Date startTime, Date endTime, String reason, String courseWork, String courseMissed) {
        this.student = student;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
        this.courseWork = courseWork;
        this.courseMissed = courseMissed;
    }
}
