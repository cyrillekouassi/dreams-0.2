package ci.jsi.entites.beneficiaire;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InstanceBeneficiaireRepository extends JpaRepository<InstanceBeneficiaire, Long>{

	//public List<Beneficiaire> findByUidIsNotNullAndOrganisationUidAndDreamsIdContaining(String organisation,String dreamsId);
	
	public List<InstanceBeneficiaire> findByBeneficiaireUidAndInstanceUid(String beneficiaireUid,String instanceUid);
	
}
