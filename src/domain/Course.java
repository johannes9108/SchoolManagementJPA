/**
  * This file was generated by the Jeddict
 */
package domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

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

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST,CascadeType.REFRESH},fetch = FetchType.EAGER)
    private List<Teacher> teachers;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST,CascadeType.REFRESH})
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
        teachers.add(teacher);
        teacher.getCourses().add(this);
    }

    public void removeTeacher(Teacher teacher) {
        teachers.remove(teacher);
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
        education.getCourses().add(this);
    }

    public void removeEducation(Education education) {
        getEducations().remove(education);
        education.getCourses().remove(this);
    }

    @PreUpdate
    public void test() {
    	
    	System.out.println("Uppdatering i " + name);
    	for (Teacher teacher : teachers) {
			System.out.println(teacher);
		}
    	System.out.println("Klar med Uppdatering!");
    }
    @PreRemove
    public void clearBindingsFromCourse() {
    	System.out.println("T size: " + teachers.size());
    	System.out.println("E size: " + educations.size());
    	clearTeacherBindingsFromCourse();
    	clearEducationBindingsFromCourse();
    	System.out.println();
    }

	public List<Education> clearEducationBindingsFromCourse() {
		System.out.println("clearEducationBindingsFromCourse()");
		for (Education education : educations) {
			education.getCourses().remove(this);
		}
		return educations;
	}

	public List<Teacher> clearTeacherBindingsFromCourse() {
		System.out.println("clearTeacherBindingsFromCourse()");
		for (Teacher teacher: teachers) {
    		teacher.getCourses().remove(this);
    	}
		return teachers;
	}
    
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Course{" + "id=" + id + ", name=" + name + ", subject=" + subject + ", difficulty=" + difficulty + ", points=" + points + ", Teachers=");
        teachers.forEach(t->sb.append(t.getFirstName()+" " + t.getLastName() + ", "));
        sb.append("\nEducations=");
        educations.forEach(t->sb.append(t.getName()+ ", "));
        sb.delete(sb.length()-2, sb.length());
        sb.append('}');
        return sb.toString();
    }

}