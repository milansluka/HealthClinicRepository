package ehc.bo;

import ehc.bo.impl.Individual;
import ehc.bo.impl.TreatmentType;

public interface TreatmentDao {
	TreatmentType findTreatment(TreatmentType treatment);
}
