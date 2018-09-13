package ci.jsi.entites.beneficiaire;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ci.jsi.entites.dataValue.DataValueTDO;
import ci.jsi.entites.dataValue.IdataValues;
import ci.jsi.entites.element.Element;
import ci.jsi.entites.element.Ielement;
import ci.jsi.entites.organisation.OrganisationConvertEntitie;
import ci.jsi.entites.programme.Iprogramme;
import ci.jsi.entites.programme.Programme;
import ci.jsi.initialisation.ConvertDate;
import ci.jsi.initialisation.Uid;

@Service
public class BeneficiaireConvert {

	@Autowired
	ConvertDate convertDate;
	@Autowired
	Uid uid;

	@Autowired
	InstanceBeneficiaireConvert instanceBeneficiaireConvert;
	@Autowired
	private OrganisationConvertEntitie organisationConvertEntitie;
	@Autowired
	private Ielement ielement;
	@Autowired
	Iprogramme iprogramme;
	@Autowired
	IdataValues idataValues;

	public BeneficiaireTDO getBeneficiaireTDO(Beneficiaire beneficiaire) {
		BeneficiaireTDO beneficiaireTDO = new BeneficiaireTDO();
		beneficiaireTDO.setId(beneficiaire.getUid());
		beneficiaireTDO.setName(beneficiaire.getName());
		beneficiaireTDO.setFirstName(beneficiaire.getFirstName());
		beneficiaireTDO.setId_dreams(beneficiaire.getId_dreams());
		beneficiaireTDO.setCode(beneficiaire.getCode());
		beneficiaireTDO.setTelephone(beneficiaire.getTelephone());
		if (beneficiaire.getDateNaissance() != null) {
			beneficiaireTDO.setDateNaissance(convertDate.getDateString(beneficiaire.getDateNaissance()));
		}
		if (beneficiaire.getDateEnrolement() != null) {
			beneficiaireTDO.setDateEnrolement(convertDate.getDateString(beneficiaire.getDateEnrolement()));
		}

		beneficiaireTDO.setAgeEnrolement(Integer.toString(beneficiaire.getAgeEnrolement()));

		if (beneficiaire.getDateCreation() != null) {
			beneficiaireTDO.setDateCreation(convertDate.getDateString(beneficiaire.getDateCreation()));
		}
		if (beneficiaire.getDateUpdate() != null) {
			beneficiaireTDO.setDateUpdate(convertDate.getDateString(beneficiaire.getDateUpdate()));
		}
		beneficiaireTDO.setInstance(
				instanceBeneficiaireConvert.getInstanceBeneficiaires(beneficiaire.getInstanceBeneficiaires()));
		beneficiaireTDO.setOrganisation(organisationConvertEntitie.getOrganisation(beneficiaire.getOrganisation()));

		return beneficiaireTDO;
	}

	public List<BeneficiaireTDO> getBeneficiaireTDOs(List<Beneficiaire> beneficiaires) {
		List<BeneficiaireTDO> beneficiaireTDOs = new ArrayList<BeneficiaireTDO>();
		for (int l = 0; l < beneficiaires.size(); l++) {
			beneficiaireTDOs.add(getBeneficiaireTDO(beneficiaires.get(l)));
		}
		return beneficiaireTDOs;
	}

	public Beneficiaire saveBeneficiaire(BeneficiaireTDO beneficiaireTDO) {
		Date laDate = null;
		Date laDateEnrol = null;
		Beneficiaire beneficiaire = new Beneficiaire();
		if (beneficiaireTDO.getId_dreams() == null)
			return null;
		if (beneficiaireTDO.getName() == null)
			return null;
		if (beneficiaireTDO.getFirstName() == null)
			return null;
		if (beneficiaireTDO.getOrganisation() == null) {
			return null;
		}
		if (beneficiaireTDO.getDateNaissance() != null) {

				laDate = convertDate.getDateParse(beneficiaireTDO.getDateNaissance());
				if (laDate == null) {
					return null;
				}
			
		}
		if (beneficiaireTDO.getDateEnrolement() != null) {
				laDateEnrol = convertDate.getDateParse(beneficiaireTDO.getDateEnrolement());
				if (laDateEnrol == null) {
					return null;
				}
			
		}

		beneficiaire.setUid(uid.getUid());
		beneficiaire.setName(beneficiaireTDO.getName());
		beneficiaire.setFirstName(beneficiaireTDO.getFirstName());
		beneficiaire.setId_dreams(beneficiaireTDO.getId_dreams());
		beneficiaire.setCode(beneficiaireTDO.getCode());
		beneficiaire.setTelephone(beneficiaireTDO.getTelephone());

		beneficiaire.setDateNaissance(laDate);
		beneficiaire.setDateEnrolement(laDateEnrol);
		beneficiaire.setAgeEnrolement(Integer.parseInt(beneficiaireTDO.getAgeEnrolement()));
		beneficiaire.setDateCreation(new Date());
		beneficiaire.setDateUpdate(new Date());
		beneficiaire.setOrganisation(organisationConvertEntitie.setOneOrganisation(beneficiaireTDO.getOrganisation()));
		if (beneficiaireTDO.getInstance() != null) {
			beneficiaire
					.setInstanceBeneficiaires(instanceBeneficiaireConvert.getInstances(beneficiaireTDO.getInstance()));
		}

		return beneficiaire;
	}

