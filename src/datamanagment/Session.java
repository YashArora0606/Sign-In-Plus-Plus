package datamanagment;
import java.util.Calendar;
import java.util.Date;

public class Session {
    public final Student student;
    public final String course;
    public final String reason;

    public Date startTime;
    private Date endTime;

    public Session(Student student, String course, String reason) {
        this.student = student;
        this.course = course;
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
