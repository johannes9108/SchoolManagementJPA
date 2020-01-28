package model;

import java.util.List;
import java.util.Set;

import domain.Course;

public interface SchoolManagementDAO<T>{
	
	public int add(T type);
//	public int update(T type); //removed since different entities have differens acossiations
	public T getById(int id);
	public List<T> getAll();
	public int removeById(int id);
	
}
