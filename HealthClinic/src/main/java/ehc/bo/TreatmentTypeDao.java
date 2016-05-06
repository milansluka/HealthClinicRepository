package ehc.bo;

import ehc.bo.impl.TreatmentType;

public interface TreatmentTypeDao {
	TreatmentType find(TreatmentType treatmentType);
	TreatmentType get(long id);
	void add(TreatmentType treatmentType);
	

}
