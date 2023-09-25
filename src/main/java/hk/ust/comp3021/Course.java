package hk.ust.comp3021;

import java.util.*;

public abstract class Course implements Enrollable {
    public static final int INITIAL_CAPACITY = 10;

    protected String courseCode;
    protected String department;
    protected int capacity;
    protected List<String> enrolledStudents;
    protected List<String> waitlist;

    /**
     * TODO: Task 2
     *
     * @param student the student being enrolled to the course
     */
    public void enroll(Student student) {

    }

    /**
     * TODO: Task 5
     *
     * @param student the student being checked
     * @return true if the student fulfills enrollment criteria
     */
    public abstract boolean enrollmentCriteria(Student student);
}

