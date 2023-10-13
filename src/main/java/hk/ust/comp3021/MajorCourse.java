package hk.ust.comp3021;

import java.util.ArrayList;
import java.util.List;

public class MajorCourse extends Course{
    List<String> Prerequisites;

    public MajorCourse(String courseCode, String department, List<String> prerequisites){
        this.courseCode = courseCode;
        this.department = department;
        this.capacity = Course.INITIAL_CAPACITY;
        this.Prerequisites = new ArrayList<>(prerequisites);
    }

    @Override
    public boolean enrollmentCriteria(Student student) {
        for (String prereq : Prerequisites){
            if (!student.completedCourses.contains(prereq)) return false;
        }
        return true;
    }


    @Override
    public void enrollWithCondition(Student student) throws CourseFullException {
        if (enrollmentCriteria(student)){
            if (enrolledStudents.size()>=capacity) throw new CourseFullException();
            else{
                enrolledStudents.add(student.studentID);
            }
        }
    }


}
