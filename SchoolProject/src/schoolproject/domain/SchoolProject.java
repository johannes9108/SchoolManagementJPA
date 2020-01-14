package schoolproject.domain;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import schoolproject.domain.Education;

public class SchoolProject {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SchoolProjectPU");
        EntityManager em = emf.createEntityManager();

        Education ed = new Education("JavaUtvecklare");

        Course c = new Course("Java");

        Student s = new Student("Patrik Storm");

        ed.addCoursese(c);
        
        ed.addStudent(s);
//        
        Education e = em.find(Education.class, 51);
        em.getTransaction().begin();
//        em.remove(e);
        em.persist(ed);
        em.getTransaction().commit();
    }

}
