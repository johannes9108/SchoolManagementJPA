package domain;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import integration.CourseJPAImpl;
import integration.EducationJPAImpl;
import integration.EntityType;
import integration.SchoolManagementDAO;
import integration.StudentJPAImpl;
import integration.TeacherJPAImpl;

public class Controller implements ControllerAPI {

	private EntityManagerFactory emf;
	
	private SchoolManagementDAO<Teacher> teacherJPAImpl;
	private SchoolManagementDAO<Course> courseJPAImpl;
	private SchoolManagementDAO<Student> studentJPAImpl;
	private SchoolManagementDAO<Education> educationJPAImpl;

	private List<Teacher> teachers;
	private List<Student> students;
	private List<Education> educations;
	private List<Course> courses;

	public Controller(SchoolManagementDAO<Teacher> t, SchoolManagementDAO<Course> c, SchoolManagementDAO<Education> e, SchoolManagementDAO<Student> s) {
		teacherJPAImpl = t;
		educationJPAImpl = e;
		studentJPAImpl = s;
		courseJPAImpl = c;

		teachers = teacherJPAImpl.getAll();
		students = studentJPAImpl.getAll();
		educations = educationJPAImpl.getAll();
		courses = courseJPAImpl.getAll();

	}

//	@Override
//	public <T> int add(T object) {
//		if(object instanceof Teacher) {
//			return teacherJPAImpl.add((Teacher) object);
//		}
//		else if(object instanceof Course) {
//			return courseJPAImpl.add((Course) object);
//		}
//		else if(object instanceof Education){
//			return educationJPAImpl.add((Education) object);
//		}
//		else if(object instanceof Student){
//			return studentJPAImpl.add((Student) object);
//		}
//
//		return -1;
//	}

//	@Override
//	public <T> int update(T object) {
//		
//
//		if(object instanceof Teacher) {
//			return teacherJPAImpl.update((Teacher) object);
//		}
//		else if(object instanceof Course) {
//			return courseJPAImpl.update((Course) object);
//		}
//		else if(object instanceof Education){
//			return educationJPAImpl.update((Education) object);
//		}
//		else if(object instanceof Student){
//			return studentJPAImpl.update((Student) object);
//		}
//
//		return -1;
//	}

//	@Override
//	public <T> T getById(int id, EntityType type) {
//		switch (type) {
//		case TEACHER:
//			return (T) teacherJPAImpl.getById(id);
//		case COURSE:
//			return (T) courseJPAImpl.getById(id);
//		case EDUCATION:
//			return (T) educationJPAImpl.getById(id);
//		case STUDENT:
//			return (T) studentJPAImpl.getById(id);
//		default:
//			return null;
//		}
//	}

//	@Override
//	public List<?> getAll(EntityType type) {
//		switch (type) {
//		case TEACHER:
//			return teachers;
//		case COURSE:
//			return courses;
//		case EDUCATION:
//			return educations;
//		case STUDENT:
//			return students;
//		default:
//			return null;
//		}
//	}

//	@Override
//	public int removeById(int id, EntityType type) {
//		switch (type) {
//		case TEACHER:
//			int teacherToRemove = teacherJPAImpl.removeById(id);
//			refreshLocalData(type);
//			refreshLocalData(EntityType.COURSE);
//			return teacherToRemove;
//			
//		case COURSE:
//			int courseToRemove = courseJPAImpl.removeById(id);
//			refreshLocalData(type);
//			refreshLocalData(EntityType.TEACHER);
//			refreshLocalData(EntityType.EDUCATION);
//			return courseToRemove;
//		case EDUCATION:
//			int educationToRemove = educationJPAImpl.removeById(id);
//			refreshLocalData(type);
//			refreshLocalData(EntityType.COURSE);
//			refreshLocalData(EntityType.STUDENT);
//			return educationToRemove;
//		case STUDENT:
//			int studentToRemove = studentJPAImpl.removeById(id);
//			refreshLocalData(type);
//			refreshLocalData(EntityType.EDUCATION);
//			return studentToRemove;
//		default:
//			return 0;
//		}
//		
//	}

