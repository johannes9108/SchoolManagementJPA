package javafxControllers;

import integration.EntityType;

public interface SubControllerAPI {

	public default EntityType convertIndexToEnum(int index) {
	
			switch(index) {
			case 0:
				return EntityType.TEACHER;
			case 1:
				return EntityType.COURSE;
			case 2:
				return EntityType.EDUCATION;
			case 3:
				return EntityType.STUDENT;
			
			}
			
			return null;
		}
		
}
