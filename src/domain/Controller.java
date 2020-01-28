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
//		switch (entityChoice) {
//		case 1:
//			return (List<T>) teacherJPAImpl.getAll();
//		case 2:
//			return (List<T>) courseJPAImpl.getAll();
//		case 3:
//			return (List<T>) educationJPAImpl.getAll();
//		case 4:
//			return (List<T>) studentJPAImpl.getAll();
//		default:
//			return null;
//		}
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
//
//	@Override
//	public <T> boolean addWithCourses(T object, List<Courses> courses) {
//		
//		return false;
//	}

	public void associate(EntityType type, int id, List<Integer> listItemIds, EntityType typeOfAssociation) {
		switch (type) {
		
		case TEACHER:
			
			Teacher teacher = teacherJPAImpl.getById(id);
			List<Course> coursesToBeMerged = teacher.clearBindingsFromTeacher();

			coursesToBeMerged.forEach(c -> {
				courseJPAImpl.update(c);
			});
			coursesToBeMerged.clear();
			for (Integer itemId : listItemIds) {
				Course c = courseJPAImpl.getById(itemId);
				teacher.addCourse(c);
				courseJPAImpl.update(c);
			}

			teacherJPAImpl.update(teacher);

//			Teacher teacher = teacherJPAImpl.getById(id);
//			List<Course> coursesToBeMerged = teacher.clearBindingsFromTeacher();
//			
//			coursesToBeMerged.forEach(c->{
//				courseJPAImpl.update(c);
//			});
//			coursesToBeMerged.clear();
//			for (Integer itemId : listItemIds) {
//				Course c = courseJPAImpl.getById(itemId);
//				teacher.addCourse(c);
//				courseJPAImpl.update(c);
//			}
//			
//			teacherJPAImpl.update(teacher);
//			refreshLocalData(type);
//			refreshLocalData(typeOfAssociation);
			break;
		case COURSE:
			Course course = courseJPAImpl.getById(id);

			switch (typeOfAssociation) {
			case TEACHER:
				List<Teacher> teachersToBeMerged = course.clearTeacherBindingsFromCourse();

				teachersToBeMerged.forEach(t -> {
					teacherJPAImpl.update(t);
				});
				teachersToBeMerged.clear();
				for (Integer itemId : listItemIds) {
					Teacher t = teacherJPAImpl.getById(itemId);
					course.addTeacher(t);
					teacherJPAImpl.update(t);
				}

				break;

			case EDUCATION:
				List<Education> educationsToBeMerged = course.clearEducationBindingsFromCourse();

				educationsToBeMerged.forEach(e -> {
					educationJPAImpl.update(e);
				});
				educationsToBeMerged.clear();
				for (Integer itemId : listItemIds) {
					Education e = educationJPAImpl.getById(itemId);
					course.addEducation(e);
					educationJPAImpl.update(e);
				}

				break;
			}
			courseJPAImpl.update(course);

			break;
		case EDUCATION:
			Education education = educationJPAImpl.getById(id);

			switch (typeOfAssociation) {
			case COURSE:
				coursesToBeMerged = education.clearCourseBindingsFromEducation();

				coursesToBeMerged.forEach(t -> {
					courseJPAImpl.update(t);
				});
				coursesToBeMerged.clear();
				for (Integer itemId : listItemIds) {
					Course c = courseJPAImpl.getById(itemId);
					education.addCourse(c);
					courseJPAImpl.update(c);
				}

				break;

			case STUDENT:
				List<Student> studentsToBeMerged = education.clearStudentBindingsFromEducation();

				studentsToBeMerged.forEach(s -> {
					studentJPAImpl.update(s);
				});
				studentsToBeMerged.clear();
				for (Integer itemId : listItemIds) {
					Student s = studentJPAImpl.getById(itemId);
					education.addStudent(s);
					studentJPAImpl.update(s);
				}

				break;
			}
			educationJPAImpl.update(education);
			break;
		case STUDENT:
			Student s = studentJPAImpl.getById(id);
			Education oldEducation = s.clearBindingsFromStudent();
			if(oldEducation!=null)
			educationJPAImpl.update(oldEducation);
			System.out.println(oldEducation);

			if(listItemIds.size()>0) {
				Education e = educationJPAImpl.getById(listItemIds.get(0));
				e.addStudent(s);
				s.setEducation(e);
				educationJPAImpl.update(e);
			}
			else {
				s.setEducation(null);
			}
			studentJPAImpl.update(s);
			break;

		default:
			break;
		}

		refreshLocalData(type);
		refreshLocalData(typeOfAssociation);
	}

	public void disAssociate(EntityType type, int i, List<Integer> indicies, EntityType course) {
		switch (type) {
		case TEACHER:

			break;
		case COURSE:

			break;
		case EDUCATION:

			break;
		case STUDENT:

			break;
		}

	}

}
