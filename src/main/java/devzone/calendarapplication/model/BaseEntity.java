package devzone.calendarapplication.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import devzone.calendarapplication.database.MongoDB;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.PrePersist;

//Abstract class is the data service layer that persists data to the DB along with auditing details
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private ObjectId id;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;
	private String createdBy;
	
	public String getCreatedBy()
	{
		return createdBy;
	}
	
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public ObjectId getId() {
		return id;
	}

	public BaseEntity() {
		super();
	}

	public <E extends BaseEntity> ObjectId persist(E entity) {
		MongoDB.getDS().save(entity);
		return entity.getId();
	}

	public <E extends BaseEntity> ObjectId delete(E entity) {
		MongoDB.getDS().delete(entity);
		return entity.getId();
	}
	
	@PrePersist
	private void prepersist() {
		if (createdDate == null)
			createdDate = LocalDateTime.now();
		modifiedDate = LocalDateTime.now();
		createdBy = "";
	}

	public abstract String toString();
}
