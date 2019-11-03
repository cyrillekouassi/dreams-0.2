package ci.jsi.entites.beneficiaire;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ci.jsi.entites.instance.Iinstance;
import ci.jsi.entites.instance.Instance;
import ci.jsi.initialisation.ConvertDate;
import ci.jsi.initialisation.ResultatRequete;

@Service
@Transactional
public class BeneficiaireService implements Ibeneficiaire {
	
	@Autowired
	BeneficiaireRepository beneficiaireRepository;
	@Autowired
	BeneficiaireConvert beneficiaireConvert;
	@Autowired
	ConvertDate convertDate;
	@Autowired
	Iinstance iinstance;
	
	
	


	@Override
	public List<BeneficiaireTDO> SearchBeneficiaireTDOByIdDreams(String idDreams,String organisation) {
		List<BeneficiaireTDO> beneficiaireTDOs = new ArrayList<BeneficiaireTDO>();
		List<Beneficiaire> beneficiaires = new ArrayList<Beneficiaire>();
		beneficiaires = beneficiaireRepository.findByUidIsNotNullAndOrganisationUidAndDreamsIdContaining(organisation, idDreams);
		if(!beneficiaires.isEmpty()) {
			beneficiaireTDOs = beneficiaireConvert.getBeneficiaireTDOs(beneficiaires);
		}
		return beneficiaireTDOs;
	}

	@Override
	public Beneficiaire getOneBeneficiaireByIdDreams(String idDreams) {
		
		return beneficiaireRepository.findByUidIsNotNullAndDreamsId(idDreams);
	}

	@Override
	public Beneficiaire updateOneBeneficiaire(Beneficiaire beneficiaire) {
		beneficiaire.setDateUpdate(new Date());
		try {
			return beneficiaireRepository.save(beneficiaire);
		} catch (Exception e) {
			return null;
		}
		
	}

	@Override
	public Beneficiaire saveOneBeneficiaire(Beneficiaire beneficiaire) {
		Beneficiaire beneficiaire2 = beneficiaireRepository.findByUidIsNotNullAndDreamsId(beneficiaire.getId_dreams());
		if(beneficiaire2 != null) {
			return null;
		}
		beneficiaire = beneficiaireRepository.save(beneficiaire);
		return beneficiaire;
	}

	@Override
	public Beneficiaire convertBeneficiaireTDO(BeneficiaireTDO beneficiaireTDO) {
		
		return beneficiaireConvert.saveBeneficiaire(beneficiaireTDO);
	}

	@Override
	public Beneficiaire getOneBeneficiaireByUid(String uid) {
		
		return beneficiaireRepository.findByUid(uid);
	}

	@Override
	public ResultatRequete saveBeneficiaireTDO(BeneficiaireTDO beneficiaireTDO) {
		ResultatRequete resultatRequete = new ResultatRequete();
		Beneficiaire beneficiaire = null;
		if(beneficiaireTDO.getId() != null) {
			beneficiaire = beneficiaireRepository.findByUid(beneficiaireTDO.getId());
			beneficiaire = beneficiaireConvert.UpdateBeneficiaireByTDO(beneficiaire,beneficiaireTDO);
		}else {
			beneficiaire = convertBeneficiaireTDO(beneficiaireTDO);
		}
		
		if(beneficiaire != null) {
			beneficiaire = beneficiaireRepository.save(beneficiaire);
		}
		if(beneficiaire != null) {
			resultatRequete.setStatus("OK");
			resultatRequete.setId(beneficiaire.getUid());
			resultatRequete.setImporte(1);
		}else {
			resultatRequete.setStatus("fail");
			resultatRequete.setIgnore(1);
		}
		return resultatRequete;
	}

	@Override
	public List<BeneficiaireTDO> getBeneficiairePeriode(List<String> organisation, String debut, String fin) {
		List<BeneficiaireTDO> beneficiaireTDOs = new ArrayList<BeneficiaireTDO>();
		List<Beneficiaire> beneficiaires = new ArrayList<Beneficiaire>();
		Date dateDebut = convertDate.getDateParse(debut);
		Date dateFin = convertDate.getDateParse(fin);
		beneficiaires = beneficiaireRepository.findByUidIsNotNullAndOrganisationUidInAndDateEnrolementGreaterThanEqualAndDateEnrolementLessThanEqual(organisation, dateDebut,dateFin);
		if(!beneficiaires.isEmpty()) {
			beneficiaireTDOs = beneficiaireConvert.getBeneficiaireTDOs(beneficiaires);
		}
		return beneficiaireTDOs;
	}

