package datamanagment;
import java.util.Calendar; 
import java.util.Date;

// Should implement comparable

public class Session {
    public final Student student;
    public final String courseWork;
    public final String courseMiss;
    public final String reason;

    public Date startTime;
    private Date endTime;

    public Session(Student student, String courseMiss, String reason, String courseWork) {
        this.student = student;
        this.courseWork = courseWork;
        this.courseMiss = courseMiss;
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
