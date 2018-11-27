package datamanagment;

import java.util.Date;

public class Session {
    public final Student student;
    public final Date startTime;
    public final Date endTime;
    public final String subjectWork;
    public final String courseMissed;
    public final String reason;

    public Session(String courseMissed, String reason, String subjectWork, Date startTime, Date endTime) {
        this.student = null;
        this.courseMissed = courseMissed;
        this.subjectWork = subjectWork;
        this.reason = reason;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Session(Student student, Date startTime, Date endTime, String reason, String subjectWork, String courseMissed) {
        this.student = student;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
        this.subjectWork = subjectWork;
        this.courseMissed = courseMissed;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

}
