package test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import domain.Course;
import domain.Education;
import domain.Student;
import domain.Teacher;
import ui.SchoolMgmtProject;

@TestInstance(Lifecycle.PER_CLASS)
class CRUDTests {

	EntityManagerFactory emf;
	EntityManager em;
	EntityTransaction tx;
	
	@BeforeAll
	void init_Factory(){
		emf = Persistence.createEntityManagerFactory("PU");	
		SchoolMgmtProject.setUpData();
		
	}
	
	@BeforeEach
	void init_EM() {
		em = emf.createEntityManager();
	}
	
	@Test
	void CourseCRUDTest() {
		tx = em.getTransaction();
		tx.begin();
		// ADD + READ
		Course newCourse = new Course("Ryska 105", "Språk", "junior", 150);
		em.persist(newCourse);
		Course findNewCourse = (Course)em.createQuery("select c from Course c WHERE c.name like 'Ryska 105'").getSingleResult();
		assertEquals(findNewCourse.getSubject(),"Språk");
		
		// UPDATE
		findNewCourse.setDifficulty("UFOO");
		Course updatedObject = (Course)em.createQuery("select c from Course c WHERE c.difficulty like 'UFOO'").getSingleResult();
		assertEquals("UFOO", updatedObject.getDifficulty());
		
		// DELETE
		long size = (long)em.createQuery("select count(c) from Course as c").getSingleResult();
		assertEquals(3, size);
		em.remove(newCourse);
		size = (long)em.createQuery("select count(c) from Course as c").getSingleResult();
		assertEquals(2, size);
		
		tx.commit();
		
	}
	
	@Test
	void TeacherCRUDTest() {
		tx = em.getTransaction();
		tx.begin();
		// ADD + READ
		Teacher newTeacher = new Teacher("Anna", "Testare", LocalDate.of(2000, 01, 05), "Anna.testare@gmail.com");
		em.persist(newTeacher);
		Teacher findAddedTeacher = (Teacher)em.createQuery("select t from Teacher t WHERE t.firstName like 'Anna' and t.lastName like 'Testare'").getSingleResult();
		assertEquals(LocalDate.of(2000, 01, 05),findAddedTeacher.getBirthDate());
		
		// UPDATE
		findAddedTeacher.setEmail("ejEMAIL@MAIL.com");
		Teacher updatedObject = (Teacher)em.createQuery("select t from Teacher t WHERE t.email like 'ejEMAIL@MAIL.com'").getSingleResult();
		assertEquals("ejEMAIL@MAIL.com", updatedObject.getEmail());
		
		// DELETE
		long size = (long)em.createQuery("select count(t) from Teacher as t").getSingleResult();
		em.remove(newTeacher);
		long removedSize = (long)em.createQuery("select count(t) from Teacher as t").getSingleResult();
		assertEquals(size-1, removedSize);
		
		tx.commit();
		
	}
	@Test
	void StudentCRUDTest() {
		tx = em.getTransaction();
		tx.begin();
		// ADD + READ
		Student newStudent = new Student("Anna", "Testare", LocalDate.of(2000, 01, 05), "Anna.testare@gmail.com");
		em.persist(newStudent);
		Student findAddedStudent = (Student)em.createQuery("select t from Student t WHERE t.firstName like 'Anna' and t.lastName like 'Testare'").getSingleResult();
		assertEquals(LocalDate.of(2000, 01, 05),findAddedStudent.getBirthDate());
		
		// UPDATE
		findAddedStudent.setEmail("ejEMAIL@MAIL.com");
		Student updatedObject = (Student)em.createQuery("select t from Student t WHERE t.email like 'ejEMAIL@MAIL.com'").getSingleResult();
		assertEquals("ejEMAIL@MAIL.com", updatedObject.getEmail());
		
		// DELETE
		long size = (long)em.createQuery("select count(t) from Student as t").getSingleResult();
		em.remove(newStudent);
		long removedSize = (long)em.createQuery("select count(t) from Student as t").getSingleResult();
		assertEquals(size-1, removedSize);
		
		tx.commit();
		
	}
	@Test
	void EducationCRUDTest() {
		tx = em.getTransaction();
		tx.begin();
		// ADD + READ
		Education newEducation= new Education("Anna", "Testare", LocalDate.of(2000, 01, 05), LocalDate.of(2005, 01, 10));
		em.persist(newEducation);
		Education findAddedEducation = (Education)em.createQuery("select t from Education t WHERE t.name like 'Anna' and t.faculty like 'Testare'").getSingleResult();
		assertEquals(LocalDate.of(2000, 01, 05),findAddedEducation.getStartDate());
		assertEquals(LocalDate.of(2005,01,10),findAddedEducation.getFinalDate());
		
		// UPDATE
		findAddedEducation.setName("ejEMAIL@MAIL.com");
		Education updatedObject = (Education)em.createQuery("select t from Education t WHERE t.name like 'ejEMAIL@MAIL.com'").getSingleResult();
		assertEquals("ejEMAIL@MAIL.com", updatedObject.getName());
		
		// DELETE
		long size = (long)em.createQuery("select count(t) from Education as t").getSingleResult();
		em.remove(newEducation);
		long removedSize = (long)em.createQuery("select count(t) from Education as t").getSingleResult();
		assertEquals(size-1, removedSize);
		
		tx.commit();
		
	}
	
	@Test
	void relationShipCourse_Teacher() {
		Teacher teacher1 = em.find(Teacher.class, 1);
		Teacher teacher2 = em.find(Teacher.class, 2);
		
		assertTrue(teacher1.getCourses().size()>0);
		assertTrue(teacher2.getCourses().size()>0);
		
		Course course1 = em.find(Course.class, 1);
		Course course2 = em.find(Course.class, 2);
		
		assertTrue(course1.getTeachers().size()>0);
		assertTrue(course2.getTeachers().size()>0);
		
	}
	@Test
	void relationShipEducation_Course() {
		Education education1 = em.find(Education.class, 1);
		Education education2 = em.find(Education.class, 2);
		
		assertTrue(education1.getCourses().size()>0);
		assertTrue(education2.getCourses().size()>0);
		
		Course course1 = em.find(Course.class, 1);
		Course course2 = em.find(Course.class, 2);
		
		assertTrue(course1.getEducations().size()>0);
		assertTrue(course2.getEducations().size()>0);
		
		
	}

}
