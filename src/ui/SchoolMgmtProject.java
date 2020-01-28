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
import java.time.Month;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import model.EntityType;
import static ui.Menu.menu;

public class SchoolMgmtProject {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU");


    public static void main(String[] args) {

        setUpData();
        
        menu();
        
//        Controller controller = new Controller();
////        
//////        Education ed1 = controller.getById(1, EntityType.EDUCATION);
////        Education ed2 = controller.getById(2, EntityType.EDUCATION);
////        System.out.println(ed2);
//////        
//        Course course1before = controller.getById(1, EntityType.COURSE);
//        System.out.println(course1before);
////        
////        ed2.removeCourse(course1before);
//////        
////////        Teacher t1 = controller.getById(1, EntityType.TEACHER);
//        Teacher t2 = controller.getById(2, EntityType.TEACHER);
////////        Course course2 = controller.getById(2, EntityType.COURSE);
////////        System.out.println("\n"+t1);
////////        System.out.println(course2);
//        System.out.println(t2);
////////        System.out.println("////////////////");
//////        
////////        course2.addTeacher(t1);
////////        course1before.removeTeacher(t1);
//        t2.removeCourse(course1before);
//////
//////////        course2.removeTeacher(t2);
////        controller.updateCourse(course1before);
////        controller.updateEducation(ed2);
//////        
//////        
////////        controller.updateCourse(course2);
//////        controller.updateTeacher(t1);
//        controller.updateTeacher(t2);
//        controller.updateCourse(course1before);
////        
////////        System.out.println(course2);
////////        System.out.println(t1);
////System.out.println(course1before);
//////        
//////        
////////        Teacher t1_newFetch = controller.getById(1, EntityType.TEACHER);
//        Course course1_newFetch = controller.getById(1, EntityType.COURSE);
////        System.out.println("//////////kalle////");
//////        System.out.println(t1_newFetch);
//        System.out.println(course1_newFetch);
//////
////        Education ed2_newFetch = controller.getById(2, EntityType.EDUCATION);
////        System.out.println("//////////////");
////        System.out.println(ed2_newFetch);
//////
//        Teacher t2_newFetch = controller.getById(2, EntityType.TEACHER);
//////        Course course2_newFetch = controller.getById(2, EntityType.COURSE);
////        System.out.println("//////////////");
//        System.out.println(t2_newFetch);
//////        System.out.println(course2_newFetch);

        
        
//////////////        Education edu1 = controller.getById(1, EntityType.EDUCATION);
//////        Education edu2 = controller.getById(2, EntityType.EDUCATION);
////////////
//////////////        Student stud1 = controller.getById(4, EntityType.STUDENT);
//////////////        Student stud2 = controller.getById(4, EntityType.STUDENT);
//////////////        Student stud3 = controller.getById(3, EntityType.STUDENT);
//////        Student stud4 = controller.getById(4, EntityType.STUDENT);
////////////        edu2.addStudent(stud4);
////////////
////////////        controller.update(edu2);
////////////        
////////////
//////        System.out.println("studentlista EDUCATION 2");
//////        for (Student student : (edu2.getStudents())) {
//////            System.out.println(student.getFirstName());
//////        }
//////
//////        Education edu1 = controller.getById(1, EntityType.EDUCATION);
////////////        controller.update(edu1);
////////////
////////////
//////        System.out.println("studentlista EDUCATION 1");
//////        for (Student student : (edu1.getStudents())) {
//////            System.out.println(student.getFirstName());
//////        }
////////////        
//////        System.out.println(stud4);
//////        List <Student> allStudents = (List<Student>) (controller.getAll(EntityType.STUDENT));
//////        for (Student student : allStudents) {
//////            System.out.println(student);
//////        }

        
        
        
        
//        Student student4 = controller.getById(4, EntityType.STUDENT);
//        System.out.println(student4.getFirstName() + "(ID:" + student4.getEducation() +")");
        //Student updateRobert = new Student("Robert", "Pettersson Svensson", LocalDate.of(1983, Month.DECEMBER, 25), "robert.p.svensson@gmail.com");
        //updateRobert.setId(4);
//
//
//        //hämta Student-objekt
//        EntityManager em = emf.createEntityManager(); //TILLHÖR LÅNGA TESTET
//        em.getTransaction().begin();
//        Student sX = em.find(Student.class, 4);
//        em.getTransaction().commit();
//        em.close();
////        Student sX = controller.getById(4, EntityType.STUDENT);
//        
//        System.out.println("have fetched "+sX.getFirstName());
//
//
//        //hämta Education-objekt
//        EntityManager em2 = emf.createEntityManager(); //TILLHÖR LÅNGA TESTET
//        em2.getTransaction().begin();
//        Education eX = em2.find(Education.class, 1);
//        em2.getTransaction().commit();
//        em2.close();      
////        Education eX = controller.getById(1, EntityType.EDUCATION);
//
//        sX.setEducation(eX);
//        
//        //uppdatera studentobjekt
//        EntityManager em3 = emf.createEntityManager(); //TILLHÖR LÅNGA TESTET
//        em3.getTransaction().begin();
//        em3.merge(sX);
//        em3.getTransaction().commit();
//        em3.close();      
////        controller.update(sX);
//
//        Iterator it = eX.getStudents().iterator();
//        while (it.hasNext()) {
//            System.out.println(it.next());
//        }
//
//        for (Student student : eX.getStudents()) {
//            System.out.println(student.getId() + " " + student.getFirstName());
//        }
//
//        System.out.println("\n");
//        
//        //hämta Education-objekt
//        EntityManager em4 = emf.createEntityManager(); //TILLHÖR LÅNGA TESTET
//        em4.getTransaction().begin();
//        Education eX2 = em4.find(Education.class, 1);
//        em4.getTransaction().commit();
//        em4.close();      
////        Education eX2 = controller.getById(1, EntityType.EDUCATION);
//
//
//        List<Student> listan = eX2.getStudents();
//
//        for (Student student : listan) {
//            System.out.println(student.getId() + " " + student.getFirstName());
//        }
//
//        Iterator it2 = eX2.getStudents().iterator();
//        while (it2.hasNext()) {
//            System.out.println(it2.next());
//        }
//        
        //////////////////////////////////////
//        boolean stop = false;
//        Scanner keyboard = new Scanner(System.in);
//
//        while (stop == false) {
//            System.out.println("Which student to set Educasiton for?");
//            int studentId = keyboard.nextInt();
//            keyboard.nextLine();
//            System.out.println("Which Education to set for studentID #" + studentId + "?");
//            int educationId = keyboard.nextInt();
//            keyboard.nextLine();
//
//            byStudentIdSetEducationIdAndPrintStudentList_MERGESTUDENT(studentId, educationId);
////            byStudentIdSetEducationIdAndPrintStudentList_MERGEEDUCATION(studentId, educationId);
//
//        }
        //////////////////////////////////////
//        controller.removeById(1, EntityType.EDUCATION);
//        controller.removeById(2, EntityType.EDUCATION);
//        List <Education> educations = (List<Education>) controller.getAll(EntityType.EDUCATION);
//        for (Education education : educations) {
//            System.out.println(education.getName());
//        }
//        List<Teacher> set = (List<Teacher>) controller.getAll(EntityType.TEACHER);
//        set.forEach(System.out::println);
//        List<Course> set1 = (List<Course>) controller.getAll(EntityType.COURSE);
//        set1.forEach(System.out::println);
//        List<Education> set2 = (List<Education>) controller.getAll(EntityType.EDUCATION);
//        set2.forEach(System.out::println);
//        List<Student> set3 = (List<Student>) controller.getAll(EntityType.STUDENT);
//        set3.forEach(System.out::println);
//        List<Integer> courses = new ArrayList<Integer>();
//        courses.add(2);
//        
//        controller.associate(EntityType.TEACHER, 1, courses);
//        set.forEach(System.out::println);
    }

