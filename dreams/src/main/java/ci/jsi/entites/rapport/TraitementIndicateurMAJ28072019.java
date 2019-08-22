package ci.jsi.entites.rapport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ci.jsi.entites.beneficiaire.Ibeneficiaire;
import ci.jsi.entites.dataValue.DataValue;
import ci.jsi.entites.dataValue.IdataValues;
import ci.jsi.entites.element.Element;
import ci.jsi.entites.element.Ielement;
import ci.jsi.entites.option.Ioption;
import ci.jsi.entites.organisation.Iorganisation;
import ci.jsi.entites.organisation.Organisation;
import ci.jsi.entites.organisation.OrganisationTDO;
import ci.jsi.entites.organisationLevel.IorganisationLevel;
import ci.jsi.entites.organisationLevel.OrganisationLevel;
import ci.jsi.entites.programme.Iprogramme;
import ci.jsi.entites.programme.ProgrammeTDO;
import ci.jsi.initialisation.ConvertDate;
import ci.jsi.initialisation.ResultatRequete;
import ci.jsi.initialisation.UidEntitie;
import ci.jsi.initialisation.ValeurAnalytic;

@Service
public class TraitementIndicateurMAJ28072019 {

	@Autowired
	IorganisationLevel iorganisationLevel;
	@Autowired
	Iorganisation iorganisation;
	@Autowired
	Iprogramme iprogramme;
	@Autowired
	IdataValues idataValues;
	@Autowired
	Ielement ielement;
	@Autowired
	Irapport irapport;
	@Autowired
	Ibeneficiaire ibeneficiaire;
	@Autowired
	Ioption ioption;
	@Autowired
	ConvertDate convertDate;

	
	
	String DossierEnrol[] = { "dat_enrol","age_enrol", "_01_participation_program", "id_dreams"};
	
	
	List<String> elementsDossierEnrol = new ArrayList<String>();
	List<Integer> lesPeriodes = new ArrayList<Integer>();
	List<String> lesPeriodesString = new ArrayList<String>();
	
	/*List<Integer> lesPeriodesInitiale = new ArrayList<Integer>();
	List<String> lesPeriodesStringInitiale = new ArrayList<String>();
	
	List<Integer> lesPeriodesIrreguliers = new ArrayList<Integer>();
	List<String> lesPeriodesStringIrreguliers = new ArrayList<String>();*/
	
	//List<DataValue> dataValues = new ArrayList<DataValue>();
	//List<List<DataValue>> lesEnregistrements = new ArrayList<List<DataValue>>();
	List<ValeurAnalytic> valeurAnalytic = new ArrayList<ValeurAnalytic>();
	
	
	
	
	ProgrammeTDO enrolement = null;
	ProgrammeTDO eligibilite = null;
	ProgrammeTDO dossierBeneficiare = null;
	ProgrammeTDO besoinBeneficiare = null;
	ProgrammeTDO vad = null;
	ProgrammeTDO reference = null;
	ProgrammeTDO groupe = null;
	
	List<OrganisationTDO> OrganisationTDOs;
	List<Organisation> lesOrganisation;
	OrganisationTDO organisationSelect;
	
	String ageCode = "age_enrol";
	String periodeCode = null;
	
	boolean execution = false;
	String HeureDebutExecution = null;
	String HeureFinExecution = null;
	ResultatRequete resultatRequete = new ResultatRequete();
	// private final DateTimeFormatter formatter = new DateTimeFormatter("dd-MM-yyyy
	// HH:mm:ss");
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	
	
	
	public ResultatRequete genereRapport(String action) {
		//System.out.println("execution = " + execution);
		if (action.equals("execute") && !execution) {
			resultatRequete.setStatus("enCours");
			execution = true;
			HeureDebutExecution = LocalDateTime.now().format(formatter);
			initialisePeriode("201801", true);
			chargeElement();
			//chargeIndicatorElement();
			chargeProgramme();
			gestionOrganisation();
			//executionIndicateurs();
			HeureFinExecution = LocalDateTime.now().format(formatter);
			System.out.println("debut HeureDebutExecution = " + HeureDebutExecution);
			System.out.println("fin HeureFinExecution = " + HeureFinExecution);
			execution = false;
			resultatRequete.setId(HeureFinExecution);
		}

		if (action.equals("status")) {
			if (execution) {
				resultatRequete.setStatus("enCours");
			} else {
				resultatRequete.setStatus("OK");
				resultatRequete.setId(HeureFinExecution);
			}
		}
		return resultatRequete;
	}

	
	