	@Override
	public List<BeneficiaireTDO> getBeneficiairePreview(List<String> organisation, String fin) {
		List<BeneficiaireTDO> beneficiaireTDOs = new ArrayList<BeneficiaireTDO>();
		List<Beneficiaire> beneficiaires = new ArrayList<Beneficiaire>();
		Date dateFin = convertDate.getDateParse(fin);
		beneficiaires = beneficiaireRepository.findByUidIsNotNullAndOrganisationUidInAndDateEnrolementLessThanEqual(organisation,dateFin);
		if(!beneficiaires.isEmpty()) {
			beneficiaireTDOs = beneficiaireConvert.getBeneficiaireTDOs(beneficiaires);
		}
		return beneficiaireTDOs;
	}

	@Override
	public List<StatusBeneficiaire> getStatusBeneficiaire(List<String> organisation, String debut, String fin) {
		List<StatusBeneficiaire> StatusBeneficiaire = new ArrayList<StatusBeneficiaire>();
		List<Beneficiaire> beneficiaires = new ArrayList<Beneficiaire>();
		Date dateDebut = convertDate.getDateParse(debut);
		Date dateFin = convertDate.getDateParse(fin);
		beneficiaires = beneficiaireRepository.findByUidIsNotNullAndOrganisationUidInAndDateEnrolementGreaterThanEqualAndDateEnrolementLessThanEqual(organisation, dateDebut,dateFin);
		if(!beneficiaires.isEmpty()) {
			StatusBeneficiaire = beneficiaireConvert.getStatusBeneficiaires(beneficiaires);
		}
		return StatusBeneficiaire;
	}

	@Override
	public Beneficiaire deleteBeneficiaireInstance(Beneficiaire beneficiaire, Instance instance) {
		
		List<InstanceBeneficiaire> beneficiairesInstance = new ArrayList<InstanceBeneficiaire>();
		List<InstanceBeneficiaire> instancesBeneficiaire = new ArrayList<InstanceBeneficiaire>();
		List<InstanceBeneficiaire> deleteinstanceBeneficiaires = new ArrayList<InstanceBeneficiaire>();
		beneficiairesInstance = beneficiaire.getInstanceBeneficiaires();
		instancesBeneficiaire = instance.getInstanceBeneficiaires();
				
		int i = 0;
		while(i < beneficiairesInstance.size()) {
			//if(beneficiairesInstance.get(i).getInstance().getUid().equals(instance.getUid())) {
			if(beneficiairesInstance.get(i).getInstance().getInstanceid() == instance.getInstanceid()) {
				deleteinstanceBeneficiaires.add(beneficiairesInstance.get(i));
				beneficiairesInstance.remove(i);
			}
			i++;
		}
		
		int b = 0;
		while(b < instancesBeneficiaire.size()) {
			//if(instancesBeneficiaire.get(b).getBeneficiaire().getUid().equals(beneficiaire.getUid())) {
			if(instancesBeneficiaire.get(b).getBeneficiaire().getBeneficiaireid() == beneficiaire.getBeneficiaireid()) {
				instancesBeneficiaire.remove(b);
			}
			b++;
		}
		
		beneficiaireConvert.deleteBeneficiaireInstance(deleteinstanceBeneficiaires);
		beneficiaire.setInstanceBeneficiaires(beneficiairesInstance);
		instance.setInstanceBeneficiaires(instancesBeneficiaire);
		//instance = iinstance.saveInstance(instance);
		beneficiaire = updateOneBeneficiaire(beneficiaire);
		System.out.println("deleteBeneficiaireInstance");
		return beneficiaire;
	}

	@Override
	public ResultatRequete deleteBeneficiaire(String beneficiaireUid) {
		ResultatRequete resultatRequete = new ResultatRequete();
		List<Instance>  instances =  new ArrayList<Instance>();
		
		Beneficiaire beneficiaire = beneficiaireRepository.findByUid(beneficiaireUid);
		//deleteBeneficiaireInstance(Beneficiaire beneficiaire, Instance instance)
		if(beneficiaire != null) {
			int i = 0;
			while(i < beneficiaire.getInstanceBeneficiaires().size()) {
				instances.add(beneficiaire.getInstanceBeneficiaires().get(i).getInstance());
				beneficiaire = deleteBeneficiaireInstance(beneficiaire, beneficiaire.getInstanceBeneficiaires().get(i).getInstance());
			}
			int b = 0;
			while(b<instances.size()) {
				Instance instance = iinstance.getOneInstance(instances.get(b).getUid());
				if(instance != null) {
					if(instance.getInstanceBeneficiaires().isEmpty()) {
						iinstance.deleteCompleteInstance(instance);
					}
				}/*else {
					iinstance.deleteCompleteInstance(instance);
				}*/
				b++;
			}
			
			beneficiaire.setOrganisation(null);
			beneficiaireRepository.delete(beneficiaire);
			resultatRequete.setStatus("OK");
			resultatRequete.setId(beneficiaire.getUid());
			resultatRequete.setDelete(1);
			return resultatRequete;
		}
		resultatRequete.setStatus("fail");
		resultatRequete.setIgnore(1);
		return resultatRequete;
	}

