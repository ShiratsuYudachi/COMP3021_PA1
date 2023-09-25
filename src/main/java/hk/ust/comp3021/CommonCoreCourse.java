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
        return false;
    }

    @Override
    public void enrollWithCondition(Student student) throws CourseFullException {

    }
}
