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
import model.EntityType;

public class SchoolMgmtProject {
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU");

    public static void main(String[] args) {

        setUpData();
        Controller controller = new Controller();
        
        
        List<Teacher> set = (List<Teacher>) controller.getAll(EntityType.TEACHER);
        set.forEach(System.out::println);
//        List<Course> set1 = (List<Course>) controller.getAll(EntityType.COURSE);
//        set1.forEach(System.out::println);
//        List<Education> set2 = (List<Education>) controller.getAll(EntityType.EDUCATION);
//        set2.forEach(System.out::println);
//        List<Student> set3 = (List<Student>) controller.getAll(EntityType.STUDENT);
//        set3.forEach(System.out::println);

        List<Integer> courses = new ArrayList<Integer>();
        courses.add(2);
        
        controller.associate(EntityType.TEACHER, 1, courses);
        set.forEach(System.out::println);
    }

	public static void setUpData() {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		Student student = new Student("Johannes", "Döpare", LocalDate.of(1999, 01, 31), "johannes@gmail.com");
        Student student1 = new Student("Nicolas", "Johansson", LocalDate.of(1987, 03, 17), "nicolas@gmail.com");
        Student student2 = new Student("Robert", "Drönare", LocalDate.of(1983, 12, 25), "rob@gmail.com");
        
        em.persist(student);
        em.persist(student1);
        em.persist(student2);
        
        Teacher t1 = new Teacher("Bita", "Jabbari", LocalDate.of(1980, 07, 13), "bita@gmail.com");
        Teacher t2 = new Teacher("Ulf", "Snulf ", LocalDate.of(1913, 07, 13), "ulf@gmail.com");
        
        em.persist(t1);
        em.persist(t2);
        
        Course c1 = new Course("Spanska A", "Språk", "junior", 5);
        Course c2 = new Course("Spanska B", "Språk", "senior", 10);
        
        em.persist(c1);
        em.persist(c2);
        
        Education e1 = new Education("Språkexpert", "Språkvetenskap", LocalDate.of(1999, 01, 31), LocalDate.of(1999, 02, 28));
        Education e2 = new Education("Javaexpert", "Coding", LocalDate.of(1999, 01, 31), LocalDate.of(1999, 02, 28));
        
        em.persist(e1);
        em.persist(e2);
        
        t1.addCourse(c1);
        t1.addCourse(c2);
        
        t2.addCourse(c1);
        
        e1.addCourse(c2);
        
        e2.addCourse(c2);
        e2.addCourse(c1);
        
        e1.addStudent(student);
        e1.addStudent(student1);
        
        e2.addStudent(student1);
        e2.addStudent(student2);
        
        tx.commit();
	}

}