	private void initialisePeriode(String laDate, boolean normale){
		//String periodeRecu = getPeriode(laDate);
		boolean continu = true;
		String periode = "";
		int annee = Integer.parseInt(laDate.substring(0, 4));
		int lemois = Integer.parseInt(laDate.substring(5, 6));
		String mois = "";
		if(lemois<10) {
			mois = "0"+lemois;
		}else {
			mois = ""+lemois;
		}
		
		
		/*int annee = 2018;
		int lemois = 1;
		String mois = "01";*/
		LocalDate todaydate = LocalDate.now();
		String todayPeriode = todaydate.toString().substring(0, 4) + todaydate.toString().substring(5, 7);
		while(continu) {
			periode = annee+""+mois;
			/*if(normale) { 
				lesPeriodesInitiale.add(Integer.parseInt(periode));
				lesPeriodesStringInitiale.add(periode);
			}else { 
				lesPeriodesIrreguliers.add(Integer.parseInt(periode));
				lesPeriodesStringIrreguliers.add(periode);
			}*/
			lesPeriodes.add(Integer.parseInt(periode));
			lesPeriodesString.add(periode);
			lemois++;
			if (lemois>12) {
				annee++;
				lemois = 1;
			}
			if(lemois<10) {
				mois = "0"+lemois;
			}else {
				mois = ""+lemois;
			}
			if(todayPeriode.equals(periode)) {
				continu = false;
			}	
		}
	}
	
	private String getPeriode(String ladate) {
		String periode = null;
		if(ladate.indexOf('-') == 2) {
			periode = ladate.substring(6, 10) + ladate.substring(3, 5);
		}else {
			periode = ladate.substring(0, 4) + ladate.substring(5, 7);
		}
		return periode;
	}
	
	private void chargeProgramme() {
		List<ProgrammeTDO> programmeTDOs = new ArrayList<ProgrammeTDO>();
		programmeTDOs = iprogramme.getAllProgrammeTDO();
		for (int i = 0; i < programmeTDOs.size(); i++) {
			if (programmeTDOs.get(i).getCode().equals("enrolement")) {
				enrolement = programmeTDOs.get(i);
			}
			if (programmeTDOs.get(i).getCode().equals("eligibilite")) {
				eligibilite = programmeTDOs.get(i);
			}
			if (programmeTDOs.get(i).getCode().equals("dossierBeneficiare")) {
				dossierBeneficiare = programmeTDOs.get(i);
			}
			if (programmeTDOs.get(i).getCode().equals("besoinBeneficiare")) {
				besoinBeneficiare = programmeTDOs.get(i);
			}
			if (programmeTDOs.get(i).getCode().equals("vad")) {
				vad = programmeTDOs.get(i);
			}
			if (programmeTDOs.get(i).getCode().equals("reference")) {
				reference = programmeTDOs.get(i);
			}
			if (programmeTDOs.get(i).getCode().equals("groupe")) {
				groupe = programmeTDOs.get(i);
			}
		}

		for (int j = 0; j < DossierEnrol.length; j++) {
			Element element = ielement.getOneElmentByCode(DossierEnrol[j]);
			elementsDossierEnrol.add(element.getUid());
		}

		/*for (int j = 0; j < InterventionDossierBeneficiaire.length; j++) {
			elementsIntervention.add(ielement.getOneElmentByCode(InterventionDossierBeneficiaire[j]));
		}
		for (int j = 0; j < InterventionPopCible.length; j++) {
			elementsInterventionPopCible.add(ielement.getOneElmentByCode(InterventionPopCible[j]));
		}
		for (int j = 0; j < InterventionPrimaireDossierBeneficiaire.length; j++) {
			elementsInterventionPrimaire.add(ielement.getOneElmentByCode(InterventionPrimaireDossierBeneficiaire[j]));
		}
		for (int j = 0; j < InterventionSecondaireDossierBeneficiaire.length; j++) {
			elementsInterventionSecondaire.add(ielement.getOneElmentByCode(InterventionSecondaireDossierBeneficiaire[j]));
		}
		for (int j = 0; j < InterventionContextuelleDossierBeneficiaire.length; j++) {
			elementsInterventionContextuelle.add(ielement.getOneElmentByCode(InterventionContextuelleDossierBeneficiaire[j]));
		}
		for (int j = 0; j < InterventionEducationSupportDossierBeneficiaire.length; j++) {
			elementsInterventionEducationSupport.add(ielement.getOneElmentByCode(InterventionEducationSupportDossierBeneficiaire[j]));
		}
		for (int j = 0; j < elementsDossierBeneficiaire.size(); j++) {
			elementsUiDossierBeneficiaire.add(elementsDossierBeneficiaire.get(j).getUid());
		}*/
		/*
		 * for (int j = 0; j < elementsIntervention.size(); j++) {
		 * elementsUidIntervention.add(elementsIntervention.get(j).getUid()); } for (int
		 * j = 0; j < elementsInterventionPopCible.size(); j++) {
		 * elementsUidInterventionPopCible.add(elementsInterventionPopCible.get(j).
		 * getUid()); } for (int j = 0; j < elementsInterventionPrimaire.size(); j++) {
		 * elementsUidInterventionPrimaire.add(elementsInterventionPrimaire.get(j).
		 * getUid()); }
		 */

		System.err.println("****** fin execution InterventionDossierBeneficiaire *******");

	}
	
