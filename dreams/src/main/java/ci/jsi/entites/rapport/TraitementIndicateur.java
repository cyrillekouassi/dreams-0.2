package ci.jsi.entites.rapport;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ci.jsi.entites.beneficiaire.BeneficiaireTDO;
import ci.jsi.entites.beneficiaire.Ibeneficiaire;
import ci.jsi.entites.dataValue.DataInstance;
import ci.jsi.entites.dataValue.DataValue;
import ci.jsi.entites.dataValue.IdataValues;
import ci.jsi.entites.element.Element;
import ci.jsi.entites.element.Ielement;
import ci.jsi.entites.instance.Instance;
import ci.jsi.entites.option.Ioption;
import ci.jsi.entites.option.Option;
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

@Service
public class TraitementIndicateur {

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

	String DossierBeneficiaire[] = { "porteEntree", "age_enrol", "id_dreams", "soutienEducatif", "conceptSexualite",
			"conceptsGenre", "connaissanceCorpsOrgane", "aspectsNegatifs", "promotionDepistage",
			"participationActivites", "participationCauseries", "ecouteConseils", "suivi", "referenceVersExperts",
			"businessPlus", "participationAVEC", "fraisScolaire", "fournitures", "uniformes", "autre",
			"alphabetisation", "utilisationPreservatifs", "distributionPreservatifs", "referencePreservatifs",
			"referencVersPF", "referenceServicesVIH", "referenceMedical", "referencePsychoSocial", "referenceJuridique",
			"referenceAbri", "fraisMedicaux", "fraisJuridiques", "referenceNutritionnel", "fraisDocument", "sinovoyu",
			"AVEC", "educationFinanciere" };
	List<Element> elementsDossierBeneficiaire = new ArrayList<Element>();
	String InterventionDossierBeneficiaire[] = { "conceptSexualite", "conceptsGenre", "connaissanceCorpsOrgane",
			"aspectsNegatifs", "promotionDepistage", "participationActivites", "participationCauseries",
			"ecouteConseils", "suivi", "referenceVersExperts", "businessPlus", "participationAVEC", "fraisScolaire",
			"fournitures", "uniformes", "autre", "alphabetisation", "utilisationPreservatifs",
			"distributionPreservatifs", "referencePreservatifs", "referencVersPF", "referenceServicesVIH",
			"referenceMedical", "referencePsychoSocial", "referenceJuridique", "referenceAbri", "fraisMedicaux",
			"fraisJuridiques", "referenceNutritionnel", "fraisDocument", "sinovoyu", "AVEC", "educationFinanciere" };
	List<Element> elementsIntervention = new ArrayList<Element>();
	String InterventionPopCible[] = { "conceptSexualite", "conceptsGenre", "connaissanceCorpsOrgane", "aspectsNegatifs",
			"promotionDepistage", "utilisationPreservatifs", "distributionPreservatifs", "referencePreservatifs",
			"referenceServicesVIH" };
	List<Element> elementsInterventionPopCible = new ArrayList<Element>();
	String InterventionPrimaireDossierBeneficiaire[] = { "conceptSexualite", "conceptsGenre", "connaissanceCorpsOrgane",
			"aspectsNegatifs", "promotionDepistage", "participationActivites", "participationCauseries",
			"ecouteConseils", "suivi", "referenceVersExperts"};
	String InterventionSecondaireDossierBeneficiaire[] = {"businessPlus", "participationAVEC", "fraisScolaire",
			"fournitures", "uniformes", "autre", "alphabetisation", "utilisationPreservatifs",
			"distributionPreservatifs", "referencePreservatifs", "referencVersPF", "referenceServicesVIH",
			"referenceMedical", "referencePsychoSocial", "referenceJuridique", "referenceAbri", "fraisMedicaux",
			"fraisJuridiques" };
	List<Element> elementsInterventionPrimaire = new ArrayList<Element>();

	List<String> elementsUiDossierBeneficiaire = new ArrayList<String>();
	/*
	 * List<String> elementsUidIntervention = new ArrayList<String>(); List<String>
	 * elementsUidInterventionPopCible = new ArrayList<String>(); List<String>
	 * elementsUidInterventionPrimaire = new ArrayList<String>();
	 */

	String laPeriode = null;
	String dateDebuts = "";
	String dateFins = null;
	List<OrganisationTDO> OrganisationTDOs;
	List<Organisation> lesOrganisation;
	OrganisationTDO organisationSelect;
	ProgrammeTDO enrolement = null;
	ProgrammeTDO eligibilite = null;
	ProgrammeTDO dossierBeneficiare = null;
	ProgrammeTDO besoinBeneficiare = null;
	ProgrammeTDO vad = null;
	ProgrammeTDO reference = null;
	ProgrammeTDO groupe = null;

	double total = 0;
	double nouveau_10_14 = 0;
	double nouveau_15_19 = 0;
	double ancien_10_14 = 0;
	double ancien_15_19 = 0;

	int nouveau_10_14Scolaire = 0;
	int nouveau_15_19Scolaire = 0;
	int ancien_10_14Scolaire = 0;
	int ancien_15_19Scolaire = 0;

	int nouveau_10_14ExtraScolaire = 0;
	int nouveau_15_19ExtraScolaire = 0;
	int ancien_10_14ExtraScolaire = 0;
	int ancien_15_19ExtraScolaire = 0;

	int nouveau_10_14ScolaireActif = 0;
	int nouveau_15_19ScolaireActif = 0;
	int ancien_10_14ScolaireActif = 0;
	int ancien_15_19ScolaireActif = 0;

	int nouveau_10_14ExtraScolaireActif = 0;
	int nouveau_15_19ExtraScolaireActif = 0;
	int ancien_10_14ExtraScolaireActif = 0;
	int ancien_15_19ExtraScolaireActif = 0;

	int nouveau_10_14IssusPop = 0;
	int nouveau_15_19IssusPop = 0;
	int ancien_10_14IssusPop = 0;
	int ancien_15_19IssusPop = 0;

	int nouveau_10_14RefPsy = 0;
	int nouveau_15_19RefPsy = 0;
	int ancien_10_14RefPsy = 0;
	int ancien_15_19RefPsy = 0;

	int nouveau_10_14Sinovoyu = 0;
	int nouveau_15_19Sinovoyu = 0;
	int ancien_10_14Sinovoyu = 0;
	int ancien_15_19Sinovoyu = 0;

	int nouveau_10_14ParEnf = 0;
	int nouveau_15_19ParEnf = 0;
	int ancien_10_14ParEnf = 0;
	int ancien_15_19ParEnf = 0;
	int totalParEnf = 0;
	
	int nouveau_10_14VAD = 0;
	int nouveau_15_19VAD = 0;
	int ancien_10_14VAD = 0;
	int ancien_15_19VAD = 0;

	int nouveau_10_14BesEdu = 0;
	int nouveau_15_19BesEdu = 0;
	int ancien_10_14BesEdu = 0;
	int ancien_15_19BesEdu = 0;

	int nouveau_10_14SoutAlphab = 0;
	int nouveau_15_19SoutAlphab = 0;
	int ancien_10_14SoutAlphab = 0;
	int ancien_15_19SoutAlphab = 0;

	int nouveau_10_14UtilPres = 0;
	int nouveau_15_19UtilPres = 0;
	int ancien_10_14UtilPres = 0;
	int ancien_15_19UtilPres = 0;
	
	int nouveau_10_14QuantPres = 0;
	int nouveau_15_19QuantPres = 0;
	int ancien_10_14QuantPres = 0;
	int ancien_15_19QuantPres = 0;
	
	int nouveau_10_14RefPres = 0;
	int nouveau_15_19RefPres = 0;
	int ancien_10_14RefPres = 0;
	int ancien_15_19RefPres = 0;
	
	int nouveau_10_14ContreRefPres = 0;
	int nouveau_15_19ContreRefPres = 0;
	int ancien_10_14ContreRefPres = 0;
	int ancien_15_19ContreRefPres = 0;
	
	int nouveau_10_14RefPF = 0;
	int nouveau_15_19RefPF = 0;
	int ancien_10_14RefPF = 0;
	int ancien_15_19RefPF = 0;
	
	int nouveau_10_14RefDep = 0;
	int nouveau_15_19RefDep = 0;
	int ancien_10_14RefDep = 0;
	int ancien_15_19RefDep = 0;
	
	int nouveau_10_14ContreRefDepistage = 0;
	int nouveau_15_19ContreRefDepistage = 0;
	int ancien_10_14ContreRefDepistage = 0;
	int ancien_15_19ContreRefDepistage = 0;
	
	int nouveau_10_14VBGrecuPsyJuri = 0;
	int nouveau_15_19VBGrecuPsyJuri = 0;
	int ancien_10_14VBGrecuPsyJuri = 0;
	int ancien_15_19VBGrecuPsyJuri = 0;
	
	int nouveau_10_14VBGrefMedic = 0;
	int nouveau_15_19VBGrefMedic = 0;
	int ancien_10_14VBGrefMedic = 0;
	int ancien_15_19VBGrefMedic = 0;
	
	int nouveau_10_14VBGsoutMedic = 0;
	int nouveau_15_19VBGsoutMedic = 0;
	int ancien_10_14VBGsoutMedic = 0;
	int ancien_15_19VBGsoutMedic = 0;
	
	int nouveau_10_14ProtectSoc = 0;
	int nouveau_15_19ProtectSoc = 0;
	int ancien_10_14ProtectSoc = 0;
	int ancien_15_19ProtectSoc = 0;
	
	int nouveau_10_14PartAVEC = 0;
	int nouveau_15_19PartAVEC = 0;
	int ancien_10_14PartAVEC = 0;
	int ancien_15_19PartAVEC = 0;
	
	int nouveau_10_14ParentPartAVEC = 0;
	int nouveau_15_19ParentPartAVEC = 0;
	int ancien_10_14ParentPartAVEC = 0;
	int ancien_15_19ParentPartAVEC = 0;
	int totalParentPartAVEC = 0;
	
	int nouveau_10_14SoutRenfEco = 0;
	int nouveau_15_19SoutRenfEco = 0;
	int ancien_10_14SoutRenfEco = 0;
	int ancien_15_19SoutRenfEco = 0;
	
	int nouveau_10_14Actives = 0;
	int nouveau_15_19Actives = 0;
	int ancien_10_14Actives = 0;
	int ancien_15_19Actives = 0;

	int nouveau_10_14Inactives = 0;
	int nouveau_15_19Inactives = 0;
	int ancien_10_14Inactives = 0;
	int ancien_15_19Inactives = 0;
	
	// autour de 5 intervention primaires
	int nouveau_10_14Mois5 = 0;
	int nouveau_15_19Mois5 = 0;
	int ancien_10_14Mois5 = 0;
	int ancien_15_19Mois5 = 0;

	int nouveau_10_14_5 = 0;
	int nouveau_15_19_5 = 0;
	int ancien_10_14_5 = 0;
	int ancien_15_19_5 = 0;

	int nouveau_10_14Plus5 = 0;
	int nouveau_15_19Plus5 = 0;
	int ancien_10_14Plus5 = 0;
	int ancien_15_19Plus5 = 0;

	// autour de 6 intervention primaires
	int nouveau_10_14Mois6 = 0;
	int nouveau_15_19Mois6 = 0;
	int ancien_10_14Mois6 = 0;
	int ancien_15_19Mois6 = 0;

	int nouveau_10_14_6 = 0;
	int nouveau_15_19_6 = 0;
	int ancien_10_14_6 = 0;
	int ancien_15_19_6 = 0;

	int nouveau_10_14Plus6 = 0;
	int nouveau_15_19Plus6 = 0;
	int ancien_10_14Plus6 = 0;
	int ancien_15_19Plus6 = 0;

	Element age_enrol;
	Element porteEntree;
	Element refPsychoElement;
	Element sinovoyuElement;
	Element thematiqueElement;
	Element nomPrenomAutreCibleElement;
	Element idDreamsElement;
	Element soutienEducatifElement;
	Element alphabetisationElement;
	Element utilisationPreservatifsElement;
	Element materielQuantiteElement;
	Element trancheAgeElement;  
	Element referencePreservatifs;
	Element motifrefElement;
	Element datecontRefElement;
	Element agebenefElement;
	Element referencVersPFElement;
	Element referenceServicesVIHElement;
	Element referenceJuridiqueElement;
	Element referencePsychoSocialElement;
	Element referenceMedicalElement;
	Element fraisMedicauxElement;
	Element fraisDocumentElement;
	Element AVECelement;
	Element businessPlusElement;
	
	Element nbreBenefEnrole;
	Element nbreFilleRecuPlus6InterventionsPrimaire;
	Element nbreFilleRecu6InterventionsPrimaire;
	Element nbreFilleRecuMoins6InterventionsPrimaire;
	Element nbreFilleRecuPlus5InterventionsPrimaire;
	Element nbreFilleRecu5InterventionsPrimaire;
	Element nbreFilleRecuMoins5InterventionsPrimaire;
	Element nbreFilleActives;
	Element nbreFilleInactives;
	Element nbreFillebeneficieSoutienRenforcementEconomique;
	Element nbreParentParticipeAVEC;
	Element nbreFille_18_19_ParticipeAVEC;
	Element nbreFillebeneficieProtectionSociale;
	Element nbreFilleMalnutrieRefere;
	Element nbreFilleVBGbeneficieSoutienMedicale;
	Element nbreFilleVBGrefereMedicale;
	Element nbreFilleVBGrecuServicePsychoSocialJuridique;
	// Element nbreContreReferenceDepistage;
	Element nbreFilleRefereDepistage;
	Element nbreFilleReferePF;
	Element nbreContreReferencePreservatif;
	Element nbreFilleReferePreservatif;
	Element nbrePreservatifDistribues;
	Element nbreFilleFormePortPreservatif;
	Element nbreFilleBeneficieSoutienAlphabetisation;
	Element nbreFilleBesoinEducation;
	Element nbreFilleVAD;
	Element nbreParentCommunicationParentEnfant;
	Element nbreBeneficiaireCommunicationParentEnfant;
	Element nbreFilleReferePsychoSocial;
	Element nbreFilleIssusPopulation;
	Element nbreBenefEnroleExtraScolaireActifs;
	Element nbreBenefEnroleScolaireActifs;
	Element nbreBenefEnroleExtraScolaire;
	Element nbreBenefEnroleScolaire;
	
	Element proportionFille_recu_moins_5_interventionsPrimaire;
	Element proportionFille_recu_5_interventionsPrimaire;
	Element proportionFille_recu_plus_5_interventionsPrimaire;
	Element ProportionFille_recu_moins_6_interventionsPrimaire;
	Element ProportionFille_recu_6_interventionsPrimaire;
	Element ProportionFille_recu_plus_6_interventionsPrimaire;
	
	
	boolean execution = false;
	String HeureDebutExecution = null;
	String HeureFinExecution = null;
	ResultatRequete resultatRequete = new ResultatRequete();
	// private final DateTimeFormatter formatter = new DateTimeFormatter("dd-MM-yyyy
	// HH:mm:ss");
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

	List<DataInstance> dataInstances = new ArrayList<DataInstance>();
	List<DataValue> dataValues = new ArrayList<DataValue>();
	List<Instance> instances = new ArrayList<Instance>();
	List<List<DataValue>> lesDossiersBeneficiaire = new ArrayList<List<DataValue>>();
	List<List<DataValue>> actuLesDossiersBeneficiaire = new ArrayList<List<DataValue>>();
	List<List<DataValue>> preLesDossiersBeneficiaire = new ArrayList<List<DataValue>>();
	List<List<DataValue>> lesActivitesGroupes = new ArrayList<List<DataValue>>();

