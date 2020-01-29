/**
 * This file was generated by the Jeddict
 */
package domain;

import java.time.LocalDate;
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
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic
    private String firstName;

    @Basic
    private String lastName;

    @Basic
    private LocalDate birthDate;

    @Basic
    private String email;

    @ManyToMany(mappedBy = "teachers",cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH},fetch = FetchType.EAGER)
    private List<Course> courses;

    public Teacher() {
        courses = new ArrayList<>();
    }
    

    public Teacher(String firstName, String lastName, LocalDate birthDate, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.email = email;
        courses = new ArrayList<>();
    }

    
    
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        courses.add(course);
        course.getTeachers().add(this);
        
    }

    public void removeCourse(Course course) {
        courses.remove(course);
        course.removeTeacher(this);

    }
    @PreUpdate
    public void test() {
    	System.out.println("Uppdatering i " + firstName + ":" + lastName);
    	for (Course course : courses) {
			System.out.println(course);
		}
    	System.out.println("Klar med Uppdatering!");
    }
    
    @PreRemove
    public void clearBindingsFromTeacher() {
    	System.out.println("C size: " + courses.size());
    	
    	for(Course course: courses) {
    		System.out.println("Plockas bort: " + course.getName());
    		System.out.println(course.getTeachers().remove(this));
    	}
    	
    	courses.clear();

    }
    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Teacher{" + "id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", birthDate=" + birthDate + ", email=" + email + ", courses=");
        courses.forEach(t->sb.append(t.getName()+", "));
        sb.delete(sb.length()-2, sb.length());
        sb.append('}');
        return sb.toString();
    }

    

}