	private void chargeElement() {
		/*age_enrol = ielement.getOneElmentByCode("age_enrol");
		porteEntree = ielement.getOneElmentByCode("porteEntree");
		idDreamsElement = ielement.getOneElmentByCode("id_dreams");
		refPsychoElement = ielement.getOneElmentByCode("referencePsychoSocial");
		sinovoyuElement = ielement.getOneElmentByCode("sinovoyu");
		thematiqueElement = ielement.getOneElmentByCode("thematique");
		nomPrenomAutreCibleElement = ielement.getOneElmentByCode("nomPrenomAutreCible");
		soutienEducatifElement = ielement.getOneElmentByCode("soutienEducatif");
		alphabetisationElement = ielement.getOneElmentByCode("alphabetisation");
		utilisationPreservatifsElement = ielement.getOneElmentByCode("utilisationPreservatifs");
		materielQuantiteElement = ielement.getOneElmentByCode("materielQuantite");
		trancheAgeElement = ielement.getOneElmentByCode("trancheAge");
		referencePreservatifs = ielement.getOneElmentByCode("referencePreservatifs");
		agebenefElement = ielement.getOneElmentByCode("agebenef");
		datecontRefElement = ielement.getOneElmentByCode("datecontRef");
		motifrefElement = ielement.getOneElmentByCode("motifref");
		referencVersPFElement = ielement.getOneElmentByCode("referencVersPF");
		referenceServicesVIHElement = ielement.getOneElmentByCode("referenceServicesVIH");
		referencePsychoSocialElement = ielement.getOneElmentByCode("referencePsychoSocial");
		referenceJuridiqueElement = ielement.getOneElmentByCode("referenceJuridique");
		referenceMedicalElement = ielement.getOneElmentByCode("referenceMedical");
		fraisMedicauxElement = ielement.getOneElmentByCode("fraisMedicaux");
		fraisDocumentElement = ielement.getOneElmentByCode("fraisDocument");
		AVECelement = ielement.getOneElmentByCode("participationAVEC");
		businessPlusElement = ielement.getOneElmentByCode("businessPlus");*/
		
	}
	
