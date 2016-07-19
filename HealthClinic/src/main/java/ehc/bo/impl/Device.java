package ehc.bo.impl;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Device extends ResourceImpl {
    String name;
    DeviceType type;
      
	protected Device() {
		super();
	}
	
	public Device(User executor, DeviceType deviceType, String name) {
		super(executor);
		this.name = name;
		this.type = deviceType;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "device_type_id")
	public DeviceType getType() {
		return type;
	}

	public void setType(DeviceType type) {
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
		if (!(resourceType instanceof DeviceType)) {
			return false;		
		}
		DeviceType deviceType = (DeviceType)resourceType;		
		return getType().containsTreatmentType(deviceType.getTreatmentTypes().get(0));
	}

}
