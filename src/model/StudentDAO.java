package model;

import domain.Student;

public interface StudentDAO {

    public int updateStudentEducationRelation(int StudentId, int EducationId);
    public int updateStudentPersonalData(Student student);
    public int removeEducationForStudent(int StudentId);

    
}
