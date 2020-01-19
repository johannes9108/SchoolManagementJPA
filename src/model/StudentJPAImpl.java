package model;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import domain.Student;

public class StudentJPAImpl implements SchoolManagementDAO<Student> {

private EntityManagerFactory emf;
	
	private EntityManager em;
	
	public StudentJPAImpl(EntityManagerFactory emf) {
		this.emf = emf;
	}

	@Override
	public int add(Student student) {

		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.persist(student);
			tx.commit();
			return student.getId();
		} catch (Exception exception) {
			System.out.println("Couldn't Persist the object" + student);
			return -1;
		} finally {
			if (em != null)
				em.close();
		}
	}

	@Override
	public int update(Student student) {
		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
                        em.merge(student);
//			Student studentUpdate = em.find(Student.class, student.getId());
//			studentUpdate.setBirthDate(student.getBirthDate());
//			studentUpdate.setEducation(student.getEducation());
//			studentUpdate.setEmail(student.getEmail());
//			studentUpdate.setFirstName(student.getFirstName());
			tx.commit();
			return student.getId();
		} catch (Exception e) {
			System.out.println("Couldn't update the object" + student);
			return -1;
		} finally {
			if (em != null)
				em.close();
		}
	}

	@Override
	public Student getById(int id) {
		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			Student student = em.find(Student.class, id);
			tx.commit();
			return student;
		} catch (Exception e) {
			System.out.println("Couldn't find the Student with ID: " + id);
			return null;
		} finally {
			if (em != null)
				em.close();
		}
	}

	@Override
	public List<Student> getAll() {
		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Query query = em.createQuery("from Student s");
			List<Student> student = (List<Student>) query.getResultStream().collect(Collectors.toList());
			tx.commit();
			return student;
		} catch (Exception exception) {
			System.out.println("Couldn't get the collection of Student objects");
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
			Student student = em.find(Student.class, id);
			em.remove(student);
			tx.commit();
			return id;
		} catch (Exception exception) {
			System.out.println("Couldn't remove the object with ID: " + id);
			return -1;
		} finally {
			if (em != null)
				em.close();
		}
	}
}