	public ResultatRequete genereRapport(String action) {
		System.out.println("execution = " + execution);
		if (action.equals("execute") && !execution) {
			resultatRequete.setStatus("enCours");
			execution = true;
			HeureDebutExecution = LocalDateTime.now().format(formatter);
			chargeElement();
			chargeIndicatorElement();
			chargeProgramme();
			executionPeriodeMois();
			executionPeriodeTrimestre();
			HeureFinExecution = LocalDateTime.now().format(formatter);
			System.out.println("debut HeureDebutExecution = " + HeureFinExecution);
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

	private void initialiseVariable(){
		total = 0;
		nouveau_10_14 = 0;
		nouveau_15_19 = 0;
		ancien_10_14 = 0;
		ancien_15_19 = 0;

		nouveau_10_14Scolaire = 0;
		nouveau_15_19Scolaire = 0;
		ancien_10_14Scolaire = 0;
		ancien_15_19Scolaire = 0;

		nouveau_10_14ExtraScolaire = 0;
		nouveau_15_19ExtraScolaire = 0;
		ancien_10_14ExtraScolaire = 0;
		ancien_15_19ExtraScolaire = 0;

		nouveau_10_14ScolaireActif = 0;
		nouveau_15_19ScolaireActif = 0;
		ancien_10_14ScolaireActif = 0;
		ancien_15_19ScolaireActif = 0;

		nouveau_10_14ExtraScolaireActif = 0;
		nouveau_15_19ExtraScolaireActif = 0;
		ancien_10_14ExtraScolaireActif = 0;
		ancien_15_19ExtraScolaireActif = 0;

		nouveau_10_14IssusPop = 0;
		nouveau_15_19IssusPop = 0;
		ancien_10_14IssusPop = 0;
		ancien_15_19IssusPop = 0;

		nouveau_10_14RefPsy = 0;
		nouveau_15_19RefPsy = 0;
		ancien_10_14RefPsy = 0;
		ancien_15_19RefPsy = 0;

		nouveau_10_14Sinovoyu = 0;
		nouveau_15_19Sinovoyu = 0;
		ancien_10_14Sinovoyu = 0;
		ancien_15_19Sinovoyu = 0;

		nouveau_10_14ParEnf = 0;
		nouveau_15_19ParEnf = 0;
		ancien_10_14ParEnf = 0;
		ancien_15_19ParEnf = 0;
		totalParEnf = 0;

		nouveau_10_14VAD = 0;
		nouveau_15_19VAD = 0;
		ancien_10_14VAD = 0;
		ancien_15_19VAD = 0;

		nouveau_10_14BesEdu = 0;
		nouveau_15_19BesEdu = 0;
		ancien_10_14BesEdu = 0;
		ancien_15_19BesEdu = 0;

		nouveau_10_14SoutAlphab = 0;
		nouveau_15_19SoutAlphab = 0;
		ancien_10_14SoutAlphab = 0;
		ancien_15_19SoutAlphab = 0;

		nouveau_10_14UtilPres = 0;
		nouveau_15_19UtilPres = 0;
		ancien_10_14UtilPres = 0;
		ancien_15_19UtilPres = 0;

		nouveau_10_14QuantPres = 0;
		nouveau_15_19QuantPres = 0;
		ancien_10_14QuantPres = 0;
		ancien_15_19QuantPres = 0;

		nouveau_10_14RefPres = 0;
		nouveau_15_19RefPres = 0;
		ancien_10_14RefPres = 0;
		ancien_15_19RefPres = 0;

		nouveau_10_14ContreRefPres = 0;
		nouveau_15_19ContreRefPres = 0;
		ancien_10_14ContreRefPres = 0;
		ancien_15_19ContreRefPres = 0;

		nouveau_10_14RefPF = 0;
		nouveau_15_19RefPF = 0;
		ancien_10_14RefPF = 0;
		ancien_15_19RefPF = 0;

		nouveau_10_14RefDep = 0;
		nouveau_15_19RefDep = 0;
		ancien_10_14RefDep = 0;
		ancien_15_19RefDep = 0;

		nouveau_10_14ContreRefDepistage = 0;
		nouveau_15_19ContreRefDepistage = 0;
		ancien_10_14ContreRefDepistage = 0;
		ancien_15_19ContreRefDepistage = 0;

		nouveau_10_14VBGrecuPsyJuri = 0;
		nouveau_15_19VBGrecuPsyJuri = 0;
		ancien_10_14VBGrecuPsyJuri = 0;
		ancien_15_19VBGrecuPsyJuri = 0;

		nouveau_10_14VBGrefMedic = 0;
		nouveau_15_19VBGrefMedic = 0;
		ancien_10_14VBGrefMedic = 0;
		ancien_15_19VBGrefMedic = 0;

		nouveau_10_14VBGsoutMedic = 0;
		nouveau_15_19VBGsoutMedic = 0;
		ancien_10_14VBGsoutMedic = 0;
		ancien_15_19VBGsoutMedic = 0;

		nouveau_10_14ProtectSoc = 0;
		nouveau_15_19ProtectSoc = 0;
		ancien_10_14ProtectSoc = 0;
		ancien_15_19ProtectSoc = 0;

		nouveau_10_14PartAVEC = 0;
		nouveau_15_19PartAVEC = 0;
		ancien_10_14PartAVEC = 0;
		ancien_15_19PartAVEC = 0;

		nouveau_10_14ParentPartAVEC = 0;
		nouveau_15_19ParentPartAVEC = 0;
		ancien_10_14ParentPartAVEC = 0;
		ancien_15_19ParentPartAVEC = 0;
		totalParentPartAVEC = 0;

		nouveau_10_14SoutRenfEco = 0;
		nouveau_15_19SoutRenfEco = 0;
		ancien_10_14SoutRenfEco = 0;
		ancien_15_19SoutRenfEco = 0;

		nouveau_10_14Actives = 0;
		nouveau_15_19Actives = 0;
		ancien_10_14Actives = 0;
		ancien_15_19Actives = 0;

		nouveau_10_14Inactives = 0;
		nouveau_15_19Inactives = 0;
		ancien_10_14Inactives = 0;
		ancien_15_19Inactives = 0;

		// autour de 5 intervention primaires
		nouveau_10_14Mois5 = 0;
		nouveau_15_19Mois5 = 0;
		ancien_10_14Mois5 = 0;
		ancien_15_19Mois5 = 0;

		nouveau_10_14_5 = 0;
		nouveau_15_19_5 = 0;
		ancien_10_14_5 = 0;
		ancien_15_19_5 = 0;

		nouveau_10_14Plus5 = 0;
		nouveau_15_19Plus5 = 0;
		ancien_10_14Plus5 = 0;
		ancien_15_19Plus5 = 0;

		// autour de 6 intervention primaires
		nouveau_10_14Mois6 = 0;
		nouveau_15_19Mois6 = 0;
		ancien_10_14Mois6 = 0;
		ancien_15_19Mois6 = 0;

		nouveau_10_14_6 = 0;
		nouveau_15_19_6 = 0;
		ancien_10_14_6 = 0;
		ancien_15_19_6 = 0;

		nouveau_10_14Plus6 = 0;
		nouveau_15_19Plus6 = 0;
		ancien_10_14Plus6 = 0;
		ancien_15_19Plus6 = 0;
	}
	
	private void chargeElement() {
		age_enrol = ielement.getOneElmentByCode("age_enrol");
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
		businessPlusElement = ielement.getOneElmentByCode("businessPlus");
		
	}

	private void chargeIndicatorElement() {
		
		nbreBenefEnrole = ielement
		.getOneElmentByCode("nbreBenefEnrole");
		
		nbreFilleRecuPlus6InterventionsPrimaire = ielement
				.getOneElmentByCode("nbreFilleRecuPlus6InterventionsPrimaire");

		nbreFilleRecu6InterventionsPrimaire = ielement.getOneElmentByCode("nbreFilleRecu6InterventionsPrimaire");

		nbreFilleRecuMoins6InterventionsPrimaire = ielement
				.getOneElmentByCode("nbreFilleRecuMoins6InterventionsPrimaire");

		nbreFilleRecuPlus5InterventionsPrimaire = ielement
				.getOneElmentByCode("nbreFilleRecuPlus5InterventionsPrimaire");

		nbreFilleRecu5InterventionsPrimaire = ielement.getOneElmentByCode("nbreFilleRecu5InterventionsPrimaire");

		nbreFilleRecuMoins5InterventionsPrimaire = ielement
				.getOneElmentByCode("nbreFilleRecuMoins5InterventionsPrimaire");

		nbreFilleActives = ielement.getOneElmentByCode("nbreFilleActives");

		nbreFilleInactives = ielement.getOneElmentByCode("nbreFilleInactives");

		nbreFillebeneficieSoutienRenforcementEconomique = ielement
				.getOneElmentByCode("nbreFillebeneficieSoutienRenforcementEconomique");

		nbreParentParticipeAVEC = ielement.getOneElmentByCode("nbreParentParticipeAVEC");

		nbreFille_18_19_ParticipeAVEC = ielement.getOneElmentByCode("nbreFille_18_19_ParticipeAVEC");

		nbreFillebeneficieProtectionSociale = ielement.getOneElmentByCode("nbreFillebeneficieProtectionSociale");

		// nbreFilleMalnutrieRefere =
		// ielement.getOneElmentByCode("nbreFilleMalnutrieRefere");

		nbreFilleVBGbeneficieSoutienMedicale = ielement.getOneElmentByCode("nbreFilleVBGbeneficieSoutienMedicale");
		nbreFilleVBGrefereMedicale = ielement.getOneElmentByCode("nbreFilleVBGrefereMedicale");

		nbreFilleVBGrecuServicePsychoSocialJuridique = ielement
				.getOneElmentByCode("nbreFilleVBGrecuServicePsychoSocialJuridique");

		// nbreContreReferenceDepistage =
		// ielement.getOneElmentByCode("nbreContreReferenceDepistage");

		nbreFilleRefereDepistage = ielement.getOneElmentByCode("nbreFilleRefereDepistage");

		nbreFilleReferePF = ielement.getOneElmentByCode("nbreFilleReferePF");

		nbreContreReferencePreservatif = ielement.getOneElmentByCode("nbreContreReferencePreservatif");

		nbreFilleReferePreservatif = ielement.getOneElmentByCode("nbreFilleReferePreservatif");

		nbrePreservatifDistribues = ielement.getOneElmentByCode("nbrePreservatifDistribues");

		nbreFilleFormePortPreservatif = ielement.getOneElmentByCode("nbreFilleFormePortPreservatif");

		nbreFilleBeneficieSoutienAlphabetisation = ielement
				.getOneElmentByCode("nbreFilleBeneficieSoutienAlphabetisation");

		nbreFilleBesoinEducation = ielement.getOneElmentByCode("nbreFilleBesoinEducation");

		nbreFilleVAD = ielement.getOneElmentByCode("nbreFilleVAD");

		nbreParentCommunicationParentEnfant = ielement.getOneElmentByCode("nbreParentCommunicationParentEnfant");

		nbreBeneficiaireCommunicationParentEnfant = ielement
				.getOneElmentByCode("nbreBeneficiaireCommunicationParentEnfant");

		nbreFilleReferePsychoSocial = ielement.getOneElmentByCode("nbreFilleReferePsychoSocial");

		nbreFilleIssusPopulation = ielement.getOneElmentByCode("nbreFilleIssusPopulation");

		nbreBenefEnroleExtraScolaireActifs = ielement.getOneElmentByCode("nbreBenefEnroleExtraScolaireActifs");

		nbreBenefEnroleScolaireActifs = ielement.getOneElmentByCode("nbreBenefEnroleScolaireActifs");

		nbreBenefEnroleExtraScolaire = ielement.getOneElmentByCode("nbreBenefEnroleExtraScolaire");

		nbreBenefEnroleScolaire = ielement.getOneElmentByCode("nbreBenefEnroleScolaire");
		
		proportionFille_recu_moins_5_interventionsPrimaire = ielement.getOneElmentByCode("proportionFille_recu_moins_5_interventionsPrimaire");
		
		proportionFille_recu_5_interventionsPrimaire = ielement.getOneElmentByCode("proportionFille_recu_5_interventionsPrimaire");
		
		proportionFille_recu_plus_5_interventionsPrimaire = ielement.getOneElmentByCode("proportionFille_recu_plus_5_interventionsPrimaire");
		
		ProportionFille_recu_moins_6_interventionsPrimaire = ielement.getOneElmentByCode("ProportionFille_recu_moins_6_interventionsPrimaire");
		
		ProportionFille_recu_6_interventionsPrimaire = ielement.getOneElmentByCode("ProportionFille_recu_6_interventionsPrimaire");
		
		ProportionFille_recu_plus_6_interventionsPrimaire = ielement.getOneElmentByCode("ProportionFille_recu_plus_6_interventionsPrimaire");
		
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

		for (int j = 0; j < DossierBeneficiaire.length; j++) {
			elementsDossierBeneficiaire.add(ielement.getOneElmentByCode(DossierBeneficiaire[j]));
		}

		for (int j = 0; j < InterventionDossierBeneficiaire.length; j++) {
			elementsIntervention.add(ielement.getOneElmentByCode(InterventionDossierBeneficiaire[j]));
		}
		for (int j = 0; j < InterventionPopCible.length; j++) {
			elementsInterventionPopCible.add(ielement.getOneElmentByCode(InterventionPopCible[j]));
		}
		for (int j = 0; j < InterventionPrimaireDossierBeneficiaire.length; j++) {
			elementsInterventionPrimaire.add(ielement.getOneElmentByCode(InterventionPrimaireDossierBeneficiaire[j]));
		}

		for (int j = 0; j < elementsDossierBeneficiaire.size(); j++) {
			elementsUiDossierBeneficiaire.add(elementsDossierBeneficiaire.get(j).getUid());
		}
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

	private void executionPeriodeTrimestre() {
		// System.err.println("execution Periode Trimestre = ");

		LocalDate todaydate = LocalDate.now();
		int intTodaydate = todaydate.getMonthValue();
		;
		if (intTodaydate <= 3 && intTodaydate >= 1) {
			dateDebuts = todaydate.getYear() + "-01-01";
			dateFins = todaydate.getYear() + "-03-31";
		} else if (intTodaydate >= 4 && intTodaydate <= 6) {
			dateDebuts = todaydate.getYear() + "-04-01";
			dateFins = todaydate.getYear() + "-06-30";
		} else if (intTodaydate >= 7 && intTodaydate <= 9) {
			dateDebuts = todaydate.getYear() + "-07-01";
			dateFins = todaydate.getYear() + "-09-30";
		} else if (intTodaydate >= 10 && intTodaydate <= 12) {
			dateDebuts = todaydate.getYear() + "-10-01";
			dateFins = todaydate.getYear() + "-12-31";
		}
		int nombre = 0;
		String commence = dateDebuts;
		while (!dateDebuts.equals("2018-01-01")) {
			chargeTrimestre(nombre, commence);
			nombre += 3;

		}
	}

	private void chargeTrimestre(int nbreMois, String commence) {

		LocalDate todaydate = LocalDate.parse(commence, DateTimeFormatter.ISO_LOCAL_DATE);
		LocalDate newDaydate = todaydate.minusMonths(nbreMois);
		LocalDate lastDaydate = newDaydate.plusMonths(2);

		dateDebuts = newDaydate.with(TemporalAdjusters.firstDayOfMonth()).toString();
		dateFins = lastDaydate.with(TemporalAdjusters.lastDayOfMonth()).toString();

		laPeriode = newDaydate.getYear() + "T" + newDaydate.get(IsoFields.QUARTER_OF_YEAR);
		System.err.println("dateDebuts = " + dateDebuts + " // dateFins = " + dateFins);
		// System.out.println("dateDebuts = " + dateDebuts);
		// System.out.println("dateFins = " + dateFins);
		// System.out.println("laPeriode = " + laPeriode);
		// System.out.println();
		gestionOrganisation();
	}

	private void executionPeriodeMois() {
		// System.err.println("execution Periode Mois = ");
		int nombre = 0;
		while (!dateDebuts.equals("2018-01-01")) {
			chargeMois(nombre);
			nombre++;

		}
	}

	private void chargeMois(int nbreMois) {
		LocalDate newDaydate = null;
		LocalDate todaydate = LocalDate.now();
		newDaydate = todaydate.minusMonths(nbreMois);
		dateDebuts = newDaydate.with(TemporalAdjusters.firstDayOfMonth()).toString();
		dateFins = newDaydate.with(TemporalAdjusters.lastDayOfMonth()).toString();
		String ladate = newDaydate.toString();
		laPeriode = ladate.substring(0, 4) + ladate.substring(5, 7);

		System.err.println("dateDebuts = " + dateDebuts + " dateFins = " + dateFins);
		// System.out.println("dateDebuts = " + dateDebuts);
		// System.out.println("dateFins = " + dateFins);
		// System.out.println("laPeriode = " + laPeriode);
		// System.out.println();
		gestionOrganisation();
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
			System.err.println("organisationSelect = "+organisationSelect.getName()+" // level = "+organisationSelect.getLevel()+" // dateDebuts = " + dateDebuts + " // dateFins = " + dateFins);
			chargeIndicateur();
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

	private void enregistreRapport(Element elementCode) {
		List<Rapport> rapports = new ArrayList<Rapport>();

		//Element  = ielement.getOneElmentByCode(elementCode);

		if (!this.organisationSelect.getChildrens().isEmpty()) {

			List<String> childrens = new ArrayList<String>();
			for (int comp = 0; comp < this.organisationSelect.getChildrens().size(); comp++) {
				childrens.add(this.organisationSelect.getChildrens().get(comp).getId());
			}

			rapports = irapport.getRapportOptionRapportCodeNull(childrens, elementCode.getCode(), laPeriode);
			if (!rapports.isEmpty()) {
				for (int rap = 0; rap < rapports.size(); rap++) {
					if (rapports.get(rap).getOption() == null) {
						total += rapports.get(rap).getValeurs();
					} else {
						if (rapports.get(rap).getOption().getCode().equals("nouveau_10_14")) {
							nouveau_10_14 += rapports.get(rap).getValeurs();
						}
						if (rapports.get(rap).getOption().getCode().equals("nouveau_15_19")) {
							nouveau_15_19 += rapports.get(rap).getValeurs();
						}
						if (rapports.get(rap).getOption().getCode().equals("ancien_10_14")) {
							ancien_10_14 += rapports.get(rap).getValeurs();
						}
						if (rapports.get(rap).getOption().getCode().equals("ancien_15_19")) {
							ancien_15_19 += rapports.get(rap).getValeurs();
						}
					}
				}
			}

		}

		rapports = irapport.getRapportOptionRapportCodeNull(this.organisationSelect.getId(), elementCode.getCode(), laPeriode);
		if (!rapports.isEmpty()) {
			rapports = updateRapport(rapports);
		} else {
			Organisation organisation = getOrganisation(this.organisationSelect.getId());
			rapports = newRapport(organisation, elementCode);
		}

		irapport.saveRapport(rapports);

	}

	private List<Rapport> updateRapport(List<Rapport> rapports) {

		for (int rap = 0; rap < rapports.size(); rap++) {
			if (rapports.get(rap).getOption() == null) {
				rapports.get(rap).setValeurs(total);
			} else {
				if (rapports.get(rap).getOption().getCode().equals("nouveau_10_14")) {
					rapports.get(rap).setValeurs(nouveau_10_14);
				}
				if (rapports.get(rap).getOption().getCode().equals("nouveau_15_19")) {
					rapports.get(rap).setValeurs(nouveau_15_19);
				}
				if (rapports.get(rap).getOption().getCode().equals("ancien_10_14")) {
					rapports.get(rap).setValeurs(ancien_10_14);
				}
				if (rapports.get(rap).getOption().getCode().equals("ancien_15_19")) {
					rapports.get(rap).setValeurs(ancien_15_19);
				}
			}
		}

		return rapports;
	}

	private List<Rapport> newRapport(Organisation organisation, Element element) {
		List<Rapport> rapports = new ArrayList<Rapport>();
		//Element element = ielement.getOneElmentByCode(elementCode);
		
		for (int opt = 0; opt < element.getEnsembleOption().getOptions().size(); opt++) {

			if (element.getEnsembleOption().getOptions().get(opt).getCode().equals("nouveau_10_14")) {
				Rapport _nouveau_10_14 = new Rapport();
				_nouveau_10_14.setElement(element);
				_nouveau_10_14.setOption(element.getEnsembleOption().getOptions().get(opt));
				_nouveau_10_14.setOrganisation(organisation);
				_nouveau_10_14.setPeriode(laPeriode);
				_nouveau_10_14.setValeurs(nouveau_10_14);
				rapports.add(_nouveau_10_14);
			}
			if (element.getEnsembleOption().getOptions().get(opt).getCode().equals("nouveau_15_19")) {
				Rapport _nouveau_15_19 = new Rapport();
				_nouveau_15_19.setElement(element);
				_nouveau_15_19.setOption(element.getEnsembleOption().getOptions().get(opt));
				_nouveau_15_19.setOrganisation(organisation);
				_nouveau_15_19.setPeriode(laPeriode);
				_nouveau_15_19.setValeurs(nouveau_15_19);
				rapports.add(_nouveau_15_19);
			}
			if (element.getEnsembleOption().getOptions().get(opt).getCode().equals("ancien_10_14")) {
				Rapport _ancien_10_14 = new Rapport();
				_ancien_10_14.setElement(element);
				_ancien_10_14.setOption(element.getEnsembleOption().getOptions().get(opt));
				_ancien_10_14.setOrganisation(organisation);
				_ancien_10_14.setPeriode(laPeriode);
				_ancien_10_14.setValeurs(ancien_10_14);
				rapports.add(_ancien_10_14);
			}
			if (element.getEnsembleOption().getOptions().get(opt).getCode().equals("ancien_15_19")) {
				Rapport _ancien_15_19 = new Rapport();
				_ancien_15_19.setElement(element);
				_ancien_15_19.setOption(element.getEnsembleOption().getOptions().get(opt));
				_ancien_15_19.setOrganisation(organisation);
				_ancien_15_19.setPeriode(laPeriode);
				_ancien_15_19.setValeurs(ancien_15_19);
				rapports.add(_ancien_15_19);
			}

		}

		Rapport _total = new Rapport();
		_total.setElement(element);
		_total.setOrganisation(organisation);
		_total.setPeriode(laPeriode);
		_total.setValeurs(total);
		rapports.add(_total);

		return rapports;
	}

	/*
	 * private void enregistreRapport(String element) { RapportTDO rapportTDO = new
	 * RapportTDO(); String combinaison = null;
	 * rapportTDO.setOrganisation(this.organisationSelect.getId());
	 * rapportTDO.setPeriode(laPeriode); /* nouveau = 0; ancien = 0; total = 0;
	 * nouveau_10_14 = 0; nouveau_15_19 = 0; ancien_10_14 = 0; ancien_15_19 = 0;
	 *
	 * 
	 * if (!this.organisationSelect.getChildrens().isEmpty()) { for (int comp = 0;
	 * comp < this.organisationSelect.getChildrens().size(); comp++) {
	 * List<RapportTDO> rapportTDOs = new ArrayList<RapportTDO>(); rapportTDOs =
	 * irapport.getRapportOptionRapportTDOCodeNull(
	 * this.organisationSelect.getChildrens().get(comp).getId(), element,
	 * laPeriode); if (!rapportTDOs.isEmpty()) { for (int rap = 0; rap <
	 * rapportTDOs.size(); rap++) { combinaison = element + ".nouveau_10_14"; if
	 * (rapportTDOs.get(rap).getElement().equals(combinaison)) { nouveau_10_14 +=
	 * rapportTDOs.get(rap).getValeurs(); } combinaison = element +
	 * ".nouveau_15_19"; if (rapportTDOs.get(rap).getElement().equals(combinaison))
	 * { nouveau_15_19 += rapportTDOs.get(rap).getValeurs(); } combinaison = element
	 * + ".ancien_10_14"; if (rapportTDOs.get(rap).getElement().equals(combinaison))
	 * { ancien_10_14 += rapportTDOs.get(rap).getValeurs(); } combinaison = element
	 * + ".ancien_15_19"; if (rapportTDOs.get(rap).getElement().equals(combinaison))
	 * { ancien_15_19 += rapportTDOs.get(rap).getValeurs(); } combinaison = element;
	 * if (rapportTDOs.get(rap).getElement().equals(combinaison)) { total +=
	 * rapportTDOs.get(rap).getValeurs(); } } } } }
	 * 
	 * // element - Nouveau, 10-14 ans combinaison = element + ".nouveau_10_14";
	 * rapportTDO.setElement(combinaison); rapportTDO.setValeurs(nouveau_10_14);
	 * irapport.saveRapportTDO(rapportTDO);
	 * 
	 * // element - Nouveau, 15-19 ans combinaison = element + ".nouveau_15_19";
	 * rapportTDO.setElement(combinaison); rapportTDO.setValeurs(nouveau_15_19);
	 * irapport.saveRapportTDO(rapportTDO);
	 * 
	 * // element - Ancien, 10-14 ans combinaison = element + ".ancien_10_14";
	 * rapportTDO.setElement(combinaison); rapportTDO.setValeurs(ancien_10_14);
	 * irapport.saveRapportTDO(rapportTDO);
	 * 
	 * // element - Ancien, 15-19 ans combinaison = element + ".ancien_15_19";
	 * rapportTDO.setElement(combinaison); rapportTDO.setValeurs(ancien_15_19);
	 * irapport.saveRapportTDO(rapportTDO);
	 * 
	 * // element (total) combinaison = element; rapportTDO.setElement(combinaison);
	 * rapportTDO.setValeurs(total); irapport.saveRapportTDO(rapportTDO);
	 * 
	 * }
	 */

	private void chargeIndicateur() {
		initialiseVariable();
		nbreBenefEnrole();
		getDossierBenef();
		gestParentingAVEC();
		nbreFilleVAD();
		nbrePreservatifDistribues();
		nbreContreReference();
		
		
		
		constituerRapport();
		gestProportion();

		/* nbreBenefEnroleScolaireExtraScolaire();
		 * nbreBenefEnroleScolaireExtraScolaireActifs(); nbreFilleIssusPopulation();
		 * nbreFilleReferePsychoSocial(); nbreBeneficiaireCommunicationParentEnfant();
		 * nbreParentCommunicationParentEnfant(); nbreFilleVAD();
		 * nbreFilleBesoinEducation(); nbreFilleBeneficieSoutienAlphabetisation();
		 * nbreFilleFormePortPreservatif(); nbrePreservatifDistribues();
		 * nbreFilleReferePreservatif(); nbreContreReferencePreservatif();
		 * nbreFilleReferePF(); nbreFilleRefereDepistage();
		 * nbreContreReferenceDepistage();
		 * nbreFilleVBGrecuServicePsychoSocialJuridique(); nbreFilleVBGrefereMedicale();
		 * nbreFilleVBGbeneficieSoutienMedicale(); nbreFilleMalnutrieRefere();
		 * nbreFillebeneficieProtectionSociale(); nbreFille_18_19_ParticipeAVEC();
		 * nbreParentParticipeAVEC(); nbreFillebeneficieSoutienRenforcementEconomique();
		 * nbreFilleActivesEtInactives(); nbreInterventionsPrimaire();
		 */
	}

	private void constituerRapport() {

		nouveau_10_14 = nouveau_10_14Scolaire;
		nouveau_15_19 = nouveau_15_19Scolaire;
		ancien_10_14 = ancien_10_14Scolaire;
		ancien_15_19 = ancien_15_19Scolaire;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreBenefEnroleScolaire);

		nouveau_10_14 = nouveau_10_14ExtraScolaire;
		nouveau_15_19 = nouveau_15_19ExtraScolaire;
		ancien_10_14 = ancien_10_14ExtraScolaire;
		ancien_15_19 = ancien_15_19ExtraScolaire;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreBenefEnroleExtraScolaire);
		

		nouveau_10_14 = nouveau_10_14ScolaireActif;
		nouveau_15_19 = nouveau_15_19ScolaireActif;
		ancien_10_14 = ancien_10_14ScolaireActif;
		ancien_15_19 = ancien_15_19ScolaireActif;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreBenefEnroleScolaireActifs);

		nouveau_10_14 = nouveau_10_14ExtraScolaireActif;
		nouveau_15_19 = nouveau_15_19ExtraScolaireActif;
		ancien_10_14 = ancien_10_14ExtraScolaireActif;
		ancien_15_19 = ancien_15_19ExtraScolaireActif;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreBenefEnroleExtraScolaireActifs);
		
		nouveau_10_14 = nouveau_10_14IssusPop;
		nouveau_15_19 = nouveau_15_19IssusPop;
		ancien_10_14 = ancien_10_14IssusPop;
		ancien_15_19 = ancien_15_19IssusPop;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreFilleIssusPopulation);

