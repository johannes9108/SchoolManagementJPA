/**
  * This file was generated by the Jeddict
 */
package domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 * @author rober
 */
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic
    private String name;

    @Basic
    private String subject;

    @Basic
    private String difficulty;

    @Basic
    private int points;

    @ManyToMany
    private List<Teacher> teachers;

    @ManyToMany(mappedBy = "courses")
    private List<Education> educations;

    public Course(String name, String subject, String difficulty, int points) {
        this.name = name;
        this.subject = subject;
        this.difficulty = difficulty;
        this.points = points;
    }

    public Course() {
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

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getPoints() {
        return this.points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public List<Teacher> getTeachers() {
        if (teachers == null) {
            teachers = new ArrayList<>();
        }
        return this.teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public void addTeacher(Teacher teacher) {
        getTeachers().add(teacher);
        teacher.getCourses().add(this);
    }

    public void removeTeacher(Teacher teacher) {
        getTeachers().remove(teacher);
        teacher.getCourses().remove(this);
    }

    public List<Education> getEducations() {
        if (educations == null) {
            educations = new ArrayList<>();
        }
        return this.educations;
    }

    public void setEducations(List<Education> educations) {
        this.educations = educations;
    }

    public void addEducation(Education education) {
        getEducations().add(education);
    }

    public void removeEducation(Education education) {
        getEducations().remove(education);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Course{" + "id=" + id + ", name=" + name + ", subject=" + subject + ", difficulty=" + difficulty + ", points=" + points + ", teachers=");
        teachers.forEach(t->sb.append(t.getFirstName()+" " + t.getLastName() + ", "));
        sb.delete(sb.length()-2, sb.length());
        sb.append('}');
        return sb.toString();
    }

}