	private void gestionOrganisation() {
		List<OrganisationLevel> organisationLevels = new ArrayList<OrganisationLevel>();
		OrganisationTDOs = new ArrayList<OrganisationTDO>();
		lesOrganisation = new ArrayList<Organisation>();
		List<OrganisationTDO> organisations = new ArrayList<OrganisationTDO>();
		organisationLevels = iorganisationLevel.getAllOrganisationLevel();
		
		if (!organisationLevels.isEmpty()) {
			int niveau = organisationLevels.get(0).getLevel();
			lesOrganisation = iorganisation.getAllOrganisation();
			OrganisationTDOs = iorganisation.getAllOrganisationTDO();
			organisations = getOrganisatioLevelOne(OrganisationTDOs, niveau);
			selectOrganisation(organisations);
		}

	}

	private List<OrganisationTDO> getOrganisatioLevelOne(List<OrganisationTDO> organisationTDOs, int niveau) {
		List<OrganisationTDO> organisationTDOs2 = new ArrayList<OrganisationTDO>();
		for (int i = 0; i < organisationTDOs.size(); i++) {
			if (niveau == organisationTDOs.get(i).getLevel()) {
				organisationTDOs2.add(organisationTDOs.get(i));
			}
		}
		return organisationTDOs2;
	}

	private Organisation getOrganisation(String organisationTDOid) {

		for (int i = 0; i < lesOrganisation.size(); i++) {
			if (lesOrganisation.get(i).getUid().equals(organisationTDOid)) {
				return lesOrganisation.get(i);
			}
		}
		return null;
	}

	private void selectOrganisation(List<OrganisationTDO> ListOrganisations) {

		for (int i = 0; i < ListOrganisations.size(); i++) {
						
			if (!ListOrganisations.get(i).getChildrens().isEmpty()) {
				List<OrganisationTDO> organisations = new ArrayList<OrganisationTDO>();
				organisations = getOrganisationTDOchildren(ListOrganisations.get(i).getChildrens());
				selectOrganisation(organisations);
			}
			organisationSelect = ListOrganisations.get(i);
			
			if (organisationSelect.getName() != null) {
				System.err.println("organisationSelect = "+organisationSelect.getName()+" // level = "+organisationSelect.getLevel());
			}
			executionIndicateurs();
		}
	}

	private List<OrganisationTDO> getOrganisationTDOchildren(List<UidEntitie> childrens) {
		List<OrganisationTDO> organisations = new ArrayList<OrganisationTDO>();
		for (int a = 0; a < childrens.size(); a++) {
			for (int i = 0; i < this.OrganisationTDOs.size(); i++) {
				if (this.OrganisationTDOs.get(i).getId().equals(childrens.get(a).getId())) {
					organisations.add(this.OrganisationTDOs.get(i));
					break;
				}
			}
		}

		return organisations;
	}

	
	private List<List<DataValue>> getProgrammeDataValue(ProgrammeTDO programme,List<String> organisation,List<String> lesElements) {
		
		List<DataValue> allDataValue = new ArrayList<DataValue>();
		
		allDataValue = idataValues.dataAnalyseElementList(organisation,
					programme.getId(), lesElements);
		
				
		//lesEnregistrements = constituerDataValueInstance(allDataValue);
		return constituerDataValueInstance(allDataValue);
		
	}
	
	private List<List<DataValue>> constituerDataValueInstance(List<DataValue> dataValues) {
		List<List<DataValue>> instanceValue = new ArrayList<List<DataValue>>();
		int deb = 0;
		int pas = deb + 1;
		List<DataValue> TempsDataValues = new ArrayList<DataValue>();
		
		if (dataValues.size() == pas) {
			TempsDataValues.add(dataValues.get(deb));
			dataValues.remove(deb);
			instanceValue.add(TempsDataValues);
		}else {
			while (deb < dataValues.size()) {
				if (dataValues.get(deb).getInstance().getInstanceid() == dataValues.get(pas).getInstance()
						.getInstanceid()) {
					TempsDataValues.add(dataValues.get(pas));
					dataValues.remove(pas);
					pas--;
				}
				pas++;
				if (dataValues.size() == pas) {
					TempsDataValues.add(0, dataValues.get(deb));
					dataValues.remove(deb);
					instanceValue.add(TempsDataValues);
					deb = 0;
					pas = deb + 1;
					TempsDataValues = new ArrayList<DataValue>();
					if (dataValues.size() == pas) {
						TempsDataValues.add(dataValues.get(deb));
						dataValues.remove(deb);
						instanceValue.add(TempsDataValues);
					}
				}
			}
		}
		return instanceValue;
	}
	
