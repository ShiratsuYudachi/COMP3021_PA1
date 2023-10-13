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
    public void enrollFirstRound() {// TODO sort student first?
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
    private void tryEnrollCourseOfIndex(Student student, int index){
        if (index < student.preferences.size()) {
            Course toenroll = courses.get(student.preferences.get(index));
            try {
                if (toenroll != null) {
                    toenroll.enrollWithCondition(student);
                }
            }catch (CourseFullException e){
                toenroll.waitlist.add(student.studentID);
            }
        }
    }
    public void enrollSecondRound() {
        int max_pref_size = 0;
        for (Student student : students){
            max_pref_size = Math.max(max_pref_size,student.preferences.size());
        }

        for (int pref_index = 2; pref_index < max_pref_size; pref_index++){
            List<Student> prioritized_students = new ArrayList<>();
            for (Student student : students){
                if (pref_index >= student.preferences.size()) continue;
                assert student.preferences.get(pref_index)!=null;
                Course toenroll = courses.get(student.preferences.get(pref_index));
                if (toenroll == null) continue;
                if (toenroll instanceof MajorCourse && Objects.equals(student.department, toenroll.department)){
                    prioritized_students.add(student);
                }
            }

            for (Student student : prioritized_students){
                tryEnrollCourseOfIndex(student, pref_index);
            }

            for (Student student : students){
                if (prioritized_students.contains(student)) continue;
                tryEnrollCourseOfIndex(student,pref_index);
            }
        }
    }

    /**
     * TODO: Task 7
     * Find the number of Teaching Assistants (TAs) required, given that
     * 1 TA is needed for every 5 students in a course.
     *
     * @return the number of TAs required
     */
    public int findNumTA() {
        int countTA = 0;
        for (Course course : courses.values()){
            int numStudents = course.enrolledStudents.size();
            countTA += numStudents / 5 + (numStudents % 5 == 0 ? 0 : 1);
        }
        return countTA;
    }

    /**
     * TODO: Task 8
     * Find the number of students who successfully enrolled in
     * all their course preferences.
     *
     * @return the number of students
     */
    public int findNumAllSuccess() {
        int countSuccess = 0;
        for (Student student : students){
            int isAllSuccess = 1;
            for (String pref_courseId : student.preferences){
                if (!courses.get(pref_courseId).enrolledStudents.contains(student.studentID)){
                    isAllSuccess = 0;
                    break;
                }
            }
            countSuccess+=isAllSuccess;

        }
        return countSuccess;
    }

    /**
     * TODO: Task 9
     * Identify the students who have not enrolled in any
     * common core courses.
     *
     * @return the list of StudentID
     */
    public List<String> findListNoCommonCore() {
        List<String> noCCCStudents = new ArrayList<>();
        for (Student student:students){
            boolean enrolledInCCC = false;
            List<Course> enrolledCourse = new ArrayList<>();

            for (String courseID : student.preferences){
                Course course = courses.get(courseID);
                if (course!=null && course.enrolledStudents.contains(student.studentID)){
                    enrolledCourse.add(course);
                }
            }

            for (Course course : enrolledCourse) {
                if (course instanceof CommonCoreCourse) {
                    enrolledInCCC = true;
                    break;
                }
            }
            if (!enrolledInCCC) noCCCStudents.add(student.studentID);

        }
        return noCCCStudents;
    }
    //TODO: what if student want to enroll a course not exists?

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
                if (completedCourses.get(0).isEmpty()) completedCourses = new ArrayList<>();
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
                    if (prerequisites.size() == 1 && prerequisites.get(0).isEmpty()) prerequisites = new ArrayList<>();
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