	@Override
	public List<BeneficiaireOEV> getBeneficiaireOEV(List<String> organisation, String debut, String fin) {
		List<BeneficiaireOEV> beneficiaireOEV = new ArrayList<BeneficiaireOEV>();
		List<Beneficiaire> beneficiaires = new ArrayList<Beneficiaire>();
		Date dateDebut = convertDate.getDateParse(debut);
		Date dateFin = convertDate.getDateParse(fin);
		beneficiaires = beneficiaireRepository.findByUidIsNotNullAndOrganisationUidInAndDateEnrolementGreaterThanEqualAndDateEnrolementLessThanEqual(organisation, dateDebut,dateFin);
		if(!beneficiaires.isEmpty()) {
			beneficiaireOEV = beneficiaireConvert.getBeneficiairesOEV(beneficiaires);
		}
		return beneficiaireOEV;
	}

	@Override
	public BeneficiaireTDO getBeneficiaireTDOByIdDreams(String idDreams) {
		BeneficiaireTDO beneficiaireTDO = null;
		Beneficiaire beneficiaire = beneficiaireRepository.findByUidIsNotNullAndDreamsId(idDreams);
		if(beneficiaire != null) {
			beneficiaireTDO = beneficiaireConvert.getBeneficiaireTDO(beneficiaire);
		}
		
		return beneficiaireTDO;
	}

	@Override
	public List<BeneficiaireTDO> getBeneficiaireTDOByInstance(String instance) {
		List<Beneficiaire> beneficiaires = new ArrayList<Beneficiaire>();
		List<BeneficiaireTDO> beneficiaireTDOs = new ArrayList<BeneficiaireTDO>();
		beneficiaires = beneficiaireRepository.findByUidIsNotNullAndInstanceBeneficiairesInstanceUid(instance);
		if(!beneficiaires.isEmpty()) {
			beneficiaires = beneficiaireConvert.deleteBeneficiaireInstances(beneficiaires,instance);
			//beneficiaires = beneficiaireRepository.save(beneficiaires);
			beneficiaireTDOs = beneficiaireConvert.getBeneficiaireTDOs(beneficiaires);
		}
		
		return beneficiaireTDOs;
	}

	@Override
	public Beneficiaire getOneBeneficiaireByInstance(String instance) {
		List<Beneficiaire> beneficiaires = new ArrayList<Beneficiaire>();
		beneficiaires = beneficiaireRepository.findByUidIsNotNullAndInstanceBeneficiairesInstanceUid(instance);
		if(beneficiaires.size() == 1) {
			return beneficiaires.get(0);
		}
		return null;
	}

	@Override
	public List<BeneficiaireTDO> getListBeneficiaireTDOByIdDreams(List<String> idDreams) {
		List<Beneficiaire> beneficiaires = new ArrayList<Beneficiaire>();
		List<BeneficiaireTDO> beneficiaireTDOs = new ArrayList<BeneficiaireTDO>();
		beneficiaires = beneficiaireRepository.findByUidIsNotNullAndDreamsIdIn(idDreams);
		if(!beneficiaires.isEmpty()) {
			beneficiaireTDOs = beneficiaireConvert.getBeneficiaireTDOs(beneficiaires);
		}
		
		return beneficiaireTDOs;
	}

	@Override
	public ResultatRequete deleteBeneficiaireByDossier(String dossierInstance) {
		ResultatRequete resultatRequete = new ResultatRequete();
		Beneficiaire beneficiaire = getOneBeneficiaireByInstance(dossierInstance);
		resultatRequete = deleteBeneficiaire(beneficiaire.getUid());
		return resultatRequete;
	}

	@Override
	public Integer erasedBeneficiaireDeleting() {
		List<Beneficiaire> beneficiaires = new ArrayList<Beneficiaire>();
		List<Instance>  instances =  new ArrayList<Instance>();
		beneficiaires = beneficiaireRepository.findByUidIsNull();
		int i =0;
		while(i<beneficiaires.size()) {
			instances.clear();
			while(!beneficiaires.get(i).getInstanceBeneficiaires().isEmpty()) {
				instances.add(beneficiaires.get(i).getInstanceBeneficiaires().get(0).getInstance());
				Beneficiaire beneficiaire = deleteBeneficiaireInstance(beneficiaires.get(i), beneficiaires.get(i).getInstanceBeneficiaires().get(0).getInstance());
				beneficiaires.set(i, beneficiaire);
			}
			int b = 0;
			while(b<instances.size()) {
				Instance instance = iinstance.getOneInstance(instances.get(b).getUid());
				if(instance != null) {
					if(instance.getInstanceBeneficiaires().isEmpty()) {
						iinstance.deleteCompleteInstance(instance);
					}
				}
				b++;
			}
			
			beneficiaires.get(i).setOrganisation(null);
			beneficiaireRepository.delete(beneficiaires.get(i));
	
			i++;
		}
		
		return beneficiaires.size();
	}



}
