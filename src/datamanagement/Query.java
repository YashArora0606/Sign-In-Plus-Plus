package datamanagement;

import utilities.SinglyLinkedList;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Representation of a query.
 * Query contains the search specifications that can be passed into {@link Database} to query
 * all the matching sessions
 *
 * @author Alston
 * last updated 12/9/2018
 */
public class Query {

    /**
     * The id of the student referenced by the queried sessions
     */
    final int id;

    /**
     * The first name of the student(s) referenced by the queried sessions
     */
    final String firstName;

    /**
     * The last name of the student(s) referenced by the queried sessions
     */
    final String lastName;

    /**
     * The grade of the student(s) referenced by the queried sessions
     */
    final int grade;

    /**
     * The earliest time (in MM/dd/yyy HH:mm:ss format) the queried sessions must start;
     * {@link Session#startTime} must be after earliestTime
     */
    final String earliestTime;

    /**
     * The latest time (in MM/dd/yyy HH:mm:ss format) the queried sessions must end;
     * {@link Session#endTime} must be before latestTime
     */
    final String latestTime;

    /**
     * The minimum duration (in minutes) of the queried sessions
     */
    final int minTime;

    /**
     * The maximum duration (in minutes) of the queried sessions
     */
    final int maxTime;

    /**
     * A list of reasons the queried sessions can have;
     * {@link Session#reason} must be an element in the "reasons" list
     */
    final SinglyLinkedList<String> reasons;

    /**
     * A list of serts the queried sessions can have;
     * {@link Session#sert} must be an element in the "serts" list
     */
    final SinglyLinkedList<String> serts;

    /**
     * A list of courses the queried sessions can have;
     * {@link Session#course} must be an element in the "courses" list
     */
    final SinglyLinkedList<String> courses;


    private SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");


    /**
     * Constructs a query by inputting the included criteria
     * If a criterion is to be ignored in the query, then it is passed in as null or -1, if it is an integer
     *
     * @param id           the id of the student the queried sessions reference
     * @param firstName    the first name of the student(s) the queried sessions reference
     * @param lastName     the last name of the student(s) the queried sessions reference
     * @param grade        the grade of the student(s) the queried sessions reference
     * @param earliestTime the earliest time queried sessions must start
     * @param latestTime   the latest time queried sessions must end
     * @param minTime      the minimum span of queried sessions
     * @param maxTime      the maximum span of queried sessions
     * @param reasons      a list of possible reasons the queried sessions must contain one of
     * @param serts        a list of possible serts the queried sessions must contain one of
     * @param courses      a list of possible courses the queried sessions must contain one of
     */
    Query(int id, String firstName, String lastName, int grade, Timestamp earliestTime, Timestamp latestTime, int minTime, int maxTime,
          SinglyLinkedList<String> reasons, SinglyLinkedList<String> serts, SinglyLinkedList<String> courses) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.grade = grade;
        this.earliestTime = toString(earliestTime);
        this.latestTime = toString(latestTime);
        this.minTime = minTime;
        this.maxTime = maxTime;
        this.reasons = reasons;
        this.serts = serts;
        this.courses = courses;
    }

    /**
     * Converts a Timestamp to a String
     *
     * @param time a Timestamp
     * @return a String representation of the time argument in MM/dd/yyy HH:mm:ss format
     */
    private String toString(Timestamp time) {
        if (time == null) {
            return null;
        }
        return dateFormatter.format(time);
    }
}
