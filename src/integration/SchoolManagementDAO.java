package integration;

import java.util.List;
import java.util.Set;

import domain.Course;

public interface SchoolManagementDAO<T>{
	
	public int add(T type);
	public int update(T type);
	public T getById(int id);
	public List<T> getAll();
	public int removeById(int id);
	
}
