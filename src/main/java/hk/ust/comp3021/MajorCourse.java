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
        return false;
    }

    @Override
    public void enrollWithCondition(Student student) throws CourseFullException {

    }
}
