package model;

import domain.Education;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import domain.Student;

public class StudentJPAImpl implements SchoolManagementDAO<Student>, StudentDAO {

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
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public int updateStudentPersonalData(Student student) {
        em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Student studentToUpdate = em.find(Student.class, student.getId());
            studentToUpdate.setBirthDate(student.getBirthDate());
            studentToUpdate.setEmail(student.getEmail());
            studentToUpdate.setFirstName(student.getFirstName());
            studentToUpdate.setLastName(student.getLastName());
            tx.commit();
            return student.getId();
        } catch (Exception e) {
            System.out.println("Couldn't update student-id: " + student.getId());
            return -1;
        } finally {
            if (em != null) {
                em.close();
            }
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
            if (em != null) {
                em.close();
            }
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
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public int removeById(int id) {

        em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Student studentToRemove = em.find(Student.class, id);
            Education oldEducation = studentToRemove.getEducation();
            if (oldEducation != null) {
                oldEducation.removeStudent(studentToRemove);
                em.merge(oldEducation);
            } //id student had a education before change, student is removed from its list of students

            em.remove(studentToRemove);
            tx.commit();
            return id;
        } catch (Exception exception) {
            System.out.println("Couldn't remove the student with ID: " + id);
            return -1;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    @Override
    public int removeEducationForStudent(int StudentId) {
        em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Student studentToUpdate = em.find(Student.class, StudentId);
            Education oldEducation = studentToUpdate.getEducation();
            studentToUpdate.setEducation(null);
            em.merge(studentToUpdate);
            if (oldEducation != null) {
                em.merge(oldEducation);
            } //if student had a education before change, student is remobed from its list of students
            tx.commit();
            return studentToUpdate.getId();
        } catch (Exception e) {
            System.out.println("Couldn't update education student-id: " + StudentId + e);
            return -1;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public int updateStudentEducationRelation(int StudentId, int EducationId) {
        em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Student studentToUpdate = em.find(Student.class, StudentId);
            Education oldEducation = studentToUpdate.getEducation();
            Education newEducation = em.find(Education.class, EducationId);

            newEducation.addStudent(studentToUpdate);
//            studentToUpdate.setEducation(newEducation); /* <-- This can also be used instead of 'newEducation.addStudent(studentToUpdate)'
            em.merge(newEducation);
            if (oldEducation != null) {
                em.merge(oldEducation);
            } //id student had a education before change, student is remobed from its list of students
            tx.commit();
            return studentToUpdate.getId();
        } catch (Exception e) {
            System.out.println("Couldn't update education student-id: " + StudentId + e);
            return -1;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