	public Beneficiaire UpdateBeneficiaireByTDO(Beneficiaire beneficiaire, BeneficiaireTDO beneficiaireTDO) {
		Date laDate = null;
		Date laDateEnrol = null;
		beneficiaire.setName(beneficiaireTDO.getName());
		beneficiaire.setFirstName(beneficiaireTDO.getFirstName());
		beneficiaire.setId_dreams(beneficiaireTDO.getId_dreams());
		beneficiaire.setCode(beneficiaireTDO.getCode());
		beneficiaire.setTelephone(beneficiaireTDO.getTelephone());
		if (beneficiaireTDO.getDateNaissance() != null) {

			
				laDate = convertDate.getDateParse(beneficiaireTDO.getDateNaissance());
				if (laDate == null) {
					return null;
				}
		}
		if (beneficiaireTDO.getDateEnrolement() != null) {
				laDateEnrol = convertDate.getDateParse(beneficiaireTDO.getDateNaissance());
				if (laDateEnrol == null) {
					return null;
				}
			
		}
		beneficiaire.setDateEnrolement(laDateEnrol);
		beneficiaire.setDateNaissance(laDate);
		beneficiaire.setAgeEnrolement(Integer.parseInt(beneficiaireTDO.getAgeEnrolement()));
		beneficiaire.setDateUpdate(new Date());

		beneficiaire.setOrganisation(organisationConvertEntitie.setOneOrganisation(beneficiaireTDO.getOrganisation()));

		beneficiaire.setInstanceBeneficiaires(instanceBeneficiaireConvert.getInstances(beneficiaireTDO.getInstance()));
		return beneficiaire;
	}

	public Beneficiaire UpdateBeneficiaire(Beneficiaire Ancbeneficiaire, Beneficiaire Newbeneficiaire) {
		Ancbeneficiaire.setAgeEnrolement(Newbeneficiaire.getAgeEnrolement());
		Ancbeneficiaire.setCode(Newbeneficiaire.getCode());
		Ancbeneficiaire.setDateEnrolement(Newbeneficiaire.getDateEnrolement());
		Ancbeneficiaire.setDateNaissance(Newbeneficiaire.getDateNaissance());
		Ancbeneficiaire.setDateUpdate(new Date());
		Ancbeneficiaire.setFirstName(Newbeneficiaire.getFirstName());
		Ancbeneficiaire.setName(Newbeneficiaire.getName());
		Ancbeneficiaire.setOrganisation(Newbeneficiaire.getOrganisation());
		Ancbeneficiaire.setTelephone(Newbeneficiaire.getTelephone());
		Ancbeneficiaire.getInstanceBeneficiaires().addAll(Newbeneficiaire.getInstanceBeneficiaires());
		return Ancbeneficiaire;
	}