		nouveau_10_14 = nouveau_10_14RefPsy;
		nouveau_15_19 = nouveau_15_19RefPsy;
		ancien_10_14 = ancien_10_14RefPsy;
		ancien_15_19 = ancien_15_19RefPsy;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreFilleReferePsychoSocial);

		nouveau_10_14 = nouveau_10_14Sinovoyu;
		nouveau_15_19 = nouveau_15_19Sinovoyu;
		ancien_10_14 = ancien_10_14Sinovoyu;
		ancien_15_19 = ancien_15_19Sinovoyu;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreBeneficiaireCommunicationParentEnfant);
		
		nouveau_10_14 = nouveau_10_14ParEnf;
		nouveau_15_19 = nouveau_15_19ParEnf;
		ancien_10_14 = ancien_10_14ParEnf;
		ancien_15_19 = ancien_15_19ParEnf;
		total = totalParEnf;
		enregistreRapport(nbreParentCommunicationParentEnfant);
		
		nouveau_10_14 = nouveau_10_14VAD;
		nouveau_15_19 = nouveau_15_19VAD;
		ancien_10_14 = ancien_10_14VAD;
		ancien_15_19 = ancien_15_19VAD;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreFilleVAD);
		
		nouveau_10_14 = nouveau_10_14BesEdu;
		nouveau_15_19 = nouveau_15_19BesEdu;
		ancien_10_14 = ancien_10_14BesEdu;
		ancien_15_19 = ancien_15_19BesEdu;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreFilleBesoinEducation);
		
		nouveau_10_14 = nouveau_10_14SoutAlphab;
		nouveau_15_19 = nouveau_15_19SoutAlphab;
		ancien_10_14 = ancien_10_14SoutAlphab;
		ancien_15_19 = ancien_15_19SoutAlphab;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreFilleBeneficieSoutienAlphabetisation);
		
		nouveau_10_14 = nouveau_10_14UtilPres;
		nouveau_15_19 = nouveau_15_19UtilPres;
		ancien_10_14 = ancien_10_14UtilPres;
		ancien_15_19 = ancien_15_19UtilPres;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreFilleFormePortPreservatif);
				
		nouveau_10_14 = nouveau_10_14QuantPres;
		nouveau_15_19 = nouveau_15_19QuantPres;
		ancien_10_14 = ancien_10_14QuantPres;
		ancien_15_19 = ancien_15_19QuantPres;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbrePreservatifDistribues);
				
		nouveau_10_14 = nouveau_10_14RefPres;
		nouveau_15_19 = nouveau_15_19RefPres;
		ancien_10_14 = ancien_10_14RefPres;
		ancien_15_19 = ancien_15_19RefPres;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreFilleReferePreservatif);
				
		nouveau_10_14 = nouveau_10_14ContreRefPres;
		nouveau_15_19 = nouveau_15_19ContreRefPres;
		ancien_10_14 = ancien_10_14ContreRefPres;
		ancien_15_19 = ancien_15_19ContreRefPres;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreContreReferencePreservatif);
				
		nouveau_10_14 = nouveau_10_14RefPF;
		nouveau_15_19 = nouveau_15_19RefPF;
		ancien_10_14 = ancien_10_14RefPF;
		ancien_15_19 = ancien_15_19RefPF;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreFilleReferePF);
				
		nouveau_10_14 = nouveau_10_14RefDep;
		nouveau_15_19 = nouveau_15_19RefDep;
		ancien_10_14 = ancien_10_14RefDep;
		ancien_15_19 = ancien_15_19RefDep;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreFilleRefereDepistage);
				
		nouveau_10_14 = nouveau_10_14ContreRefDepistage;
		nouveau_15_19 = nouveau_15_19ContreRefDepistage;
		ancien_10_14 = ancien_10_14ContreRefDepistage;
		ancien_15_19 = ancien_15_19ContreRefDepistage;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		//enregistreRapport(nbreContreReferenceDepistage);
				
		nouveau_10_14 = nouveau_10_14VBGrecuPsyJuri;
		nouveau_15_19 = nouveau_15_19VBGrecuPsyJuri;
		ancien_10_14 = ancien_10_14VBGrecuPsyJuri;
		ancien_15_19 = ancien_15_19VBGrecuPsyJuri;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreFilleVBGrecuServicePsychoSocialJuridique);
				
		nouveau_10_14 = nouveau_10_14VBGrefMedic;
		nouveau_15_19 = nouveau_15_19VBGrefMedic;
		ancien_10_14 = ancien_10_14VBGrefMedic;
		ancien_15_19 = ancien_15_19VBGrefMedic;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreFilleVBGrefereMedicale);
				
		nouveau_10_14 = nouveau_10_14VBGsoutMedic;
		nouveau_15_19 = nouveau_15_19VBGsoutMedic;
		ancien_10_14 = ancien_10_14VBGsoutMedic;
		ancien_15_19 = ancien_15_19VBGsoutMedic;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreFilleVBGbeneficieSoutienMedicale);
				
		/*nouveau_10_14 = ;
		nouveau_15_19 = ;
		ancien_10_14 = ;
		ancien_15_19 = ;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreFilleMalnutrieRefere);*/
		
		
		nouveau_10_14 = nouveau_10_14ProtectSoc;
		nouveau_15_19 = nouveau_15_19ProtectSoc;
		ancien_10_14 = ancien_10_14ProtectSoc;
		ancien_15_19 = ancien_15_19ProtectSoc;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreFillebeneficieProtectionSociale);
				
		nouveau_10_14 = nouveau_10_14PartAVEC;
		nouveau_15_19 = nouveau_15_19PartAVEC;
		ancien_10_14 = ancien_10_14PartAVEC;
		ancien_15_19 = ancien_15_19PartAVEC;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreFille_18_19_ParticipeAVEC);
				
		nouveau_10_14 = nouveau_10_14ParentPartAVEC;
		nouveau_15_19 = nouveau_15_19ParentPartAVEC;
		ancien_10_14 = ancien_10_14ParentPartAVEC;
		ancien_15_19 = ancien_15_19ParentPartAVEC;
		total = totalParentPartAVEC;
		enregistreRapport(nbreParentParticipeAVEC);
				
		nouveau_10_14 = nouveau_10_14SoutRenfEco;
		nouveau_15_19 = nouveau_15_19SoutRenfEco;
		ancien_10_14 = ancien_10_14SoutRenfEco;
		ancien_15_19 = ancien_15_19SoutRenfEco;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreFillebeneficieSoutienRenforcementEconomique);
				
		nouveau_10_14 = nouveau_10_14Inactives;
		nouveau_15_19 = nouveau_15_19Inactives;
		ancien_10_14 = ancien_10_14Inactives;
		ancien_15_19 = ancien_15_19Inactives;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreFilleInactives);
		
		nouveau_10_14 = nouveau_10_14Actives;
		nouveau_15_19 = nouveau_15_19Actives;
		ancien_10_14 = ancien_10_14Actives;
		ancien_15_19 = ancien_15_19Actives;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreFilleActives);
				
		// autour de 5 intervention primaires

		nouveau_10_14 = nouveau_10_14Mois5;
		nouveau_15_19 = nouveau_15_19Mois5;
		ancien_10_14 = ancien_10_14Mois5;
		ancien_15_19 = ancien_15_19Mois5;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreFilleRecuMoins5InterventionsPrimaire);
			
		nouveau_10_14 = nouveau_10_14_5;
		nouveau_15_19 = nouveau_15_19_5;
		ancien_10_14 = ancien_10_14_5;
		ancien_15_19 = ancien_15_19_5;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreFilleRecu5InterventionsPrimaire);
		
		nouveau_10_14 = nouveau_10_14Plus5;
		nouveau_15_19 = nouveau_15_19Plus5;
		ancien_10_14 = ancien_10_14Plus5;
		ancien_15_19 = ancien_15_19Plus5;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreFilleRecuPlus5InterventionsPrimaire);
		
		// autour de 6 intervention primaires

		nouveau_10_14 = nouveau_10_14Mois6;
		nouveau_15_19 = nouveau_15_19Mois6;
		ancien_10_14 = ancien_10_14Mois6;
		ancien_15_19 = ancien_15_19Mois6;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreFilleRecuMoins6InterventionsPrimaire);
		
		nouveau_10_14 = nouveau_10_14_6;
		nouveau_15_19 = nouveau_15_19_6;
		ancien_10_14 = ancien_10_14_6;
		ancien_15_19 = ancien_15_19_6;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreFilleRecu6InterventionsPrimaire);
				
		nouveau_10_14 = nouveau_10_14Plus6;
		nouveau_15_19 = nouveau_15_19Plus6;
		ancien_10_14 = ancien_10_14Plus6;
		ancien_15_19 = ancien_15_19Plus6;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport(nbreFilleRecuPlus6InterventionsPrimaire);
		
		
	}

	private void getDossierBenef() {

		List<String> organisation = new ArrayList<String>();
		List<String> lesElements = new ArrayList<String>();
		List<DataValue> allDataValueDossierBeneficiare = new ArrayList<DataValue>();

		lesElements = elementsUiDossierBeneficiaire;
		organisation.add(this.organisationSelect.getId());

		/*
		 * lesElements.add(porteEntree.getUid()); lesElements.add(age_enrol.getUid());
		 * lesElements.add(idDreamsElement.getUid());
		 * 
		 * Collections.reverse(lesElements);
		 */

		// nbreBenefEnrole NOUVEAU
		allDataValueDossierBeneficiare = idataValues.dataAnalyseElementListPeriode(organisation,
				dossierBeneficiare.getId(), lesElements, dateDebuts, dateFins);
		if (!allDataValueDossierBeneficiare.isEmpty()) {
			contituerDossier(allDataValueDossierBeneficiare, true);
		}

		// nbreBenefEnrole ANCIEN
		allDataValueDossierBeneficiare = idataValues.dataAnalyseElementListPreview(organisation,
				dossierBeneficiare.getId(), lesElements, dateDebuts);

		if (!allDataValueDossierBeneficiare.isEmpty()) {
			contituerDossier(allDataValueDossierBeneficiare, false);
		}
	}

	private void nbreBenefEnrole() {
		List<String> organisation = new ArrayList<String>();

		total = 0;
		nouveau_10_14 = 0;
		nouveau_15_19 = 0;
		ancien_10_14 = 0;
		ancien_15_19 = 0;
		organisation.add(this.organisationSelect.getId());

		// nbreBenefEnrole NOUVEAU
		dataValues = idataValues.dataAnalyseElementPeriode(organisation, enrolement.getId(), age_enrol.getUid(),
				dateDebuts, dateFins);

		for (int i = 0; i < dataValues.size(); i++) {
			if (dataValues.get(i).getValue() != null) {
				String age_enrol = dataValues.get(i).getValue();
				int age = Integer.parseInt(age_enrol);
				if (age < 15) {
					nouveau_10_14++;
				} else {
					nouveau_15_19++;
				}

			}

		}

		// nbreBenefEnrole ANCIEN
		dataValues = idataValues.dataAnalyseElementPreview(organisation, enrolement.getId(), age_enrol.getUid(),
				dateDebuts);
		for (int i = 0; i < dataValues.size(); i++) {
			if (dataValues.get(i).getValue() != null) {
				String age_enrol = dataValues.get(i).getValue();
				int age = Integer.parseInt(age_enrol);
				if (age < 15) {
					ancien_10_14++;
				} else {
					ancien_15_19++;
				}
			}
		}

		total = ancien_15_19 + ancien_10_14 + nouveau_15_19 + nouveau_10_14;
		enregistreRapport(nbreBenefEnrole);

	}

	/*
	 * private void nbreBenefEnrole() { List<String> organisation = new
	 * ArrayList<String>(); nouveau = 0; ancien = 0; total = 0; nouveau_10_14 = 0;
	 * nouveau_15_19 = 0; ancien_10_14 = 0; ancien_15_19 = 0;
	 * organisation.add(this.organisationSelect.getId()); Element element =
	 * ielement.getOneElmentByCode("age_enrol"); // nbreBenefEnrole NOUVEAU
	 * dataInstances = idataValues.dataAnalysePeriode(organisation,
	 * enrolement.getId(), dateDebuts, dateFins); nouveau = dataInstances.size();
	 * 
	 * for (int i = 0; i < dataInstances.size(); i++) { for (int b = 0; b <
	 * dataInstances.get(i).getDataValue().size(); b++) { if
	 * (dataInstances.get(i).getDataValue().get(b).getElement().equals(element.
	 * getUid())) { String age_enrol =
	 * dataInstances.get(i).getDataValue().get(b).getValue(); int age =
	 * Integer.parseInt(age_enrol); if (age < 15) { nouveau_10_14++; } else {
	 * nouveau_15_19++; } break; } } }
	 * 
	 * // nbreBenefEnrole ANCIEN dataInstances =
	 * idataValues.dataAnalysePreview(organisation, enrolement.getId(), dateDebuts);
	 * ancien = dataInstances.size(); for (int i = 0; i < dataInstances.size(); i++)
	 * { for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) { if
	 * (dataInstances.get(i).getDataValue().get(b).getElement().equals(element.
	 * getUid())) { String age_enrol =
	 * dataInstances.get(i).getDataValue().get(b).getValue(); int age =
	 * Integer.parseInt(age_enrol); if (age < 15) { ancien_10_14++; } else {
	 * ancien_15_19++; } break; } } }
	 * 
	 * total = nouveau + ancien; enregistreRapport("nbreBenefEnrole");
	 * 
	 * }
	 */

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

	private void contituerDossier(List<DataValue> dataValues, boolean nouveau) {
		/*
		 * lesDossiersBeneficiaire = new ArrayList<List<DataValue>>(); int deb = 0; int
		 * pas = deb + 1; List<DataValue> TempsDataValues = new ArrayList<DataValue>();
		 * 
		 * while (deb < dataValues.size()) { if
		 * (dataValues.get(deb).getInstance().getInstanceid() ==
		 * dataValues.get(pas).getInstance().getInstanceid()) {
		 * TempsDataValues.add(dataValues.get(pas)); dataValues.remove(pas); pas--; }
		 * pas++; if(dataValues.size() == pas) {
		 * TempsDataValues.add(0,dataValues.get(deb)); dataValues.remove(deb);
		 * lesDossiersBeneficiaire.add(TempsDataValues); deb = 0; pas = deb+1;
		 * TempsDataValues = new ArrayList<DataValue>(); if(dataValues.size() == pas) {
		 * TempsDataValues.add(dataValues.get(deb)); dataValues.remove(deb);
		 * lesDossiersBeneficiaire.add(TempsDataValues); } } }
		 */

		lesDossiersBeneficiaire = constituerDataValueInstance(dataValues);
		if (nouveau) {
			actuLesDossiersBeneficiaire = lesDossiersBeneficiaire;
		} else {
			preLesDossiersBeneficiaire = lesDossiersBeneficiaire;
		}
		ageDossierBenef(nouveau);

	}

	private void ageDossierBenef(boolean nouveau) {
		for (int i = 0; i < lesDossiersBeneficiaire.size(); i++) {
			for (int ag = 0; ag < lesDossiersBeneficiaire.get(i).size(); ag++) {
				if (lesDossiersBeneficiaire.get(i).get(ag).getElement().getUid().equals(age_enrol.getUid())) {
					String _age = lesDossiersBeneficiaire.get(i).get(ag).getValue();

					try {
						int age = Integer.parseInt(_age);
						determinerIndicateur(age, lesDossiersBeneficiaire.get(i), nouveau);
					} catch (NumberFormatException e) {
						//
					}
					break;
				}
			}
		}
	}

	private void determinerIndicateur(int age, List<DataValue> dataValues, boolean nouveau) {

		nbreBenefEnroleScolaireExtraScolaire(age, dataValues, nouveau);
		nbreFilleIssusPopulation(age, dataValues, nouveau);
		nbreFilleReferePsychoSocial(age, dataValues, nouveau);
		nbreBeneficiaireCommunicationParentEnfant(age, dataValues, nouveau);
		nbreFilleBesoinEducation(age, dataValues, nouveau);
		nbreFilleBeneficieSoutienAlphabetisation(age, dataValues, nouveau);
		nbreFilleFormePortPreservatif(age, dataValues, nouveau);
		nbreFilleReferePreservatif(age, dataValues, nouveau);
		nbreFilleReferePF(age, dataValues, nouveau);
		nbreFilleRefereDepistage(age, dataValues, nouveau);
		nbreFilleVBGrecuServicePsychoSocialJuridique(age, dataValues, nouveau);
		nbreFilleVBGrefereMedicale(age, dataValues, nouveau);
		nbreFilleVBGbeneficieSoutienMedicale(age, dataValues, nouveau);
		nbreFillebeneficieProtectionSociale(age, dataValues, nouveau);
		nbreFille_18_19_ParticipeAVEC(age, dataValues, nouveau);
		nbreFillebeneficieSoutienRenforcementEconomique(age, dataValues, nouveau);
		nbreFilleActivesEtInactives(age, dataValues, nouveau);
		nbreInterventionsPrimaire(age, dataValues, nouveau);

	}

	private void nbreBenefEnroleScolaireExtraScolaire(int age, List<DataValue> dataValues, boolean nouveau) {
		boolean valide = false;
		boolean ecole = false;

		for (int i = 0; i < dataValues.size(); i++) {
			if (dataValues.get(i).getElement().getUid().equals(porteEntree.getUid())) {
				String porteEntre = dataValues.get(i).getValue();
				if (porteEntre != null) {
					valide = true;
					if (porteEntre.equals("ecole")) {
						ecole = true;
					} else {
						ecole = false;
					}
				}
				break;
			}
		}

		if (valide) {
			if (nouveau) {
				if (ecole) {
					if (age < 15) {
						nouveau_10_14Scolaire++;
					} else {
						nouveau_15_19Scolaire++;
					}

				} else {
					if (age < 15) {
						nouveau_10_14ExtraScolaire++;
					} else {
						nouveau_15_19ExtraScolaire++;
					}
				}
			} else {
				if (ecole) {
					if (age < 15) {
						ancien_10_14Scolaire++;
					} else {
						ancien_15_19Scolaire++;
					}
				} else {
					if (age < 15) {
						ancien_10_14ExtraScolaire++;
					} else {
						ancien_15_19ExtraScolaire++;
					}
				}
			}

			nbreBenefEnroleScolaireExtraScolaireActifs(age, dataValues, nouveau, ecole);
		}

	}

	/*
	 * private void nbreBenefEnroleScolaireExtraScolaire() { List<String>
	 * organisation = new ArrayList<String>(); //List<BeneficiaireTDO>
	 * beneficiaireTDOs = new ArrayList<BeneficiaireTDO>();
	 * 
	 * organisation.add(this.organisationSelect.getId()); Element element =
	 * ielement.getOneElmentByCode("porteEntree"); Element ageEnrolElement =
	 * ielement.getOneElmentByCode("age_enrol"); nouveau = 0; ancien = 0; total = 0;
	 * nouveau_10_14 = 0; nouveau_15_19 = 0; ancien_10_14 = 0; ancien_15_19 = 0;
	 * 
	 * /*int nouveauExtra = 0; int ancienExtra = 0; int totalExtra = 0;* int
	 * nouveau_10_14Extra = 0; int nouveau_15_19Extra = 0; int ancien_10_14Extra =
	 * 0; int ancien_15_19Extra = 0;
	 * 
	 * //beneficiaireTDOs = ibeneficiaire.getBeneficiairePeriode(organisation,
	 * dateDebuts, dateFins);
	 * 
	 * // nbreBenefEnrole NOUVEAU dataInstances =
	 * idataValues.dataAnalysePeriode(organisation, dossierBeneficiare.getId(),
	 * dateDebuts, dateFins); nouveau = dataInstances.size();
	 * 
	 * for (int i = 0; i < dataInstances.size(); i++) { for (int b = 0; b <
	 * dataInstances.get(i).getDataValue().size(); b++) { if
	 * (dataInstances.get(i).getDataValue().get(b).getElement().equals(element.
	 * getUid())) { String porteEntree =
	 * dataInstances.get(i).getDataValue().get(b).getValue(); if(porteEntree !=
	 * null) { if (porteEntree.equals("ecole")) {
	 * 
	 * for (int ag = 0; ag < dataInstances.get(i).getDataValue().size(); ag++) { if
	 * (dataInstances.get(i).getDataValue().get(ag).getElement()
	 * .equals(ageEnrolElement.getUid())) { int age =
	 * Integer.parseInt(dataInstances.get(i).getDataValue().get(ag).getValue()); if
	 * (age < 15) { nouveau_10_14++; } else { nouveau_15_19++; } break; } } } else {
	 * for (int ag = 0; ag < dataInstances.get(i).getDataValue().size(); ag++) { if
	 * (dataInstances.get(i).getDataValue().get(ag).getElement()
	 * .equals(ageEnrolElement.getUid())) { int age =
	 * Integer.parseInt(dataInstances.get(i).getDataValue().get(ag).getValue()); if
	 * (age < 15) { nouveau_10_14Extra++; } else { nouveau_15_19Extra++; } break; }
	 * } } }
	 * 
	 * break; } } }
	 * 
	 * // nbreBenefEnrole ANCIEN dataInstances =
	 * idataValues.dataAnalysePreview(organisation, dossierBeneficiare.getId(),
	 * dateDebuts); ancien = dataInstances.size(); for (int i = 0; i <
	 * dataInstances.size(); i++) { for (int b = 0; b <
	 * dataInstances.get(i).getDataValue().size(); b++) { if
	 * (dataInstances.get(i).getDataValue().get(b).getElement().equals(element.
	 * getUid())) { String porteEntree =
	 * dataInstances.get(i).getDataValue().get(b).getValue(); if(porteEntree !=
	 * null) { if (porteEntree.equals("ecole")) { for (int ag = 0; ag <
	 * dataInstances.get(i).getDataValue().size(); ag++) { if
	 * (dataInstances.get(i).getDataValue().get(ag).getElement()
	 * .equals(ageEnrolElement.getUid())) { int age =
	 * Integer.parseInt(dataInstances.get(i).getDataValue().get(ag).getValue()); if
	 * (age < 15) { ancien_10_14++; } else { ancien_15_19++; } break; } } } else {
	 * for (int ag = 0; ag < dataInstances.get(i).getDataValue().size(); ag++) { if
	 * (dataInstances.get(i).getDataValue().get(ag).getElement()
	 * .equals(ageEnrolElement.getUid())) { int age =
	 * Integer.parseInt(dataInstances.get(i).getDataValue().get(ag).getValue()); if
	 * (age < 15) { ancien_10_14Extra++; } else { ancien_15_19Extra++; } break; } }
	 * } }
	 * 
	 * break; } } } total = nouveau + ancien;
	 * enregistreRapport("nbreBenefEnroleScolaire"); // nouveau = nouveauExtra; //
	 * ancien = ancienExtra; // total = totalExtra; nouveau_10_14 =
	 * nouveau_10_14Extra; nouveau_15_19 = nouveau_15_19Extra; ancien_10_14 =
	 * ancien_10_14Extra; ancien_15_19 = ancien_15_19Extra;
	 * enregistreRapport("nbreBenefEnroleExtraScolaire");
	 * 
	 * }
	 */

	private void nbreBenefEnroleScolaireExtraScolaireActifs(int age, List<DataValue> dataValues, boolean nouveau,
			boolean scolaire) {
		boolean actif = false;
		for (int i = 0; i < dataValues.size(); i++) {
			if (dataValues.get(i).getValue() != null && !dataValues.get(i).getValue().equals("")
					&& !dataValues.get(i).getValue().equals(" ")) {
				for (int j = 0; j < elementsIntervention.size(); j++) {
					if (dataValues.get(i).getElement().getUid().equals(elementsIntervention.get(j).getUid())) {
						actif = true;
						break;
					}
				}
			}
			if (actif) {
				break;
			}
		}

		if (actif) {
			if (nouveau) {
				if (scolaire) {
					if (age < 15) {
						nouveau_10_14ScolaireActif++;
					} else {
						nouveau_15_19ScolaireActif++;
					}

				} else {
					if (age < 15) {
						nouveau_10_14ExtraScolaireActif++;
					} else {
						nouveau_15_19ExtraScolaireActif++;
					}
				}
			} else {
				if (scolaire) {
					if (age < 15) {
						ancien_10_14ScolaireActif++;
					} else {
						ancien_15_19ScolaireActif++;
					}
				} else {
					if (age < 15) {
						ancien_10_14ExtraScolaireActif++;
					} else {
						ancien_15_19ExtraScolaireActif++;
					}
				}
			}
		}

	}

	/*
	 * private void nbreBenefEnroleScolaireExtraScolaireActifs() { List<String>
	 * organisation = new ArrayList<String>(); // nouveau = 0; // ancien = 0; total
	 * = 0; nouveau_10_14 = 0; nouveau_15_19 = 0; ancien_10_14 = 0; ancien_15_19 =
	 * 0;
	 * 
	 * // int nouveauExtra = 0; // int ancienExtra = 0; // int totalExtra = 0; int
	 * nouveau_10_14Extra = 0; int nouveau_15_19Extra = 0; int ancien_10_14Extra =
	 * 0; int ancien_15_19Extra = 0;
	 * 
	 * organisation.add(this.organisationSelect.getId()); Element element =
	 * ielement.getOneElmentByCode("porteEntree"); Element ageEnrolElement =
	 * ielement.getOneElmentByCode("age_enrol"); // nbreBenefEnrole NOUVEAU
	 * dataInstances = idataValues.dataAnalysePeriode(organisation,
	 * dossierBeneficiare.getId(), dateDebuts, dateFins); // nouveau =
	 * dataInstances.size();
	 * 
	 * for (int i = 0; i < dataInstances.size(); i++) { for (int b = 0; b <
	 * dataInstances.get(i).getDataValue().size(); b++) { if
	 * (dataInstances.get(i).getDataValue().get(b).getElement().equals(element.
	 * getUid())) { String porteEntree =
	 * dataInstances.get(i).getDataValue().get(b).getValue(); if(porteEntree !=
	 * null) { if (porteEntree.equals("ecole")) { for (int interv = 0; interv <
	 * dataInstances.get(i).getDataValue().size(); interv++) { for (int eleInter =
	 * 0; eleInter < elementsIntervention.size(); eleInter++) { if
	 * (dataInstances.get(i).getDataValue().get(interv).getElement()
	 * .equals(elementsIntervention.get(eleInter).getUid())) { for (int ag = 0; ag <
	 * dataInstances.get(i).getDataValue().size(); ag++) { if
	 * (dataInstances.get(i).getDataValue().get(ag).getElement()
	 * .equals(ageEnrolElement.getUid())) { int age = Integer
	 * .parseInt(dataInstances.get(i).getDataValue().get(ag).getValue()); if (age <
	 * 15) { nouveau_10_14++; } else { nouveau_15_19++; } break; } } } } }
	 * 
	 * } else { for (int interv = 0; interv <
	 * dataInstances.get(i).getDataValue().size(); interv++) { for (int eleInter =
	 * 0; eleInter < elementsIntervention.size(); eleInter++) { if
	 * (dataInstances.get(i).getDataValue().get(interv).getElement()
	 * .equals(elementsIntervention.get(eleInter).getUid())) { for (int ag = 0; ag <
	 * dataInstances.get(i).getDataValue().size(); ag++) { if
	 * (dataInstances.get(i).getDataValue().get(ag).getElement()
	 * .equals(ageEnrolElement.getUid())) { int age = Integer
	 * .parseInt(dataInstances.get(i).getDataValue().get(ag).getValue()); if (age <
	 * 15) { nouveau_10_14Extra++; } else { nouveau_15_19Extra++; } break; } }
	 * break; } } } } }
	 * 
	 * break; } } }
	 * 
	 * // nbreBenefEnrole ANCIEN dataInstances =
	 * idataValues.dataAnalysePreview(organisation, dossierBeneficiare.getId(),
	 * dateDebuts); // ancien = dataInstances.size(); for (int i = 0; i <
	 * dataInstances.size(); i++) { for (int b = 0; b <
	 * dataInstances.get(i).getDataValue().size(); b++) { if
	 * (dataInstances.get(i).getDataValue().get(b).getElement().equals(element.
	 * getUid())) { String porteEntree =
	 * dataInstances.get(i).getDataValue().get(b).getValue(); if(porteEntree !=
	 * null) { if (porteEntree.equals("ecole")) { for (int interv = 0; interv <
	 * dataInstances.get(i).getDataValue().size(); interv++) { for (int eleInter =
	 * 0; eleInter < elementsIntervention.size(); eleInter++) { if
	 * (dataInstances.get(i).getDataValue().get(interv).getElement()
	 * .equals(elementsIntervention.get(eleInter).getUid())) { for (int ag = 0; ag <
	 * dataInstances.get(i).getDataValue().size(); ag++) { if
	 * (dataInstances.get(i).getDataValue().get(ag).getElement()
	 * .equals(ageEnrolElement.getUid())) { int age = Integer
	 * .parseInt(dataInstances.get(i).getDataValue().get(ag).getValue()); if (age <
	 * 15) { ancien_10_14++; } else { ancien_15_19++; } break; } } } } }
	 * 
	 * } else { for (int interv = 0; interv <
	 * dataInstances.get(i).getDataValue().size(); interv++) { for (int eleInter =
	 * 0; eleInter < elementsIntervention.size(); eleInter++) { if
	 * (dataInstances.get(i).getDataValue().get(interv).getElement()
	 * .equals(elementsIntervention.get(eleInter).getUid())) { for (int ag = 0; ag <
	 * dataInstances.get(i).getDataValue().size(); ag++) { if
	 * (dataInstances.get(i).getDataValue().get(ag).getElement()
	 * .equals(ageEnrolElement.getUid())) { int age = Integer
	 * .parseInt(dataInstances.get(i).getDataValue().get(ag).getValue()); if (age <
	 * 15) { ancien_10_14Extra++; } else { ancien_15_19Extra++; } break; } } break;
	 * } } } } }
	 * 
	 * break; } } }
	 * 
	 * total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
	 * enregistreRapport("nbreBenefEnroleScolaireActifs"); // nouveau =
	 * nouveauExtra; // ancien = ancienExtra; // total = totalExtra; nouveau_10_14 =
	 * nouveau_10_14Extra; nouveau_15_19 = nouveau_15_19Extra; ancien_10_14 =
	 * ancien_10_14Extra; ancien_15_19 = ancien_15_19Extra; total = nouveau_10_14 +
	 * nouveau_15_19 + ancien_10_14 + ancien_15_19;
	 * enregistreRapport("nbreBenefEnroleExtraScolaireActifs");
	 * 
	 * }
	 */

	private void nbreFilleIssusPopulation(int age, List<DataValue> dataValues, boolean nouveau) {

		List<Option> options = new ArrayList<Option>();
		// nbreBenefEnrole NOUVEAU
		int nbreOption = 0;
		int nbreObtenu = 0;

		for (int interv = 0; interv < elementsInterventionPopCible.size(); interv++) {
			for (int i = 0; i < dataValues.size(); i++) {
				if (dataValues.get(i).getElement().getUid().equals(elementsInterventionPopCible.get(interv).getUid())) {
					options = ioption
							.OptionOfEnsemble(elementsInterventionPopCible.get(interv).getEnsembleOption().getUid());
					nbreOption += options.size();
					String ElementValue = dataValues.get(i).getValue();
					if (ElementValue != null) {
						for (int opt = 0; opt < options.size(); opt++) {
							if (ElementValue.indexOf(options.get(opt).getCode()) != -1) {
								nbreObtenu++;
							}
						}
					}
				}

			}

		}

		if (nbreOption == nbreObtenu) {
			if (nouveau) {
				if (age < 15) {
					nouveau_10_14IssusPop++;
				} else {
					nouveau_15_19IssusPop++;
				}
			} else {
				if (age < 15) {
					ancien_10_14IssusPop++;
				} else {
					ancien_15_19IssusPop++;
				}
			}
		}

	}

	/*
	 * private void nbreFilleIssusPopulation() { List<String> organisation = new
	 * ArrayList<String>(); List<Option> options = new ArrayList<Option>(); total =
	 * 0; nouveau_10_14 = 0; nouveau_15_19 = 0; ancien_10_14 = 0; ancien_15_19 = 0;
	 * 
	 * organisation.add(this.organisationSelect.getId()); Element ageEnrolElement =
	 * ielement.getOneElmentByCode("age_enrol"); // nbreBenefEnrole NOUVEAU
	 * dataInstances = idataValues.dataAnalysePeriode(organisation,
	 * dossierBeneficiare.getId(), dateDebuts, dateFins);
	 * 
	 * for (int i = 0; i < dataInstances.size(); i++) { int nbreOption = 0; int
	 * nbreObtenu = 0; for (int interv = 0; interv <
	 * elementsInterventionPopCible.size(); interv++) { options = ioption
	 * .OptionOfEnsemble(elementsInterventionPopCible.get(interv).getEnsembleOption(
	 * ).getUid()); nbreOption += options.size(); for (int b = 0; b <
	 * dataInstances.get(i).getDataValue().size(); b++) { if
	 * (dataInstances.get(i).getDataValue().get(b).getElement()
	 * .equals(elementsInterventionPopCible.get(interv).getUid())) { String
	 * ElementValue = dataInstances.get(i).getDataValue().get(b).getValue();
	 * if(ElementValue != null) { for (int opt = 0; opt < options.size(); opt++) {
	 * if (ElementValue.indexOf(options.get(opt).getCode()) != -1) { nbreObtenu++; }
	 * } }
	 * 
	 * } } } for (int ag = 0; ag < dataInstances.get(i).getDataValue().size(); ag++)
	 * { if (dataInstances.get(i).getDataValue().get(ag).getElement().equals(
	 * ageEnrolElement.getUid())) { int age =
	 * Integer.parseInt(dataInstances.get(i).getDataValue().get(ag).getValue()); if
	 * (nbreOption == nbreObtenu) { if (age < 15) { nouveau_10_14++; } else {
	 * nouveau_15_19++; } break; } } } }
	 * 
	 * // BenefEnrole ANCIEN dataInstances =
	 * idataValues.dataAnalysePreview(organisation, dossierBeneficiare.getId(),
	 * dateDebuts); for (int i = 0; i < dataInstances.size(); i++) { int nbreOption
	 * = 0; int nbreObtenu = 0; for (int interv = 0; interv <
	 * elementsInterventionPopCible.size(); interv++) { options = ioption
	 * .OptionOfEnsemble(elementsInterventionPopCible.get(interv).getEnsembleOption(
	 * ).getUid()); nbreOption += options.size(); for (int b = 0; b <
	 * dataInstances.get(i).getDataValue().size(); b++) { if
	 * (dataInstances.get(i).getDataValue().get(b).getElement()
	 * .equals(elementsInterventionPopCible.get(interv).getUid())) { String
	 * ElementValue = dataInstances.get(i).getDataValue().get(b).getValue();
	 * if(ElementValue != null) { for (int opt = 0; opt < options.size(); opt++) {
	 * if (ElementValue.indexOf(options.get(opt).getCode()) != -1) { nbreObtenu++; }
	 * } }
	 * 
	 * } } } for (int ag = 0; ag < dataInstances.get(i).getDataValue().size(); ag++)
	 * { if (dataInstances.get(i).getDataValue().get(ag).getElement().equals(
	 * ageEnrolElement.getUid())) { int age =
	 * Integer.parseInt(dataInstances.get(i).getDataValue().get(ag).getValue()); if
	 * (nbreOption == nbreObtenu) { if (age < 15) { ancien_10_14++; } else {
	 * ancien_15_19++; } break; } } } }
	 * 
	 * total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
	 * enregistreRapport("nbreFilleIssusPopulation");
	 * 
	 * }
	 */

	private void nbreFilleReferePsychoSocial(int age, List<DataValue> dataValues, boolean nouveau) {
		boolean refPsy = false;
		for (int i = 0; i < dataValues.size(); i++) {
			if (dataValues.get(i).getElement().getUid().equals(refPsychoElement.getUid())) {
				String valeur = dataValues.get(i).getValue();
				if (valeur != null && !valeur.equals("") && !valeur.equals(" ")) {
					refPsy = true;
					break;
				}
			}
		}

		if (refPsy) {
			if (nouveau) {
				if (age < 15) {
					nouveau_10_14RefPsy++;
				} else {
					nouveau_15_19RefPsy++;
				}
			} else {
				if (age < 15) {
					ancien_10_14RefPsy++;
				} else {
					ancien_15_19RefPsy++;
				}
			}

		}

	}

	/*
	 * private void nbreFilleReferePsychoSocial() { List<String> organisation = new
	 * ArrayList<String>(); //List<Option> options = new ArrayList<Option>(); total
	 * = 0; nouveau_10_14 = 0; nouveau_15_19 = 0; ancien_10_14 = 0; ancien_15_19 =
	 * 0;
	 * 
	 * organisation.add(this.organisationSelect.getId()); Element refPsychoElement =
	 * ielement.getOneElmentByCode("referencePsychoSocial"); Element ageEnrolElement
	 * = ielement.getOneElmentByCode("age_enrol"); // nbreBenefEnrole NOUVEAU
	 * dataInstances = idataValues.dataAnalysePeriode(organisation,
	 * dossierBeneficiare.getId(), dateDebuts, dateFins);
	 * 
	 * for (int i = 0; i < dataInstances.size(); i++) { for (int b = 0; b <
	 * dataInstances.get(i).getDataValue().size(); b++) { if
	 * (dataInstances.get(i).getDataValue().get(b).getElement().equals(
	 * refPsychoElement.getUid())) { if
	 * (dataInstances.get(i).getDataValue().get(b).getValue() != null &&
	 * !dataInstances.get(i).getDataValue().get(b).getValue().equals("")) { for (int
	 * ag = 0; ag < dataInstances.get(i).getDataValue().size(); ag++) { if
	 * (dataInstances.get(i).getDataValue().get(ag).getElement()
	 * .equals(ageEnrolElement.getUid())) { int age =
	 * Integer.parseInt(dataInstances.get(i).getDataValue().get(ag).getValue()); if
	 * (age < 15) { nouveau_10_14++; } else { nouveau_15_19++; } break;
	 * 
	 * } } } } } }
	 * 
	 * // BenefEnrole ANCIEN dataInstances =
	 * idataValues.dataAnalysePreview(organisation, dossierBeneficiare.getId(),
	 * dateDebuts); for (int i = 0; i < dataInstances.size(); i++) { for (int b = 0;
	 * b < dataInstances.get(i).getDataValue().size(); b++) { if
	 * (dataInstances.get(i).getDataValue().get(b).getElement().equals(
	 * refPsychoElement.getUid())) { if
	 * (dataInstances.get(i).getDataValue().get(b).getValue() != null &&
	 * !dataInstances.get(i).getDataValue().get(b).getValue().equals("")) { for (int
	 * ag = 0; ag < dataInstances.get(i).getDataValue().size(); ag++) { if
	 * (dataInstances.get(i).getDataValue().get(ag).getElement()
	 * .equals(ageEnrolElement.getUid())) { int age =
	 * Integer.parseInt(dataInstances.get(i).getDataValue().get(ag).getValue()); if
	 * (age < 15) { ancien_10_14++; } else { ancien_15_19++; } break;
	 * 
	 * } } } } } }
	 * 
	 * total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
	 * enregistreRapport("nbreFilleReferePsychoSocial");
	 * 
	 * }
	 */

	private void nbreBeneficiaireCommunicationParentEnfant(int age, List<DataValue> dataValues, boolean nouveau) {

		boolean sinovoyu = false;
		for (int i = 0; i < dataValues.size(); i++) {
			if (dataValues.get(i).getElement().getUid().equals(sinovoyuElement.getUid())) {
				String valeur = dataValues.get(i).getValue();
				if (valeur != null && !valeur.equals("") && !valeur.equals(" ")) {
					sinovoyu = true;
					break;
				}
			}
		}

		if (sinovoyu) {
			if (nouveau) {
				if (age < 15) {
					nouveau_10_14Sinovoyu++;
				} else {
					nouveau_15_19Sinovoyu++;
				}
			} else {
				if (age < 15) {
					ancien_10_14Sinovoyu++;
				} else {
					ancien_15_19Sinovoyu++;
				}
			}

		}

	}

	/*
	 * private void nbreBeneficiaireCommunicationParentEnfant() { List<String>
	 * organisation = new ArrayList<String>(); //List<Option> options = new
	 * ArrayList<Option>(); total = 0; nouveau_10_14 = 0; nouveau_15_19 = 0;
	 * ancien_10_14 = 0; ancien_15_19 = 0;
	 * 
	 * organisation.add(this.organisationSelect.getId()); Element sinovoyuElement =
	 * ielement.getOneElmentByCode("sinovoyu"); Element ageEnrolElement =
	 * ielement.getOneElmentByCode("age_enrol"); // nbreBenefEnrole NOUVEAU
	 * dataInstances = idataValues.dataAnalysePeriode(organisation,
	 * dossierBeneficiare.getId(), dateDebuts, dateFins);
	 * 
	 * for (int i = 0; i < dataInstances.size(); i++) { for (int b = 0; b <
	 * dataInstances.get(i).getDataValue().size(); b++) { if
	 * (dataInstances.get(i).getDataValue().get(b).getElement().equals(
	 * sinovoyuElement.getUid())) { if
	 * (dataInstances.get(i).getDataValue().get(b).getValue() != null &&
	 * !dataInstances.get(i).getDataValue().get(b).getValue().equals("")) { for (int
	 * ag = 0; ag < dataInstances.get(i).getDataValue().size(); ag++) { if
	 * (dataInstances.get(i).getDataValue().get(ag).getElement()
	 * .equals(ageEnrolElement.getUid())) { int age =
	 * Integer.parseInt(dataInstances.get(i).getDataValue().get(ag).getValue()); if
	 * (age < 15) { nouveau_10_14++; } else { nouveau_15_19++; } break;
	 * 
	 * } } } } } }
	 * 
	 * // BenefEnrole ANCIEN dataInstances =
	 * idataValues.dataAnalysePreview(organisation, dossierBeneficiare.getId(),
	 * dateDebuts); for (int i = 0; i < dataInstances.size(); i++) { for (int b = 0;
	 * b < dataInstances.get(i).getDataValue().size(); b++) { if
	 * (dataInstances.get(i).getDataValue().get(b).getElement().equals(
	 * sinovoyuElement.getUid())) { if
	 * (dataInstances.get(i).getDataValue().get(b).getValue() != null &&
	 * !dataInstances.get(i).getDataValue().get(b).getValue().equals("")) { for (int
	 * ag = 0; ag < dataInstances.get(i).getDataValue().size(); ag++) { if
	 * (dataInstances.get(i).getDataValue().get(ag).getElement()
	 * .equals(ageEnrolElement.getUid())) { int age =
	 * Integer.parseInt(dataInstances.get(i).getDataValue().get(ag).getValue()); if
	 * (age < 15) { ancien_10_14++; } else { ancien_15_19++; } break;
	 * 
	 * } } } } } }
	 * 
	 * total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
	 * enregistreRapport("nbreBeneficiaireCommunicationParentEnfant");
	 * 
	 * }
	 */

	private void gestParentingAVEC() {
		List<String> organisation = new ArrayList<String>();
		List<String> lesElements = new ArrayList<String>();
		List<DataValue> allDataValueDossierBeneficiare = new ArrayList<DataValue>();

		organisation.add(this.organisationSelect.getId());

		lesElements.add(thematiqueElement.getUid());
		lesElements.add(nomPrenomAutreCibleElement.getUid());

		allDataValueDossierBeneficiare = idataValues.dataAnalyseElementListPreview(organisation, groupe.getId(),
				lesElements, dateFins);

		if (!allDataValueDossierBeneficiare.isEmpty()) {
			lesActivitesGroupes = constituerDataValueInstance(allDataValueDossierBeneficiare);
			for (int i = 0; i < lesActivitesGroupes.size(); i++) {
				for (int j = 0; j < lesActivitesGroupes.get(i).size(); j++) {
					
					if (lesActivitesGroupes.get(i).get(j).getElement().getUid().equals(thematiqueElement.getUid())) {
						String valeur = lesActivitesGroupes.get(i).get(j).getValue();
						if (valeur != null && !valeur.equals("")) {
							if (valeur.equals("sinovoyu")) {
								nbreParentCommunicationParentEnfant(lesActivitesGroupes.get(i));
							}
							if (valeur.equals("avec")) {
								nbreParentParticipeAVEC(lesActivitesGroupes.get(i));
							}
						}
						
					}
					
				}

			}
		}
	}
	
	private void nbreParentCommunicationParentEnfant(List<DataValue> dataValues) {
		for (int a = 0; a < dataValues.size(); a++) {
			if (dataValues.get(a).getElement().getUid()
					.equals(nomPrenomAutreCibleElement.getUid())) {
				String nom = dataValues.get(a).getValue();
				if (nom != null && !nom.equals("") && !nom.equals(" ")) {
					totalParEnf++;
				}
			}
		}
	}
	
	private void nbreParentParticipeAVEC(List<DataValue> dataValues) {
		for (int a = 0; a < dataValues.size(); a++) {
			if (dataValues.get(a).getElement().getUid()
					.equals(nomPrenomAutreCibleElement.getUid())) {
				String nom = dataValues.get(a).getValue();
				if (nom != null && !nom.equals("") && !nom.equals(" ")) {
					totalParentPartAVEC++;
				}
			}
		}
		
	}
	
	/*
	 * private void nbreParentCommunicationParentEnfant() { List<String>
	 * organisation = new ArrayList<String>(); total = 0; nouveau_10_14 = 0;
	 * nouveau_15_19 = 0; ancien_10_14 = 0; ancien_15_19 = 0;
	 * 
	 * organisation.add(this.organisationSelect.getId()); Element thematiqueElement
	 * = ielement.getOneElmentByCode("thematique"); Element
	 * nomPrenomAutreCibleElement =
	 * ielement.getOneElmentByCode("nomPrenomAutreCible"); // nbreBenefEnrole
	 * NOUVEAU dataInstances = idataValues.dataAnalysePreview(organisation,
	 * groupe.getId(), dateFins); for (int i = 0; i < dataInstances.size(); i++) {
	 * for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) { if
	 * (dataInstances.get(i).getDataValue().get(b).getElement().equals(
	 * thematiqueElement.getUid())) {
	 * 
	 * if (dataInstances.get(i).getDataValue().get(b).getValue().equals("sinovoyu"))
	 * { for (int pers = 0; pers < dataInstances.get(i).getDataValue().size();
	 * pers++) { if (dataInstances.get(i).getDataValue().get(pers).getElement()
	 * .equals(nomPrenomAutreCibleElement.getUid())) { if
	 * (dataInstances.get(i).getDataValue().get(pers).getValue() != null &&
	 * !dataInstances.get(i).getDataValue().get(pers).getValue().equals("")) {
	 * total++; } } } } } } }
	 * 
	 * enregistreRapport("nbreParentCommunicationParentEnfant"); }
	 */

	private void nbreFilleVAD() {

		List<String> organisation = new ArrayList<String>();
		List<String> lesElements = new ArrayList<String>();
		List<DataValue> allVad = new ArrayList<DataValue>();
		List<List<DataValue>> vadDataValueInstances = new ArrayList<List<DataValue>>();

		organisation.add(this.organisationSelect.getId());

		// lesElements.add(age_enrol.getUid());
		lesElements.add(idDreamsElement.getUid());

		// nbreBenefEnrole NOUVEAU
		allVad = idataValues.dataAnalyseElementListPeriode(organisation, vad.getId(), lesElements, dateDebuts,
				dateFins);
		if (!allVad.isEmpty()) {
			vadDataValueInstances = constituerDataValueInstance(allVad);
			gestVAD(vadDataValueInstances, actuLesDossiersBeneficiaire, true);
		}

		// nbreBenefEnrole ANCIEN
		allVad = idataValues.dataAnalyseElementListPreview(organisation, vad.getId(), lesElements, dateDebuts);

		if (!allVad.isEmpty()) {
			vadDataValueInstances = constituerDataValueInstance(allVad);
			gestVAD(vadDataValueInstances, preLesDossiersBeneficiaire, false);
		}

	}

	private void gestVAD(List<List<DataValue>> lesVAD, List<List<DataValue>> lesDossierBenef, boolean nouveau) {

		for (int i = 0; i < lesVAD.size(); i++) {
			if (lesVAD.get(i).get(0).getValue() != null && !lesVAD.get(i).get(0).getValue().equals("")
					&& !lesVAD.get(i).get(0).getValue().equals(" ")) {
				for (int j = 0; j < lesDossierBenef.size(); j++) {
					int trouve = 0;
					String age = null;
					String idDreams = null;
					for (int a = 0; a < lesDossierBenef.get(j).size(); a++) {
						if (lesDossierBenef.get(j).get(a).getElement().getUid().equals(idDreamsElement.getUid())
								|| lesDossierBenef.get(j).get(a).getElement().getUid().equals(age_enrol.getUid())) {
							if (lesDossierBenef.get(j).get(a).getElement().getUid().equals(idDreamsElement.getUid())) {
								trouve++;
								idDreams = lesDossierBenef.get(j).get(a).getValue();
							}
							if (lesDossierBenef.get(j).get(a).getElement().getUid().equals(age_enrol.getUid())) {
								trouve++;
								age = lesDossierBenef.get(j).get(a).getValue();
							}
							if (trouve == 2) {
								break;
							}
						}
					}
					if (trouve == 2 && lesVAD.get(i).get(0).getValue().equals(idDreams)) {
						ageVAD(age, nouveau);
					}

				}
			}
		}
	}

	private void ageVAD(String age, boolean nouveau) {
		try {
			int _age = Integer.parseInt(age);

			if (nouveau) {
				if (_age < 15) {
					nouveau_10_14VAD++;
				} else {
					nouveau_15_19VAD++;
				}
			} else {
				if (_age < 15) {
					ancien_10_14VAD++;
				} else {
					ancien_15_19VAD++;
				}
			}

		} catch (NumberFormatException e) {
			//
		}
	}

	/*
	 * private void nbreFilleVAD() { List<String> organisation = new
	 * ArrayList<String>(); List<BeneficiaireTDO> beneficiaireTDOs = new
	 * ArrayList<BeneficiaireTDO>(); total = 0; nouveau_10_14 = 0; nouveau_15_19 =
	 * 0; ancien_10_14 = 0; ancien_15_19 = 0;
	 * 
	 * organisation.add(this.organisationSelect.getId()); Element idDreamsElement =
	 * ielement.getOneElmentByCode("id_dreams");
	 * 
	 * // BenefEnrole NOUVEAU dataInstances =
	 * idataValues.dataAnalysePeriode(organisation, vad.getId(), dateDebuts,
	 * dateFins); beneficiaireTDOs =
	 * ibeneficiaire.getBeneficiairePeriode(organisation, dateDebuts, dateFins); for
	 * (int i = 0; i < dataInstances.size(); i++) { for (int b = 0; b <
	 * dataInstances.get(i).getDataValue().size(); b++) { if
	 * (dataInstances.get(i).getDataValue().get(b).getElement().equals(
	 * idDreamsElement.getUid())) { boolean trouve = false; int age = 0; for (int
	 * benef = 0; benef < beneficiaireTDOs.size(); benef++) { if
	 * (dataInstances.get(i).getDataValue().get(b).getValue()
	 * .equals(beneficiaireTDOs.get(benef).getId_dreams())) { age =
	 * Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement()); trouve =
	 * true; } } if (trouve) { if (age < 15) { nouveau_10_14++; } else {
	 * nouveau_15_19++; } } else { if (age < 15) { ancien_10_14++; } else {
	 * ancien_15_19++; } } break; } } } // BenefEnrole Ancien dataInstances =
	 * idataValues.dataAnalysePreview(organisation, vad.getId(), dateDebuts);
	 * beneficiaireTDOs = ibeneficiaire.getBeneficiairePreview(organisation,
	 * dateDebuts);
	 * 
	 * for (int benef = 0; benef < beneficiaireTDOs.size(); benef++) { boolean
	 * trouve = false; int age = 0; for (int i = 0; i < dataInstances.size(); i++) {
	 * for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) { if
	 * (dataInstances.get(i).getDataValue().get(b).getElement().equals(
	 * idDreamsElement.getUid())) { if
	 * (dataInstances.get(i).getDataValue().get(b).getValue()
	 * .equals(beneficiaireTDOs.get(benef).getId_dreams())) { age =
	 * Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement()); trouve =
	 * true; break; } break; } } } if (trouve) { if (age < 15) { ancien_10_14++; }
	 * else { ancien_15_19++; } } } total = nouveau_10_14 + nouveau_15_19 +
	 * ancien_10_14 + ancien_15_19;
	 * 
	 * enregistreRapport("nbreFilleVAD"); }
	 */

	private void nbreFilleBesoinEducation(int age, List<DataValue> dataValues, boolean nouveau) {
		boolean BesEdu = false;
		for (int i = 0; i < dataValues.size(); i++) {
			if (dataValues.get(i).getElement().getUid().equals(soutienEducatifElement.getUid())) {
				String valeur = dataValues.get(i).getValue();
				if (valeur != null && !valeur.equals("") && !valeur.equals(" ")) {
					BesEdu = true;
				}
				break;
			}
		}

		if (BesEdu) {
			if (nouveau) {
				if (age < 15) {
					nouveau_10_14BesEdu++;
				} else {
					nouveau_15_19BesEdu++;
				}
			} else {
				if (age < 15) {
					ancien_10_14BesEdu++;
				} else {
					ancien_15_19BesEdu++;
				}
			}

		}
	}

	/*
	 * private void nbreFilleBesoinEducation() { List<String> organisation = new
	 * ArrayList<String>(); List<BeneficiaireTDO> beneficiaireTDOs = new
	 * ArrayList<BeneficiaireTDO>(); total = 0; nouveau_10_14 = 0; nouveau_15_19 =
	 * 0; ancien_10_14 = 0; ancien_15_19 = 0;
	 * 
	 * organisation.add(this.organisationSelect.getId()); Element idDreamsElement =
	 * ielement.getOneElmentByCode("id_dreams"); Element soutienEducatifElement =
	 * ielement.getOneElmentByCode("soutienEducatif");
	 * 
	 * // BenefEnrole NOUVEAU dataInstances =
	 * idataValues.dataAnalysePeriode(organisation, dossierBeneficiare.getId(),
	 * dateDebuts, dateFins); beneficiaireTDOs =
	 * ibeneficiaire.getBeneficiairePeriode(organisation, dateDebuts, dateFins); for
	 * (int benef = 0; benef < beneficiaireTDOs.size(); benef++) { boolean trouve =
	 * false; int age = 0; for (int i = 0; i < dataInstances.size(); i++) { for (int
	 * b = 0; b < dataInstances.get(i).getDataValue().size(); b++) { if
	 * (dataInstances.get(i).getDataValue().get(b).getElement().equals(
	 * idDreamsElement.getUid())) { if
	 * (dataInstances.get(i).getDataValue().get(b).getValue()
	 * .equals(beneficiaireTDOs.get(benef).getId_dreams())) { for (int sout = 0;
	 * sout < dataInstances.get(i).getDataValue().size(); sout++) { if
	 * (dataInstances.get(i).getDataValue().get(sout).getElement()
	 * .equals(soutienEducatifElement.getUid())) { if
	 * (dataInstances.get(i).getDataValue().get(sout).getValue() != null &&
	 * !dataInstances.get(i).getDataValue().get(sout).getValue().equals("")) { age =
	 * Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement()); trouve =
	 * true; } } }
	 * 
	 * } } } } if (trouve) { if (age < 15) { nouveau_10_14++; } else {
	 * nouveau_15_19++; } } } // BenefEnrole Ancien dataInstances =
	 * idataValues.dataAnalysePreview(organisation, dossierBeneficiare.getId(),
	 * dateDebuts); beneficiaireTDOs =
	 * ibeneficiaire.getBeneficiairePreview(organisation, dateDebuts); for (int
	 * benef = 0; benef < beneficiaireTDOs.size(); benef++) { boolean trouve =
	 * false; int age = 0; for (int i = 0; i < dataInstances.size(); i++) { for (int
	 * b = 0; b < dataInstances.get(i).getDataValue().size(); b++) { if
	 * (dataInstances.get(i).getDataValue().get(b).getElement().equals(
	 * idDreamsElement.getUid())) { if
	 * (dataInstances.get(i).getDataValue().get(b).getValue()
	 * .equals(beneficiaireTDOs.get(benef).getId_dreams())) { for (int sout = 0;
	 * sout < dataInstances.get(i).getDataValue().size(); sout++) { if
	 * (dataInstances.get(i).getDataValue().get(sout).getElement()
	 * .equals(soutienEducatifElement.getUid())) { if
	 * (dataInstances.get(i).getDataValue().get(sout).getValue() != null &&
	 * !dataInstances.get(i).getDataValue().get(sout).getValue().equals("")) { age =
	 * Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement()); trouve =
	 * true; } } }
	 * 
	 * } } } } if (trouve) { if (age < 15) { ancien_10_14++; } else {
	 * ancien_15_19++; } } }
	 * 
	 * total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
	 * 
	 * enregistreRapport("nbreFilleBesoinEducation"); }
	 */

	private void nbreFilleBeneficieSoutienAlphabetisation(int age, List<DataValue> dataValues, boolean nouveau) {
		boolean SoutAlphab = false;
		for (int i = 0; i < dataValues.size(); i++) {
			if (dataValues.get(i).getElement().getUid().equals(alphabetisationElement.getUid())) {
				String valeur = dataValues.get(i).getValue();
				if (valeur != null && !valeur.equals("") && !valeur.equals(" ")) {
					SoutAlphab = true;
				}
				break;
			}
		}

		if (SoutAlphab) {
			if (nouveau) {
				if (age < 15) {
					nouveau_10_14SoutAlphab++;
				} else {
					nouveau_15_19SoutAlphab++;
				}
			} else {
				if (age < 15) {
					ancien_10_14SoutAlphab++;
				} else {
					ancien_15_19SoutAlphab++;
				}
			}

		}
	}

	/*
	 * private void nbreFilleBeneficieSoutienAlphabetisation() { List<String>
	 * organisation = new ArrayList<String>(); List<BeneficiaireTDO>
	 * beneficiaireTDOs = new ArrayList<BeneficiaireTDO>(); total = 0; nouveau_10_14
	 * = 0; nouveau_15_19 = 0; ancien_10_14 = 0; ancien_15_19 = 0;
	 * 
	 * organisation.add(this.organisationSelect.getId()); Element idDreamsElement =
	 * ielement.getOneElmentByCode("id_dreams"); Element alphabetisation =
	 * ielement.getOneElmentByCode("alphabetisation");
	 * 
	 * // BenefEnrole NOUVEAU dataInstances =
	 * idataValues.dataAnalysePeriode(organisation, dossierBeneficiare.getId(),
	 * dateDebuts, dateFins); beneficiaireTDOs =
	 * ibeneficiaire.getBeneficiairePeriode(organisation, dateDebuts, dateFins); for
	 * (int benef = 0; benef < beneficiaireTDOs.size(); benef++) { boolean trouve =
	 * false; int age = 0; for (int i = 0; i < dataInstances.size(); i++) { for (int
	 * b = 0; b < dataInstances.get(i).getDataValue().size(); b++) { if
	 * (dataInstances.get(i).getDataValue().get(b).getElement().equals(
	 * idDreamsElement.getUid())) { if
	 * (dataInstances.get(i).getDataValue().get(b).getValue()
	 * .equals(beneficiaireTDOs.get(benef).getId_dreams())) { for (int sout = 0;
	 * sout < dataInstances.get(i).getDataValue().size(); sout++) { if
	 * (dataInstances.get(i).getDataValue().get(sout).getElement()
	 * .equals(alphabetisation.getUid())) { if
	 * (dataInstances.get(i).getDataValue().get(sout).getValue() != null &&
	 * !dataInstances.get(i).getDataValue().get(sout).getValue().equals("")) { age =
	 * Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement()); trouve =
	 * true; } } }
	 * 
	 * } } } } if (trouve) { if (age < 15) { nouveau_10_14++; } else {
	 * nouveau_15_19++; } } } // BenefEnrole Ancien dataInstances =
	 * idataValues.dataAnalysePreview(organisation, dossierBeneficiare.getId(),
	 * dateDebuts); beneficiaireTDOs =
	 * ibeneficiaire.getBeneficiairePreview(organisation, dateDebuts); for (int
	 * benef = 0; benef < beneficiaireTDOs.size(); benef++) { boolean trouve =
	 * false; int age = 0; for (int i = 0; i < dataInstances.size(); i++) { for (int
	 * b = 0; b < dataInstances.get(i).getDataValue().size(); b++) { if
	 * (dataInstances.get(i).getDataValue().get(b).getElement().equals(
	 * idDreamsElement.getUid())) { if
	 * (dataInstances.get(i).getDataValue().get(b).getValue()
	 * .equals(beneficiaireTDOs.get(benef).getId_dreams())) { for (int sout = 0;
	 * sout < dataInstances.get(i).getDataValue().size(); sout++) { if
	 * (dataInstances.get(i).getDataValue().get(sout).getElement()
	 * .equals(alphabetisation.getUid())) { if
	 * (dataInstances.get(i).getDataValue().get(sout).getValue() != null &&
	 * !dataInstances.get(i).getDataValue().get(sout).getValue().equals("")) { age =
	 * Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement()); trouve =
	 * true; } } }
	 * 
	 * } } } } if (trouve) { if (age < 15) { ancien_10_14++; } else {
	 * ancien_15_19++; } } }
	 * 
	 * total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
	 * 
	 * enregistreRapport("nbreFilleBeneficieSoutienAlphabetisation");
	 * 
	 * }
	 */

	private void nbreFilleFormePortPreservatif(int age, List<DataValue> dataValues, boolean nouveau) {

		boolean utilPres = false;
		for (int i = 0; i < dataValues.size(); i++) {
			if (dataValues.get(i).getElement().getUid().equals(utilisationPreservatifsElement.getUid())) {
				String valeur = dataValues.get(i).getValue();
				if (valeur != null && !valeur.equals("") && !valeur.equals(" ")) {
					utilPres = true;
				}
				break;
			}
		}

		if (utilPres) {
			if (nouveau) {
				if (age < 15) {
					nouveau_10_14UtilPres++;
				} else {
					nouveau_15_19UtilPres++;
				}
			} else {
				if (age < 15) {
					ancien_10_14UtilPres++;
				} else {
					ancien_15_19UtilPres++;
				}
			}

		}

	}

	/*
	 * private void nbreFilleFormePortPreservatif() { List<String> organisation =
	 * new ArrayList<String>(); List<BeneficiaireTDO> beneficiaireTDOs = new
	 * ArrayList<BeneficiaireTDO>(); total = 0; nouveau_10_14 = 0; nouveau_15_19 =
	 * 0; ancien_10_14 = 0; ancien_15_19 = 0;
	 * 
	 * organisation.add(this.organisationSelect.getId()); Element idDreamsElement =
	 * ielement.getOneElmentByCode("id_dreams"); Element utilisationPreservatifs =
	 * ielement.getOneElmentByCode("utilisationPreservatifs");
	 * 
	 * // BenefEnrole NOUVEAU dataInstances =
	 * idataValues.dataAnalysePeriode(organisation, dossierBeneficiare.getId(),
	 * dateDebuts, dateFins); beneficiaireTDOs =
	 * ibeneficiaire.getBeneficiairePeriode(organisation, dateDebuts, dateFins); for
	 * (int benef = 0; benef < beneficiaireTDOs.size(); benef++) { boolean trouve =
	 * false; int age = 0; for (int i = 0; i < dataInstances.size(); i++) { for (int
	 * b = 0; b < dataInstances.get(i).getDataValue().size(); b++) { if
	 * (dataInstances.get(i).getDataValue().get(b).getElement().equals(
	 * idDreamsElement.getUid())) { if
	 * (dataInstances.get(i).getDataValue().get(b).getValue()
	 * .equals(beneficiaireTDOs.get(benef).getId_dreams())) { for (int sout = 0;
	 * sout < dataInstances.get(i).getDataValue().size(); sout++) { if
	 * (dataInstances.get(i).getDataValue().get(sout).getElement()
	 * .equals(utilisationPreservatifs.getUid())) { if
	 * (dataInstances.get(i).getDataValue().get(sout).getValue() != null &&
	 * !dataInstances.get(i).getDataValue().get(sout).getValue().equals("")) { age =
	 * Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement()); trouve =
	 * true; } } }
	 * 
	 * } } } } if (trouve) { if (age < 15) { nouveau_10_14++; } else {
	 * nouveau_15_19++; } } } // BenefEnrole Ancien dataInstances =
	 * idataValues.dataAnalysePreview(organisation, dossierBeneficiare.getId(),
	 * dateDebuts); beneficiaireTDOs =
	 * ibeneficiaire.getBeneficiairePreview(organisation, dateDebuts); for (int
	 * benef = 0; benef < beneficiaireTDOs.size(); benef++) { boolean trouve =
	 * false; int age = 0; for (int i = 0; i < dataInstances.size(); i++) { for (int
	 * b = 0; b < dataInstances.get(i).getDataValue().size(); b++) { if
	 * (dataInstances.get(i).getDataValue().get(b).getElement().equals(
	 * idDreamsElement.getUid())) { if
	 * (dataInstances.get(i).getDataValue().get(b).getValue()
	 * .equals(beneficiaireTDOs.get(benef).getId_dreams())) { for (int sout = 0;
	 * sout < dataInstances.get(i).getDataValue().size(); sout++) { if
	 * (dataInstances.get(i).getDataValue().get(sout).getElement()
	 * .equals(utilisationPreservatifs.getUid())) { if
	 * (dataInstances.get(i).getDataValue().get(sout).getValue() != null &&
	 * !dataInstances.get(i).getDataValue().get(sout).getValue().equals("")) { age =
	 * Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement()); trouve =
	 * true; } } }
	 * 
	 * } } } } if (trouve) { if (age < 15) { ancien_10_14++; } else {
	 * ancien_15_19++; } } }
	 * 
	 * total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
	 * 
	 * enregistreRapport("nbreFilleFormePortPreservatif");
	 * 
	 * }
	 */

	private void nbrePreservatifDistribues() {

		
		List<String> organisation = new ArrayList<String>();
		List<String> lesElements = new ArrayList<String>();
		List<DataValue> allgroupe = new ArrayList<DataValue>();
		List<List<DataValue>> groupeDataValueInstances = new ArrayList<List<DataValue>>();

		organisation.add(this.organisationSelect.getId());

		
		lesElements.add(materielQuantiteElement.getUid());
		lesElements.add(trancheAgeElement.getUid());

		// nbreBenefEnrole NOUVEAU
		allgroupe = idataValues.dataAnalyseElementListPeriode(organisation,  groupe.getId(), lesElements, dateDebuts,
				dateFins);
		if (!allgroupe.isEmpty()) {
			groupeDataValueInstances = constituerDataValueInstance(allgroupe);
			gestDistributionPreservatif(groupeDataValueInstances, true);
		}

		// nbreBenefEnrole ANCIEN
		allgroupe = idataValues.dataAnalyseElementListPreview(organisation, groupe.getId(), lesElements, dateDebuts);

		if (!allgroupe.isEmpty()) {
			groupeDataValueInstances = constituerDataValueInstance(allgroupe);
			gestDistributionPreservatif(groupeDataValueInstances, false);
		}

	}
	
	private void gestDistributionPreservatif(List<List<DataValue>> lesgroupe, boolean nouveau) {
		
		for (int i = 0; i < lesgroupe.size(); i++) {
			String tranche = null; 
			int quantite = 0;
			for (int a = 0; a < lesgroupe.get(i).size(); a++) {
				if(lesgroupe.get(i).get(a).getValue() != null) {
					if (lesgroupe.get(i).get(a).getElement().getUid().equals(materielQuantiteElement.getUid())) {
						quantite += retirerIntValue(lesgroupe.get(i).get(a).getValue());
					}
					if (lesgroupe.get(i).get(a).getElement().getUid().equals(trancheAgeElement.getUid())) {
						tranche = lesgroupe.get(i).get(a).getValue();
					}
				}
				
				
			}
			if(tranche != null) {				
				if(nouveau) {
					if(tranche.equals("10-14")) {
						nouveau_10_14QuantPres += quantite;
					}
					if(tranche.equals("15-19")) {
						nouveau_15_19QuantPres += quantite;
					}
				}else {
					if(tranche.equals("10-14")) {
						ancien_10_14QuantPres += quantite;
					}
					if(tranche.equals("15-19")) {
						ancien_15_19QuantPres += quantite;
					}
				}
			}
		}

		
	}


	/*private void nbrePreservatifDistribues() {
		List<String> organisation = new ArrayList<String>();
		// List<BeneficiaireTDO> beneficiaireTDOs = new ArrayList<BeneficiaireTDO>();
		total = 0;
		nouveau_10_14 = 0;
		nouveau_15_19 = 0;
		ancien_10_14 = 0;
		ancien_15_19 = 0;

		organisation.add(this.organisationSelect.getId());
		// Element idDreamsElement = ielement.getOneElmentByCode("id_dreams");
		Element materielQuantite = ielement.getOneElmentByCode("materielQuantite");
		Element trancheAge = ielement.getOneElmentByCode("trancheAge");

		// BenefEnrole NOUVEAU
		dataInstances = idataValues.dataAnalysePeriode(organisation, groupe.getId(), dateDebuts, dateFins);
		for (int i = 0; i < dataInstances.size(); i++) {
			for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
				String valeur = dataInstances.get(i).getDataValue().get(b).getValue();
				System.out.println("materielQuantite = " + valeur);
				if (valeur != null) {
					int nombre = 0;
					// String leNombre = null;
					int masDebut = 0;
					int mascPosi = valeur.indexOf("preserMasc");
					int feminPosi = valeur.indexOf("preserFemin");
					if (mascPosi != -1) {
						nombre += retirerInt(valeur, mascPosi);
						/*
						 * mascPosi -= 1; masDebut = valeur.lastIndexOf(" ", mascPosi-1); if(masDebut ==
						 * -1) { masDebut = 0; } //leNombre = valeur.substring(masDebut, mascPosi);
						 * nombre += Integer.parseInt(valeur.substring(masDebut, mascPosi));
						 *
					}
					if (feminPosi != -1) {
						nombre += retirerInt(valeur, feminPosi);
						/*
						 * feminPosi -= 1; int feminDeb = valeur.lastIndexOf(" ", feminPosi-1);
						 * if(feminDeb == -1) feminDeb = 0; nombre +=
						 * Integer.parseInt(valeur.substring(feminDeb, feminPosi));
						 *
					}

					for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
						if (dataInstances.get(i).getDataValue().get(sout).getElement().equals(trancheAge.getUid())) {
							if (dataInstances.get(i).getDataValue().get(sout).getValue().equals("10-14")) {
								nouveau_10_14 += nombre;
							}
							if (dataInstances.get(i).getDataValue().get(sout).getValue().equals("15-19")) {
								nouveau_15_19 += nombre;
							}
						}
					}

				}
			}
		}
		// }
		// BenefEnrole Ancien
		dataInstances = idataValues.dataAnalysePreview(organisation, groupe.getId(), dateDebuts);
		// beneficiaireTDOs = ibeneficiaire.getBeneficiairePreview(organisation,
		// dateDebuts);
		for (int i = 0; i < dataInstances.size(); i++) {
			for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {

				if (dataInstances.get(i).getDataValue().get(b).getElement().equals(materielQuantite.getUid())) {
					String valeur = dataInstances.get(i).getDataValue().get(b).getValue();
					System.out.println("materielQuantite = " + valeur);
					if (valeur != null) {
						int nombre = 0;
						int mascPosi = valeur.indexOf("preserMasc");
						int feminPosi = valeur.indexOf("preserFemin");
						if (mascPosi != -1) {
							nombre += retirerInt(valeur, mascPosi);
							/*
							 * mascPosi--; int masDebut = valeur.lastIndexOf(" ", mascPosi); String val =
							 * valeur.substring(masDebut, mascPosi); nombre += Integer.parseInt(val);
							 *
						}
						if (feminPosi != -1) {
							nombre += retirerInt(valeur, feminPosi);
							/*
							 * feminPosi--; int feminDeb = valeur.lastIndexOf(" ", feminPosi); nombre +=
							 * Integer.parseInt(valeur.substring(feminDeb, feminPosi));
							 *
						}

						for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
							if (dataInstances.get(i).getDataValue().get(sout).getElement()
									.equals(trancheAge.getUid())) {
								if (dataInstances.get(i).getDataValue().get(sout).getValue().equals("10-14")) {
									ancien_10_14 += nombre;
								}
								if (dataInstances.get(i).getDataValue().get(sout).getValue().equals("15-19")) {
									ancien_15_19 += nombre;
								}
							}
						}

					}
				}
			}
		}

		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;

		enregistreRapport("nbrePreservatifDistribues");

	}*/

	
	private void nbreFilleReferePreservatif(int age, List<DataValue> dataValues, boolean nouveau) {
		boolean refPres = false;
		for (int i = 0; i < dataValues.size(); i++) {
			if (dataValues.get(i).getElement().getUid().equals(referencePreservatifs.getUid())) {
				String valeur = dataValues.get(i).getValue();
				if (valeur != null && !valeur.equals("") && !valeur.equals(" ")) {
					refPres = true;
				}
				break;
			}
		}

		if (refPres) {
			if (nouveau) {
				if (age < 15) {
					nouveau_10_14RefPres++;
				} else {
					nouveau_15_19RefPres++;
				}
			} else {
				if (age < 15) {
					ancien_10_14RefPres++;
				} else {
					ancien_15_19RefPres++;
				}
			}

		}

	}

	/*private void nbreFilleReferePreservatif() {
		List<String> organisation = new ArrayList<String>();
		List<BeneficiaireTDO> beneficiaireTDOs = new ArrayList<BeneficiaireTDO>();
		total = 0;
		nouveau_10_14 = 0;
		nouveau_15_19 = 0;
		ancien_10_14 = 0;
		ancien_15_19 = 0;

		organisation.add(this.organisationSelect.getId());
		Element idDreamsElement = ielement.getOneElmentByCode("id_dreams");
		Element referencePreservatifs = ielement.getOneElmentByCode("referencePreservatifs");

		// BenefEnrole NOUVEAU
		dataInstances = idataValues.dataAnalysePeriode(organisation, dossierBeneficiare.getId(), dateDebuts, dateFins);
		beneficiaireTDOs = ibeneficiaire.getBeneficiairePeriode(organisation, dateDebuts, dateFins);
		for (int benef = 0; benef < beneficiaireTDOs.size(); benef++) {
			boolean trouve = false;
			int age = 0;
			for (int i = 0; i < dataInstances.size(); i++) {
				for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
					if (dataInstances.get(i).getDataValue().get(b).getElement().equals(idDreamsElement.getUid())) {
						if (dataInstances.get(i).getDataValue().get(b).getValue()
								.equals(beneficiaireTDOs.get(benef).getId_dreams())) {
							for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
								if (dataInstances.get(i).getDataValue().get(sout).getElement()
										.equals(referencePreservatifs.getUid())) {
									if (dataInstances.get(i).getDataValue().get(sout).getValue() != null
											&& !dataInstances.get(i).getDataValue().get(sout).getValue().equals("")) {
										age = Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement());
										trouve = true;
									}
								}
							}

						}
					}
				}
			}
			if (trouve) {
				if (age < 15) {
					nouveau_10_14++;
				} else {
					nouveau_15_19++;
				}
			}
		}
		// BenefEnrole Ancien
		dataInstances = idataValues.dataAnalysePreview(organisation, dossierBeneficiare.getId(), dateDebuts);
		beneficiaireTDOs = ibeneficiaire.getBeneficiairePreview(organisation, dateDebuts);
		for (int benef = 0; benef < beneficiaireTDOs.size(); benef++) {
			boolean trouve = false;
			int age = 0;
			for (int i = 0; i < dataInstances.size(); i++) {
				for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
					if (dataInstances.get(i).getDataValue().get(b).getElement().equals(idDreamsElement.getUid())) {
						if (dataInstances.get(i).getDataValue().get(b).getValue()
								.equals(beneficiaireTDOs.get(benef).getId_dreams())) {
							for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
								if (dataInstances.get(i).getDataValue().get(sout).getElement()
										.equals(referencePreservatifs.getUid())) {
									if (dataInstances.get(i).getDataValue().get(sout).getValue() != null
											&& !dataInstances.get(i).getDataValue().get(sout).getValue().equals("")) {
										age = Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement());
										trouve = true;
									}
								}
							}

						}
					}
				}
			}
			if (trouve) {
				if (age < 15) {
					ancien_10_14++;
				} else {
					ancien_15_19++;
				}
			}
		}

		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;

		enregistreRapport("nbreFilleReferePreservatif");

	}*/

	
	private void nbreContreReference() {

		List<String> organisation = new ArrayList<String>();
		List<String> lesElements = new ArrayList<String>();
		List<DataValue> allReference = new ArrayList<DataValue>();
		List<List<DataValue>> referenceDataValueInstances = new ArrayList<List<DataValue>>();

		organisation.add(this.organisationSelect.getId());

		
		lesElements.add(agebenefElement.getUid());
		lesElements.add(datecontRefElement.getUid());
		lesElements.add(motifrefElement.getUid());

		// nbreBenefEnrole NOUVEAU
		allReference = idataValues.dataAnalyseElementListPeriode(organisation,  reference.getId(), lesElements, dateDebuts,
				dateFins);
		if (!allReference.isEmpty()) {
			referenceDataValueInstances = constituerDataValueInstance(allReference);
			gestContreReference(referenceDataValueInstances, true);
		}

		// nbreBenefEnrole ANCIEN
		allReference = idataValues.dataAnalyseElementListPreview(organisation, reference.getId(), lesElements, dateDebuts);

		if (!allReference.isEmpty()) {
			referenceDataValueInstances = constituerDataValueInstance(allReference);
			gestContreReference(referenceDataValueInstances, false);
		}

	}
	private void gestContreReference(List<List<DataValue>> lesReferences, boolean nouveau) {
		
		for (int i = 0; i < lesReferences.size(); i++) {
			boolean contreRef = false;
			boolean contreRefPres = false;
			boolean contreRefDepistage = false;
			String age = null;
			for (int a = 0; a < lesReferences.get(i).size(); a++) {
				
				if (lesReferences.get(i).get(a).getValue() != null && lesReferences.get(i).get(a).getElement().getUid().equals(datecontRefElement.getUid())) {
					contreRef = true;
				}
				if (lesReferences.get(i).get(a).getValue() != null && lesReferences.get(i).get(a).getElement().getUid().equals(motifrefElement.getUid())) {
					if(lesReferences.get(i).get(a).getValue().contains("preservatif")) {
						contreRefPres = true;
					}
					if(lesReferences.get(i).get(a).getValue().contains("depistage")) {
						contreRefDepistage = true;
					}
				}
				if (lesReferences.get(i).get(a).getValue() != null && lesReferences.get(i).get(a).getElement().getUid().equals(agebenefElement.getUid())) {
					age = lesReferences.get(i).get(a).getValue();
				}
			}
			if (contreRef) {
				int _age = -1;
				try {
					_age = Integer.parseInt(age);
				} catch (NumberFormatException e) {
					System.out.println("Not interger");
				} catch (NullPointerException e) {
					System.out.println("Not interger");
				}
				if (_age != -1) {
					if(contreRefPres) {
						nbreContreReferencePreservatif(_age,nouveau);
					}
				if(contreRefDepistage) {
					nbreContreReferenceDepistage(_age,nouveau);
				}
					
				}

			}
		}

		
	}
	
	private void nbreContreReferencePreservatif(int age, boolean nouveau) {
		if (nouveau) {
			if (age < 15) {
				nouveau_10_14ContreRefPres++;
			} else {
				nouveau_15_19ContreRefPres++;
			}
		} else {
			if (age < 15) {
				ancien_10_14ContreRefPres++;
			} else {
				ancien_15_19ContreRefPres++;
			}
		}
	}

	/*private void nbreContreReferencePreservatif() {
	
		List<String> organisation = new ArrayList<String>();
		// List<BeneficiaireTDO> beneficiaireTDOs = new ArrayList<BeneficiaireTDO>();
		total = 0;
		nouveau_10_14 = 0;
		nouveau_15_19 = 0;
		ancien_10_14 = 0;
		ancien_15_19 = 0;

		organisation.add(this.organisationSelect.getId());
		Element agebenef = ielement.getOneElmentByCode("agebenef");
		Element datecontRef = ielement.getOneElmentByCode("datecontRef");
		Element motifref = ielement.getOneElmentByCode("motifref");

		// BenefEnrole NOUVEAU
		dataInstances = idataValues.dataAnalysePeriode(organisation, reference.getId(), dateDebuts, dateFins);

		for (int i = 0; i < dataInstances.size(); i++) {
			for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
				if (dataInstances.get(i).getDataValue().get(b).getElement().equals(datecontRef.getUid())) {
					if (!dataInstances.get(i).getDataValue().get(b).getValue().equals("")
							&& dataInstances.get(i).getDataValue().get(b).getValue() != null) {
						for (int motif = 0; motif < dataInstances.get(i).getDataValue().size(); motif++) {
							if (dataInstances.get(i).getDataValue().get(motif).getElement().equals(motifref.getUid())) {
								if (dataInstances.get(i).getDataValue().get(motif).getValue().contains("preservatif")) {
									for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
										if (dataInstances.get(i).getDataValue().get(sout).getElement()
												.equals(agebenef.getUid())) {
											if (dataInstances.get(i).getDataValue().get(sout).getValue() != null
													&& !dataInstances.get(i).getDataValue().get(sout).getValue()
															.equals("")) {
												int age = Integer.parseInt(
														dataInstances.get(i).getDataValue().get(sout).getValue());
												if (age < 15) {
													nouveau_10_14++;
												} else {
													nouveau_15_19++;
												}
											}
										}
									}
								}
							}

						}

					}
				}
			}
		}

		// BenefEnrole Ancien
		dataInstances = idataValues.dataAnalysePreview(organisation, reference.getId(), dateDebuts);

		for (int i = 0; i < dataInstances.size(); i++) {
			for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
				if (dataInstances.get(i).getDataValue().get(b).getElement().equals(datecontRef.getUid())) {
					if (!dataInstances.get(i).getDataValue().get(b).getValue().equals("")
							&& dataInstances.get(i).getDataValue().get(b).getValue() != null) {
						for (int motif = 0; motif < dataInstances.get(i).getDataValue().size(); motif++) {
							if (dataInstances.get(i).getDataValue().get(motif).getElement().equals(motifref.getUid())) {
								if (dataInstances.get(i).getDataValue().get(motif).getValue().contains("preservatif")) {
									for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
										if (dataInstances.get(i).getDataValue().get(sout).getElement()
												.equals(agebenef.getUid())) {
											if (dataInstances.get(i).getDataValue().get(sout).getValue() != null
													&& !dataInstances.get(i).getDataValue().get(sout).getValue()
															.equals("")) {
												int age = Integer.parseInt(
														dataInstances.get(i).getDataValue().get(sout).getValue());
												if (age < 15) {
													ancien_10_14++;
												} else {
													ancien_15_19++;
												}
											}
										}
									}
								}
							}

						}

					}
				}
			}
		}

		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;

		enregistreRapport("nbreContreReferencePreservatif");

	}*/

	private void nbreFilleReferePF(int age, List<DataValue> dataValues, boolean nouveau) {
		boolean refPF = false;
		for (int i = 0; i < dataValues.size(); i++) {
			if (dataValues.get(i).getElement().getUid().equals(referencVersPFElement.getUid())) {
				String valeur = dataValues.get(i).getValue();
				if (valeur != null && !valeur.equals("") && !valeur.equals(" ")) {
					refPF = true;
				}
				break;
			}
		}

		if (refPF) {
			if (nouveau) {
				if (age < 15) {
					nouveau_10_14RefPF++;
				} else {
					nouveau_15_19RefPF++;
				}
			} else {
				if (age < 15) {
					ancien_10_14RefPF++;
				} else {
					ancien_15_19RefPF++;
				}
			}

		}

	}

	/*private void nbreFilleReferePF() {
	List<String> organisation = new ArrayList<String>();
	List<BeneficiaireTDO> beneficiaireTDOs = new ArrayList<BeneficiaireTDO>();
	total = 0;
	nouveau_10_14 = 0;
	nouveau_15_19 = 0;
	ancien_10_14 = 0;
	ancien_15_19 = 0;

	organisation.add(this.organisationSelect.getId());
	Element idDreamsElement = ielement.getOneElmentByCode("id_dreams");
	Element referencVersPF = ielement.getOneElmentByCode("referencVersPF");

	// BenefEnrole NOUVEAU
	dataInstances = idataValues.dataAnalysePeriode(organisation, dossierBeneficiare.getId(), dateDebuts, dateFins);
	beneficiaireTDOs = ibeneficiaire.getBeneficiairePeriode(organisation, dateDebuts, dateFins);
	for (int benef = 0; benef < beneficiaireTDOs.size(); benef++) {
		boolean trouve = false;
		int age = 0;
		for (int i = 0; i < dataInstances.size(); i++) {
			for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
				if (dataInstances.get(i).getDataValue().get(b).getElement().equals(idDreamsElement.getUid())) {
					if (dataInstances.get(i).getDataValue().get(b).getValue()
							.equals(beneficiaireTDOs.get(benef).getId_dreams())) {
						for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
							if (dataInstances.get(i).getDataValue().get(sout).getElement()
									.equals(referencVersPF.getUid())) {
								if (dataInstances.get(i).getDataValue().get(sout).getValue() != null
										&& !dataInstances.get(i).getDataValue().get(sout).getValue().equals("")) {
									age = Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement());
									trouve = true;
								}
							}
						}

					}
				}
			}
		}
		if (trouve) {
			if (age < 15) {
				nouveau_10_14++;
			} else {
				nouveau_15_19++;
			}
		}
	}
	// BenefEnrole Ancien
	dataInstances = idataValues.dataAnalysePreview(organisation, dossierBeneficiare.getId(), dateDebuts);
	beneficiaireTDOs = ibeneficiaire.getBeneficiairePreview(organisation, dateDebuts);
	for (int benef = 0; benef < beneficiaireTDOs.size(); benef++) {
		boolean trouve = false;
		int age = 0;
		for (int i = 0; i < dataInstances.size(); i++) {
			for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
				if (dataInstances.get(i).getDataValue().get(b).getElement().equals(idDreamsElement.getUid())) {
					if (dataInstances.get(i).getDataValue().get(b).getValue()
							.equals(beneficiaireTDOs.get(benef).getId_dreams())) {
						for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
							if (dataInstances.get(i).getDataValue().get(sout).getElement()
									.equals(referencVersPF.getUid())) {
								if (dataInstances.get(i).getDataValue().get(sout).getValue() != null
										&& !dataInstances.get(i).getDataValue().get(sout).getValue().equals("")) {
									age = Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement());
									trouve = true;
								}
							}
						}

					}
				}
			}
		}
		if (trouve) {
			if (age < 15) {
				ancien_10_14++;
			} else {
				ancien_15_19++;
			}
		}
	}

	total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;

	enregistreRapport("nbreFilleReferePF");

}*/

	
	private void nbreFilleRefereDepistage(int age, List<DataValue> dataValues, boolean nouveau) {
		boolean refServVIH = false;
		for (int i = 0; i < dataValues.size(); i++) {
			if (dataValues.get(i).getElement().getUid().equals(referenceServicesVIHElement.getUid())) {
				String valeur = dataValues.get(i).getValue();
				if (valeur != null && !valeur.equals("") && !valeur.equals(" ")) {
					refServVIH = true;
				}
				break;
			}
		}

		if (refServVIH) {
			if (nouveau) {
				if (age < 15) {
					nouveau_10_14RefDep++;
				} else {
					nouveau_15_19RefDep++;
				}
			} else {
				if (age < 15) {
					ancien_10_14RefDep++;
				} else {
					ancien_15_19RefDep++;
				}
			}

		}

	}

	/*private void nbreFilleRefereDepistage() {
		List<String> organisation = new ArrayList<String>();
		List<BeneficiaireTDO> beneficiaireTDOs = new ArrayList<BeneficiaireTDO>();
		total = 0;
		nouveau_10_14 = 0;
		nouveau_15_19 = 0;
		ancien_10_14 = 0;
		ancien_15_19 = 0;

		organisation.add(this.organisationSelect.getId());
		Element idDreamsElement = ielement.getOneElmentByCode("id_dreams");
		Element referenceServicesVIH = ielement.getOneElmentByCode("referenceServicesVIH");

		// BenefEnrole NOUVEAU
		dataInstances = idataValues.dataAnalysePeriode(organisation, dossierBeneficiare.getId(), dateDebuts, dateFins);
		beneficiaireTDOs = ibeneficiaire.getBeneficiairePeriode(organisation, dateDebuts, dateFins);
		for (int benef = 0; benef < beneficiaireTDOs.size(); benef++) {
			boolean trouve = false;
			int age = 0;
			for (int i = 0; i < dataInstances.size(); i++) {
				for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
					if (dataInstances.get(i).getDataValue().get(b).getElement().equals(idDreamsElement.getUid())) {
						if (dataInstances.get(i).getDataValue().get(b).getValue()
								.equals(beneficiaireTDOs.get(benef).getId_dreams())) {
							for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
								if (dataInstances.get(i).getDataValue().get(sout).getElement()
										.equals(referenceServicesVIH.getUid())) {
									if (dataInstances.get(i).getDataValue().get(sout).getValue() != null
											&& !dataInstances.get(i).getDataValue().get(sout).getValue().equals("")) {
										age = Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement());
										trouve = true;
									}
								}
							}

						}
					}
				}
			}
			if (trouve) {
				if (age < 15) {
					nouveau_10_14++;
				} else {
					nouveau_15_19++;
				}
			}
		}
		// BenefEnrole Ancien
		dataInstances = idataValues.dataAnalysePreview(organisation, dossierBeneficiare.getId(), dateDebuts);
		beneficiaireTDOs = ibeneficiaire.getBeneficiairePreview(organisation, dateDebuts);
		for (int benef = 0; benef < beneficiaireTDOs.size(); benef++) {
			boolean trouve = false;
			int age = 0;
			for (int i = 0; i < dataInstances.size(); i++) {
				for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
					if (dataInstances.get(i).getDataValue().get(b).getElement().equals(idDreamsElement.getUid())) {
						if (dataInstances.get(i).getDataValue().get(b).getValue()
								.equals(beneficiaireTDOs.get(benef).getId_dreams())) {
							for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
								if (dataInstances.get(i).getDataValue().get(sout).getElement()
										.equals(referenceServicesVIH.getUid())) {
									if (dataInstances.get(i).getDataValue().get(sout).getValue() != null
											&& !dataInstances.get(i).getDataValue().get(sout).getValue().equals("")) {
										age = Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement());
										trouve = true;
									}
								}
							}

						}
					}
				}
			}
			if (trouve) {
				if (age < 15) {
					ancien_10_14++;
				} else {
					ancien_15_19++;
				}
			}
		}

		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;

		enregistreRapport("nbreFilleRefereDepistage");

	}
*/
	
	private void nbreContreReferenceDepistage(int age, boolean nouveau) {
		if (nouveau) {
			if (age < 15) {
				nouveau_10_14ContreRefDepistage++;
			} else {
				nouveau_15_19ContreRefDepistage++;
			}
		} else {
			if (age < 15) {
				ancien_10_14ContreRefDepistage++;
			} else {
				ancien_15_19ContreRefDepistage++;
			}
		}
		
	}

