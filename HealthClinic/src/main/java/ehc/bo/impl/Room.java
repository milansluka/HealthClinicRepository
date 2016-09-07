package ehc.bo.impl;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Room extends ResourceImpl {
    String name;
    RoomType type;
      
	protected Room() {
		super();
	}
	
	public Room(User executor, RoomType roomType, String name) {
		super(executor);
		this.name = name;
		this.type = roomType;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "roomtype")
	public RoomType getType() {
		return type;
	}

	public void setType(RoomType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean isSuitable(ResourceType resourceType) {
		if (!(resourceType instanceof RoomType)) {
			return false;		
		}
		RoomType roomType = (RoomType)resourceType;		
		return getType().containsTreatmentType(roomType.getTreatmentType());
	} 

    
}