	private void executionIndicateurs() {
		nbreBenefEnrole();
	}
	
	private void nbreBenefEnrole() {
		List<String> organisation = new ArrayList<String>();
		List<List<DataValue>> lesEnregistrements = new ArrayList<List<DataValue>>();
		ValeurAnalytic analytic = new ValeurAnalytic();
		
		String indicator1 = "nbreBenefEnrole";
		String indicator2 = "nbreOEVserviParDREAMS";
		boolean indicator1ValeurExiste = false;
		boolean indicator2ValeurExiste = false;
		
		String critere1 = "_01_participation_program";
		Boolean crit2 = false;
		periodeCode = "dat_enrol";
		
		organisation.add(this.organisationSelect.getId());
		
		lesEnregistrements = getProgrammeDataValue(enrolement,organisation,elementsDossierEnrol);
		
		for(int i=0; i<lesEnregistrements.size();i++) {
			
			String periode = null;
			String trancheAge = null;
			int age = 0;
			for(int j=0; j <lesEnregistrements.get(i).size();j++) {
				if(lesEnregistrements.get(i).get(j).getElement().getCode().equals(critere1)) {
					if(lesEnregistrements.get(i).get(j).getValue().contains("OEV")) {
						crit2 = true;
					}
				}
				if(lesEnregistrements.get(i).get(j).getElement().getCode().equals(ageCode)) {
					age = Integer.parseInt(lesEnregistrements.get(i).get(j).getValue());
				}
				if(lesEnregistrements.get(i).get(j).getElement().getCode().equals(periodeCode)) {
					String ladate = lesEnregistrements.get(i).get(j).getValue();
					periode = getPeriode(ladate);
				}
			}
			if(age < 15) {
				trancheAge = "10_14";
			}else {
				trancheAge = "15_19";
			}
			
			indicator1ValeurExiste = true;
			analytic = new ValeurAnalytic();
			analytic.setTranche(trancheAge);
			analytic.setPeriode(periode);
			analytic.setIndicator(indicator1);
			analytic.setValeurNouveau(1);
			compileData(analytic);
			
			if(crit2) {
				indicator2ValeurExiste = true;
				analytic = new ValeurAnalytic();
				analytic.setTranche(trancheAge);
				analytic.setPeriode(periode);
				analytic.setIndicator(indicator2);
				analytic.setValeurNouveau(1);
				compileData(analytic);
			}
			
		}
		
		//System.out.println("cyrille");
		//System.out.println("cyrille");
		if(!indicator1ValeurExiste) {
			analytic = new ValeurAnalytic();
			analytic.setTranche("10_14");
			analytic.setPeriode("201801");
			analytic.setIndicator(indicator1);
			compileData(analytic);
		}
		if(!indicator2ValeurExiste) {
			analytic = new ValeurAnalytic();
			analytic.setTranche("10_14");
			analytic.setPeriode("201801");
			analytic.setIndicator(indicator2);
			compileData(analytic);
		}	
		agregeData();
		
	}

	private void compileData(ValeurAnalytic valeur) {
		//System.err.println(valeur.getIndicator()+" == "+valeur.getPeriode());
		
		int laperiode = Integer.parseInt(valeur.getPeriode());
		boolean indexGrand = false;
		int index = 0;
		int indexIndicateur = 0;
		boolean trouve = false;
		boolean indicateurExist = false;
				
		for(int i = 0;i<valeurAnalytic.size();i++) {
			if(valeurAnalytic.get(i).getIndicator().equals(valeur.getIndicator())) {
				indicateurExist = true;
				indexIndicateur = i;
				int periodeExit = Integer.parseInt(valeurAnalytic.get(i).getPeriode());
				if(periodeExit == laperiode) {
					if(valeurAnalytic.get(i).getTranche().equals(valeur.getTranche())) {
						valeurAnalytic.get(i).setValeurNouveau(valeurAnalytic.get(i).getValeurNouveau() + valeur.getValeurNouveau());
						trouve = true;
						break;
					}
				}
				if(periodeExit > laperiode) {
					indexGrand = true;
					index = i;
					break;
				}
			}else {
				if(indicateurExist) {
					break;
				}
			}
		}
		
		if(!trouve) {
			if(indexGrand) {
				valeurAnalytic.add(index, valeur);
			}else {
				if(indicateurExist) {
					valeurAnalytic.add(indexIndicateur + 1, valeur);
				}else {
					valeurAnalytic.add(valeur);
				}
			}
		}
	
	}
	
