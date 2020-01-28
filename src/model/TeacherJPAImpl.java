package model;

import domain.Course;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import domain.Teacher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class TeacherJPAImpl implements SchoolManagementDAO<Teacher>, TeacherDAO {

    private EntityManagerFactory emf;
    private EntityManager em;

    public TeacherJPAImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public int add(Teacher teacher) {
        em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(teacher);
            tx.commit();
            return teacher.getId();
        } catch (PersistenceException exception) {
            System.out.println("Couldn't Persist the object" + teacher);
            return -1;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public int updateTeacherPersonalData(Teacher teacher) {
        em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Teacher teacherUpdate = em.find(Teacher.class, teacher.getId());
            teacherUpdate.setFirstName(teacher.getFirstName());
            teacherUpdate.setLastName(teacher.getLastName());
            teacherUpdate.setEmail(teacher.getEmail());
            teacherUpdate.setBirthDate(teacher.getBirthDate());
//            System.out.println(teacherUpdate);
            tx.commit();
            return teacher.getId();
        } catch (PersistenceException exception) {
            System.out.println("Couldn't update the object" + teacher);
            return -1;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public Teacher getById(int id) {
        em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            Teacher newTeacher = em.find(Teacher.class, id);
//            System.out.println(newTeacher);
            em.getTransaction().commit();
            return newTeacher;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<Teacher> getAll() {
        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createQuery("FROM Teacher t");
            System.out.println(query.toString());
            List<Teacher> list = (List<Teacher>) query.getResultStream().collect(Collectors.toList());
            em.getTransaction().commit();
            return list;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public int removeById(int id
    ) {
        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Teacher teacherToRemove = em.find(Teacher.class, id);
            em.remove(teacherToRemove);
            em.getTransaction().commit();
            return id;
        } catch (Exception e) {
            System.out.println(e);
            return -1;
        } finally {
            if (em != null) {
                em.close();
            }
        }

    }

    @Override
    public int updateCoursesForTeacher(Integer teacherId, List<Integer> updatedListofCourseIds) {
        em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Teacher teacherToUpdate = em.find(Teacher.class, teacherId);

            List<Integer> oldCoursesIds = new ArrayList<>();
            
            if (teacherToUpdate.getCourses() != null) {
                em.find(Teacher.class, teacherId).getCourses().forEach((course) -> { //kolla om det spelar roll om listan är tom
                    oldCoursesIds.add(course.getId());
                });
            }

            if ((updatedListofCourseIds == null || updatedListofCourseIds.isEmpty()) && oldCoursesIds.isEmpty()) {
                //INGET GÖRS: updatedListofCourseIds var null/empty OCH gamla listan var null/tom.
            } else {
                Collections.sort(oldCoursesIds);
                Collections.sort(updatedListofCourseIds);
                if (oldCoursesIds.equals(updatedListofCourseIds)) {
                    //INGET GÖRS: updatedListofCourseIds OCH gamla listan var LIKA.
                } else {
                    if (updatedListofCourseIds != null && updatedListofCourseIds.isEmpty() == false) {
                        //Kopierar listan med id till lista med id att lägga till (kommer dock (längre ned) jämföra skillnader med tidigare kurslista innan åtgärd!)
                        List<Integer> courseIdsToAddToTeacher = updatedListofCourseIds.stream()
                                .collect(Collectors.toList());
                        if (!oldCoursesIds.isEmpty()) {
                            courseIdsToAddToTeacher.removeAll(oldCoursesIds); //eftersom tidigare kurslistan inte var tom ska vi ta bort de som redan finns från listan att lägag till
                            //Kopierar listan med id från befintliga kurser till lista med id att ta bort (kommer dock (längre ned) jämföra skillnader med nya kurslista innan åtgärd!)
                            List<Integer> courseIdsToRemoveFromTeacher = oldCoursesIds.stream()
                                    .collect(Collectors.toList());
                            courseIdsToRemoveFromTeacher.removeAll(updatedListofCourseIds);//eftersom tidigare kurslistan inte var tom ska vi ta bort de nya från listan att ta bort

                            if (courseIdsToRemoveFromTeacher.isEmpty()) {
                                //LÄGGER BARA TILL:
                                List<Course> coursesToUpdateAndMerge = new ArrayList<>();
                                courseIdsToAddToTeacher.forEach(courseId -> coursesToUpdateAndMerge.add(em.find(Course.class, courseId)));

                                for (Course course : coursesToUpdateAndMerge) {
                                    course.addTeacher(teacherToUpdate);
                                    em.merge(course);
                                }
                            } else {
                                //LÄGG BÅDE TILL OCH TA BORT eftersom vi vet att båda inte är empty:
                                List<Course> coursesToAddToTeacherAndMerge = new ArrayList<>();
                                courseIdsToAddToTeacher.forEach(courseId -> coursesToAddToTeacherAndMerge.add(em.find(Course.class, courseId)));
                                for (Course course : coursesToAddToTeacherAndMerge) {
                                    course.addTeacher(teacherToUpdate);
                                    em.merge(course); // Kanske ska ta bort? Blir dublett?
                                }

                                List<Course> coursestoRemoveAndClearAssociationFromTeacher = new ArrayList<>();
                                courseIdsToRemoveFromTeacher.forEach(courseId -> coursestoRemoveAndClearAssociationFromTeacher.add(em.find(Course.class, courseId)));
                                for (Course course : coursestoRemoveAndClearAssociationFromTeacher) {
                                    course.removeTeacher(teacherToUpdate);
                                    em.merge(course);
                                }
                                em.merge(teacherToUpdate);

                            };
                        } else { //LÄGGER BARA TILL KURESER eftersom inga tdigare fanns och nya listan inte vara tom
                            List<Course> coursesToAddToTeacherAndMerge = new ArrayList<>();
                            courseIdsToAddToTeacher.forEach(courseId -> coursesToAddToTeacherAndMerge.add(em.find(Course.class, courseId)));
                            for (Course course : coursesToAddToTeacherAndMerge) {
                                course.addTeacher(teacherToUpdate);
                                em.merge(course);
                            }
                        } // else = uppdaterade listan innehåller värden OCH gamla listan var inte tom eller null

                    } else if (!oldCoursesIds.isEmpty()) { //Här ska bara kurser tas bort eftersom uppdaterade listan var null eller tom
                        List<Course> coursestoRemoveAndClearAssociationFromTeacher = new ArrayList<>();
                        oldCoursesIds.forEach(courseId -> coursestoRemoveAndClearAssociationFromTeacher.add(em.find(Course.class, courseId)));
                        for (Course course : coursestoRemoveAndClearAssociationFromTeacher) {
                            course.removeTeacher(teacherToUpdate);
                            em.merge(course);
                        }
                        em.merge(teacherToUpdate);
                    }
                }
            }
            tx.commit();
            return teacherToUpdate.getId();
        } catch (Exception e) {
            System.out.println("Couldn't update courses for teacher-id: " + teacherId + e);
            return -1;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

}
