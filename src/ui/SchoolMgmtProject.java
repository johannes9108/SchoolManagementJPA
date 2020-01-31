package ui;
/*caga pinga*/
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import domain.Controller;
import domain.Course;
import domain.Education;
import domain.Student;
import domain.Teacher;
import integration.EntityType;

import java.util.Set;

public class SchoolMgmtProject {
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU");

    public static void main(String[] args) {

        setUpData();
        Controller controller = null;
                                
        
        
        
//        List <Education> educations = (List<Education>) controller.getAll(EntityType.EDUCATION);
//        for (Education education : educations) {
//            System.out.println(education.getName());
//        }
//        
        
        
//        List<Teacher> set = (List<Teacher>) controller.getAll(EntityType.TEACHER);
//        set.forEach(System.out::println);
//        List<Course> set1 = (List<Course>) controller.getAll(EntityType.COURSE);
//        set1.forEach(System.out::println);
//        List<Education> set2 = (List<Education>) controller.getAll(EntityType.EDUCATION);
//        set2.forEach(System.out::println);
//        List<Student> set3 = (List<Student>) controller.getAll(EntityType.STUDENT);
//        set3.forEach(System.out::println);
        
        
        
//        controller.removeById(1, EntityType.STUDENT);
//        controller.removeById(1, EntityType.EDUCATION);
        
        
//        Student s = controller.getById(1, EntityType.STUDENT);
//        System.out.println("INNA	N:" + s);
//        List<Integer> listItemIds = new ArrayList<Integer>();
//        listItemIds.add(2);
//        controller.associate(EntityType.STUDENT, 1, listItemIds, EntityType.EDUCATION);
//        s = controller.getById(1, EntityType.STUDENT);
//        System.out.println("EFTER ASSOCIATION: " + s);
        
//        Education e = controller.getById(1, EntityType.EDUCATION);
//        System.out.println("INNA	N:" + e);
//        List<Integer> listItemIds = new ArrayList<Integer>();
//        listItemIds.add(2);
//        listItemIds.add(3);
//        listItemIds.add(4);
//        controller.associate(EntityType.EDUCATION, 1, listItemIds, EntityType.STUDENT);
//        e = controller.getById(1, EntityType.EDUCATION);
//        System.out.println("EFTER ASSOCIATION: " + e);
        
//        Education e = controller.getById(1, EntityType.EDUCATION);
//        System.out.println("INNA	N:" + e);
//        List<Integer> listItemIds = new ArrayList<Integer>();
//        listItemIds.add(2);
//        listItemIds.add(3);
//        listItemIds.add(4);
//        controller.associate(EntityType.EDUCATION, 1, listItemIds, EntityType.COURSE);
//        e = controller.getById(1, EntityType.EDUCATION);
//        System.out.println("EFTER ASSOCIATION: " + e);

//        Course c = controller.getById(1, EntityType.COURSE);
//        System.out.println("INNAN:\n" + c);
//        List<Integer> listItemIds = new ArrayList<Integer>();
//        listItemIds.add(1);
//        listItemIds.add(3);
//        controller.associate(EntityType.COURSE, 1, listItemIds, EntityType.EDUCATION);
//        c = controller.getById(1, EntityType.COURSE);
//        System.out.println("EFTER ASSOCIATION:\n" + c);
//        Course c = controller.getById(1, EntityType.COURSE);
//        System.out.println("INNAN:\n" + c);
//        List<Integer> listItemIds = new ArrayList<Integer>();
//        listItemIds.add(1);
//        listItemIds.add(3);
//        listItemIds.add(5);
//        listItemIds.add(6);
//        listItemIds.add(7);
//        controller.associate(EntityType.COURSE, 1, listItemIds, EntityType.TEACHER);
//        c = controller.getById(1, EntityType.COURSE);
//        System.out.println("EFTER ASSOCIATION:\n" + c);
        
	//        Teacher t = controller.getById(1, EntityType.TEACHER);
	//        System.out.println("INNAN:\n" + t);
	//        List<Integer> listItemIds = new ArrayList<Integer>();
	//        listItemIds.add(1);
	//        listItemIds.add(4);
	//        controller.associate(EntityType.TEACHER, 1, listItemIds, EntityType.COURSE);
	//        t = controller.getById(1, EntityType.TEACHER);
	//        System.out.println("EFTER ASSOCIATION:\n" + t);
        
        
//        Teacher t = controller.getById(1, EntityType.TEACHER);
//        System.out.println("INNAN:\n" + t);
//        List<Integer> listItemIds = new ArrayList<Integer>();
//        listItemIds.add(1);
//        listItemIds.add(3);
//        controller.associate(EntityType.TEACHER, 1, listItemIds, EntityType.COURSE);
//        t = controller.getById(1, EntityType.TEACHER);
//        System.out.println("EFTER ASSOCIATION:\n" + t);
        
//        controller.removeById(2, EntityType.COURSE);
//        controller.removeById(2, EntityType.EDUCATION);
        
        
        
//        controller.disAssociate(EntityType.EDUCATION, 2,listItemIds,EntityType.COURSE);

//        List<Integer> courses = new ArrayList<Integer>();
//        courses.add(2);
//        
//        controller.associate(EntityType.TEACHER, 1, courses);
//        set.forEach(System.out::println);
        
//        
//        Course c = controller.getById(1, EntityType.COURSE);
//      System.out.println("INNAN:\n" + c);
//        c.setName("TEST");
//        controller.update(c);
//      System.out.println("EFTER ASSOCIATION:\n" + c);
    }

