package domain;

import java.util.List;

import integration.EntityType;

public interface ControllerAPI {

	public int addTeacher(Teacher teacher);
	public int addCourse(Course course);
	public int addEducation(Education education);
	public int addStudent(Student student);
	
	public int updateTeacher(Teacher teacher);
	public int updateCourse(Course course);
	public int updateEducation(Education education);
	public int updateStudent(Student student);
	
	public Teacher getTeacherById(int id);
	public Course getCourseById(int id);
	public Education getEducationById(int id);
	public Student getStudentById(int id);
	
	public List<Teacher> getAllTeachers();
	public List<Course> getAllCourses();
	public List<Education> getAllEducations();
	public List<Student> getAllStudents();
	
	public int removeTeacherById(int id);
	public int removeCourseById(int id);
	public int removeEducationById(int id);
	public int removeStudentById(int id);
	
	public void refreshLocalData(EntityType type);
	
	
}
