package hk.ust.comp3021;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class EnrollmentSystem {
    public List<Student> students;
    public Map<String, Course> courses;

    public EnrollmentSystem() {
        this.students = new ArrayList<>();
        this.courses = new HashMap<>();
    }

    /**
     * TODO: Part 1
     * The first two choices in students' preferences must be enrolled,
     * regardless of course capacity or prerequisites.
     * The course capacity, initially sets at 10, expands if necessary
     * to accommodate these guaranteed choices.
     */
    public void enrollFirstRound() {
        for (Student student : students){
            try {
                courses.get(student.preferences.get(0)).enroll(student);
                courses.get(student.preferences.get(1)).enroll(student);
            }catch (IndexOutOfBoundsException e) {
                continue;
            }

        }
    }

    /**
     * TODO: Part 2
     * Task 3: All third choices from preferences will be processed first,
     * followed by fourth choices, and so on until the last preference.
     * Task 4: MajorCourses and CommonCoreCourse should implement Enrollable.
     * They also have different priorities: common core courses gives priority
     * to descending seniority (year of study), while major courses grants
     * precedence to students from the same department. The courses will be
     * filled, first by the priority, then in the descending order of CGA,
     * and finally in the descending order of StudentID.
     */
    public void enrollSecondRound() {

    }

    /**
     * TODO: Task 7
     * Find the number of Teaching Assistants (TAs) required, given that
     * 1 TA is needed for every 5 students in a course.
     *
     * @return the number of TAs required
     */
    public int findNumTA() {
        return 0;
    }

    /**
     * TODO: Task 8
     * Find the number of students who successfully enrolled in
     * all their course preferences.
     *
     * @return the number of students
     */
    public int findNumAllSuccess() {
        return 0;
    }

    /**
     * TODO: Task 9
     * Identify the students who have not enrolled in any
     * common core courses.
     *
     * @return the list of StudentID
     */
    public List<String> findListNoCommonCore() {
        return null;
    }

    public void parseStudents(String fileName) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                // TODO: Task 1
                String[] params = line.split(", ");

                String studentID = params[0];
                String department = params[1];
                int yearOfStudy = Integer.parseInt(params[2]);
                double CGA = Double.parseDouble(params[3]);

                List<String> preferences = Arrays.asList(params[4].substring(1,params[4].length()-1).split(" "));
                if (preferences.get(0).isEmpty()){
                    preferences = new ArrayList<>();
                }
                List<String> completedCourses = Arrays.asList(params[5].substring(1,params[5].length()-1).split(" "));
                students.add(new Student(studentID,department,yearOfStudy,CGA,preferences,completedCourses));
            }
        }
    }

    public void parseCourses(String fileName) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                // TODO: Task 1
                String[] params = line.split(", ");

                String courseID = params[0];
                String department = params[1];
                String CourseType = params[2];
                if (CourseType.equals("Major")){
                    List<String> prerequisites = Arrays.asList(params[4].substring(1,params[4].length()-1).split(" "));
                    courses.put(courseID,new MajorCourse(courseID,department, prerequisites));
                }else{
                    boolean isHonorCourse = Boolean.parseBoolean(params[3]);
                    courses.put(courseID,new CommonCoreCourse(courseID,department, isHonorCourse));

                }
            }
        }
    }

    public void writeCourseEnrollment(String fileName) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Course course : courses.values()) {
                bw.write(course.courseCode + ", " + course.capacity + ", [" +
                        String.join(" ", course.enrolledStudents) + "], [" +
                        String.join(" ", course.waitlist) + "]"
                );
                bw.newLine();
            }
        }
    }

    public void writeCourseAnalysis(String fileName) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write(findNumTA() + ", " + findNumAllSuccess() + ", [" + String.join(" ", findListNoCommonCore()) + "]");
            bw.newLine();
        }
    }

    public static void main(String[] args) {
        EnrollmentSystem system = new EnrollmentSystem();

        try {
            system.parseStudents("student.txt");
            system.parseCourses("course.txt");
            system.enrollFirstRound();
            system.writeCourseEnrollment("firstRoundEnrollment.txt");
            system.enrollSecondRound();
            system.writeCourseEnrollment("secondRoundEnrollment.txt");
            system.writeCourseAnalysis("dataAnalytics.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
