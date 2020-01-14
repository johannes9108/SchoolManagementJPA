package ui;

import domain.Course;
import domain.Education;
import domain.Student;
import java.time.LocalDate;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class annotTestMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU");
        EntityManager em = emf.createEntityManager();

        /*
        Test 1.
        Skapa education, course och student. Knyta course till education
        och student till education.
         */
        Education ed = new Education("Javadeveloper", "Computer Science", LocalDate.now(), LocalDate.now().plusYears(2));
        Course c = new Course("JPA", "Java", "Medium", 25);
        Student s = new Student("Nicolas", "Dumont", LocalDate.now().minusYears(25), "nicolas@dumont.se");

        ed.addCourse(c);
        ed.addStudent(s);

        em.getTransaction().begin();
        em.persist(ed);
        em.getTransaction().commit();

//        /*
//        Test 2.
//        Hitta education och ta bort denna. 
//        Kontrollera om Student och Course kvarstår i databas eller
//        om de också försvinner!
//        */
//        
//        /*
//        KRASCH!!
//        Cannot delete or update a parent row: a foreign key constraint fails 
//        (`schoolmgmt`.`student`, CONSTRAINT `FK_STUDENT_EDUCATION_ID` 
//        FOREIGN KEY (`EDUCATION_ID`) REFERENCES `education` (`ID`))
//        */
//        Education e = em.find(Education.class, 1);
//        
//        em.getTransaction().begin();
//        em.remove(e);
//        em.getTransaction().commit();
        
//        /*
//        Test 3.
//        Knyta fler Course till en Education och radera Education
//        utan att course förvinner.
//        */
//        Course c1 = new Course("Java", "Java", "Easy", 50);
//        Course c2 = new Course("WebbApp", "Java", "Medium", 25);
//        ed.addCourse(c1);
//        ed.addCourse(c2);
//        em.getTransaction().begin();
//        em.persist(ed);
//        em.getTransaction().commit();
//        Education e1 = em.find(Education.class, 51);
//        em.getTransaction().begin();
//        em.remove(e1);
//        em.getTransaction().commit();
//        
//        /*
//        Test 4.
//        Knyta fler Students till en Education och radera Education
//        utan att student förvinner.
//        */
//        Student stud = new Student("Matteo", "Dumont", LocalDate.now().minusMonths(11), "matteo@hotmail.se");
//        Student stud1 = new Student("Test", "Person", LocalDate.now(), "test@person.se");
//        ed.addStudent(stud);
//        ed.addStudent(stud1);
//        em.getTransaction().begin();
//        em.persist(ed);
//        em.getTransaction().commit();
//        Education e2 = em.find(Education.class, 51);
//        em.getTransaction().begin();
//        em.remove(e2);
//        em.getTransaction().commit();
//        
//        /*
//        Test 5.
//        Radera Course knyten till Education.
//        */
//        Course course = em.find(Course.class, 51);
//        
//        em.getTransaction().begin();
//        em.remove(course);
//        em.getTransaction().commit();
//        
//        /*
//        Test 6.
//        Radera student knyten till Education.
//        */
//        Student student = em.find(Student.class, 51);
//        
//        em.getTransaction().begin();
//        em.remove(student);
//        em.getTransaction().commit();
    }

}
