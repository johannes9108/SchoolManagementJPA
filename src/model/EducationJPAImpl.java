package model;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import domain.Education;
import domain.Student;
import java.util.HashSet;

public class EducationJPAImpl implements SchoolManagementDAO<Education>, EducationDAO {

	private EntityManagerFactory emf;

	private EntityManager em;

	public EducationJPAImpl(EntityManagerFactory emf) {
		this.emf = emf;
	}

	@Override
	public int add(Education education) {

		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.persist(education);
			tx.commit();
			return education.getId();
		} catch (Exception exception) {
			System.out.println("Couldn't Persist the object" + education);
			return -1;
		} finally {
			if (em != null)
				em.close();
		}
	}

	@Override
	public int updateEducation(Education education) {
		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
                        em.merge(education);
//			Education educationUpdate = em.find(Education.class, education.getId());
//			educationUpdate.setName(education.getName());
//			educationUpdate.setFaculty(education.getFaculty());
//			educationUpdate.setFinalDate(education.getFinalDate());
//			educationUpdate.setStartDate(education.getStartDate());
//			educationUpdate.setCourses(education.getCourses());
//			educationUpdate.setStudents(education.getStudents());
			tx.commit();
			return education.getId();
		} catch (Exception e) {
			System.out.println("Couldn't Persist the object" + education);
			return -1;
		} finally {
			if (em != null)
				em.close();
		}
	}

	@Override
	public Education getById(int id) {
		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			Education education = em.find(Education.class, id);
			tx.commit();
                        
                     
			return education;
		} catch (Exception e) {
			System.out.println("Couldn't find the Education with ID: " + id);
			return null;
		} finally {
			if (em != null)
				em.close();
		}
	}

	@Override
	public List<Education> getAll() {
		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Query query = em.createQuery("from Education e");
			List<Education> education = (List<Education>) query.getResultStream().collect(Collectors.toList());

			tx.commit();
			return education;
		} catch (Exception exception) {
			System.out.println("Couldn't get the collection of Education objects");
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
			Education education = em.find(Education.class, id);
                        List <Student> students = education.getStudents();
                        for (Student student : students) {
                        student.clearEducation();
                        }
//                        education.getStudents().clear();
			em.remove(education);
			tx.commit();
			return id;
		} catch (Exception exception) {
			System.out.println("Couldn't remove the object with ID: " + id + exception);
			return -1;
		} finally {
			if (em != null)
				em.close();
		}
	}
}
