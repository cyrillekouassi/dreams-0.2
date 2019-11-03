package ci.jsi.entites.beneficiaire;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ci.jsi.entites.dataValue.DataValue;
import ci.jsi.entites.dataValue.DataValueTDO;
import ci.jsi.entites.dataValue.IdataValues;
import ci.jsi.entites.element.Element;
import ci.jsi.entites.element.Ielement;
import ci.jsi.entites.instance.Iinstance;
import ci.jsi.entites.instance.Instance;
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
	@Autowired
	BeneficiaireRepository beneficiaireRepository;
	@Autowired
	Iinstance iinstance;
	@Autowired
	InstanceBeneficiaireRepository instanceBeneficiaireRepository;

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

		// beneficiaire.setInstanceBeneficiaires(instanceBeneficiaireConvert.getInstances(beneficiaireTDO.getInstance()));
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
		String[] elementCode = { "no_benef", "porteEntree", "categorieDreams", "quatierBenef" };
		String[] elementCodeSatuts = { "conceptSexualite", "conceptsGenre", "connaissanceCorpsOrgane",
				"aspectsNegatifs", "promotionDepistage", "participationActivites", "participationCauseries",
				"ecouteConseils", "suivi", "referenceVersExperts", "businessPlus", "participationAVEC", "fraisScolaire",
				"fournitures", "uniformes", "autre", "alphabetisation", "utilisationPreservatifs",
				"distributionPreservatifs", "referencePreservatifs", "referencVersPF", "referenceServicesVIH",
				"referenceMedical", "referencePsychoSocial", "referenceJuridique", "referenceAbri", "fraisMedicaux",
				"fraisJuridiques", "referenceNutritionnel", "fraisDocument", "sinovoyu", "AVEC",
				"educationFinanciere" };

		for (int i = 0; i < elementCode.length; i++) {
			element = ielement.getOneElmentByCode(elementCode[i]);
			if (element != null) {
				elements.add(element);
			} else {
				System.err.println("Element introuvable: code = " + elementCode[i]);
				// return null;
			}
		}
		for (int i = 0; i < elementCodeSatuts.length; i++) {
			element = ielement.getOneElmentByCode(elementCodeSatuts[i]);
			if (element != null) {
				elementsStatus.add(element);
			} else {
				System.err.println("Element introuvable: code = " + elementCodeSatuts[i]);
				// return null;
			}
		}

		for (int i = 0; i < beneficiaires.size(); i++) {
			statusBeneficiaire = getStatusBeneficiaire(beneficiaires.get(i));
			for (int j = 0; j < beneficiaires.get(i).getInstanceBeneficiaires().size(); j++) {
				if (beneficiaires.get(i).getInstanceBeneficiaires().get(j).getInstance()
						.getProgramme() == programmeEnrolement) {
					dataValueTDO = idataValues.getDataValueTDO(
							beneficiaires.get(i).getInstanceBeneficiaires().get(j).getInstance().getUid(),
							elements.get(0).getCode());
					// if(dataValueTDO != null) {
					statusBeneficiaire.setNumeroOrdre(dataValueTDO.getValue());
					// }
				}
				if (beneficiaires.get(i).getInstanceBeneficiaires().get(j).getInstance()
						.getProgramme() == programmeDossierBeneficiare) {

					dataValueTDO = idataValues.getDataValueTDO(
							beneficiaires.get(i).getInstanceBeneficiaires().get(j).getInstance().getUid(),
							elements.get(3).getCode());
					statusBeneficiaire.setQuatier(dataValueTDO.getValue());
					dataValueTDO = idataValues.getDataValueTDO(
							beneficiaires.get(i).getInstanceBeneficiaires().get(j).getInstance().getUid(),
							elements.get(1).getCode());
					statusBeneficiaire.setPorteEntree(elementValue(elements.get(1), dataValueTDO.getValue()));
					dataValueTDO = idataValues.getDataValueTDO(
							beneficiaires.get(i).getInstanceBeneficiaires().get(j).getInstance().getUid(),
							elements.get(2).getCode());
					statusBeneficiaire.setCategorieDreams(elementValue(elements.get(1), dataValueTDO.getValue()));
					statusBeneficiaire.setStatus(getStatus(elementsStatus,
							beneficiaires.get(i).getInstanceBeneficiaires().get(j).getInstance().getUid()));

				}
			}
			statusBeneficiaires.add(statusBeneficiaire);
		}

		return statusBeneficiaires;
	}

	private String elementValue(Element element, String valeur) {
		String lesValeurs = null;
		for (int i = 0; i < element.getEnsembleOption().getOptions().size(); i++) {
			if (!element.getEnsembleOption().isMultiple()) {
				if (element.getEnsembleOption().getOptions().get(i).getCode().equals(valeur)) {
					return element.getEnsembleOption().getOptions().get(i).getName();
				}
			} else {
				if (valeur.indexOf(element.getEnsembleOption().getOptions().get(i).getCode()) != -1) {
					if (lesValeurs == null) {
						lesValeurs = element.getEnsembleOption().getOptions().get(i).getName();
					} else {
						lesValeurs = lesValeurs + ";" + element.getEnsembleOption().getOptions().get(i).getName();
					}
				}
			}
		}

		return lesValeurs;
	}

	private String getStatus(List<Element> elementsStatus, String instance) {
		for (int i = 0; i < elementsStatus.size(); i++) {
			DataValueTDO dataValueTDO = null;
			dataValueTDO = idataValues.getDataValueTDO(instance, elementsStatus.get(i).getCode());
			if (dataValueTDO.getValue() != null && !dataValueTDO.getValue().equals("")
					&& !dataValueTDO.getValue().equals(" ")) {
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

	public List<BeneficiaireOEV> getBeneficiairesOEV(List<Beneficiaire> beneficiaires) {

		List<BeneficiaireOEV> listBeneficiaireOEV = new ArrayList<BeneficiaireOEV>();
		List<DataValue> dataValue = new ArrayList<DataValue>();
		;
		Programme programmeEnrolement = null;
		BeneficiaireOEV beneficiaireOEV;
		programmeEnrolement = iprogramme.getOneProgrammeByCode("enrolement");
		List<String> elementCodeOEV = new ArrayList<String>();
		;
		elementCodeOEV.add("_01_participation_program");
		elementCodeOEV.add("codeOEV");

		for (int i = 0; i < beneficiaires.size(); i++) {
			beneficiaireOEV = getOEVBeneficiaires(beneficiaires.get(i));
			for (int j = 0; j < beneficiaires.get(i).getInstanceBeneficiaires().size(); j++) {
				if (beneficiaires.get(i).getInstanceBeneficiaires().get(j).getInstance()
						.getProgramme() == programmeEnrolement) {
					dataValue = idataValues.getDataValueTDO(
							beneficiaires.get(i).getInstanceBeneficiaires().get(j).getInstance().getUid(),
							elementCodeOEV);
					String codeOEV = null;
					boolean oev = false;
					for (int a = 0; a < dataValue.size(); a++) {
						if (dataValue.get(a) != null && dataValue.get(a).getValue() != null) {
							if (dataValue.get(a).getValue().contains("OEV")) {
								oev = true;
							}

						}
						if (dataValue.get(a).getElement().getCode().equals("codeOEV")) {
							codeOEV = dataValue.get(a).getValue();

						}
					}

					if (oev) {
						beneficiaireOEV.setCodeOEV(codeOEV);
						listBeneficiaireOEV.add(beneficiaireOEV);
					}
				}

			}

		}

		listBeneficiaireOEV = searchBBOEVvalue(listBeneficiaireOEV);

		return listBeneficiaireOEV;
	}

	private BeneficiaireOEV getOEVBeneficiaires(Beneficiaire beneficiaire) {
		BeneficiaireOEV beneficiaireOEV = new BeneficiaireOEV();
		beneficiaireOEV.setId(beneficiaire.getUid());
		beneficiaireOEV.setName(beneficiaire.getName());
		beneficiaireOEV.setFirstName(beneficiaire.getFirstName());
		beneficiaireOEV.setIdDreams(beneficiaire.getId_dreams());
		beneficiaireOEV.setAgeEnrolement(beneficiaire.getAgeEnrolement());
		beneficiaireOEV.setDateEnrolement(beneficiaire.getDateEnrolement().toString());
		beneficiaireOEV.setTelephone(beneficiaire.getTelephone());
		beneficiaireOEV.setDateNaissance(beneficiaire.getDateEnrolement().toString());
		beneficiaireOEV.setDateCreation(beneficiaire.getDateCreation().toString());
		beneficiaireOEV.setDateUpdate(beneficiaire.getDateUpdate().toString());
		beneficiaireOEV.setInstance(
				instanceBeneficiaireConvert.getInstanceBeneficiaires(beneficiaire.getInstanceBeneficiaires()));
		beneficiaireOEV.setOrganisation(organisationConvertEntitie.getOrganisation(beneficiaire.getOrganisation()));

		return beneficiaireOEV;
	}

	private List<BeneficiaireOEV> searchBBOEVvalue(List<BeneficiaireOEV> beneficiaireOEV) {

		final String uri = "http://localhost/api/?menage=014AGK20130001";

		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(uri, String.class);

		System.out.println(result);

		return beneficiaireOEV;
	}

	public List<Beneficiaire> deleteBeneficiaireInstances(List<Beneficiaire> beneficiaires, String instance) {

		List<InstanceBeneficiaire> deleteInstanceBeneficiaires = new ArrayList<InstanceBeneficiaire>();
		int pas = 0;
		int i = pas + 1;
		while (i < beneficiaires.size()) {
			if (beneficiaires.get(i).getUid().equals(beneficiaires.get(pas).getUid())) {
				beneficiaires.remove(i);
				i--;
			}
			i++;
			if (i == beneficiaires.size()) {
				pas++;
				if (pas < beneficiaires.size()) {
					i = pas + 1;
				}
			}
		}

		for (int j = 0; j < beneficiaires.size(); j++) {
			List<InstanceBeneficiaire> instanceBeneficiaires = new ArrayList<InstanceBeneficiaire>();
			Beneficiaire beneficiaire = new Beneficiaire();
			beneficiaire = beneficiaires.get(j);
			instanceBeneficiaires = beneficiaire.getInstanceBeneficiaires();
			pas = 0;
			i = pas + 1;
			// boolean trouve = false;
			while (i < instanceBeneficiaires.size()) {

				if (instanceBeneficiaires.get(i).getInstance().getUid()
						.equals(instanceBeneficiaires.get(pas).getInstance().getUid())) {
					deleteInstanceBeneficiaires.add(instanceBeneficiaires.get(i));
					instanceBeneficiaires.remove(i);
					i--;
				}
				i++;
				if (i == instanceBeneficiaires.size()) {
					pas++;
					if (pas < instanceBeneficiaires.size()) {
						i = pas + 1;
					}
				}
			}
		}
		instanceBeneficiaireRepository.delete(deleteInstanceBeneficiaires);
		return beneficiaires;

	}

	public void deleteBeneficiaireInstance(List<InstanceBeneficiaire> deleteInstanceBeneficiaires) {
		instanceBeneficiaireRepository.delete(deleteInstanceBeneficiaires);
	}

}