    public static void setUpData() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Student student1 = new Student("Johannes", "Döpare", LocalDate.of(1999, 1, 31), "johannes@gmail.com");
        Student student2 = new Student("Nicolas", "Johansson", LocalDate.of(1987, 3, 17), "nicolas@gmail.com");
        Student student3 = new Student("Robert", "Drönare", LocalDate.of(1983, 12, 25), "rob@gmail.com");
        Student student4 = new Student("Lisa", "Andersson", LocalDate.of(1999, 3, 24), "lisa@gmail.com");
        Student student5 = new Student("Yuliet", "Ramirez", LocalDate.of(1984, 8, 11), "yuliet@gmail.com");

        em.persist(student1);
        em.persist(student2);
        em.persist(student3);
        em.persist(student4);
        em.persist(student5);

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
        
//        c1.addTeacher(t1);
//        c1.addTeacher(t2);
//        
//        c2.addTeacher(t1);

        t1.addCourse(c1);
        t1.addCourse(c2);

        t2.addCourse(c1);

        e1.addCourse(c2);

        e2.addCourse(c2);
        e2.addCourse(c1);

        e1.addStudent(student1);
//        student.setEducation(e1); //johannes

        e2.addStudent(student2);//nicolas
        e2.addStudent(student3);//robert
        e2.addStudent(student4);//lisa

//        student1.setEducation(e2); //nicolas
//        student2.setEducation(e2);//robert
//        student3.setEducation(e2); //lisa
        tx.commit();

        em.close();
    }

}
