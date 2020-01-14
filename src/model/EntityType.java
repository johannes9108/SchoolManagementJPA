package model;

public enum EntityType {
	TEACHER(0), COURSE(1), EDUCATION(2), STUDENT(3);

	private final int entityType;

	private EntityType(int entityType) {
		this.entityType = entityType;
	}
	
	public int getEntityType() {
		return this.entityType;
	}
}