	@Override
	public void refreshLocalData(EntityType type) {
		switch (type) {
		case TEACHER:
			teachers = teacherJPAImpl.getAll();
			break;
		case COURSE:
			courses = courseJPAImpl.getAll();
			break;
		case EDUCATION:
			educations = educationJPAImpl.getAll();
			break;
		case STUDENT:
			students = studentJPAImpl.getAll();
			break;
		default:
			System.out.println("No updates");
		}

	}

	public void associate(EntityType type, int id, List<Integer> listItemIds, EntityType typeOfAssociation) {
		switch (type) {

		case TEACHER:
			((TeacherJPAImpl) teacherJPAImpl).changeCoursesForTeacher(id, listItemIds);
			break;
		case COURSE:

			switch (typeOfAssociation) {
			case TEACHER:
				((CourseJPAImpl) courseJPAImpl).changeTeachersForCourse(id, listItemIds);
				break;
			case EDUCATION:
				((CourseJPAImpl) courseJPAImpl).changeEducationsForCourse(id, listItemIds);
				break;
			}

		case EDUCATION:
			switch (typeOfAssociation) {
			case COURSE:
				((EducationJPAImpl) educationJPAImpl).changeCoursesForEducation(id, listItemIds);
				break;
			case STUDENT:
				((EducationJPAImpl) educationJPAImpl).changeStudentsForEducation(id, listItemIds);
				break;
			}
			break;
		case STUDENT:
			((StudentJPAImpl) studentJPAImpl).changeEducationForStudent(id, listItemIds);
			break;
		default:
			break;
		}

		refreshLocalData(type);
		refreshLocalData(typeOfAssociation);
	}

	@Override
	public int addTeacher(Teacher teacher) {
		return teacherJPAImpl.add(teacher);
	}

	@Override
	public int addCourse(Course course) {
		return courseJPAImpl.add(course);
	}

	@Override
	public int addEducation(Education education) {
		return educationJPAImpl.add(education);
	}

	@Override
	public int addStudent(Student student) {
		return studentJPAImpl.add(student);
	}

	@Override
	public int updateTeacher(Teacher teacher) {
		return teacherJPAImpl.update(teacher);
	}

	@Override
	public int updateCourse(Course course) {
		return courseJPAImpl.update(course);
	}

	@Override
	public int updateEducation(Education education) {
		return educationJPAImpl.update(education);
	}

	@Override
	public int updateStudent(Student student) {
		return studentJPAImpl.update(student);
	}

	@Override
	public Teacher getTeacherById(int id) {
		return teacherJPAImpl.getById(id);
	}

	@Override
	public Course getCourseById(int id) {
		return courseJPAImpl.getById(id);
	}

	@Override
	public Education getEducationById(int id) {
		return educationJPAImpl.getById(id);
	}

	@Override
	public Student getStudentById(int id) {
		return studentJPAImpl.getById(id);
	}

	@Override
	public List<Teacher> getAllTeachers() {
		return teacherJPAImpl.getAll();
	}

	@Override
	public List<Course> getAllCourses() {
		return courseJPAImpl.getAll();
	}

	@Override
	public List<Education> getAllEducations() {
		return educationJPAImpl.getAll();
	}

	@Override
	public List<Student> getAllStudents() {
		return studentJPAImpl.getAll();
	}

	@Override
	public int removeTeacherById(int id) {
		int removedID = teacherJPAImpl.removeById(id);
		refreshLocalData(EntityType.TEACHER);
		refreshLocalData(EntityType.COURSE);
		return removedID;
				
	}

	@Override
	public int removeCourseById(int id) {
		int removedID = courseJPAImpl.removeById(id);
		refreshLocalData(EntityType.COURSE);
		refreshLocalData(EntityType.TEACHER);
		refreshLocalData(EntityType.EDUCATION);
		return removedID;
	}

	@Override
	public int removeEducationById(int id) {
		int removedID = educationJPAImpl.removeById(id);
		refreshLocalData(EntityType.EDUCATION);
		refreshLocalData(EntityType.COURSE);
		refreshLocalData(EntityType.STUDENT);
		return removedID;
	}

	@Override
	public int removeStudentById(int id) {
		int removedID = studentJPAImpl.removeById(id);
		refreshLocalData(EntityType.STUDENT);
		refreshLocalData(EntityType.EDUCATION);
		return removedID;
	}

}
