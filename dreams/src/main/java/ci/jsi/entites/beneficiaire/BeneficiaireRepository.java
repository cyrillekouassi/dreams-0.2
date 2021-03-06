package ci.jsi.entites.beneficiaire;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BeneficiaireRepository extends JpaRepository<Beneficiaire, Long> {
	
	
	public Beneficiaire findByUidIsNotNullAndDreamsId(String idDreams);
	
	public List<Beneficiaire> findByUidIsNotNullAndDreamsIdIn(List<String> idDreams);
	
	public Beneficiaire findByUid(String uid);
	
	public List<Beneficiaire> findByUidIsNull();
	
	public List<Beneficiaire> findByUidIsNotNullAndInstanceBeneficiairesInstanceUid(String instanceUid);
	
	public List<Beneficiaire> findByUidIsNotNullAndOrganisationUidAndDreamsIdContaining(String organisation,String dreamsId);
	
	public List<Beneficiaire> findByUidIsNotNullAndOrganisationUidInAndDateEnrolementGreaterThanEqualAndDateEnrolementLessThanEqual(List<String> organisation,Date debut, Date fin);
	
	public List<Beneficiaire> findByUidIsNotNullAndOrganisationUidInAndDateEnrolementLessThanEqual(List<String> organisation, Date fin);
}