    public static void setUpData() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		// Students owned by Education
		
		Student student1 = new Student("Johannes", "D�pare", LocalDate.of(1980, 5, 31), "johannes@gmail.com");
		Student student2 = new Student("Nicolas", "Loggins", LocalDate.of(1965, 3, 17), "nicolas@gmail.com");
		Student student3 = new Student("Robert", "Dr�pare", LocalDate.of(1983, 12, 25), "rober@gmail.com");
		Student student4 = new Student("Anna", "Andersson", LocalDate.of(1990, 3, 4), "anna@gmail.com");
		Student student5 = new Student("John", "Stig", LocalDate.of(1987, 2, 27), "john@gmail.com");
		Student student6 = new Student("Lukas", "Myren", LocalDate.of(1955, 4, 20), "lukas@gmail.com");
		Student student7 = new Student("Sofia", "Hansen", LocalDate.of(2005, 3, 3), "sofia@gmail.com");
		Student student8 = new Student("Zara", "Larsson", LocalDate.of(1930, 10, 12), "zara@gmail.com");
		Student student9 = new Student("Wilma", "Jackson", LocalDate.of(1966, 12, 29), "wilma@gmail.com");
				
		// Teachers owned by Courses
		Teacher teacher1 = new Teacher("Bita", "Jabbari", LocalDate.of(1968, 9, 13), "bita@gmail.com");
		Teacher teacher2 = new Teacher("Ulf", "Bilting", LocalDate.of(1955, 8, 14), "ulf@gmail.com");
		Teacher teacher3 = new Teacher("Ali", "Hemez", LocalDate.of(1976, 7, 15), "ali@gmail.com");
		Teacher teacher4 = new Teacher("Nypan", "H�k", LocalDate.of(1945, 6, 16), "nypan@gmail.com");
		Teacher teacher5 = new Teacher("Wim", "Koppear", LocalDate.of(1988, 5, 17), "wim@gmail.com");
		Teacher teacher6 = new Teacher("Kesse", "Mannez", LocalDate.of(1995, 4, 18), "kesse@gmail.com");
		Teacher teacher7 = new Teacher("Siv", "Namme", LocalDate.of(1950, 3, 19), "siv@gmail.com");
		Teacher teacher8 = new Teacher("Linda", "Inton", LocalDate.of(1960, 2, 20), "linda@gmail.com");
		Teacher teacher9 = new Teacher("Tomas", "Safari", LocalDate.of(1970, 1, 21), "tomas@gmail.com");

		
		//Courses own both Education/Teachers
		Course course1 = new Course("Spanska A", "Språk", "junior", 5);
		Course course2 = new Course("Spanska B", "Språk", "senior", 10);
		Course course3 = new Course("Engelska A", "Språk", "junior", 5);
		Course course4 = new Course("Matematik C", "Språk", "senior", 10);
		Course course5 = new Course("Filosofi GrundKurs", "Språk", "junior", 5);
		Course course6 = new Course("Biologi B", "Språk", "senior", 10);
		
		// Education owns students but are owned by Course
		Education education1 = new Education("Spr�kvetare", "Spr�kvetenskap", LocalDate.of(2015, 01, 31),LocalDate.of(2019, 7, 1));
		Education education2 = new Education("Systemutvecklare", "Coding", LocalDate.of(2016, 01, 31), LocalDate.of(2018, 6, 28));
		Education education3 = new Education("Naturvetare", "Naturvetenskap", LocalDate.of(2017, 01, 31),LocalDate.of(2022, 6, 25));
		Education education4 = new Education("Lektor", "Humaniora", LocalDate.of(2016, 01, 31), LocalDate.of(2020, 6, 28));
		

		education1.addStudent(student1);
		education1.addStudent(student2);
		education2.addStudent(student3);
		education2.addStudent(student4);
		education3.addStudent(student5);
		education3.addStudent(student6);
		education4.addStudent(student7);
		education4.addStudent(student8);
//		
//		// THESE ARENT NEEDED SINCE COURSE HAS CASCADEPERSIST
//		em.persist(education1);
//		em.persist(education2);
//		em.persist(education3);
//		em.persist(education4);
//		
		em.persist(student9);
//		
//		
//		
		teacher1.addCourse(course1);
		teacher1.addCourse(course2);
		teacher1.addCourse(course3);
		teacher1.addCourse(course4);
		
		teacher2.addCourse(course3);
		teacher2.addCourse(course4);
		teacher2.addCourse(course5);
		
		
		teacher3.addCourse(course6);
		teacher4.addCourse(course2);
		teacher5.addCourse(course4);
		teacher6.addCourse(course5);
		teacher7.addCourse(course6);
		
		teacher8.addCourse(course6);
		teacher8.addCourse(course5);
		teacher8.addCourse(course1);
		
		teacher9.addCourse(course6);
		teacher9.addCourse(course2);
//
//		//Connect educations with course so they'll be persisted along with the courses
//		
		course1.addEducation(education1);
		course1.addEducation(education2);
		course1.addEducation(education3);
		
		course2.addEducation(education2);
		course2.addEducation(education3);
		course2.addEducation(education4);
		
		course3.addEducation(education1);
		course4.addEducation(education4);
		
		course5.addEducation(education3);
		course5.addEducation(education1);
		course6.addEducation(education1);
		course6.addEducation(education3);
//		
//		
//		
		em.persist(course1);
		em.persist(course2);
		em.persist(course3);
		em.persist(course4);
		em.persist(course5);
		em.persist(course6);
		
		
		

		tx.commit();
	}

}
