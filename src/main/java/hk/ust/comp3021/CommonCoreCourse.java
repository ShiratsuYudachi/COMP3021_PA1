package hk.ust.comp3021;

import java.util.ArrayList;
import java.util.List;

public class CommonCoreCourse extends Course{
    boolean isHonorCourse;
    public CommonCoreCourse(String courseCode, String department, boolean isHonorCourse){
        this.courseCode = courseCode;
        this.department = department;
        this.capacity = Course.INITIAL_CAPACITY;
        this.isHonorCourse = isHonorCourse;
    }
    @Override
    public boolean enrollmentCriteria(Student student) {
        if (!isHonorCourse) return true;
        else return student.CGA>=3.5;
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