	private void agregeData() {
		List<ValeurAnalytic> valeurAnalytic1 = new ArrayList<ValeurAnalytic>();
		valeurAnalytic1 = harmoniseDataPeriode(valeurAnalytic);
		
		int index = 0;
		int i = 1;
		while (i < valeurAnalytic1.size()) {
			if(valeurAnalytic1.get(index).getIndicator().equals(valeurAnalytic1.get(i).getIndicator())) {
				if(valeurAnalytic1.get(index).getTranche().equals(valeurAnalytic1.get(i).getTranche())) {
					valeurAnalytic1.get(i).setValeurAncien(valeurAnalytic1.get(index).getValeurAncien() + valeurAnalytic1.get(index).getValeurNouveau());
					index++;
					i=index;
				}
			}else {
				//index = i;
				index++;
				i=index;
			}
			
			i++;
			
			if(i == valeurAnalytic1.size() &&  index+1 < i) {
				index++;
				i=index+1;
			}
		}
		prepareRapport(valeurAnalytic1);
		valeurAnalytic.clear();
		System.out.println("Fin");
		
	}
	
	private List<ValeurAnalytic> harmoniseDataPeriode(List<ValeurAnalytic> valeur) {
		List<ValeurAnalytic> valeurHarmoniser = new ArrayList<ValeurAnalytic>();
		int periodeDebut = lesPeriodes.get(0);
		int periodeFin = lesPeriodes.get(lesPeriodes.size() - 1);
		
		String indicateurActu = valeur.get(0).getIndicator();
		String indicateurNext = indicateurActu;
		int indexNextIndicator = 0;
		int i = 0;
		int j = 0;
		int debutPeriode = 0;
			
		while(i<lesPeriodes.size()) {
			int nbre = 0;
			String tranche = null;
			j = debutPeriode;
			while(j <valeur.size()) {
				if(valeur.get(j).getIndicator().equals(indicateurActu)) {
					int periodeActu = Integer.parseInt(valeur.get(j).getPeriode());
					if(periodeActu<periodeDebut || periodeActu>periodeFin) {
						String erreur;
						if(organisationSelect.getName() != null) {
							erreur = "Erreur: Date hors periode: Structure = "+organisationSelect.getName()+", periode = "+periodeActu;
						}else {
							erreur = "Erreur: Date hors periode: Structure id = "+organisationSelect.getId()+", periode = "+periodeActu;
						}
						
						compileError(erreur);
					}
					if(lesPeriodes.get(i) == periodeActu) {
						valeurHarmoniser.add(valeur.get(j));
						tranche = valeur.get(j).getTranche();
						nbre++;
					}
				}else {
					indexNextIndicator = j;
					indicateurNext = valeur.get(j).getIndicator();
					break;
				}
				j++;
			}
			
			if(nbre < 2) {
				valeurHarmoniser = addList(valeurHarmoniser,indicateurActu,lesPeriodes.get(i), tranche);
			}
			
			i++;
			if(i == lesPeriodes.size()) {
				if(!indicateurNext.equals(indicateurActu)) {
					indicateurActu = indicateurNext;
					debutPeriode = indexNextIndicator;
					i = 0;
				}
			}
		}
			
		System.out.println("Fin harmoniseDataPeriode");
		return valeurHarmoniser;
	}
	
