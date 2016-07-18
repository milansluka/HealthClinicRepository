package ehc.bo.impl;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Room extends ResourceImpl {
	/*List<Device> devices;*/
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
	@JoinColumn(name = "room_type_id")
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
		// TODO Auto-generated method stub
		return true;
	} 
	
	
    
}
