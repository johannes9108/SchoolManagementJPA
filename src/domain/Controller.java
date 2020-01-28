package domain;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import model.CourseJPAImpl;
import model.EducationJPAImpl;
import model.EntityType;
import model.StudentJPAImpl;
import model.TeacherJPAImpl;

public class Controller implements ControllerAPI {

	private EntityManagerFactory emf;

	private TeacherJPAImpl teacherJPAImpl;
	private CourseJPAImpl courseJPAImpl;
	private StudentJPAImpl studentJPAImpl;
	private EducationJPAImpl educationJPAImpl;

	private List<Teacher> teachers;
	private List<Student> students;
	private List<Education> educations;
	private List<Course> courses;

	public Controller() {
		emf = Persistence.createEntityManagerFactory("PU");
		teacherJPAImpl = new TeacherJPAImpl(emf);
		educationJPAImpl = new EducationJPAImpl(emf);
		studentJPAImpl = new StudentJPAImpl(emf);
		courseJPAImpl = new CourseJPAImpl(emf);

		teachers = teacherJPAImpl.getAll();
		students = studentJPAImpl.getAll();
		educations = educationJPAImpl.getAll();
		courses = courseJPAImpl.getAll();

	}

	@Override
	public <T> int add(T object) {
		switch (object.getClass().getName()) {
		case "domain.Teacher":
			return teacherJPAImpl.add((Teacher) object);
		case "domain.Course":
			return courseJPAImpl.add((Course) object);
		case "domain.Education":
			return educationJPAImpl.add((Education) object);
		case "domain.Student":
			return studentJPAImpl.add((Student) object);
		}
		return -1;
	}

	@Override
	public <T> int update(T object) {
		switch (object.getClass().getName()) {
		case "domain.Teacher":
			return teacherJPAImpl.update((Teacher) object);
		case "domain.Course":
			return courseJPAImpl.update((Course) object);
		case "domain.Education":
			return educationJPAImpl.update((Education) object);
		case "domain.Student":
			return studentJPAImpl.update((Student) object);
		default:
			return -1;
		}
	}

	@Override
	public <T> T getById(int id, EntityType type) {
		switch (type) {
		case TEACHER:
			return (T) teacherJPAImpl.getById(id);
		case COURSE:
			return (T) courseJPAImpl.getById(id);
		case EDUCATION:
			return (T) educationJPAImpl.getById(id);
		case STUDENT:
			return (T) studentJPAImpl.getById(id);
		default:
			return null;
		}
	}

	@Override
	public List<?> getAll(EntityType type) {
		switch (type) {
		case TEACHER:
			return teachers;
		case COURSE:
			return courses;
		case EDUCATION:
			return educations;
		case STUDENT:
			return students;
		default:
			return null;
		}
	}

	@Override
	public int removeById(int id, EntityType type) {
		switch (type) {
		case TEACHER:
			return teacherJPAImpl.removeById(id);
		case COURSE:
			return courseJPAImpl.removeById(id);
		case EDUCATION:
			return educationJPAImpl.removeById(id);
		case STUDENT:
			return studentJPAImpl.removeById(id);
		default:
			return 0;
		}
	}

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
			teacherJPAImpl.changeCoursesForTeacher(id, listItemIds);
			break;
		case COURSE:

			switch (typeOfAssociation) {
			case TEACHER:
				courseJPAImpl.changeTeachersForCourse(id, listItemIds);
				break;
			case EDUCATION:
				courseJPAImpl.changeEducationsForCourse(id, listItemIds);
				break;
			}

		case EDUCATION:
			switch (typeOfAssociation) {
			case COURSE:
				educationJPAImpl.changeCoursesForEducation(id, listItemIds);
				break;
			case STUDENT:
				educationJPAImpl.changeStudentsForEducation(id, listItemIds);
				break;
			}
			break;
		case STUDENT:
			studentJPAImpl.changeEducationForStudent(id, listItemIds);
			break;
		default:
			break;
		}

		refreshLocalData(type);
		refreshLocalData(typeOfAssociation);
	}

}
