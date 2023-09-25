package hk.ust.comp3021;

public interface Enrollable {
    void enrollWithCondition(Student student) throws CourseFullException;
}