/*	private void nbreContreReferenceDepistage() {
		
		List<String> organisation = new ArrayList<String>();
		// List<BeneficiaireTDO> beneficiaireTDOs = new ArrayList<BeneficiaireTDO>();
		total = 0;
		nouveau_10_14 = 0;
		nouveau_15_19 = 0;
		ancien_10_14 = 0;
		ancien_15_19 = 0;

		organisation.add(this.organisationSelect.getId());
		Element agebenef = ielement.getOneElmentByCode("agebenef");
		Element datecontRef = ielement.getOneElmentByCode("datecontRef");
		Element motifref = ielement.getOneElmentByCode("motifref");

		// BenefEnrole NOUVEAU
		dataInstances = idataValues.dataAnalysePeriode(organisation, reference.getId(), dateDebuts, dateFins);

		for (int i = 0; i < dataInstances.size(); i++) {
			for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
				if (dataInstances.get(i).getDataValue().get(b).getElement().equals(datecontRef.getUid())) {
					if (!dataInstances.get(i).getDataValue().get(b).getValue().equals("")
							&& dataInstances.get(i).getDataValue().get(b).getValue() != null) {
						for (int motif = 0; motif < dataInstances.get(i).getDataValue().size(); motif++) {
							if (dataInstances.get(i).getDataValue().get(motif).getElement().equals(motifref.getUid())) {
								if (dataInstances.get(i).getDataValue().get(motif).getValue().contains("depistage")) {
									for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
										if (dataInstances.get(i).getDataValue().get(sout).getElement()
												.equals(agebenef.getUid())) {
											if (dataInstances.get(i).getDataValue().get(sout).getValue() != null
													&& !dataInstances.get(i).getDataValue().get(sout).getValue()
															.equals("")) {
												int age = Integer.parseInt(
														dataInstances.get(i).getDataValue().get(sout).getValue());
												if (age < 15) {
													nouveau_10_14++;
												} else {
													nouveau_15_19++;
												}
											}
										}
									}
								}
							}

						}

					}
				}
			}
		}

		// BenefEnrole Ancien
		dataInstances = idataValues.dataAnalysePreview(organisation, reference.getId(), dateDebuts);

		for (int i = 0; i < dataInstances.size(); i++) {
			for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
				if (dataInstances.get(i).getDataValue().get(b).getElement().equals(datecontRef.getUid())) {
					if (!dataInstances.get(i).getDataValue().get(b).getValue().equals("")
							&& dataInstances.get(i).getDataValue().get(b).getValue() != null) {
						for (int motif = 0; motif < dataInstances.get(i).getDataValue().size(); motif++) {
							if (dataInstances.get(i).getDataValue().get(motif).getElement().equals(motifref.getUid())) {
								if (dataInstances.get(i).getDataValue().get(motif).getValue().contains("depistage")) {
									for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
										if (dataInstances.get(i).getDataValue().get(sout).getElement()
												.equals(agebenef.getUid())) {
											if (dataInstances.get(i).getDataValue().get(sout).getValue() != null
													&& !dataInstances.get(i).getDataValue().get(sout).getValue()
															.equals("")) {
												int age = Integer.parseInt(
														dataInstances.get(i).getDataValue().get(sout).getValue());
												if (age < 15) {
													ancien_10_14++;
												} else {
													ancien_15_19++;
												}
											}
										}
									}
								}
							}

						}

					}
				}
			}
		}

		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;

		enregistreRapport("nbreContreReferencePreservatif");

	}
*/
	
	private void nbreFilleVBGrecuServicePsychoSocialJuridique(int age, List<DataValue> dataValues, boolean nouveau) {

		boolean VBGrecuPsyJuri = false;
		for (int i = 0; i < dataValues.size(); i++) {
			if (dataValues.get(i).getElement().getUid().equals(referenceJuridiqueElement.getUid())
					|| dataValues.get(i).getElement().getUid().equals(referencePsychoSocialElement.getUid())) {
				String valeur = dataValues.get(i).getValue();
				if (valeur != null && !valeur.equals("") && !valeur.equals(" ")) {
					VBGrecuPsyJuri = true;
				}
				break;
			}
		}

		if (VBGrecuPsyJuri) {
			if (nouveau) {
				if (age < 15) {
					nouveau_10_14VBGrecuPsyJuri++;
				} else {
					nouveau_15_19VBGrecuPsyJuri++;
				}
			} else {
				if (age < 15) {
					ancien_10_14VBGrecuPsyJuri++;
				} else {
					ancien_15_19VBGrecuPsyJuri++;
				}
			}

		}
	}

	/*private void nbreFilleVBGrecuServicePsychoSocialJuridique() {
		List<String> organisation = new ArrayList<String>();
		List<BeneficiaireTDO> beneficiaireTDOs = new ArrayList<BeneficiaireTDO>();
		total = 0;
		nouveau_10_14 = 0;
		nouveau_15_19 = 0;
		ancien_10_14 = 0;
		ancien_15_19 = 0;

		organisation.add(this.organisationSelect.getId());
		Element idDreamsElement = ielement.getOneElmentByCode("id_dreams");
		Element referencePsychoSocial = ielement.getOneElmentByCode("referencePsychoSocial");
		Element referenceJuridique = ielement.getOneElmentByCode("referenceJuridique");

		// BenefEnrole NOUVEAU
		dataInstances = idataValues.dataAnalysePeriode(organisation, dossierBeneficiare.getId(), dateDebuts, dateFins);
		beneficiaireTDOs = ibeneficiaire.getBeneficiairePeriode(organisation, dateDebuts, dateFins);
		for (int benef = 0; benef < beneficiaireTDOs.size(); benef++) {
			boolean trouve = false;
			int age = 0;
			for (int i = 0; i < dataInstances.size(); i++) {
				for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
					if (dataInstances.get(i).getDataValue().get(b).getElement().equals(idDreamsElement.getUid())) {
						if (dataInstances.get(i).getDataValue().get(b).getValue()
								.equals(beneficiaireTDOs.get(benef).getId_dreams())) {
							for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
								if (dataInstances.get(i).getDataValue().get(sout).getElement()
										.equals(referencePsychoSocial.getUid())
										|| dataInstances.get(i).getDataValue().get(sout).getElement()
												.equals(referenceJuridique.getUid())) {
									if (dataInstances.get(i).getDataValue().get(sout).getValue() != null
											&& !dataInstances.get(i).getDataValue().get(sout).getValue().equals("")) {
										age = Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement());
										trouve = true;
									}
								}
							}

						}
					}
				}
			}
			if (trouve) {
				if (age < 15) {
					nouveau_10_14++;
				} else {
					nouveau_15_19++;
				}
			}
		}
		// BenefEnrole Ancien
		dataInstances = idataValues.dataAnalysePreview(organisation, dossierBeneficiare.getId(), dateDebuts);
		beneficiaireTDOs = ibeneficiaire.getBeneficiairePreview(organisation, dateDebuts);
		for (int benef = 0; benef < beneficiaireTDOs.size(); benef++) {
			boolean trouve = false;
			int age = 0;
			for (int i = 0; i < dataInstances.size(); i++) {
				for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
					if (dataInstances.get(i).getDataValue().get(b).getElement().equals(idDreamsElement.getUid())) {
						if (dataInstances.get(i).getDataValue().get(b).getValue()
								.equals(beneficiaireTDOs.get(benef).getId_dreams())) {
							for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
								if (dataInstances.get(i).getDataValue().get(sout).getElement()
										.equals(referencePsychoSocial.getUid())
										|| dataInstances.get(i).getDataValue().get(sout).getElement()
												.equals(referenceJuridique.getUid())) {
									if (dataInstances.get(i).getDataValue().get(sout).getValue() != null
											&& !dataInstances.get(i).getDataValue().get(sout).getValue().equals("")) {
										age = Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement());
										trouve = true;
									}
								}
							}

						}
					}
				}
			}
			if (trouve) {
				if (age < 15) {
					ancien_10_14++;
				} else {
					ancien_15_19++;
				}
			}
		}

		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;

		enregistreRapport("nbreFilleVBGrecuServicePsychoSocialJuridique");

	}
*/
	
	private void nbreFilleVBGrefereMedicale(int age, List<DataValue> dataValues, boolean nouveau) {

		boolean VBGrefMedic = false;
		for (int i = 0; i < dataValues.size(); i++) {
			if (dataValues.get(i).getElement().getUid().equals(referenceMedicalElement.getUid())) {
				String valeur = dataValues.get(i).getValue();
				if (valeur != null && !valeur.equals("") && !valeur.equals(" ")) {
					VBGrefMedic = true;
				}
				break;
			}
		}

		if (VBGrefMedic) {
			if (nouveau) {
				if (age < 15) {
					nouveau_10_14VBGrefMedic++;
				} else {
					nouveau_15_19VBGrefMedic++;
				}
			} else {
				if (age < 15) {
					ancien_10_14VBGrefMedic++;
				} else {
					ancien_15_19VBGrefMedic++;
				}
			}

		}
		
	}

	/*private void nbreFilleVBGrefereMedicale() {
		List<String> organisation = new ArrayList<String>();
		List<BeneficiaireTDO> beneficiaireTDOs = new ArrayList<BeneficiaireTDO>();
		total = 0;
		nouveau_10_14 = 0;
		nouveau_15_19 = 0;
		ancien_10_14 = 0;
		ancien_15_19 = 0;

		organisation.add(this.organisationSelect.getId());
		Element idDreamsElement = ielement.getOneElmentByCode("id_dreams");
		Element referenceMedical = ielement.getOneElmentByCode("referenceMedical");

		// BenefEnrole NOUVEAU
		dataInstances = idataValues.dataAnalysePeriode(organisation, dossierBeneficiare.getId(), dateDebuts, dateFins);
		beneficiaireTDOs = ibeneficiaire.getBeneficiairePeriode(organisation, dateDebuts, dateFins);
		for (int benef = 0; benef < beneficiaireTDOs.size(); benef++) {
			boolean trouve = false;
			int age = 0;
			for (int i = 0; i < dataInstances.size(); i++) {
				for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
					if (dataInstances.get(i).getDataValue().get(b).getElement().equals(idDreamsElement.getUid())) {
						if (dataInstances.get(i).getDataValue().get(b).getValue()
								.equals(beneficiaireTDOs.get(benef).getId_dreams())) {
							for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
								if (dataInstances.get(i).getDataValue().get(sout).getElement()
										.equals(referenceMedical.getUid())) {
									if (dataInstances.get(i).getDataValue().get(sout).getValue() != null
											&& !dataInstances.get(i).getDataValue().get(sout).getValue().equals("")) {
										age = Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement());
										trouve = true;
									}
								}
							}

						}
					}
				}
			}
			if (trouve) {
				if (age < 15) {
					nouveau_10_14++;
				} else {
					nouveau_15_19++;
				}
			}
		}
		// BenefEnrole Ancien
		dataInstances = idataValues.dataAnalysePreview(organisation, dossierBeneficiare.getId(), dateDebuts);
		beneficiaireTDOs = ibeneficiaire.getBeneficiairePreview(organisation, dateDebuts);
		for (int benef = 0; benef < beneficiaireTDOs.size(); benef++) {
			boolean trouve = false;
			int age = 0;
			for (int i = 0; i < dataInstances.size(); i++) {
				for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
					if (dataInstances.get(i).getDataValue().get(b).getElement().equals(idDreamsElement.getUid())) {
						if (dataInstances.get(i).getDataValue().get(b).getValue()
								.equals(beneficiaireTDOs.get(benef).getId_dreams())) {
							for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
								if (dataInstances.get(i).getDataValue().get(sout).getElement()
										.equals(referenceMedical.getUid())) {
									if (dataInstances.get(i).getDataValue().get(sout).getValue() != null
											&& !dataInstances.get(i).getDataValue().get(sout).getValue().equals("")) {
										age = Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement());
										trouve = true;
									}
								}
							}

						}
					}
				}
			}
			if (trouve) {
				if (age < 15) {
					ancien_10_14++;
				} else {
					ancien_15_19++;
				}
			}
		}

		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;

		enregistreRapport("nbreFilleVBGrefereMedicale");

	}
*/
	
	private void nbreFilleVBGbeneficieSoutienMedicale(int age, List<DataValue> dataValues, boolean nouveau) {

		boolean VBGsoutMedic = false;
		for (int i = 0; i < dataValues.size(); i++) {
			if (dataValues.get(i).getElement().getUid().equals(fraisMedicauxElement.getUid())) {
				String valeur = dataValues.get(i).getValue();
				if (valeur != null && !valeur.equals("") && !valeur.equals(" ")) {
					VBGsoutMedic = true;
				}
				break;
			}
		}

		if (VBGsoutMedic) {
			if (nouveau) {
				if (age < 15) {
					nouveau_10_14VBGsoutMedic++;
				} else {
					nouveau_15_19VBGsoutMedic++;
				}
			} else {
				if (age < 15) {
					ancien_10_14VBGsoutMedic++;
				} else {
					ancien_15_19VBGsoutMedic++;
				}
			}

		}

	}

	/*private void nbreFilleVBGbeneficieSoutienMedicale() {
		List<String> organisation = new ArrayList<String>();
		List<BeneficiaireTDO> beneficiaireTDOs = new ArrayList<BeneficiaireTDO>();
		total = 0;
		nouveau_10_14 = 0;
		nouveau_15_19 = 0;
		ancien_10_14 = 0;
		ancien_15_19 = 0;

		organisation.add(this.organisationSelect.getId());
		Element idDreamsElement = ielement.getOneElmentByCode("id_dreams");
		Element fraisMedicaux = ielement.getOneElmentByCode("fraisMedicaux");

		// BenefEnrole NOUVEAU
		dataInstances = idataValues.dataAnalysePeriode(organisation, dossierBeneficiare.getId(), dateDebuts, dateFins);
		beneficiaireTDOs = ibeneficiaire.getBeneficiairePeriode(organisation, dateDebuts, dateFins);
		for (int benef = 0; benef < beneficiaireTDOs.size(); benef++) {
			boolean trouve = false;
			int age = 0;
			for (int i = 0; i < dataInstances.size(); i++) {
				for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
					if (dataInstances.get(i).getDataValue().get(b).getElement().equals(idDreamsElement.getUid())) {
						if (dataInstances.get(i).getDataValue().get(b).getValue()
								.equals(beneficiaireTDOs.get(benef).getId_dreams())) {
							for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
								if (dataInstances.get(i).getDataValue().get(sout).getElement()
										.equals(fraisMedicaux.getUid())) {
									if (dataInstances.get(i).getDataValue().get(sout).getValue() != null
											&& !dataInstances.get(i).getDataValue().get(sout).getValue().equals("")) {
										age = Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement());
										trouve = true;
									}
								}
							}

						}
					}
				}
			}
			if (trouve) {
				if (age < 15) {
					nouveau_10_14++;
				} else {
					nouveau_15_19++;
				}
			}
		}
		// BenefEnrole Ancien
		dataInstances = idataValues.dataAnalysePreview(organisation, dossierBeneficiare.getId(), dateDebuts);
		beneficiaireTDOs = ibeneficiaire.getBeneficiairePreview(organisation, dateDebuts);
		for (int benef = 0; benef < beneficiaireTDOs.size(); benef++) {
			boolean trouve = false;
			int age = 0;
			for (int i = 0; i < dataInstances.size(); i++) {
				for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
					if (dataInstances.get(i).getDataValue().get(b).getElement().equals(idDreamsElement.getUid())) {
						if (dataInstances.get(i).getDataValue().get(b).getValue()
								.equals(beneficiaireTDOs.get(benef).getId_dreams())) {
							for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
								if (dataInstances.get(i).getDataValue().get(sout).getElement()
										.equals(fraisMedicaux.getUid())) {
									if (dataInstances.get(i).getDataValue().get(sout).getValue() != null
											&& !dataInstances.get(i).getDataValue().get(sout).getValue().equals("")) {
										age = Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement());
										trouve = true;
									}
								}
							}

						}
					}
				}
			}
			if (trouve) {
				if (age < 15) {
					ancien_10_14++;
				} else {
					ancien_15_19++;
				}
			}
		}

		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;

		enregistreRapport("nbreFilleVBGbeneficieSoutienMedicale");

	}*/

	
	private void nbreFilleMalnutrieRefere() {
		List<String> organisation = new ArrayList<String>();
		List<BeneficiaireTDO> beneficiaireTDOs = new ArrayList<BeneficiaireTDO>();
		total = 0;
		nouveau_10_14 = 0;
		nouveau_15_19 = 0;
		ancien_10_14 = 0;
		ancien_15_19 = 0;

		organisation.add(this.organisationSelect.getId());
		Element idDreamsElement = ielement.getOneElmentByCode("id_dreams");
		Element fraisMedicaux = ielement.getOneElmentByCode("fraisMedicaux");

		// BenefEnrole NOUVEAU
		dataInstances = idataValues.dataAnalysePeriode(organisation, dossierBeneficiare.getId(), dateDebuts, dateFins);
		beneficiaireTDOs = ibeneficiaire.getBeneficiairePeriode(organisation, dateDebuts, dateFins);
		for (int benef = 0; benef < beneficiaireTDOs.size(); benef++) {
			boolean trouve = false;
			int age = 0;
			for (int i = 0; i < dataInstances.size(); i++) {
				for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
					if (dataInstances.get(i).getDataValue().get(b).getElement().equals(idDreamsElement.getUid())) {
						if (dataInstances.get(i).getDataValue().get(b).getValue()
								.equals(beneficiaireTDOs.get(benef).getId_dreams())) {
							for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
								if (dataInstances.get(i).getDataValue().get(sout).getElement()
										.equals(fraisMedicaux.getUid())) {
									if (dataInstances.get(i).getDataValue().get(sout).getValue() != null
											&& !dataInstances.get(i).getDataValue().get(sout).getValue().equals("")) {
										age = Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement());
										trouve = true;
									}
								}
							}

						}
					}
				}
			}
			if (trouve) {
				if (age < 15) {
					nouveau_10_14++;
				} else {
					nouveau_15_19++;
				}
			}
		}
		// BenefEnrole Ancien
		dataInstances = idataValues.dataAnalysePreview(organisation, dossierBeneficiare.getId(), dateDebuts);
		beneficiaireTDOs = ibeneficiaire.getBeneficiairePreview(organisation, dateDebuts);
		for (int benef = 0; benef < beneficiaireTDOs.size(); benef++) {
			boolean trouve = false;
			int age = 0;
			for (int i = 0; i < dataInstances.size(); i++) {
				for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
					if (dataInstances.get(i).getDataValue().get(b).getElement().equals(idDreamsElement.getUid())) {
						if (dataInstances.get(i).getDataValue().get(b).getValue()
								.equals(beneficiaireTDOs.get(benef).getId_dreams())) {
							for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
								if (dataInstances.get(i).getDataValue().get(sout).getElement()
										.equals(fraisMedicaux.getUid())) {
									if (dataInstances.get(i).getDataValue().get(sout).getValue() != null
											&& !dataInstances.get(i).getDataValue().get(sout).getValue().equals("")) {
										age = Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement());
										trouve = true;
									}
								}
							}

						}
					}
				}
			}
			if (trouve) {
				if (age < 15) {
					ancien_10_14++;
				} else {
					ancien_15_19++;
				}
			}
		}

		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;

		enregistreRapport(nbreFilleMalnutrieRefere);

	}

	/*private void nbreFilleMalnutrieRefere() {
		List<String> organisation = new ArrayList<String>();
		List<BeneficiaireTDO> beneficiaireTDOs = new ArrayList<BeneficiaireTDO>();
		total = 0;
		nouveau_10_14 = 0;
		nouveau_15_19 = 0;
		ancien_10_14 = 0;
		ancien_15_19 = 0;

		organisation.add(this.organisationSelect.getId());
		Element idDreamsElement = ielement.getOneElmentByCode("id_dreams");
		Element fraisMedicaux = ielement.getOneElmentByCode("fraisMedicaux");

		// BenefEnrole NOUVEAU
		dataInstances = idataValues.dataAnalysePeriode(organisation, dossierBeneficiare.getId(), dateDebuts, dateFins);
		beneficiaireTDOs = ibeneficiaire.getBeneficiairePeriode(organisation, dateDebuts, dateFins);
		for (int benef = 0; benef < beneficiaireTDOs.size(); benef++) {
			boolean trouve = false;
			int age = 0;
			for (int i = 0; i < dataInstances.size(); i++) {
				for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
					if (dataInstances.get(i).getDataValue().get(b).getElement().equals(idDreamsElement.getUid())) {
						if (dataInstances.get(i).getDataValue().get(b).getValue()
								.equals(beneficiaireTDOs.get(benef).getId_dreams())) {
							for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
								if (dataInstances.get(i).getDataValue().get(sout).getElement()
										.equals(fraisMedicaux.getUid())) {
									if (dataInstances.get(i).getDataValue().get(sout).getValue() != null
											&& !dataInstances.get(i).getDataValue().get(sout).getValue().equals("")) {
										age = Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement());
										trouve = true;
									}
								}
							}

						}
					}
				}
			}
			if (trouve) {
				if (age < 15) {
					nouveau_10_14++;
				} else {
					nouveau_15_19++;
				}
			}
		}
		// BenefEnrole Ancien
		dataInstances = idataValues.dataAnalysePreview(organisation, dossierBeneficiare.getId(), dateDebuts);
		beneficiaireTDOs = ibeneficiaire.getBeneficiairePreview(organisation, dateDebuts);
		for (int benef = 0; benef < beneficiaireTDOs.size(); benef++) {
			boolean trouve = false;
			int age = 0;
			for (int i = 0; i < dataInstances.size(); i++) {
				for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
					if (dataInstances.get(i).getDataValue().get(b).getElement().equals(idDreamsElement.getUid())) {
						if (dataInstances.get(i).getDataValue().get(b).getValue()
								.equals(beneficiaireTDOs.get(benef).getId_dreams())) {
							for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
								if (dataInstances.get(i).getDataValue().get(sout).getElement()
										.equals(fraisMedicaux.getUid())) {
									if (dataInstances.get(i).getDataValue().get(sout).getValue() != null
											&& !dataInstances.get(i).getDataValue().get(sout).getValue().equals("")) {
										age = Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement());
										trouve = true;
									}
								}
							}

						}
					}
				}
			}
			if (trouve) {
				if (age < 15) {
					ancien_10_14++;
				} else {
					ancien_15_19++;
				}
			}
		}

		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;

		enregistreRapport("nbreFilleMalnutrieRefere");

	}*/

	
	private void nbreFillebeneficieProtectionSociale(int age, List<DataValue> dataValues, boolean nouveau) {
		boolean protectSoc = false;
		for (int i = 0; i < dataValues.size(); i++) {
			if (dataValues.get(i).getElement().getUid().equals(fraisDocumentElement.getUid())) {
				String valeur = dataValues.get(i).getValue();
				if (valeur != null && !valeur.equals("") && !valeur.equals(" ")) {
					protectSoc = true;
				}
				break;
			}
		}

		if (protectSoc) {
			if (nouveau) {
				if (age < 15) {
					nouveau_10_14ProtectSoc++;
				} else {
					nouveau_15_19ProtectSoc++;
				}
			} else {
				if (age < 15) {
					ancien_10_14ProtectSoc++;
				} else {
					ancien_15_19ProtectSoc++;
				}
			}

		}
		
	}

	/*private void nbreFillebeneficieProtectionSociale() {
		List<String> organisation = new ArrayList<String>();
		List<BeneficiaireTDO> beneficiaireTDOs = new ArrayList<BeneficiaireTDO>();
		total = 0;
		nouveau_10_14 = 0;
		nouveau_15_19 = 0;
		ancien_10_14 = 0;
		ancien_15_19 = 0;

		organisation.add(this.organisationSelect.getId());
		Element idDreamsElement = ielement.getOneElmentByCode("id_dreams");
		Element fraisDocument = ielement.getOneElmentByCode("fraisDocument");

		// BenefEnrole NOUVEAU
		dataInstances = idataValues.dataAnalysePeriode(organisation, dossierBeneficiare.getId(), dateDebuts, dateFins);
		beneficiaireTDOs = ibeneficiaire.getBeneficiairePeriode(organisation, dateDebuts, dateFins);
		for (int benef = 0; benef < beneficiaireTDOs.size(); benef++) {
			boolean trouve = false;
			int age = 0;
			for (int i = 0; i < dataInstances.size(); i++) {
				for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
					if (dataInstances.get(i).getDataValue().get(b).getElement().equals(idDreamsElement.getUid())) {
						if (dataInstances.get(i).getDataValue().get(b).getValue()
								.equals(beneficiaireTDOs.get(benef).getId_dreams())) {
							for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
								if (dataInstances.get(i).getDataValue().get(sout).getElement()
										.equals(fraisDocument.getUid())) {
									if (dataInstances.get(i).getDataValue().get(sout).getValue() != null
											&& !dataInstances.get(i).getDataValue().get(sout).getValue().equals("")) {
										age = Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement());
										trouve = true;
									}
								}
							}

						}
					}
				}
			}
			if (trouve) {
				if (age < 15) {
					nouveau_10_14++;
				} else {
					nouveau_15_19++;
				}
			}
		}
		// BenefEnrole Ancien
		dataInstances = idataValues.dataAnalysePreview(organisation, dossierBeneficiare.getId(), dateDebuts);
		beneficiaireTDOs = ibeneficiaire.getBeneficiairePreview(organisation, dateDebuts);
		for (int benef = 0; benef < beneficiaireTDOs.size(); benef++) {
			boolean trouve = false;
			int age = 0;
			for (int i = 0; i < dataInstances.size(); i++) {
				for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
					if (dataInstances.get(i).getDataValue().get(b).getElement().equals(idDreamsElement.getUid())) {
						if (dataInstances.get(i).getDataValue().get(b).getValue()
								.equals(beneficiaireTDOs.get(benef).getId_dreams())) {
							for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
								if (dataInstances.get(i).getDataValue().get(sout).getElement()
										.equals(fraisDocument.getUid())) {
									if (dataInstances.get(i).getDataValue().get(sout).getValue() != null
											&& !dataInstances.get(i).getDataValue().get(sout).getValue().equals("")) {
										age = Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement());
										trouve = true;
									}
								}
							}

						}
					}
				}
			}
			if (trouve) {
				if (age < 15) {
					ancien_10_14++;
				} else {
					ancien_15_19++;
				}
			}
		}

		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;

		enregistreRapport("nbreFillebeneficieProtectionSociale");

	}*/

	
	private void nbreFille_18_19_ParticipeAVEC(int age, List<DataValue> dataValues, boolean nouveau) {
		boolean partAVEC = false;
		for (int i = 0; i < dataValues.size(); i++) {
			if (dataValues.get(i).getElement().getUid().equals(AVECelement.getUid())) {
				String valeur = dataValues.get(i).getValue();
				if (valeur != null && !valeur.equals("") && !valeur.equals(" ")) {
					partAVEC = true;
				}
				break;
			}
		}

		if (partAVEC) {
			if (nouveau) {
				if (age < 15) {
					nouveau_10_14PartAVEC++;
				} else {
					nouveau_15_19PartAVEC++;
				}
			} else {
				if (age < 15) {
					ancien_10_14PartAVEC++;
				} else {
					ancien_15_19PartAVEC++;
				}
			}

		}
	}

	/*private void nbreFille_18_19_ParticipeAVEC() {
		List<String> organisation = new ArrayList<String>();
		List<BeneficiaireTDO> beneficiaireTDOs = new ArrayList<BeneficiaireTDO>();
		total = 0;
		nouveau_10_14 = 0;
		nouveau_15_19 = 0;
		ancien_10_14 = 0;
		ancien_15_19 = 0;

		organisation.add(this.organisationSelect.getId());
		Element idDreamsElement = ielement.getOneElmentByCode("id_dreams");
		Element AVEC = ielement.getOneElmentByCode("participationAVEC");

		// BenefEnrole NOUVEAU
		dataInstances = idataValues.dataAnalysePeriode(organisation, dossierBeneficiare.getId(), dateDebuts, dateFins);
		beneficiaireTDOs = ibeneficiaire.getBeneficiairePeriode(organisation, dateDebuts, dateFins);
		for (int benef = 0; benef < beneficiaireTDOs.size(); benef++) {
			boolean trouve = false;
			int age = 0;
			for (int i = 0; i < dataInstances.size(); i++) {
				for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
					if (dataInstances.get(i).getDataValue().get(b).getElement().equals(idDreamsElement.getUid())) {
						if (dataInstances.get(i).getDataValue().get(b).getValue()
								.equals(beneficiaireTDOs.get(benef).getId_dreams())) {
							for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
								if (dataInstances.get(i).getDataValue().get(sout).getElement().equals(AVEC.getUid())) {
									if (dataInstances.get(i).getDataValue().get(sout).getValue() != null
											&& !dataInstances.get(i).getDataValue().get(sout).getValue().equals("")) {
										age = Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement());
										trouve = true;
									}
								}
							}

						}
					}
				}
			}
			if (trouve) {
				if (age < 15) {
					nouveau_10_14++;
				} else {
					nouveau_15_19++;
				}
			}
		}
		// BenefEnrole Ancien
		dataInstances = idataValues.dataAnalysePreview(organisation, dossierBeneficiare.getId(), dateDebuts);
		beneficiaireTDOs = ibeneficiaire.getBeneficiairePreview(organisation, dateDebuts);
		for (int benef = 0; benef < beneficiaireTDOs.size(); benef++) {
			boolean trouve = false;
			int age = 0;
			for (int i = 0; i < dataInstances.size(); i++) {
				for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
					if (dataInstances.get(i).getDataValue().get(b).getElement().equals(idDreamsElement.getUid())) {
						if (dataInstances.get(i).getDataValue().get(b).getValue()
								.equals(beneficiaireTDOs.get(benef).getId_dreams())) {
							for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
								if (dataInstances.get(i).getDataValue().get(sout).getElement().equals(AVEC.getUid())) {
									if (dataInstances.get(i).getDataValue().get(sout).getValue() != null
											&& !dataInstances.get(i).getDataValue().get(sout).getValue().equals("")) {
										age = Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement());
										trouve = true;
									}
								}
							}

						}
					}
				}
			}
			if (trouve) {
				if (age < 15) {
					ancien_10_14++;
				} else {
					ancien_15_19++;
				}
			}
		}

		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;

		enregistreRapport("nbreFille_18_19_ParticipeAVEC");

	}*/

	
	/*private void nbreParentParticipeAVEC() {
		List<String> organisation = new ArrayList<String>();
		// List<BeneficiaireTDO> beneficiaireTDOs = new ArrayList<BeneficiaireTDO>();
		total = 0;
		nouveau_10_14 = 0;
		nouveau_15_19 = 0;
		ancien_10_14 = 0;
		ancien_15_19 = 0;

		organisation.add(this.organisationSelect.getId());
		// Element idDreamsElement = ielement.getOneElmentByCode("id_dreams");
		Element thematique = ielement.getOneElmentByCode("thematique");
		Element nomPrenomAutreCible = ielement.getOneElmentByCode("nomPrenomAutreCible");

		// BenefEnrole NOUVEAU
		dataInstances = idataValues.dataAnalysePeriode(organisation, groupe.getId(), dateDebuts, dateFins);
		for (int i = 0; i < dataInstances.size(); i++) {
			for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
				if (dataInstances.get(i).getDataValue().get(b).getElement().equals(thematique.getUid())) {
					if (dataInstances.get(i).getDataValue().get(b).getValue().equals("avec")) {
						for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
							if (dataInstances.get(i).getDataValue().get(sout).getElement()
									.equals(nomPrenomAutreCible.getUid())) {
								if (!dataInstances.get(i).getDataValue().get(sout).getValue().equals("")
										&& dataInstances.get(i).getDataValue().get(sout).getValue() != null) {
									total++;
								}

							}
						}
					}
				}
			}
		}
		// }
		// BenefEnrole Ancien
		dataInstances = idataValues.dataAnalysePreview(organisation, groupe.getId(), dateDebuts);
		for (int i = 0; i < dataInstances.size(); i++) {
			for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
				if (dataInstances.get(i).getDataValue().get(b).getElement().equals(thematique.getUid())) {
					if (dataInstances.get(i).getDataValue().get(b).getValue().equals("avec")) {
						for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
							if (dataInstances.get(i).getDataValue().get(sout).getElement()
									.equals(nomPrenomAutreCible.getUid())) {
								if (!dataInstances.get(i).getDataValue().get(sout).getValue().equals("")
										&& dataInstances.get(i).getDataValue().get(sout).getValue() != null) {
									total++;
								}

							}
						}
					}
				}
			}
		}

		// total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;

		enregistreRapport("nbreParentParticipeAVEC");

	}
*/
	
	private void nbreFillebeneficieSoutienRenforcementEconomique(int age, List<DataValue> dataValues, boolean nouveau) {
		boolean soutRenfEco = false;
		for (int i = 0; i < dataValues.size(); i++) {
			if (dataValues.get(i).getElement().getUid().equals(businessPlusElement.getUid())) {
				String valeur = dataValues.get(i).getValue();
				if (valeur != null && !valeur.equals("") && !valeur.equals(" ")) {
					soutRenfEco = true;
				}
				break;
			}
		}

		if (soutRenfEco) {
			if (nouveau) {
				if (age < 15) {
					nouveau_10_14SoutRenfEco++;
				} else {
					nouveau_15_19SoutRenfEco++;
				}
			} else {
				if (age < 15) {
					ancien_10_14SoutRenfEco++;
				} else {
					ancien_15_19SoutRenfEco++;
				}
			}

		}

	}

	/*private void nbreFillebeneficieSoutienRenforcementEconomique() {
		List<String> organisation = new ArrayList<String>();
		List<BeneficiaireTDO> beneficiaireTDOs = new ArrayList<BeneficiaireTDO>();
		total = 0;
		nouveau_10_14 = 0;
		nouveau_15_19 = 0;
		ancien_10_14 = 0;
		ancien_15_19 = 0;

		organisation.add(this.organisationSelect.getId());
		Element idDreamsElement = ielement.getOneElmentByCode("id_dreams");
		Element businessPlus = ielement.getOneElmentByCode("businessPlus");

		// BenefEnrole NOUVEAU
		dataInstances = idataValues.dataAnalysePeriode(organisation, dossierBeneficiare.getId(), dateDebuts, dateFins);
		beneficiaireTDOs = ibeneficiaire.getBeneficiairePeriode(organisation, dateDebuts, dateFins);
		for (int benef = 0; benef < beneficiaireTDOs.size(); benef++) {
			boolean trouve = false;
			int age = 0;
			for (int i = 0; i < dataInstances.size(); i++) {
				for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
					if (dataInstances.get(i).getDataValue().get(b).getElement().equals(idDreamsElement.getUid())) {
						if (dataInstances.get(i).getDataValue().get(b).getValue()
								.equals(beneficiaireTDOs.get(benef).getId_dreams())) {
							for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
								if (dataInstances.get(i).getDataValue().get(sout).getElement()
										.equals(businessPlus.getUid())) {
									if (dataInstances.get(i).getDataValue().get(sout).getValue() != null
											&& !dataInstances.get(i).getDataValue().get(sout).getValue().equals("")) {
										age = Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement());
										trouve = true;
									}
								}
							}

						}
					}
				}
			}
			if (trouve) {
				if (age < 15) {
					nouveau_10_14++;
				} else {
					nouveau_15_19++;
				}
			}
		}
		// BenefEnrole Ancien
		dataInstances = idataValues.dataAnalysePreview(organisation, dossierBeneficiare.getId(), dateDebuts);
		beneficiaireTDOs = ibeneficiaire.getBeneficiairePreview(organisation, dateDebuts);
		for (int benef = 0; benef < beneficiaireTDOs.size(); benef++) {
			boolean trouve = false;
			int age = 0;
			for (int i = 0; i < dataInstances.size(); i++) {
				for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
					if (dataInstances.get(i).getDataValue().get(b).getElement().equals(idDreamsElement.getUid())) {
						if (dataInstances.get(i).getDataValue().get(b).getValue()
								.equals(beneficiaireTDOs.get(benef).getId_dreams())) {
							for (int sout = 0; sout < dataInstances.get(i).getDataValue().size(); sout++) {
								if (dataInstances.get(i).getDataValue().get(sout).getElement()
										.equals(businessPlus.getUid())) {
									if (dataInstances.get(i).getDataValue().get(sout).getValue() != null
											&& !dataInstances.get(i).getDataValue().get(sout).getValue().equals("")) {
										age = Integer.parseInt(beneficiaireTDOs.get(benef).getAgeEnrolement());
										trouve = true;
									}
								}
							}

						}
					}
				}
			}
			if (trouve) {
				if (age < 15) {
					ancien_10_14++;
				} else {
					ancien_15_19++;
				}
			}
		}

		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;

		enregistreRapport("nbreFillebeneficieSoutienRenforcementEconomique");

	}*/

	
	private void nbreFilleActivesEtInactives(int age, List<DataValue> dataValues, boolean nouveau) {
		boolean actif = false;
		for (int i = 0; i < dataValues.size(); i++) {
			if (dataValues.get(i).getValue() != null && !dataValues.get(i).getValue().equals("")
					&& !dataValues.get(i).getValue().equals(" ")) {
				for (int j = 0; j < elementsIntervention.size(); j++) {
					if (dataValues.get(i).getElement().getUid().equals(elementsIntervention.get(j).getUid())) {
						actif = true;
						break;
					}
				}
			}
			if (actif) {
				break;
			}
		}

		if (actif) {
			if (nouveau) {
				if (age < 15) {
					nouveau_10_14Actives++;
				} else {
					nouveau_15_19Actives++;
				}
			} else {
				if (age < 15) {
					ancien_10_14Actives++;
				} else {
					ancien_15_19Actives++;
				}

			}
		}else {

			if (nouveau) {
				if (age < 15) {
					nouveau_10_14Inactives++;
				} else {
					nouveau_15_19Inactives++;
				}
			} else {
				if (age < 15) {
					ancien_10_14Inactives++;
				} else {
					ancien_15_19Inactives++;
				}

			}
		
		}

	}

	/*private void nbreFilleActivesEtInactives() {
		List<String> organisation = new ArrayList<String>();
		// nouveau = 0;
		// ancien = 0;
		total = 0;
		nouveau_10_14 = 0;
		nouveau_15_19 = 0;
		ancien_10_14 = 0;
		ancien_15_19 = 0;

		// int nouveauExtra = 0;
		// int ancienExtra = 0;
		// int totalExtra = 0;
		int nouveau_10_14Actives = 0;
		int nouveau_15_19Actives = 0;
		int ancien_10_14Actives = 0;
		int ancien_15_19Actives = 0;

		int nouveau_10_14Inactives = 0;
		int nouveau_15_19Inactives = 0;
		int ancien_10_14Inactives = 0;
		int ancien_15_19Inactives = 0;

		organisation.add(this.organisationSelect.getId());
		// Element element = ielement.getOneElmentByCode("porteEntree");
		Element ageEnrolElement = ielement.getOneElmentByCode("age_enrol");

		// nbreBenefEnrole NOUVEAU
		dataInstances = idataValues.dataAnalysePeriode(organisation, dossierBeneficiare.getId(), dateDebuts, dateFins);
		for (int i = 0; i < dataInstances.size(); i++) {
			int age = 0;
			int nbreInterv = 0;
			for (int eleInter = 0; eleInter < elementsIntervention.size(); eleInter++) {
				for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
					if (dataInstances.get(i).getDataValue().get(b).getElement()
							.equals(elementsIntervention.get(eleInter).getUid())) {
						if (dataInstances.get(i).getDataValue().get(b).getValue() != null
								&& !dataInstances.get(i).getDataValue().get(b).getValue().equals("")) {

							for (int ag = 0; ag < dataInstances.get(i).getDataValue().size(); ag++) {
								if (dataInstances.get(i).getDataValue().get(ag).getElement()
										.equals(ageEnrolElement.getUid())) {
									nbreInterv++;
									age = Integer.parseInt(dataInstances.get(i).getDataValue().get(ag).getValue());

									break;
								}
							}
						}
						break;
					}
				}

			}
			if (nbreInterv == 0) {
				if (age < 15) {
					nouveau_10_14Inactives++;
				} else {
					nouveau_15_19Inactives++;
				}
			} else {
				if (age < 15) {
					nouveau_10_14Actives++;
				} else {
					nouveau_15_19Actives++;
				}
			}
		}

		// nbreBenefEnrole ANCIEN
		dataInstances = idataValues.dataAnalysePreview(organisation, dossierBeneficiare.getId(), dateDebuts);
		// ancien = dataInstances.size();
		for (int i = 0; i < dataInstances.size(); i++) {
			int age = 0;
			int nbreInterv = 0;
			for (int eleInter = 0; eleInter < elementsIntervention.size(); eleInter++) {
				for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
					if (dataInstances.get(i).getDataValue().get(b).getElement()
							.equals(elementsIntervention.get(eleInter).getUid())) {
						if (dataInstances.get(i).getDataValue().get(b).getValue() != null
								&& !dataInstances.get(i).getDataValue().get(b).getValue().equals("")) {

							for (int ag = 0; ag < dataInstances.get(i).getDataValue().size(); ag++) {
								if (dataInstances.get(i).getDataValue().get(ag).getElement()
										.equals(ageEnrolElement.getUid())) {
									nbreInterv++;
									age = Integer.parseInt(dataInstances.get(i).getDataValue().get(ag).getValue());

									break;
								}
							}
						}
						break;
					}
				}

			}
			if (nbreInterv == 0) {
				if (age < 15) {
					ancien_10_14Inactives++;
				} else {
					ancien_15_19Inactives++;
				}
			} else {
				if (age < 15) {
					ancien_10_14Actives++;
				} else {
					ancien_15_19Actives++;
				}
			}
		}
		nouveau_10_14 = nouveau_10_14Inactives;
		nouveau_15_19 = nouveau_15_19Inactives;
		ancien_10_14 = ancien_10_14Inactives;
		ancien_15_19 = ancien_15_19Inactives;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport("nbreFilleInactives");

		nouveau_10_14 = nouveau_10_14Actives;
		nouveau_15_19 = nouveau_15_19Actives;
		ancien_10_14 = ancien_10_14Actives;
		ancien_15_19 = ancien_15_19Actives;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport("nbreFilleActives");

	}*/

	
	private void nbreInterventionsPrimaire(int age, List<DataValue> dataValues, boolean nouveau) {
		int nbreInterv = 0;
		for (int i = 0; i < dataValues.size(); i++) {
			if (dataValues.get(i).getValue() != null && !dataValues.get(i).getValue().equals("")
					&& !dataValues.get(i).getValue().equals(" ")) {
				for (int j = 0; j < elementsInterventionPrimaire.size(); j++) {
					if (dataValues.get(i).getElement().getUid().equals(elementsInterventionPrimaire.get(j).getUid())) {
						nbreInterv++;
						break;
					}
				}
			}
		}
		if (nbreInterv != 0) {
			if(nouveau) {
				// autour de 5 intervention primaires
				if (nbreInterv < 5) {
					if (age < 15) {
						nouveau_10_14Mois5++;
					} else {
						nouveau_15_19Mois5++;
					}
				}
				if (nbreInterv == 5) {
					if (age < 15) {
						nouveau_10_14_5++;
					} else {
						nouveau_15_19_5++;
					}
				}
				if (nbreInterv > 5) {
					if (age < 15) {
						nouveau_10_14Plus5++;
					} else {
						nouveau_15_19Plus5++;
					}
				}

				// autour de 6 intervention primaires
				if (nbreInterv < 6) {
					if (age < 15) {
						nouveau_10_14Mois6++;
					} else {
						nouveau_15_19Mois6++;
					}
				}
				if (nbreInterv == 6) {
					if (age < 15) {
						nouveau_10_14_6++;
					} else {
						nouveau_15_19_6++;
					}
				}
				if (nbreInterv > 6) {
					if (age < 15) {
						nouveau_10_14Plus6++;
					} else {
						nouveau_15_19Plus6++;
					}
				}
			}else {
				// autour de 5 intervention primaires
				if (nbreInterv < 5) {
					if (age < 15) {
						ancien_10_14Mois5++;
					} else {
						ancien_15_19Mois5++;
					}
				}
				if (nbreInterv == 5) {
					if (age < 15) {
						ancien_10_14_5++;
					} else {
						ancien_15_19_5++;
					}
				}
				if (nbreInterv > 5) {
					if (age < 15) {
						ancien_10_14Plus5++;
					} else {
						ancien_15_19Plus5++;
					}
				}
				// autour de 6 intervention primaires
				if (nbreInterv < 6) {
					if (age < 15) {
						ancien_10_14Mois6++;
					} else {
						ancien_15_19Mois6++;
					}
				}
				if (nbreInterv == 6) {
					if (age < 15) {
						ancien_10_14_6++;
					} else {
						ancien_15_19_6++;
					}
				}
				if (nbreInterv > 6) {
					if (age < 15) {
						ancien_10_14Plus6++;
					} else {
						ancien_15_19Plus6++;
					}
				}
			
			}
			
		}
	}
	
	private void gestProportion() {
		enregistreProportion(proportionFille_recu_moins_5_interventionsPrimaire,
				nbreFilleRecuMoins5InterventionsPrimaire, nbreFilleActives);
		enregistreProportion(proportionFille_recu_5_interventionsPrimaire, nbreFilleRecu5InterventionsPrimaire,
				nbreFilleActives);
		enregistreProportion(proportionFille_recu_plus_5_interventionsPrimaire,
				nbreFilleRecuPlus5InterventionsPrimaire, nbreFilleActives);
		enregistreProportion(ProportionFille_recu_moins_6_interventionsPrimaire,
				nbreFilleRecuMoins6InterventionsPrimaire, nbreFilleActives);
		enregistreProportion(ProportionFille_recu_6_interventionsPrimaire, nbreFilleRecu6InterventionsPrimaire,
				nbreFilleActives);
		enregistreProportion(ProportionFille_recu_plus_6_interventionsPrimaire,
				nbreFilleRecuPlus6InterventionsPrimaire, nbreFilleActives);
	}

	/*private void nbreInterventionsPrimaire() {
		List<String> organisation = new ArrayList<String>();

		/*
		 * LocalDate newDaydate = null; LocalDate todaydate = LocalDate.parse(dateFins);
		 * newDaydate = todaydate.plusMonths(1); String dateFinLocal =
		 * newDaydate.with(TemporalAdjusters.firstDayOfMonth()).toString(); //String
		 * dateFin = newDaydate.with(TemporalAdjusters.lastDayOfMonth()).toString();
		 * //String ladate = newDaydate.toString(); //String laPeriod =
		 * ladate.substring(0, 4) + ladate.substring(5, 7);
		 * 
		 * System.out.println("todaydate = " + todaydate);
		 * System.out.println("newDaydate = " + newDaydate);
		 * System.out.println("dateFinLocal = " + dateFinLocal);
		 *

		total = 0;
		nouveau_10_14 = 0;
		nouveau_15_19 = 0;
		ancien_10_14 = 0;
		ancien_15_19 = 0;

		// autour de 5 intervention primaires
		int nouveau_10_14Mois5 = 0;
		int nouveau_15_19Mois5 = 0;
		int ancien_10_14Mois5 = 0;
		int ancien_15_19Mois5 = 0;

		int nouveau_10_14_5 = 0;
		int nouveau_15_19_5 = 0;
		int ancien_10_14_5 = 0;
		int ancien_15_19_5 = 0;

		int nouveau_10_14Plus5 = 0;
		int nouveau_15_19Plus5 = 0;
		int ancien_10_14Plus5 = 0;
		int ancien_15_19Plus5 = 0;

		// autour de 6 intervention primaires
		int nouveau_10_14Mois6 = 0;
		int nouveau_15_19Mois6 = 0;
		int ancien_10_14Mois6 = 0;
		int ancien_15_19Mois6 = 0;

		int nouveau_10_14_6 = 0;
		int nouveau_15_19_6 = 0;
		int ancien_10_14_6 = 0;
		int ancien_15_19_6 = 0;

		int nouveau_10_14Plus6 = 0;
		int nouveau_15_19Plus6 = 0;
		int ancien_10_14Plus6 = 0;
		int ancien_15_19Plus6 = 0;

		organisation.add(this.organisationSelect.getId());
		// Element element = ielement.getOneElmentByCode("porteEntree");
		Element ageEnrolElement = ielement.getOneElmentByCode("age_enrol");

		// nbreBenefEnrole NOUVEAU
		dataInstances = idataValues.dataAnalysePeriode(organisation, dossierBeneficiare.getId(), dateDebuts, dateFins);
		for (int i = 0; i < dataInstances.size(); i++) {
			int age = 0;
			int nbreInterv = 0;
			for (int eleInter = 0; eleInter < elementsInterventionPrimaire.size(); eleInter++) {
				for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
					if (dataInstances.get(i).getDataValue().get(b).getElement()
							.equals(elementsInterventionPrimaire.get(eleInter).getUid())) {
						if (dataInstances.get(i).getDataValue().get(b).getValue() != null
								&& !dataInstances.get(i).getDataValue().get(b).getValue().equals("")) {
							for (int ag = 0; ag < dataInstances.get(i).getDataValue().size(); ag++) {
								if (dataInstances.get(i).getDataValue().get(ag).getElement()
										.equals(ageEnrolElement.getUid())) {
									nbreInterv++;
									age = Integer.parseInt(dataInstances.get(i).getDataValue().get(ag).getValue());

									break;
								}
							}
						}
						break;
					}
				}

			}
			if (nbreInterv != 0) {
				if (nbreInterv < 5) {
					if (age < 15) {
						nouveau_10_14Mois5++;
					} else {
						nouveau_15_19Mois5++;
					}
				}
				if (nbreInterv == 5) {
					if (age < 15) {
						nouveau_10_14_5++;
					} else {
						nouveau_15_19_5++;
					}
				}
				if (nbreInterv > 5) {
					if (age < 15) {
						nouveau_10_14Plus5++;
					} else {
						nouveau_15_19Plus5++;
					}
				}

				if (nbreInterv < 6) {
					if (age < 15) {
						nouveau_10_14Mois6++;
					} else {
						nouveau_15_19Mois6++;
					}
				}
				if (nbreInterv == 6) {
					if (age < 15) {
						nouveau_10_14_6++;
					} else {
						nouveau_15_19_6++;
					}
				}
				if (nbreInterv > 6) {
					if (age < 15) {
						nouveau_10_14Plus6++;
					} else {
						nouveau_15_19Plus6++;
					}
				}
			}
		}
		// nbreBenefEnrole ANCIEN
		dataInstances = idataValues.dataAnalysePreview(organisation, dossierBeneficiare.getId(), dateDebuts);
		// ancien = dataInstances.size();
		for (int i = 0; i < dataInstances.size(); i++) {
			int age = 0;
			int nbreInterv = 0;
			for (int eleInter = 0; eleInter < elementsInterventionPrimaire.size(); eleInter++) {
				for (int b = 0; b < dataInstances.get(i).getDataValue().size(); b++) {
					if (dataInstances.get(i).getDataValue().get(b).getElement()
							.equals(elementsInterventionPrimaire.get(eleInter).getUid())) {
						if (dataInstances.get(i).getDataValue().get(b).getValue() != null
								&& !dataInstances.get(i).getDataValue().get(b).getValue().equals("")) {
							for (int ag = 0; ag < dataInstances.get(i).getDataValue().size(); ag++) {
								if (dataInstances.get(i).getDataValue().get(ag).getElement()
										.equals(ageEnrolElement.getUid())) {
									nbreInterv++;
									age = Integer.parseInt(dataInstances.get(i).getDataValue().get(ag).getValue());

									break;
								}
							}
						}
						break;
					}
				}

			}
			if (nbreInterv != 0) {
				if (nbreInterv < 5) {
					if (age < 15) {
						ancien_10_14Mois5++;
					} else {
						ancien_15_19Mois5++;
					}
				}
				if (nbreInterv == 5) {
					if (age < 15) {
						ancien_10_14_5++;
					} else {
						ancien_15_19_5++;
					}
				}
				if (nbreInterv > 5) {
					if (age < 15) {
						ancien_10_14Plus5++;
					} else {
						ancien_15_19Plus5++;
					}
				}

				if (nbreInterv < 6) {
					if (age < 15) {
						ancien_10_14Mois6++;
					} else {
						ancien_15_19Mois6++;
					}
				}
				if (nbreInterv == 6) {
					if (age < 15) {
						ancien_10_14_6++;
					} else {
						ancien_15_19_6++;
					}
				}
				if (nbreInterv > 6) {
					if (age < 15) {
						ancien_10_14Plus6++;
					} else {
						ancien_15_19Plus6++;
					}
				}
			}
		}
		// autour de 5 intervention primaires

		nouveau_10_14 = nouveau_10_14Mois5;
		nouveau_15_19 = nouveau_15_19Mois5;
		ancien_10_14 = ancien_10_14Mois5;
		ancien_15_19 = ancien_15_19Mois5;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport("nbreFilleRecuMoins5InterventionsPrimaire");
		enregistreProportion("proportionFille_recu_moins_5_interventionsPrimaire",
				"nbreFilleRecuMoins5InterventionsPrimaire", "nbreFilleActives");

		nouveau_10_14 = nouveau_10_14_5;
		nouveau_15_19 = nouveau_15_19_5;
		ancien_10_14 = ancien_10_14_5;
		ancien_15_19 = ancien_15_19_5;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport("nbreFilleRecu5InterventionsPrimaire");
		enregistreProportion("proportionFille_recu_5_interventionsPrimaire", "nbreFilleRecu5InterventionsPrimaire",
				"nbreFilleActives");

		nouveau_10_14 = nouveau_10_14Plus5;
		nouveau_15_19 = nouveau_15_19Plus5;
		ancien_10_14 = ancien_10_14Plus5;
		ancien_15_19 = ancien_15_19Plus5;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport("nbreFilleRecuPlus5InterventionsPrimaire");
		enregistreProportion("proportionFille_recu_plus_5_interventionsPrimaire",
				"nbreFilleRecuPlus5InterventionsPrimaire", "nbreFilleActives");

		// autour de 6 intervention primaires

		nouveau_10_14 = nouveau_10_14Mois6;
		nouveau_15_19 = nouveau_15_19Mois6;
		ancien_10_14 = ancien_10_14Mois6;
		ancien_15_19 = ancien_15_19Mois6;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport("nbreFilleRecuMoins6InterventionsPrimaire");
		enregistreProportion("ProportionFille_recu_moins_6_interventionsPrimaire",
				"nbreFilleRecuMoins6InterventionsPrimaire", "nbreFilleActives");

		nouveau_10_14 = nouveau_10_14_6;
		nouveau_15_19 = nouveau_15_19_6;
		ancien_10_14 = ancien_10_14_6;
		ancien_15_19 = ancien_15_19_6;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport("nbreFilleRecu6InterventionsPrimaire");
		enregistreProportion("ProportionFille_recu_6_interventionsPrimaire", "nbreFilleRecu6InterventionsPrimaire",
				"nbreFilleActives");

		nouveau_10_14 = nouveau_10_14Plus6;
		nouveau_15_19 = nouveau_15_19Plus6;
		ancien_10_14 = ancien_10_14Plus6;
		ancien_15_19 = ancien_15_19Plus6;
		total = nouveau_10_14 + nouveau_15_19 + ancien_10_14 + ancien_15_19;
		enregistreRapport("nbreFilleRecuPlus6InterventionsPrimaire");
		enregistreProportion("ProportionFille_recu_plus_6_interventionsPrimaire",
				"nbreFilleRecuPlus6InterventionsPrimaire", "nbreFilleActives");

	}*/

	
	private void enregistreProportion(Element elementProportion, Element elementNumerateur, Element elementDenominateur) {
		List<Rapport> rapportsElementNumerateur = new ArrayList<Rapport>();
		List<Rapport> rapportsElementDenominateur = new ArrayList<Rapport>();
		List<Rapport> rapportsElementProportion = new ArrayList<Rapport>();
		List<String> childrens = new ArrayList<String>();
		
		int nouveau_10_14Num = 0;
		int nouveau_15_19Num = 0;
		int ancien_10_14Num = 0;
		int ancien_15_19Num = 0;
		int totalNum = 0;
		int nouveau_10_14Den = 0;
		int nouveau_15_19Den = 0;
		int ancien_10_14Den = 0;
		int ancien_15_19Den = 0;
		int totalDen = 0;
		
		nouveau_10_14 = 0;
		nouveau_15_19 = 0;
		ancien_10_14 = 0;
		ancien_15_19 = 0;
		total = 0;
		
		childrens.add(this.organisationSelect.getId());
		if (!this.organisationSelect.getChildrens().isEmpty()) {
			for (int comp = 0; comp < this.organisationSelect.getChildrens().size(); comp++) {
				childrens.add(this.organisationSelect.getChildrens().get(comp).getId());
			}
		}

		rapportsElementNumerateur = irapport.getRapportOptionRapportCodeNull(childrens, elementNumerateur.getCode(), laPeriode);
		rapportsElementDenominateur = irapport.getRapportOptionRapportCodeNull(childrens, elementDenominateur.getCode(),
				laPeriode);

		if (!rapportsElementNumerateur.isEmpty() && !rapportsElementDenominateur.isEmpty()) {

			for (int rap = 0; rap < rapportsElementNumerateur.size(); rap++) {
				if (rapportsElementNumerateur.get(rap).getOption() == null) {
					totalNum += rapportsElementNumerateur.get(rap).getValeurs();
				} else {
					if (rapportsElementNumerateur.get(rap).getOption().getCode().equals("nouveau_10_14")) {
						nouveau_10_14Num += rapportsElementNumerateur.get(rap).getValeurs();
					}
					if (rapportsElementNumerateur.get(rap).getOption().getCode().equals("nouveau_15_19")) {
						nouveau_15_19Num += rapportsElementNumerateur.get(rap).getValeurs();
					}
					if (rapportsElementNumerateur.get(rap).getOption().getCode().equals("ancien_10_14")) {
						ancien_10_14Num += rapportsElementNumerateur.get(rap).getValeurs();
					}
					if (rapportsElementNumerateur.get(rap).getOption().getCode().equals("ancien_15_19")) {
						ancien_15_19Num += rapportsElementNumerateur.get(rap).getValeurs();
					}
				}
			}

			for (int rap = 0; rap < rapportsElementDenominateur.size(); rap++) {
				if (rapportsElementDenominateur.get(rap).getOption() == null) {
					totalDen += rapportsElementDenominateur.get(rap).getValeurs();
				} else {
					if (rapportsElementDenominateur.get(rap).getOption().getCode().equals("nouveau_10_14")) {
						nouveau_10_14Den += rapportsElementDenominateur.get(rap).getValeurs();
					}
					if (rapportsElementDenominateur.get(rap).getOption().getCode().equals("nouveau_15_19")) {
						nouveau_15_19Den += rapportsElementDenominateur.get(rap).getValeurs();
					}
					if (rapportsElementDenominateur.get(rap).getOption().getCode().equals("ancien_10_14")) {
						ancien_10_14Den += rapportsElementDenominateur.get(rap).getValeurs();
					}
					if (rapportsElementDenominateur.get(rap).getOption().getCode().equals("ancien_15_19")) {
						ancien_15_19Den += rapportsElementDenominateur.get(rap).getValeurs();
					}
				}
			}

		}
		if (nouveau_10_14Den != 0) {
			nouveau_10_14 = (nouveau_10_14Num / nouveau_10_14Den) * 100;
		}
		if (nouveau_15_19Den != 0) {
			nouveau_15_19 = (nouveau_15_19Num / nouveau_15_19Den) * 100;
		}
		if (ancien_10_14Den != 0) {
			ancien_10_14 = (ancien_10_14Num / ancien_10_14Den) * 100;
		}
		if (ancien_15_19Den != 0) {
			ancien_15_19 = (ancien_15_19Num / ancien_15_19Den) * 100;
		}
		if (totalDen != 0) {
			total = (totalNum / totalDen) * 100;
		}
		
		rapportsElementProportion = irapport.getRapportOptionRapportCodeNull(this.organisationSelect.getId(), elementProportion.getCode(), laPeriode);
		if (!rapportsElementProportion.isEmpty()) {
			rapportsElementProportion = updateRapport(rapportsElementProportion);
		} else {
			Organisation organisation = getOrganisation(this.organisationSelect.getId());
			rapportsElementProportion = newRapport(organisation, elementProportion);
		}

		irapport.saveRapport(rapportsElementProportion);

	}

	/*private void enregistreProportion(String ProportionElement, String element, String elementTotal) {
		RapportTDO rapportTDO = new RapportTDO();
		// RapportTDO rapportTDOelement = new RapportTDO();
		// RapportTDO rapportTDOelementTotal = new RapportTDO();
		String combinaison = null;
		rapportTDO.setOrganisation(this.organisationSelect.getId());
		rapportTDO.setPeriode(laPeriode);

		List<RapportTDO> rapportTDOelements = new ArrayList<RapportTDO>();
		List<RapportTDO> rapportTDOelementTotals = new ArrayList<RapportTDO>();
		rapportTDOelements = irapport.getRapportOptionRapportTDOCodeNull(this.organisationSelect.getId(), element,
				laPeriode);
		rapportTDOelementTotals = irapport.getRapportOptionRapportTDOCodeNull(this.organisationSelect.getId(),
				elementTotal, laPeriode);
		if (!rapportTDOelements.isEmpty() && !rapportTDOelementTotals.isEmpty()) {
			for (int rap = 0; rap < rapportTDOelements.size(); rap++) {
				for (int tot = 0; tot < rapportTDOelements.size(); tot++) {
					// combinaison = element + ".nouveau_10_14";
					if (rapportTDOelements.get(rap).getElement().equals(element + ".nouveau_10_14")
							&& rapportTDOelementTotals.get(tot).getElement().equals(elementTotal + ".nouveau_10_14")) {
						nouveau_10_14 = rapportTDOelements.get(rap).getValeurs()
								/ rapportTDOelementTotals.get(tot).getValeurs();
						nouveau_10_14 *= 100;
					}
					// combinaison = element + ".nouveau_15_19";
					if (rapportTDOelements.get(rap).getElement().equals(element + ".nouveau_15_19")
							&& rapportTDOelementTotals.get(tot).getElement().equals(elementTotal + ".nouveau_15_19")) {
						nouveau_15_19 = rapportTDOelements.get(rap).getValeurs()
								/ rapportTDOelementTotals.get(tot).getValeurs();
						nouveau_15_19 *= 100;
					}
					// combinaison = element + ".ancien_10_14";
					if (rapportTDOelements.get(rap).getElement().equals(element + ".ancien_10_14")
							&& rapportTDOelementTotals.get(tot).getElement().equals(elementTotal + ".ancien_10_14")) {
						ancien_10_14 = rapportTDOelements.get(rap).getValeurs()
								/ rapportTDOelementTotals.get(tot).getValeurs();
						ancien_10_14 *= 100;
					}
					// combinaison = element + ".ancien_15_19";
					if (rapportTDOelements.get(rap).getElement().equals(element + ".ancien_15_19")
							&& rapportTDOelementTotals.get(tot).getElement().equals(elementTotal + ".ancien_15_19")) {
						ancien_15_19 = rapportTDOelements.get(rap).getValeurs()
								/ rapportTDOelementTotals.get(tot).getValeurs();
						ancien_15_19 *= 100;
					}
					// combinaison = element;
					if (rapportTDOelements.get(rap).getElement().equals(element)
							&& rapportTDOelementTotals.get(tot).getElement().equals(elementTotal)) {
						total = rapportTDOelements.get(rap).getValeurs()
								/ rapportTDOelementTotals.get(tot).getValeurs();
						total *= 100;
					}
				}
			}
		}

		// element - Nouveau, 10-14 ans
		combinaison = ProportionElement + ".nouveau_10_14";
		rapportTDO.setElement(combinaison);
		// rapportTDO.setValeurs(nouveau_10_14);
		if (Double.isNaN(nouveau_10_14)) {
			rapportTDO.setValeurs(0);
		} else {
			rapportTDO.setValeurs(nouveau_10_14);
		}
		irapport.saveRapportTDO(rapportTDO);

		// element - Nouveau, 15-19 ans
		combinaison = ProportionElement + ".nouveau_15_19";
		rapportTDO.setElement(combinaison);
		// rapportTDO.setValeurs(nouveau_15_19);
		if (Double.isNaN(nouveau_15_19)) {
			rapportTDO.setValeurs(0);
		} else {
			rapportTDO.setValeurs(nouveau_15_19);
		}
		irapport.saveRapportTDO(rapportTDO);

		// element - Ancien, 10-14 ans
		combinaison = ProportionElement + ".ancien_10_14";
		rapportTDO.setElement(combinaison);
		// rapportTDO.setValeurs(ancien_10_14);
		if (Double.isNaN(ancien_10_14)) {
			rapportTDO.setValeurs(0);
		} else {
			rapportTDO.setValeurs(ancien_10_14);
		}
		irapport.saveRapportTDO(rapportTDO);

		// element - Ancien, 15-19 ans
		combinaison = ProportionElement + ".ancien_15_19";
		rapportTDO.setElement(combinaison);
		// rapportTDO.setValeurs(ancien_15_19);
		if (Double.isNaN(ancien_15_19)) {
			rapportTDO.setValeurs(0);
		} else {
			rapportTDO.setValeurs(ancien_15_19);
		}
		irapport.saveRapportTDO(rapportTDO);

		// element (total)
		combinaison = ProportionElement;
		rapportTDO.setElement(combinaison);
		// rapportTDO.setValeurs(total);
		if (Double.isNaN(total)) {
			rapportTDO.setValeurs(0);
		} else {
			rapportTDO.setValeurs(total);
		}
		irapport.saveRapportTDO(rapportTDO);

	}*/

	
	private int retirerIntValue(String valeur) {
		int nombre = 0;
		int mascPosi = valeur.indexOf("preserMasc");
		int feminPosi = valeur.indexOf("preserFemin");
		
		if (mascPosi != -1) {
			//nombre += retirerInt(valeur, mascPosi);
			mascPosi -= 2;
			int masDebut = valeur.lastIndexOf(' ', mascPosi);
			mascPosi++;
			masDebut++;
			String Mval = valeur.substring(masDebut, mascPosi);

			try {
				nombre += Integer.parseInt(Mval);
				
			} catch (NumberFormatException e) {
				System.out.println("Not interger");
			} catch (NullPointerException e) {
				System.out.println("Not interger");
			}
		}
		
		if (feminPosi != -1) {
			//nombre += retirerInt(valeur, feminPosi);
			feminPosi -= 2;
			int masDebut = valeur.lastIndexOf(' ', feminPosi);
			feminPosi++;
			masDebut++;
			String Fval = valeur.substring(masDebut, feminPosi);

			try {
				nombre += Integer.parseInt(Fval);
			} catch (NumberFormatException e) {
				System.out.println("Not interger");
			} catch (NullPointerException e) {
				System.out.println("Not interger");
			}
		}
		
		
		return nombre;
	}

}
