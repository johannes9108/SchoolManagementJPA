package domain;

import java.util.List;

import model.EntityType;

public interface ControllerAPI {

	public <T> int add(T object);
//	public <T> boolean addWithCourses(T object,List<Courses> courses);
//	public <T> int update(T object); // RPS: Took away since different objects need to be updated differently due to associations
	public <T> T getById(int id,EntityType type);
	public <T> List<T> getAll(EntityType type);
	public int removeById(int id,EntityType type);
	
	public void refreshLocalData(EntityType type);
	
}
