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

    TeacherJPAImpl teacherJPAImpl;
    CourseJPAImpl courseJPAImpl;
    StudentJPAImpl studentJPAImpl;
    EducationJPAImpl educationJPAImpl;

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

// The update is moved to Entityspecific controller since entities cannot be updated the same manner du to associations
//	@Override
//	public <T> int update(T object) 
//        {
//		switch (object.getClass().getName()) {
//		case "domain.Teacher":
//			return teacherJPAImpl.update((Teacher) object);
//		case "domain.Course":
//			return courseJPAImpl.update((Course) object);
//		case "domain.Education":
//			return educationJPAImpl.update((Education) object);
//		case "domain.Student":
//			return studentJPAImpl.update((Student) object);
//		default:
//			return -1;
//		}
//	}
    //////////// STUDENT //////////////
    public int updateStudentPersonalData(Student student) {
        return studentJPAImpl.updateStudentPersonalData(student);
    }

    public int updateStudentEducationRelation(int StudentId, int EducationId) {
        return studentJPAImpl.updateStudentEducationRelation(StudentId, EducationId);
    }

    public int removeEducationForStudent(int StudentId) {
        return studentJPAImpl.removeEducationForStudent(StudentId); 
    }
    //////////// END STUDENT //////////////
    
    
    
        //////////// TEACHER //////////////
    
    public int updateCoursesForTeacher(Integer teacherId, List<Integer> updatedListofCourseIds){
        return teacherJPAImpl.updateCoursesForTeacher(teacherId, updatedListofCourseIds);
    }
    
    
        //////////// END TEACHER //////////////

    public int updateCourse(Course course) {
        return courseJPAImpl.updateCourse(course);
    }

    public int updateEducation(Education education) {
        return educationJPAImpl.updateEducation(education);
    }

    public int updateTeacherPersonalData(Teacher teacher) {
        return teacherJPAImpl.updateTeacherPersonalData(teacher);
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
////		switch (type) {
////		case TEACHER:
////			return teachers;
////		case COURSE:
////			return courses;
////		case EDUCATION:
////			return educations;
////		case STUDENT:
////			return students;
////		default:
////			return null;
////		}
        switch (type) {
            case TEACHER:
                return (List<Teacher>) teacherJPAImpl.getAll();
            case COURSE:
                return (List<Course>) courseJPAImpl.getAll();
            case EDUCATION:
                return (List<Education>) educationJPAImpl.getAll();
            case STUDENT:
                return (List<Student>) studentJPAImpl.getAll();
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

//    public void associate(EntityType type, int id, List<Integer> indicies) {
//        System.out.println(indicies);
//        System.out.println(courses);
//        switch (type) {
//            case TEACHER:
//                Teacher teacher = teacherJPAImpl.getById(id);
//                for (Integer index : indicies) {
//                    Course c = courseJPAImpl.getById(index);
//                    System.out.println(c);
//                    teacher.addCourse(c);
//                }
//                teacherJPAImpl.updateTeacher(teacher);
//                refreshLocalData(type);
//                break;
//            case COURSE:
//
//                break;
//            case EDUCATION:
//
//                break;
//            case STUDENT:
//
//                break;
//
//            default:
//                break;
//        }
//
//    }

}
