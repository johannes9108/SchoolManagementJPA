package ui;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import domain.Course;
import domain.Education;
import domain.Student;
import domain.Teacher;


public class TestHarness {
	static EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU");

	public static void main(String[] args) {
		
		setUpData();
		EntityManager  em = emf.createEntityManager();
		em.getTransaction().begin();
		
		long size = (long) em.createQuery("select count(c) from Course as c").getSingleResult();
		System.out.println(size);
		
		List<Course> result = em.createQuery("select c from  Course c").getResultList();
		for (Course course : result) {
			System.out.println(course);
		}
		
		em.getTransaction().commit();
//		em.getTransaction().begin();
//		
////		em.persist(new Computer("IBM"));
//		
//		Query q = em.createQuery("SELECT c FROM Computer c");
//		q.getResultStream().forEach(System.out::println);
//		
//		em.getTransaction().commit();

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
        tx.commit();
	}

}
