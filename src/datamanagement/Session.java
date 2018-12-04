package datamanagement;

import java.sql.Timestamp;

/**
 * Represents a session within the Student Services room.
 * Sessions can be unresolved (the student has signed in, but not signed out)
 * or resolved (the student has signed in and signed out)
 *
 * @author Alston
 * last updated 12/3/2018
 */
public class Session {

    /**
     * The student whom the session belongs to
     */
    public final Student student;

    /**
     * The starting time of the session (the time the student signed in)
     */
    public final Timestamp startTime;

    /**
     * The ending time of the session (the time the student signed out).
     * If the student has not signed out and the session is unresolved, endTime is set to null
     */
    public final Timestamp endTime;

    /**
     * The reason the student is using the Student Services room
     */
    public final String reason;

    /**
     * The SERT of the student
     */
    public final String sert;

    /**
     * The course/subject the student is missing or working on in the room (e.g. Math)
     */
    public final String course;

    /**
     * Constructs a new resolved or unresolved session.
     *
     * @param student   the student whom the session belongs to
     * @param startTime the starting time of the session
     * @param endTime   the ending time of the session
     * @param reason    the reason the student is using the Student Services room
     * @param sert      the student's SERT
     * @param course    the course the student is working on in the room
     */
    public Session(Student student, Timestamp startTime, Timestamp endTime, String reason, String sert, String course) {
        this.student = student;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
        this.sert = sert;
        this.course = course;
    }
}
