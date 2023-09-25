package hk.ust.comp3021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Student {
    String studentID;
    String department;
    int yearOfStudy;
    double CGA;
    List<String> preferences;
    List<String> completedCourses;

    Student(String studentID, String department, int yearOfStudy, double CGA, List<String> preferences, List<String> completedCourses){
        this.studentID = studentID;
        this.department = department;
        this.yearOfStudy = yearOfStudy;
        this.CGA = CGA;
        this.preferences = new ArrayList<>(preferences);
        this.completedCourses = new ArrayList<>(completedCourses);

    }
}
