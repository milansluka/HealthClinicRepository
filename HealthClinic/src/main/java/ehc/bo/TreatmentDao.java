package ehc.bo;

import ehc.bo.impl.Individual;
import ehc.bo.impl.Treatment;

public interface TreatmentDao {
	Treatment findTreatment(Treatment treatment);
}
