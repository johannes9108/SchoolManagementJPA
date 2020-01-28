package model;

import domain.Teacher;
import java.util.List;

public interface TeacherDAO {

//    public int updateTeacher(Teacher teacher);
    
    public int updateCoursesForTeacher(Integer teacherId, List <Integer> updatedListofCourses);
    
    public int updateTeacherPersonalData(Teacher teacher);

}
 