	public List<StatusBeneficiaire> getStatusBeneficiaires(List<Beneficiaire> beneficiaires) {
		List<StatusBeneficiaire> statusBeneficiaires = new ArrayList<StatusBeneficiaire>();
		List<Element> elements = new ArrayList<Element>();
		List<Element> elementsStatus = new ArrayList<Element>();
		Element element = null;
		DataValueTDO dataValueTDO = null;
		Programme programmeEnrolement = null;
		Programme programmeDossierBeneficiare = null;
		StatusBeneficiaire statusBeneficiaire;
		programmeEnrolement = iprogramme.getOneProgrammeByCode("enrolement");
		programmeDossierBeneficiare = iprogramme.getOneProgrammeByCode("dossierBeneficiare");
		String[] elementCode = {"numeroOrdre","porteEntree","categorieDreams","quatierBenef"};
		String[] elementCodeSatuts = { "conceptSexualite", "conceptsGenre", "connaissanceCorpsOrgane", "aspectsNegatifs",
				"promotionDepistage", "participationActivites", "participationCauseries", "ecouteConseils", "suivi",
				"referenceVersExperts", "businessPlus", "participationAVEC", "appuiScolaire", "fournitures",
				"uniformes", "autre", "alphabetisation", "utilisationPreservatifs", "distributionPreservatifs",
				"referencePreservatifs", "referencVersPF", "referenceServices", "referenceMedical",
				"referencePsychoSocial", "referenceJuridique", "referenceAbri", "appuiMedicaux", "appuiJuridiques",
				"referenceNutritionnel", "appuiDocument", "sinovoyu", "AVEC", "educationFinanciere" };
		
		for(int i=0;i<elementCode.length;i++) {
			element = ielement.getOneElmentByCode(elementCode[i]);
			if(element != null) {
				elements.add(element);
			}else {
				System.out.println("Element introuvable: code = "+elementCode[i]);
				return null;
			}
		}
		for(int i=0;i<elementCodeSatuts.length;i++) {
			element = ielement.getOneElmentByCode(elementCodeSatuts[i]);
			if(element != null) {
				elementsStatus.add(element);
			}else {
				System.out.println("Element introuvable: code = "+elementCodeSatuts[i]);
				return null;
			}
		}
		
		for(int i = 0;i<beneficiaires.size();i++) {
			statusBeneficiaire = getStatusBeneficiaire(beneficiaires.get(i));
			for(int j=0;j<beneficiaires.get(i).getInstanceBeneficiaires().size();j++) {
				if(beneficiaires.get(i).getInstanceBeneficiaires().get(j).getInstance().getProgramme() == programmeEnrolement) {
					dataValueTDO = idataValues.getDataValueTDO(beneficiaires.get(i).getInstanceBeneficiaires().get(j).getInstance().getUid(), elements.get(0).getUid());
					statusBeneficiaire.setNumeroOrdre(dataValueTDO.getValue());
				}
				if(beneficiaires.get(i).getInstanceBeneficiaires().get(j).getInstance().getProgramme() == programmeDossierBeneficiare) {
					
					dataValueTDO = idataValues.getDataValueTDO(beneficiaires.get(i).getInstanceBeneficiaires().get(j).getInstance().getUid(), elements.get(3).getUid());
					statusBeneficiaire.setQuatier(dataValueTDO.getValue());
					dataValueTDO = idataValues.getDataValueTDO(beneficiaires.get(i).getInstanceBeneficiaires().get(j).getInstance().getUid(), elements.get(1).getUid());
					statusBeneficiaire.setPorteEntree(elementValue(elements.get(1),dataValueTDO.getValue()));
					dataValueTDO = idataValues.getDataValueTDO(beneficiaires.get(i).getInstanceBeneficiaires().get(j).getInstance().getUid(), elements.get(1).getUid());
					statusBeneficiaire.setCategorieDreams(elementValue(elements.get(1),dataValueTDO.getValue()));
					statusBeneficiaire.setStatus(getStatus(elementsStatus,beneficiaires.get(i).getInstanceBeneficiaires().get(j).getInstance().getUid()));
					
					
				}
				statusBeneficiaires.add(statusBeneficiaire);
			}
			
		}
		
		//numeroOrdre
		//porteEntree
		//categorieDreams
		//quatier
		
		
		return statusBeneficiaires;
	}
	
	private String elementValue(Element element, String valeur) {
		String lesValeurs = null;
		for(int i =0;i<element.getEnsembleOption().getOptions().size();i++) {
			if(!element.getEnsembleOption().isMultiple()) {
				if(element.getEnsembleOption().getOptions().get(i).getCode().equals(valeur)){
					return element.getEnsembleOption().getOptions().get(i).getName();
				}
			}else {
				if(valeur.indexOf(element.getEnsembleOption().getOptions().get(i).getCode()) != -1) {
					if(lesValeurs == null) {
						lesValeurs = element.getEnsembleOption().getOptions().get(i).getName();
					}else {
						lesValeurs = lesValeurs + ";" +element.getEnsembleOption().getOptions().get(i).getName();
					}
				}
			}
		}
		
		return lesValeurs;
	}
	
	private String getStatus(List<Element> elementsStatus,String instance) {
		for(int i = 0;i<elementsStatus.size();i++) {
			DataValueTDO dataValueTDO = null;
			dataValueTDO = idataValues.getDataValueTDO(instance, elementsStatus.get(i).getUid());
			if(dataValueTDO.getValue() != null && !dataValueTDO.getValue().equals("") && !dataValueTDO.getValue().equals(" ")) {
				return "active";
			}
		}
		return "inactive";
	}

	private StatusBeneficiaire getStatusBeneficiaire(Beneficiaire beneficiaire) {
		StatusBeneficiaire statusBeneficiaire = new StatusBeneficiaire();
		statusBeneficiaire.setId(beneficiaire.getUid());
		statusBeneficiaire.setName(beneficiaire.getName());
		statusBeneficiaire.setFirstName(beneficiaire.getFirstName());
		statusBeneficiaire.setIdDreams(beneficiaire.getId_dreams());
		statusBeneficiaire.setAgeEnrolement(beneficiaire.getAgeEnrolement());
		statusBeneficiaire.setDateEnrolement(beneficiaire.getDateEnrolement().toString());
		statusBeneficiaire.setTelephone(beneficiaire.getTelephone());		
		statusBeneficiaire.setDateNaissance(beneficiaire.getDateEnrolement().toString());
		statusBeneficiaire.setDateCreation(beneficiaire.getDateCreation().toString());
		statusBeneficiaire.setDateUpdate(beneficiaire.getDateUpdate().toString());
		statusBeneficiaire.setInstance(
				instanceBeneficiaireConvert.getInstanceBeneficiaires(beneficiaire.getInstanceBeneficiaires()));
		statusBeneficiaire.setOrganisation(organisationConvertEntitie.getOrganisation(beneficiaire.getOrganisation()));
		
		return statusBeneficiaire;
	}

}
