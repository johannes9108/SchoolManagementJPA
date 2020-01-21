/**
 * This file was generated by the Jeddict, very good
 */
package domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 * @author rober
 */
@Entity
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic
    private String name;

    @Basic
    private String faculty;

    @Basic
    private LocalDate startDate;

    @Basic
    private LocalDate finalDate;

    @OneToMany(mappedBy = "education", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<Student> students;

    @ManyToMany
    private List<Course> courses;

    public Education(String name, String faculty, LocalDate startDate, LocalDate finalDate) {
        this.name = name;
        this.faculty = faculty;
        this.startDate = startDate;
        this.finalDate = finalDate;
        this.students = new ArrayList<>();
        courses = new ArrayList<>();

    }

    public Education() {
        this.students = new ArrayList<>();
        courses = new ArrayList<>();

    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFaculty() {
        return this.faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getFinalDate() {
        return this.finalDate;
    }

    public void setFinalDate(LocalDate finalDate) {
        this.finalDate = finalDate;
    }

//    public List<Student> getStudents() {
//        if (students == null) {
//            students = new ArrayList<>();
//        }
//        return this.students;
//    }
    
       public List<Student> getStudents() {
        if (students == null) {
            students = new ArrayList<>();
        }
        return students;//Collections.unmodifiableList(students);
    }
    
    public void internalRemoveStudent(Student student){this.students.remove(student);}
    public void internalAddStudent(Student student){this.students.add(student);}
    

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void addStudent(Student student) {
        student.setEducation(this);
//        students.add(student);
    }

    public void removeStudent(Student student) {
//        getStudents().remove(student);
        student.setEducation(null);
    }

    public List<Course> getCourses() {
        if (courses == null) {
            courses = new ArrayList<>();
        }
        return this.courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public void addCourse(Course course) {
        getCourses().add(course);
        course.getEducations().add(this);
    }

    public void removeCourse(Course course) {
        getCourses().remove(course);
        course.getEducations().remove(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Education{" + "id=" + id + ", name=" + name + ", faculty=" + faculty + ", startDate=" + startDate + ", finalDate=" + finalDate + ", Courses=");
        courses.forEach(t -> sb.append(t.getName() + ", "));
        sb.append("\nStudents=");
        students.forEach(t -> sb.append(t.getFirstName() + " " + t.getLastName() + ", "));
        sb.delete(sb.length() - 2, sb.length());
        sb.append('}');
        return sb.toString();
    }
}
