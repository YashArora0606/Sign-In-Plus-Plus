package datamanagement;

import utilities.SinglyLinkedList;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Query {

    final int id;
    final String earliestDate;
    final String latestDate;
    int minTime;
    int maxTime;
    final SinglyLinkedList<String> reasons;
    final SinglyLinkedList<String> serts;
    final SinglyLinkedList<String> courses;

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    Query(int id, Timestamp earliestDate, Timestamp latestDate, int minTime, int maxTime,
          SinglyLinkedList<String> reasons, SinglyLinkedList<String> serts, SinglyLinkedList<String> courses) {

        this.id = id;
        this.earliestDate = toString(earliestDate);
        this.latestDate = toString(latestDate);
        this.minTime = minTime;
        this.maxTime = maxTime;
        this.reasons = reasons;
        this.serts = serts;
        this.courses = courses;
    }

    private String toString(Timestamp time) {
        return dateFormatter.format(time);
    }
}