	private List<ValeurAnalytic> addList(List<ValeurAnalytic> valeurHarmoniser,String indicateur, int periode, String tranche) {
		ValeurAnalytic defaut;
		String tranche1014= "10_14";
		String tranche1519= "15_19";
		
		if(tranche == null) {
			defaut = new ValeurAnalytic();
			defaut.setIndicator(indicateur);
			defaut.setPeriode(""+periode);
			defaut.setTranche(tranche1014);
			valeurHarmoniser.add(defaut);
			
			defaut = new ValeurAnalytic();
			defaut.setIndicator(indicateur);
			defaut.setPeriode(""+periode);
			defaut.setTranche(tranche1519);
			valeurHarmoniser.add(defaut);
		}else {
			if(tranche.equals(tranche1014)) {
				defaut = new ValeurAnalytic();
				defaut.setIndicator(indicateur);
				defaut.setPeriode(""+periode);
				defaut.setTranche(tranche1519);
				valeurHarmoniser.add(defaut);
			}else {
				defaut = new ValeurAnalytic();
				defaut.setIndicator(indicateur);
				defaut.setPeriode(""+periode);
				defaut.setTranche(tranche1014);
				valeurHarmoniser.add(defaut);
			}
		}
		
		return valeurHarmoniser;
	}
	
	private void prepareRapport(List<ValeurAnalytic> valeur) {
		List<ValeurAnalytic> indicateurValeur = new ArrayList<ValeurAnalytic>();
		String indicateur = valeur.get(0).getIndicator();
				
		for(int i = 0; i<valeur.size();i++) {
			if(valeur.get(i).getIndicator().equals(indicateur)) {
				indicateurValeur.add(valeur.get(i));
			}else {
				
				enregistreRapport(indicateur, addTotal(indicateurValeur));
				
				indicateurValeur.clear();
				indicateur = valeur.get(i).getIndicator();
				indicateurValeur.add(valeur.get(i));
				
			}
			if(i+1 == valeur.size()) {
				enregistreRapport(indicateur, addTotal(indicateurValeur));
			}
		}
		
	}

	private List<ValeurAnalytic> addTotal(List<ValeurAnalytic> valeur){
		ValeurAnalytic defaut;
		int i = 0;
		double total = 0;
		String periode = valeur.get(0).getPeriode();
		String indicateur = valeur.get(0).getIndicator();
		boolean conti = true;
		while(conti) {
			if(i<valeur.size() && valeur.get(i).getPeriode().equals(periode)) {
				total = total + valeur.get(i).getValeurAncien() + valeur.get(i).getValeurNouveau();
			}else {
								
				defaut = new ValeurAnalytic();
				defaut.setIndicator(indicateur);
				defaut.setPeriode(periode);
				defaut.setValeurNouveau(total);
								
				if(i == valeur.size()) {
					conti = false;
				}else {
					periode = valeur.get(i).getPeriode();
				}
					
				valeur.add(i, defaut);
				total = 0;
			}
			i++;
		}
		
		return valeur;
	}
	
