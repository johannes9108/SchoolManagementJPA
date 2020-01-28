package model;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import domain.Course;

public class CourseJPAImpl implements SchoolManagementDAO<Course>, CourseDAO {

	private EntityManagerFactory emf;

	private EntityManager em;

	public CourseJPAImpl(EntityManagerFactory emf) {
		this.emf = emf;
	}

	@Override
	public int add(Course course) {
		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.persist(course);
			tx.commit();
			return course.getId();
		} catch (PersistenceException exception) {
			System.out.println("Couldn't Persist the object" + course);
			return -1;
		} finally {
			if (em != null)
				em.close();
		}
	}

	@Override
	public int updateCourse(Course course) {
		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
//			Course courseUpdate = em.find(Course.class, course.getId());
//			courseUpdate.setName(course.getName());
//			courseUpdate.setDifficulty(course.getDifficulty());
//			courseUpdate.setSubject(course.getSubject());
//			courseUpdate.setEducations(course.getEducations());
//			courseUpdate.setPoints(course.getPoints());
//			courseUpdate.setTeachers(course.getTeachers());
                        em.merge(course);
			tx.commit();
			return course.getId();
		} catch (PersistenceException exception) {
			System.out.println("Couldn't update the object" + course);
			return -1;
		} finally {
			if (em != null)
				em.close();
		}
	}

	@Override
	public Course getById(int id) {
		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Course course = em.find(Course.class, id);
			tx.commit();
			return course;
		} catch (PersistenceException exception) {
			System.out.println("Couldn't find the course with the ID: " + id);
			return null;
		}finally {
			if (em != null)
				em.close();
		}
	}

	@Override
	public List<Course> getAll() {
		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Query query = em.createQuery("from Course c");
			List<Course> courses = (List<Course>) query.getResultStream().collect(Collectors.toList());

			tx.commit();
			return courses;
		} catch (PersistenceException exception) {
			System.out.println("Couldn't get the collection of Course objects");
			return null;
		} finally {
			if (em != null)
				em.close();
		}
	}

	@Override
	public int removeById(int id) {
		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Course course = em.find(Course.class, id);
			em.remove(course);
			tx.commit();
			return id;
		} catch (PersistenceException exception) {
			System.out.println("Couldn't remove the Course with the ID: " + id);
			return -1;
		} finally {
			if (em != null)
				em.close();
		}
	}

}
