package ehc.bo.impl;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "room_type")
@PrimaryKeyJoinColumn(name="id") 
public class RoomType extends ResourceType {
	
	protected RoomType() {
		super();
	}
	
	public RoomType(User executor) {
		super(executor);
	}
	

}
