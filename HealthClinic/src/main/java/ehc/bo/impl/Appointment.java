package ehc.bo.impl;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "appointment")
public class Appointment extends BaseObject {
	Date from;
	Date to;
	AppointmentState state;
	Individual individual;
	TreatmentType treatmentType;
	Physician physician;
	Nurse nurse;
	Room room;

	protected Appointment() {
		super();
	}

	public Appointment(User executor, Date from, Date to, TreatmentType treatmentType, Physician physician, Nurse nurse,
			Room room, Individual individual) {
		super(executor);
		this.from = from;
		this.to = to;
		assignTreatmentType(treatmentType);
		assignPhysician(physician);
		assignNurse(nurse);
		assignRoom(room);
		this.individual = individual;
		this.state = AppointmentState.PROPOSED;
	}

	@Enumerated(EnumType.STRING)
	public AppointmentState getState() {
		return state;
	}

	public void setState(AppointmentState state) {
		this.state = state;
	}

	@Column(name = "\"from\"")
	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	@Column(name = "\"to\"")
	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	@ManyToOne
	@JoinColumn(name = "physician_id")
	public Physician getPhysician() {
		return physician;
	}

	public void setPhysician(Physician physician) {
		this.physician = physician;
	}

	@ManyToOne
	@JoinColumn(name = "nurse_id")
	public Nurse getNurse() {
		return nurse;
	}

	public void setNurse(Nurse nurse) {
		this.nurse = nurse;
	}

	@ManyToOne
	@JoinColumn(name = "room_id")
	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "individual_id", nullable = false)
	public Individual getIndividual() {
		return individual;
	}

	public void setIndividual(Individual person) {
		this.individual = person;
	}

	@ManyToOne
	@JoinColumn(name = "treatment_id")
	public TreatmentType getTreatmentType() {
		return treatmentType;
	}

	public void setTreatmentType(TreatmentType treatmentType) {
		this.treatmentType = treatmentType;
	}

	public void assignPerson(Individual person) {
		if (person != null) {
			setIndividual(person);
			person.addAppointment(this);
		}
	}
	
	public void assignPhysician(Physician physician) {
		if (physician == null) {
			return;
		}	
		setPhysician(physician);
		physician.addAppointment(this);
	}
	
	public void assignNurse(Nurse nurse) {
		if (nurse == null) {
			return;
		}	
		setNurse(nurse);
		nurse.addAppointment(this);
	}
	
	public void assignRoom(Room room) {
		if (room == null) {
			return;
		}	
		setRoom(room);
		room.addAppointment(this);
	}

	public void assignTreatmentType(TreatmentType treatmentType) {
		if (treatmentType != null) {
			setTreatmentType(treatmentType);
			treatmentType.addAppointment(this);
		}

	}
}
