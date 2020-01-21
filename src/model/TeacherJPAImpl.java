package model;
 
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import domain.Teacher;
 
public class TeacherJPAImpl implements SchoolManagementDAO<Teacher> {
 
    private EntityManagerFactory emf;
    private EntityManager em;
 
    public TeacherJPAImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }
 
    @Override
    public int add(Teacher teacher) {
        em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(teacher);
            tx.commit();
            return teacher.getId();
        } catch (PersistenceException exception) {
            System.out.println("Couldn't Persist the object" + teacher);
            return -1;
        } finally {
			if (em != null)
				em.close();
		}
    }
 
    @Override
    public int update(Teacher teacher) {
        em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Teacher teacherUpdate = em.find(Teacher.class, teacher.getId());
            teacherUpdate.setFirstName(teacher.getFirstName());
            teacherUpdate.setLastName(teacher.getLastName());
            teacherUpdate.setEmail(teacher.getEmail());
            teacherUpdate.setBirthDate(teacher.getBirthDate());
            teacherUpdate.setCourses(teacher.getCourses());
            System.out.println(teacherUpdate);
            tx.commit();
            return teacherUpdate.getId();
        } catch (PersistenceException exception) {
            System.out.println("Couldn't update the object" + teacher);
            return -1;
        } finally {
			if (em != null)
				em.close();
		}
    }
 
    @Override
    public Teacher getById(int id) {
        em = emf.createEntityManager();
 
        try {
            em.getTransaction().begin();
            Teacher newTeacher = em.find(Teacher.class, id);
//            System.out.println(newTeacher);
            em.getTransaction().commit();
            return newTeacher;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
			if (em != null)
				em.close();
		}
    }
 
    @Override
    public List<Teacher> getAll() {
        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createQuery("FROM Teacher t");
            System.out.println(query.toString());
            List<Teacher> list = (List<Teacher>) query.getResultStream().collect(Collectors.toList());
            em.getTransaction().commit();
            return list;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
			if (em != null)
				em.close();
		}
    }
 
    @Override
    public int removeById(int id
    ) {
        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Teacher teacherToRemove = em.find(Teacher.class, id);
            em.remove(teacherToRemove);
            em.getTransaction().commit();
            return id;
        } catch (Exception e) {
            System.out.println(e);
            return -1;
        } finally {
			if (em != null)
				em.close();
		}
 
    }
 
}