package ui;

import domain.Controller;
import domain.Course;
import domain.Education;
import domain.Student;
import domain.Teacher;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import model.EntityType;

public class Menu {

    public static Scanner keyboard = new Scanner(System.in);
    public static Controller controller = new Controller();
//    public static StudentController studentController = new StudentController();
//    public static EducationController educationController = new EducationController();
//    public static TeacherController teacherController = new TeacherController();
//    public static CourseController courseController = new CourseController();

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

    static LocalDate stringAsDateInput() {
        boolean stringIsFalse = true;
        String dateString = "";
        while (stringIsFalse) {
            System.out.print(": ");
            dateString = keyboard.nextLine();
            if (isDateFormat(dateString)) {
                stringIsFalse = false;
            } else {
                System.out.println("Ogiltigt datumformat, ange YYYY-MM-DD. Förssök igen.");
            }
        }
        return LocalDate.parse(dateString);
    }

    static boolean isDateFormat(String dateString) {
        try {
            LocalDate date = LocalDate.parse(dateString);
            return true;
        } catch (DateTimeParseException e) {
        }
        return false;
    }

    static String fixed(String string, int length) {
        return String.format("%1$" + length + "s", string);
    }

    ///////PRINT FUNCTIONS/////////////////////
    static void printsListOfStudents(List<Student> students) {
        for (Student student : students) {
            if (student.getEducation() != null) {
                System.out.println("ID: " + student.getId() + " " + fixed(student.getBirthDate().toString(), 15) + " " + fixed(student.getEmail(), 20) + " " + fixed(student.getFirstName(), 15) + " " + fixed(student.getLastName(), 15) + "      Education: " + student.getEducation().getId());
            } else {
                System.out.println("ID: " + student.getId() + " " + fixed(student.getBirthDate().toString(), 15) + " " + fixed(student.getEmail(), 20) + " " + fixed(student.getFirstName(), 15) + " " + fixed(student.getLastName(), 15) + "      Education: ingen");
            }
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

    static void printStudentForEdit(Student student) {
        System.out.print(student.getFirstName().toUpperCase() + " " + student.getLastName().toUpperCase()
                + ", ID: " + student.getId()
        );
        System.out.println(
                "\n[1]Förnamn: " + student.getFirstName()
                + "\n[2]Efternamn: " + student.getLastName()
                + "\n[3]Födelsedatum: " + student.getBirthDate()
                + "\n[4]E-post: " + student.getEmail()
        );
    }

    static void printStudent(Student student) {
        System.out.println(student.getFirstName().toUpperCase() + " " + student.getFirstName().toUpperCase()
                + ", ID: " + student.getId()
                + "\nFödelsedatum: " + student.getBirthDate()
                + "\nE-post: " + student.getEmail());
        if (student.getEducation() != null) {
            System.out.println("\nUtbildning: " + student.getEducation().getName() + "(id:" + student.getEducation().getId() + ")");
        } else {
            System.out.println("\nUtbildning: ingen utbildning");
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
        System.out.println("[3] STUDENTER");
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
        System.out.print("\nAnge LÄRARES [ID] för att VISA LÄRARES KURSER eller UPPDATERA LÄRARE, eller ange [0] för TILLBAKA");

        List<Integer> listofTeacherIds = teachers.stream().map(Teacher::getId).collect(Collectors.toList());
        boolean loop = true;
        while (loop) {
            int choice = intInput();
            if (listofTeacherIds.contains(choice)) {
                loop = false;
                showCoursesOrUpdateTeacherById(choice);
                showMenu();
            } else if (choice == 0) {
                loop = false;
                showMenu();
            } else {
                System.out.println("Du har angivet ett ID för LÄRARE om inte existerar");
            }
        }
    }

    static void showCoursesOrUpdateTeacherById(int TeacherId) {
        Teacher teacher = controller.getById(TeacherId, EntityType.TEACHER);
        System.out.println("\nKURSER - " + teacher.getFirstName().toUpperCase() + " " + teacher.getLastName().toUpperCase() + "(ID: " + teacher.getId() + ")");
        printsListOfCourses(teacher.getCourses());
        updateTeacherMenu(TeacherId);
    }
    //////////// END DISPLAY MENUS ////////////////

    //////////// UPDATE STUDENT MENUS ////////////////
    static void updateEducationForStudentByID(int studentID) {
        Student student = controller.getById(studentID, EntityType.STUDENT);

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
                if (controller.updateStudentEducationRelation(studentID, newEducationID) != -1) {
                    System.out.println("Utbildnig för student " + student.getFirstName() + " " + student.getLastName() + " (id:" + student.getId() + ") har uppdaterats till " + student.getEducation().getName() + " (id:" + student.getEducation().getId() + ")");
                } else {
                    System.out.println("Fel: Kunde inte uppdatera utbildning.");
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

    static void updateDataForStudentByID(int studentID) {
        Student student = controller.getById(studentID, EntityType.STUDENT);

        int updateIsMade = 0;
        boolean menuLoop = true;
        while (menuLoop) {
            updateIsMade = updateIsMade + 1;
            System.out.print("\nUPPDATERA PERSONUPPGIFTER FÖR: ");
            printStudentForEdit(student);

            int minIntInclusive;
            int maxIntInclusive;
            if (updateIsMade > 1) {
                System.out.println("\n[5] Bekräfta uppdatering");
            }
            System.out.println("\n[0] TILLBAKA");

            if (updateIsMade > 1) {
                System.out.print("\nVälj mellan [1][2][3][4][5][0]");
                minIntInclusive = 0;
                maxIntInclusive = 5;
            } else {
                System.out.print("\nVälj mellan [1][2][3][4][0]");
                minIntInclusive = 0;
                maxIntInclusive = 4;
            }

            int choice = tryCatchUserPositiveIntegerInputMenu(minIntInclusive, maxIntInclusive);
            switch (choice) {
                case 1:
                    System.out.print("Nytt förnamn");
                    String newFirstName = stringInput();
                    student.setFirstName(newFirstName);
                    System.out.println("Nytt förnamn angivet\n");
                    break;
                case 2:
                    System.out.print("Nytt efternamn");
                    String newLastName = stringInput();
                    student.setLastName(newLastName);
                    System.out.println("Nytt förnamn angivet\n");
                    break;
                case 3:
                    System.out.print("Nytt födelsedatum");
                    LocalDate newBirthDate = stringAsDateInput();
                    student.setBirthDate(newBirthDate);
                    System.out.println("Nytt födelsedatum angivet\n");
                    break;
                case 4:
                    System.out.print("Ny e-postadress");
                    String newEmail = stringInput();
                    student.setEmail(newEmail);
                    System.out.println("Ny e-postadress angiven\n");
                    break;
                case 5:

                    if (controller.updateStudentPersonalData(student) != -1) {
                        System.out.println("Personuppgifter uppdaterade");
                    } else {
                        System.out.println("Problem inträffade, personuppgifter är inte uppdaterade");
                    }
                    menuLoop = false;
                    break;
                case 0:
                    menuLoop = false;
                    break;
            }
        }
    }

    static void deleteStudentByID(int studentID) {
        Student student = controller.getById(studentID, EntityType.STUDENT);
        System.out.println("\nBEKRÄFTA ATT DU VILL RADERA:");
        printStudent(student);
        System.out.println("\n[99]: BEKRÄFTA");
        System.out.println("[0]: AVBRYT");
        boolean loop = true;
        while (loop) {
            int confirm = intInput();
            switch (confirm) {
                case 99:
                    loop = false;
                    if (controller.removeById(studentID, EntityType.STUDENT) != -1) {
                        System.out.println(student.getFirstName() + " " + student.getLastName() + " borttagen!");
                    } else {
                        System.out.println("Fel: Kunde inte radera " + student.getFirstName() + " " + student.getLastName());
                    }
                    showStudents();
                    break;
                case 0:
                    loop = false;
                    showStudents();
                    break;
                default:
                    System.out.println("Du har inte gjort ett giltigt val. Försök igen.");
                    break;
            }

        }

//        if (controller.removeById(studentID, EntityType.STUDENT) != -1) {
//            System.out.println("");
//        }
    }

    static void removeEducationForStudent(int studentID) {
        Student student = controller.getById(studentID, EntityType.STUDENT);

        if (student.getEducation() != null) {
            System.out.println("\n" + student.getFirstName()
                    + " är nu inskriven på: " + student.getEducation().getName()
                    + "(ID:" + student.getEducation().getId() + ")");

            System.out.println("\nBEKRÄFTA ATT DU TA BORT DENNA UTBILDNING");
            System.out.println("\n[99]: BEKRÄFTA");
            System.out.println("[0]: AVBRYT");
            boolean loop = true;
            while (loop) {
                int confirm = intInput();
                switch (confirm) {
                    case 99:
                        loop = false;
                        if (controller.removeEducationForStudent(studentID) != -1) {
                            System.out.println("Utbildning är borttagen för " + student.getFirstName() + " " + student.getLastName() + "!");
                        } else {
                            System.out.println("Fel: Kunde inte ta bort utbildnig för " + student.getFirstName() + " " + student.getLastName());
                        }
                        showStudents();
                        break;
                    case 0:
                        loop = false;
                        showStudents();
                        break;
                    default:
                        System.out.println("Du har inte gjort ett giltigt val. Försök igen.");
                        break;
                }
            }

        }

    }

    static void updateStudentMenu() {
        System.out.print("\nAnge STUDENT genom att ange [ID] om du vill uppdatera eller ange [0] för TILLBAKA");

        List<Student> allStudents = (List<Student>) controller.getAll(EntityType.STUDENT);
        List<Integer> listofStudentIds = allStudents.stream().map(Student::getId).collect(Collectors.toList());

        boolean loop = true;
        while (loop) {
            int studentID = intInput();
            if (listofStudentIds.contains(studentID)) {
                loop = false;

                System.out.println("\nVilken uppdatering vill du göra?");
                System.out.println("[1] PERSONUPPGIFTER");
                System.out.println("[2] UPPDATERA UTBILDNING");
                System.out.println("[3] TA BORT UTBILDNING");
                System.out.println("[4] RADERA");
                System.out.println("[0] TILLBAKA");
                System.out.print("\nVälj mellan [1][2][3][0]");
                int minIntInclusive = 0;
                int maxIntInclusive = 4;
                int choice = tryCatchUserPositiveIntegerInputMenu(minIntInclusive, maxIntInclusive);
                switch (choice) {
                    case 1:
                        updateDataForStudentByID(studentID);
                        break;
                    case 2:
                        updateEducationForStudentByID(studentID);
                        break;
                    case 3:
                        removeEducationForStudent(studentID);
                        break;
                    case 4:
                        deleteStudentByID(studentID);
                        break;
                    case 0:
                        showStudents();
                        break;
                }

            } else if (studentID == 0) {
                loop = false;
                showMenu();
            } else {
                System.out.println("Du har angivet ett ID för STUDENT om inte existerar");
            }
        }

    }

    //////////// UPDATE TEACHER MENUS ////////////////
    static void updateTeacherMenu(int teacherID) {

        System.out.println("\nVilken uppdatering vill du göra?");
        System.out.println("[1] UPPDATERA PERSONUPPGIFTER");
        System.out.println("[2] UPPDATERA KURSER");
        System.out.println("[3] RADERA LÄRARE");
        System.out.println("[0] TILLBAKA");
        System.out.print("\nVälj mellan [1][2][3][0]");
        int minIntInclusive = 0;
        int maxIntInclusive = 3;
        int choice = tryCatchUserPositiveIntegerInputMenu(minIntInclusive, maxIntInclusive);
        switch (choice) {
            case 1:
                updateDataForTeacherByID(teacherID);
                break;
            case 2:
                updateCoursesForTeacherById(teacherID);
                break;
            case 3:
                deleteTeacherByID(teacherID);
                break;
            case 0:
                showTeachers();
                break;
        }
    }

    static void updateCoursesForTeacherById(int teacherID) {

        Teacher teacherToUpdate = controller.getById(teacherID, EntityType.TEACHER);

        System.out.println("\nVad vill göra??");
        System.out.println("[1] Lägga till kurs");
        System.out.println("[2] Ta bort kurs");
        System.out.println("[3] Radera alla kurser");
        System.out.println("[4] Radera lärare");
        System.out.println("[0] TILLBAKA");
        System.out.print("\nVälj mellan [1][2][3][4][0]");
        int minIntInclusive = 0;
        int maxIntInclusive = 4;
        int choice = tryCatchUserPositiveIntegerInputMenu(minIntInclusive, maxIntInclusive);
        switch (choice) {
            case 1:
                //add course
                break;
            case 2:
                // remove course
                break;
            case 3:
                System.out.println("\nBEKRÄFTA ATT DU VILL TA BORT ALLA KURSER FÖR:" + teacherToUpdate.getFirstName() + " " + teacherToUpdate.getLastName());
                System.out.println("\n[99]: BEKRÄFTA");
                System.out.println("[0]: AVBRYT");
                boolean loop = true;
                while (loop) {
                    int confirm = intInput();
                    switch (confirm) {
                        case 99:
                            loop = false;
                            if (controller.updateCoursesForTeacher(teacherID, null) != -1) {
                                System.out.println(teacherToUpdate.getFirstName() + " " + teacherToUpdate.getLastName() + " borttagen!");
                            } else {
                                System.out.println("Fel: Kunde inte radera " + teacherToUpdate.getFirstName() + " " + teacherToUpdate.getLastName());
                            }
                            showStudents();
                            break;
                        case 0:
                            loop = false;
                            updateTeacherMenu(teacherID);
                            break;
                        default:
                            System.out.println("Du har inte gjort ett giltigt val. Försök igen.");
                            break;
                    }
                }
                break;
            case 4:
                deleteTeacherByID(teacherID);
                break;
            case 0:
                showTeachers();
                break;
        }

    }
    
    List list = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "f"));
   
    static void deleteTeacherByID(int teacherID) {

    }
    
    static void updateDataForTeacherByID(int teacherID) {

    }
    
    

}
