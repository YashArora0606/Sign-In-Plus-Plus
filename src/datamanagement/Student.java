package datamanagement;

/**
 * Represents a student
 *
 * @author Alston
 * last updated on 12/3/2018
 */
public class Student {

    /**
     * The student's id number, with leading zeroes removed
     */
    public final int id;

    /**
     * The student's first name
     */
    public final String firstName;

    /**
     * THe student's last name
     */
    public final String lastName;

    /**
     * The student's grade
     */
    public final int grade;

    /**
     * Constructs a Student based on their student id, first name, last name, and grade
     *
     * @param id        the student's id
     * @param firstName the student's first name
     * @param lastName  the student's last name
     * @param grade     the student's grade
     */
    public Student(int id, String firstName, String lastName, int grade) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.grade = grade;
    }

    /**
     * Equivalent to {@link java.lang.Object#equals(Object)}, with the exception that
     * if two Student objects are compared, their student ids are compared instead
     *
     * @param obj the reference object with which to compare
     * @return true if this object is the same as the obj argument; false otherwise
     */
    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Student) { //compare two Students based on id
            return id == ((Student) obj).id;
        }

        return super.equals(obj);
    }
}