	private void enregistreRapport(String elementCode, List<ValeurAnalytic> valeurAnalytic) {
		List<Rapport> rapports = new ArrayList<Rapport>();
		List<Rapport> listRapports = new ArrayList<Rapport>();
		
		Organisation organisation = getOrganisation(this.organisationSelect.getId());
		Element  element = ielement.getOneElmentByCode(elementCode);
		
		for (int pero = 0; pero < lesPeriodesString.size(); pero++) {
			for(int i = 0; i < element.getEnsembleOption().getOptions().size();i++) {
				Rapport rapport = new Rapport();
				rapport.setElement(element);
				rapport.setOrganisation(organisation);
				rapport.setOption(element.getEnsembleOption().getOptions().get(i));
				rapport.setPeriode(lesPeriodesString.get(pero));
				listRapports.add(rapport);
			}
			Rapport rapport = new Rapport();
			rapport.setElement(element);
			rapport.setOrganisation(organisation);
			rapport.setPeriode(lesPeriodesString.get(pero));
			listRapports.add(rapport);
		}
		
		if (!this.organisationSelect.getChildrens().isEmpty()) {

			List<String> childrens = new ArrayList<String>();
			for (int comp = 0; comp < this.organisationSelect.getChildrens().size(); comp++) {
				childrens.add(this.organisationSelect.getChildrens().get(comp).getId());
			}
			
			rapports = irapport.getRapportOptionRapportCodeNull(childrens, elementCode, lesPeriodesString);
			if (!rapports.isEmpty()) {
				for (int liste = 0; liste < listRapports.size(); liste++) {
					for (int rap = 0; rap < rapports.size(); rap++) {
						if(listRapports.get(liste).getPeriode().equals(rapports.get(rap).getPeriode())) {
							if(listRapports.get(liste).getOption() != null && rapports.get(rap).getOption() != null) {
								if(listRapports.get(liste).getOption().getUid().equals(rapports.get(rap).getOption().getUid())) {
									listRapports.get(liste).setValeurs(listRapports.get(liste).getValeurs() + rapports.get(rap).getValeurs());
								}
							}
							if(listRapports.get(liste).getOption() == null && rapports.get(rap).getOption() == null) {
								listRapports.get(liste).setValeurs(listRapports.get(liste).getValeurs() + rapports.get(rap).getValeurs());
							}
						}
						
						
					}
				}
				
			}

		}

		rapports = irapport.getRapportOptionRapportCodeNull(this.organisationSelect.getId(), elementCode, lesPeriodesString);
		
		rapports = updateRapport(rapports, listRapports,valeurAnalytic);
		
		irapport.saveRapport(rapports);

	}

	private List<Rapport> updateRapport(List<Rapport> rapports, List<Rapport> listRapports, List<ValeurAnalytic> valeurAnalytic) {

		List<Rapport> newRapports = new ArrayList<Rapport>();
		
		for (int liste = 0; liste < listRapports.size(); liste++) {
			boolean inexistant = true;
			for (int rap = 0; rap < rapports.size(); rap++) {
				if(listRapports.get(liste).getPeriode().equals(rapports.get(rap).getPeriode())) {
					if(listRapports.get(liste).getOption() != null && rapports.get(rap).getOption() != null) {
						if(listRapports.get(liste).getOption().getUid().equals(rapports.get(rap).getOption().getUid())) {
							rapports.get(rap).setValeurs(listRapports.get(liste).getValeurs());
							newRapports.add(rapports.get(rap));
							inexistant = false;
						}
					}
					if(listRapports.get(liste).getOption() == null && rapports.get(rap).getOption() == null) {
						rapports.get(rap).setValeurs(listRapports.get(liste).getValeurs());
						newRapports.add(rapports.get(rap));
						inexistant = false;
					}
				}
			}
			if(inexistant) {
				newRapports.add(listRapports.get(liste));
			}
		}
		
		for (int newRap = 0; newRap < newRapports.size(); newRap++) {
			for (int val = 0; val < valeurAnalytic.size(); val++) {
				if(newRapports.get(newRap).getPeriode().equals(valeurAnalytic.get(val).getPeriode())) {
					if(newRapports.get(newRap).getOption() != null && valeurAnalytic.get(val).getTranche() != null) {
						if(newRapports.get(newRap).getOption().getCode().contains(valeurAnalytic.get(val).getTranche())) {
							String etat = "nouveau_"+valeurAnalytic.get(val).getTranche();
							if(newRapports.get(newRap).getOption().getCode().equals(etat)) {
								newRapports.get(newRap).setValeurs(newRapports.get(newRap).getValeurs() + valeurAnalytic.get(val).getValeurNouveau());
							}else {
								newRapports.get(newRap).setValeurs(newRapports.get(newRap).getValeurs() + valeurAnalytic.get(val).getValeurAncien());
							}
							
						}
					}
					if(newRapports.get(newRap).getOption() == null && valeurAnalytic.get(val).getTranche() == null) {
						newRapports.get(newRap).setValeurs(newRapports.get(newRap).getValeurs() + valeurAnalytic.get(val).getValeurNouveau());
					}
					
				}
			}
		}
			
		return newRapports;
	}
	
	private void compileError(String erreur) {
		if(resultatRequete.getRaisonAutreEchec().indexOf(erreur) == -1) {
			resultatRequete.getRaisonAutreEchec().add(erreur);
		}
		
	}

	
}
