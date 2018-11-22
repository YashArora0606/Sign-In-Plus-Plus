package datamanagment;
import java.util.Calendar; 
import java.util.Date;

// Should implement comparable

public class Session {
    public final Student student;
    public final String subjectWork;
    public final String courseMissed;
    public final String reason;

    public Date startTime;
    private Date endTime;

    public Session(Student student, String courseMissed, String reason, String subjectWork) {
        this.student = student;
        this.courseMissed = courseMissed;
        this.subjectWork = subjectWork;
        this.reason = reason;
        this.startTime = Calendar.getInstance().getTime();
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void resolve() {
        endTime = Calendar.getInstance().getTime();
    }
}
