package ci.jsi.entites.instance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ci.jsi.entites.beneficiaire.Ibeneficiaire;
import ci.jsi.entites.programme.Iprogramme;
import ci.jsi.entites.programme.Programme;


@Service
public class InstanceService implements Iinstance {
	@Autowired
	InstanceConvert instanceConvert;
	@Autowired
	InstanceRepository instanceRepository;
	@Autowired
	Iprogramme iprogramme;
	@Autowired
	Ibeneficiaire ibeneficiaire;
	
	Instance instance = null;
	InstanceTDO instanceTDO = null;

	@Override
	public List<InstanceTDO> getAllInstanceTDO() {
		
		return instanceConvert.getInstanceTDOs(instanceRepository.getAllInstance());
	}

	@Override
	public InstanceTDO getOneInstanceTDO(String id) {
		instanceTDO = null;
		instance = instanceRepository.findAllByDeletedIsFalseAndUid(id);
		if(instance != null) {
			instanceTDO = instanceConvert.getInstanceTDO(instance);
		}

		return instanceTDO;
	}

	@Override
	public String saveInstanceTDO(InstanceTDO instanceTDO) {
		instance = instanceConvert.saveInstanceTDO(instanceTDO);
		if(instance != null) {
			instance = instanceRepository.save(instance);
			return instance.getUid();
		}
		
		return "fail";
	}
	@Override
	public Instance saveInstance(InstanceTDO instanceTDO) {
		instance = instanceConvert.saveInstanceTDO(instanceTDO);
		if(instance != null) {
			instance = instanceRepository.save(instance);
		}
		
		return instance;
	}

	@Override
	public String deleteInstanceTDO(String id) {
		/*Programme dossierProg = iprogramme.getOneProgrammeByCode("dossierBeneficiare");
		instance = instanceRepository.findAllByDeletedIsFalseAndUid(id);
		if(instance != null) {
			if(dossierProg == instance.getProgramme()) {
				instanceConvert.deleteBeneficiaireAllInstance(instance.getInstanceBeneficiaires());
			}else {
				instanceConvert.deleteInBeneficiaire(instance.getInstanceBeneficiaires(),instance);
				deleteInstance(instance);
			}
			return "Success Deleted";
		}
		return "fail";*/
		
		instance = instanceRepository.findAllByDeletedIsFalseAndUid(id);
		if(instance != null) {
			instanceConvert.deleteInBeneficiaire(instance.getInstanceBeneficiaires(),instance);
			deleteInstance(instance);
			return "Success Deleted";
		}
		return "fail";
	}

	@Override
	public Instance getOneInstance(String id) {
		/*instance = instanceRepository.getOneInstance(id);
		if(instance != null) {
			return instance;
		}*/
	
		return instanceRepository.findAllByDeletedIsFalseAndUid(id);
	}
	
	public void deleteInstance(Instance instance) {
		instance.setDeleted(true);
		instance = instanceRepository.save(instance);
	}
	
	@Override
	public Page<Instance> getInstanceselectByProgrammeAndOrganisation(String programmeuid, String organisationuid,Pageable pageable) {
		System.out.println("Entrer dans InstanceService - getInstanceselectByProgrammeAndOrganisation");
		return instanceRepository.findAllByDeletedIsFalseAndProgrammeUidAndOrganisationUid(programmeuid, organisationuid,pageable);
	}
	@Override
	public Instance saveInstance(Instance instance) {
		instance = instanceConvert.CheckInstance(instance);
		if(instance == null) {
			return null;
		}
		instance = instanceRepository.save(instance);
		return instance;
	}

	@Override
	public void deleteCompleteInstance(Instance instance) {
		instance = instanceRepository.findOne(instance.getInstanceid());
		if(instance != null) {
			instance = instanceConvert.deleteInstance(instance);
			//instance = instanceRepository.save(instance);
			instanceRepository.delete(instance);
		}
	}

	@Override
	public List<Instance> getInstanceAnalysePeriode(List<String> organisation, String programme, Date debut, Date fin) {
		return instanceRepository.findAllByDeletedIsFalseAndOrganisationUidInAndProgrammeUidAndDateActiviteGreaterThanEqualAndDateActiviteLessThanEqual(organisation, programme, debut, fin);
	}

	@Override
	public List<Instance> getInstanceAnalysePreview(List<String> organisation, String programme, Date dateFin) {
		return instanceRepository.findAllByDeletedIsFalseAndOrganisationUidInAndProgrammeUidAndDateActiviteLessThan(organisation, programme, dateFin);

	}

	@Override
	public List<Instance> getAllInstanceAnalyse(List<String> organisation, String programme) {
		return instanceRepository.findAllByDeletedIsFalseAndOrganisationUidInAndProgrammeUid(organisation, programme);
	}

	@Override
	public Integer erasedInstanceDeleting() {
		
		List<Instance> instances = new ArrayList<Instance>();
		instances = instanceRepository.findAllByDeletedIsTrue();
		
		for(int i = 0; i < instances.size(); i++) {
			while(!instances.get(i).getInstanceBeneficiaires().isEmpty()) {
				//if(instances.get(i).getInstanceBeneficiaires().get(0).getBeneficiaire().getUid() != null) {
					ibeneficiaire.deleteBeneficiaireInstance(instances.get(i).getInstanceBeneficiaires().get(0).getBeneficiaire(), instances.get(i));
				//}
			}
			deleteCompleteInstance(instances.get(i));
		}
		return instances.size();
		
		
		
	}

	

	
	
}
