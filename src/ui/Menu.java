package ui;

import domain.Controller;
import domain.Course;
import domain.Education;
import domain.Student;
import domain.Teacher;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import model.EntityType;

public class Menu {

    public static Scanner keyboard = new Scanner(System.in);
    public static Controller controller = new Controller();

    //////////FUNCTIONS//////////////////////
    static int tryCatchUserPositiveIntegerInputMenu(int min, int max) {
        int choice = -1;
        try {
            choice = intInput();
            if (choice < min || choice > max) {
                System.out.println("Not in range, try again!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ooops, not a number, try again!");
        }
        return choice;
    }

    static int intInput() {
        int intInPut = 0;
        boolean loop = true;
        while (loop) {
            System.out.print(": ");
            try {
                intInPut = Integer.parseInt(keyboard.nextLine());
                loop = false;
            } catch (NumberFormatException e) {
                System.out.print("Use numbers");
            }
        }
        return intInPut;
    }

    static String stringInput() {
        boolean stringIsValid = true;
        String string = "";
        while (stringIsValid) {
            System.out.print(": ");
            char first;
            string = keyboard.nextLine();
            if (string.length() > 0) {
                first = string.charAt(0);
                stringIsValid = false;
            } else {
                System.out.println("invalid input, try again");
            }
        }
        return string;
    }

    static String fixed(String string, int length) {
        return String.format("%1$" + length + "s", string);
    }

    ///////PRINT FUNCTIONS/////////////////////
    static void printsListOfStudents(List<Student> students) {
        for (Student student : students) {
            System.out.println("ID: " + student.getId() + " " + fixed(student.getBirthDate().toString(), 15) + " " + fixed(student.getEmail(), 20) + " " + fixed(student.getFirstName(), 15) + " " + fixed(student.getLastName(), 15) + "      Education: " + student.getEducation().getId());
        }
    }

    static void printsListOfTeachers(List<Teacher> teachers) {
        for (Teacher teacher : teachers) {
            System.out.println("ID: " + teacher.getId() + " " + fixed(teacher.getBirthDate().toString(), 15) + " " + fixed(teacher.getEmail(), 20) + " " + fixed(teacher.getFirstName(), 15) + " " + fixed(teacher.getLastName(), 15));
        }
    }

    static void printsListOfEducations(List<Education> educations) {
        for (Education education : educations) {
            System.out.println("ID: " + education.getId() + "  " + fixed(education.getFaculty(), 20) + "  " + fixed(education.getFinalDate().toString(), 15) + "  " + fixed(education.getName(), 20) + " " + fixed(education.getStartDate().toString(), 15));
        }
    }

    static void printsListOfCourses(List<Course> courses) {
        for (Course course : courses) {
            System.out.println("ID: " + course.getId() + "  " + fixed(course.getDifficulty(), 10) + "  " + fixed(course.getName(), 15) + fixed("Points:", 10) + fixed(Integer.toString(course.getPoints()), 3) + fixed(course.getSubject(), 15));
        }
    }

    static void printDivider() {
        System.out.println("//////////////////////////////////////////////////////////////////////////");
    }

    /////////// MAIN MENU /////////////////////
    public static void menu() {
        boolean menuLoop = true;
        while (menuLoop) {
            System.out.println("\nHUVUDMENY");
            System.out.println("[1] VISA");
            System.out.println("[2] UPPDATERA");
            System.out.println("[3] TA BORT");
            System.out.println("[4] NYREGISTRERING");
            System.out.println("[0] EXIT PROGRAM");
            System.out.print("\nVälj mellan [1][2][3][4][0]");
            int minIntInclusive = 0;
            int maxIntInclusive = 4;
            int choice = tryCatchUserPositiveIntegerInputMenu(minIntInclusive, maxIntInclusive);
            switch (choice) {
                case 1:
                    showMenu();
                    break;
                case 2:
//                    updateMenu();
                    break;
                case 3:
//                    removeMenu();
                    break;
                case 4:
//                    newRegistrationMenu();
                    break;
                case 0:
                    System.out.println("Exit program");
                    menuLoop = false;
                    break;

            }
        }
    }
    /////////// END MAIN MANU ////////////////

    //////////// DISPLAY MENUS ////////////////
    static void showMenu() {
        System.out.println("\nVAD VILL DU SE?");
        System.out.println("[1] UTBILDNINGAR");
        System.out.println("[2] KURSER");
        System.out.println("[3] ELEVER");
        System.out.println("[4] LÄRARE");
        System.out.println("[0] TILLBAKA TILL HUVUDMENY");
        System.out.print("\nVälj mellan [1][2][3][4][0]");
        int minIntInclusive = 0;
        int maxIntInclusive = 4;
        int choice = tryCatchUserPositiveIntegerInputMenu(minIntInclusive, maxIntInclusive);
        switch (choice) {
            case 1:
                showEducations();
                break;
            case 2:
                showCourses();
                break;
            case 3:
                showStudents();
                break;
            case 4:
                showTeachers();
                break;
            case 0:
                menu();
                break;
        }

    }

    static void showEducations() {
        printDivider();
        List<Education> educations = (List<Education>) controller.getAll(EntityType.EDUCATION);
        System.out.println("\nUTBILDNINGAR:");
        printsListOfEducations(educations);

        System.out.print("\nVälj UTBILDNING att visa student- och kurslista genom att ange [ID] eller ange [0] för TILLBAKA");

        List<Integer> listofEducationIds = educations.stream().map(Education::getId).collect(Collectors.toList());
        boolean loop = true;
        while (loop) {
            int choice = intInput();
            if (listofEducationIds.contains(choice)) {
                loop = false;
                showStudentsforEducationById(choice);
                showCoursesforEducationById(choice);
                showMenu();
            } else if (choice == 0) {
                loop = false;
                showMenu();
            } else {
                System.out.println("Du har angivet ett ID för UTBILDNING om inte existerar");
            }
        }
    }

    static void showStudentsforEducationById(int EducationId) {
        Education education = controller.getById(EducationId, EntityType.EDUCATION);
        System.out.println("\nSTUDENTER - " + education.getName().toUpperCase() + "(ID: " + education.getId() + ")");
        printsListOfStudents(education.getStudents());
    }

    static void showCoursesforEducationById(int EducationId) {
        Education education = controller.getById(EducationId, EntityType.EDUCATION);
        System.out.println("\nKURSER - " + education.getName().toUpperCase() + "(ID: " + education.getId() + ")");
        printsListOfCourses(education.getCourses());
    }

    static void showCourses() {
        printDivider();
        List<Course> courses = (List<Course>) controller.getAll(EntityType.COURSE);
        System.out.println("\nKURSER:");
        printsListOfCourses(courses);

        System.out.print("\nVälj KURS att visa vilka Utbildningar kursen ingår i och vilka Lärare som utbildar i kursen genom att ange [ID] eller ange [0] för TILLBAKA");

        List<Integer> listofCourseIds = courses.stream().map(Course::getId).collect(Collectors.toList());
        boolean loop = true;
        while (loop) {
            int choice = intInput();
            if (listofCourseIds.contains(choice)) {
                loop = false;
                showTeachersforCourseById(choice);
                showEducationsforCourseById(choice);
                showMenu();
            } else if (choice == 0) {
                loop = false;
                showMenu();
            } else {
                System.out.println("Du har angivet ett ID för KURS om inte existerar");
            }
        }

    }

    static void showTeachersforCourseById(int CourseId) {
        Course course = controller.getById(CourseId, EntityType.COURSE);
        System.out.println("\nLÄRARE - " + course.getName().toUpperCase() + "(ID: " + course.getId() + ")");
        printsListOfTeachers(course.getTeachers());
    }

    static void showEducationsforCourseById(int CourseId) {
        Course course = controller.getById(CourseId, EntityType.COURSE);
        System.out.println("\nUTBILDNINGAR - " + course.getName().toUpperCase() + "(ID: " + course.getId() + ")");
        printsListOfEducations(course.getEducations());
    }

    static void showStudents() {
        printDivider();
        List<Student> students = (List<Student>) controller.getAll(EntityType.STUDENT);
        System.out.println("\nSTUDENTER:");
        printsListOfStudents(students);
        updateStudentMenu();
    }

    static void showTeachers() {
        printDivider();
        List<Teacher> teachers = (List<Teacher>) controller.getAll(EntityType.TEACHER);
        System.out.println("\nLÄRARE:");
        printsListOfTeachers(teachers);
        System.out.print("\nVälj LÄRARE för att visa vilka Kurser som läraren utbildar i genom att ange [ID] eller ange [0] för TILLBAKA");

        List<Integer> listofTeacherIds = teachers.stream().map(Teacher::getId).collect(Collectors.toList());
        boolean loop = true;
        while (loop) {
            int choice = intInput();
            if (listofTeacherIds.contains(choice)) {
                loop = false;
                showCoursesforTeacherById(choice);
                showMenu();
            } else if (choice == 0) {
                loop = false;
                showMenu();
            } else {
                System.out.println("Du har angivet ett ID för LÄRARE om inte existerar");
            }
        }
    }

    static void showCoursesforTeacherById(int TeacherId) {
        Teacher teacher = controller.getById(TeacherId, EntityType.TEACHER);
        System.out.println("\nKURSER - " + teacher.getFirstName().toUpperCase() + " " + teacher.getLastName().toUpperCase() + "(ID: " + teacher.getId() + ")");
        printsListOfCourses(teacher.getCourses());
    }
    //////////// END DISPLAY MENUS ////////////////

    //////////// UPDATE MENUS ////////////////
    static void updateEducationForByStudentID(int StudentID) {
        Student student = controller.getById(StudentID, EntityType.STUDENT);

        System.out.println("\nUTBILDNINGAR:");
        List<Education> educations = (List<Education>) controller.getAll(EntityType.EDUCATION);
        printsListOfEducations(educations);

        if (student.getEducation() != null) {
            System.out.println("\n" + student.getFirstName()
                    + " är nu inskriven på: " + student.getEducation().getName()
                    + "(ID:" + student.getEducation().getId() + ")");
        }

        System.out.print("Ange ID för utbildning som " + student.getFirstName() + " ska skirvas in på (0=Avbryt)");

        List<Integer> listofEducationIds = educations.stream().map(Education::getId).collect(Collectors.toList());
        boolean loop = true;
        while (loop) {
            int newEducationID = intInput();
            if (listofEducationIds.contains(newEducationID)) {
                loop = false;
                Education oldEducation = student.getEducation();
                Education newEducation = controller.getById(newEducationID, EntityType.EDUCATION);
//                newEducation.addStudent(student);
                student.setEducation(newEducation);
                if (controller.update(student) != -1) {
//                    controller.update(newEducation);
//                    controller.update(oldEducation);
                    System.out.println("Utbildnig för student " + student.getFirstName() + " " + student.getLastName() + " (id:" + student.getId() + ") har uppdaterats till " + student.getEducation().getName() + " (id:" + student.getEducation().getId() + ")");
                }
                showStudents();
            } else if (newEducationID == 0) {
                loop = false;
                showStudents();
            } else {
                System.out.println("Du har angivet ett ID för UTBILDNING om inte existerar");
            }

        }
    }

    static void updateStudentMenu() {
        System.out.print("\nAnge STUDENT genom att ange [ID] om du vill uppdatera eller ange [0] för TILLBAKA");

        List<Student> allStudents = (List<Student>) controller.getAll(EntityType.STUDENT);
        List<Integer> listofStudentIds = allStudents.stream().map(Student::getId).collect(Collectors.toList());

        boolean loop = true;
        while (loop) {
            int student = intInput();
            if (listofStudentIds.contains(student)) {
                loop = false;
                updateEducationForByStudentID(student);
            } else if (student == 0) {
                loop = false;
                showMenu();
            } else {
                System.out.println("Du har angivet ett ID för STUDENT om inte existerar");
            }
        }
    }

}
