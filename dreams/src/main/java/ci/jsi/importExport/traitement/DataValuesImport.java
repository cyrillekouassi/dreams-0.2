package ci.jsi.importExport.traitement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;

import ci.jsi.entites.beneficiaire.BeneficiaireTDO;
import ci.jsi.entites.beneficiaire.Ibeneficiaire;
import ci.jsi.entites.dataValue.DataInstance;
import ci.jsi.entites.dataValue.DataValueTDO;
import ci.jsi.entites.dataValue.IdataValues;
import ci.jsi.entites.element.Element;
import ci.jsi.entites.element.Ielement;
import ci.jsi.entites.instance.Iinstance;
import ci.jsi.entites.instance.Instance;
import ci.jsi.entites.organisation.Iorganisation;
import ci.jsi.entites.organisation.Organisation;
import ci.jsi.entites.programme.Iprogramme;
import ci.jsi.entites.programme.Programme;
import ci.jsi.entites.utilisateur.Iuser;
import ci.jsi.entites.utilisateur.UserApp;
import ci.jsi.initialisation.ConvertDate;
import ci.jsi.initialisation.ResultatRequete;
import ci.jsi.initialisation.UidEntitie;

@Service
public class DataValuesImport {
	
	@Autowired
	Ielement ielement;
	@Autowired
	Iprogramme iprogramme;
	@Autowired
	Iinstance iinstance;
	@Autowired
	Iorganisation iorganisation;
	@Autowired
	Iuser iuser;
	@Autowired
	IdataValues idataValues;
	@Autowired
	ServicesDreams servicesDreams;
	@Autowired
	ConvertDate convertDate;
	@Autowired
	Ibeneficiaire ibeneficiaire;
	@Autowired
	CreateDossierBeneficiaire createDossierBeneficiaire;
	
	ResultatRequete resultatRequete;
	private String orgID = null;
	private String progId = null;
	private String benefID = null;
	
	
	
	//private String contitEntete[] = { "programmeid", "instanceid", "organisationid", "organisationcode", "userid","numeroElement" };
	private List<String[]> myEntries;
	private List<Element>  listElement = new ArrayList<Element>();
	private List<Integer>  listIndex = new ArrayList<Integer>();
	private List<String> noElement = new ArrayList<String>();
	
	
	/*private int orgid = -1;
	private int orgcode = -1;*/
	private int codeSafespace = -1;
	private int codeCentreSociale = -1;
	private int userid = -1;
	private int dat_enrol = -1;
	private int nom = -1;
	private int pren = -1;
	private int dat_nais = -1;
	private int age_enrol = -1;
	private int id_dreams = -1;
	private int tel = -1;
	int ligneNo = 0;
	
	@SuppressWarnings("deprecation")
	public ResultatRequete lireCSV(String fileName) {
		System.out.println("////// entrer DataValuesImport lireCSV");
		resultatRequete = new ResultatRequete();
		String chemin = File.separator+"DREAMS"+File.separator+"upload-dir"+File.separator;
		String file = chemin+fileName;
		//String file = "DREAMS/upload-dir/" + fileName;
		servicesDreams.genererService();
		progId = getProgramme();
		if(progId == null) {
			resultatRequete.setStatus("Programme Enrolement inexistant");
			resultatRequete.setIgnore(myEntries.size());
			return resultatRequete;
		}
		
		CSVReader csvReader = null;

		try {
			csvReader = new CSVReader(new FileReader(file), ';');
			// String[] ligne;

			// List<String[]> myEntries = csvReader.readAll();
			myEntries = csvReader.readAll();
			if (controleEntete(myEntries.get(0))) {
				seachElement(myEntries.get(0));
				if(!listElement.isEmpty()) {
					//System.err.println("nbre d'element trouvé: "+listElement.size());
					ligneNo++;
					System.out.println("++++++++++ ligne = "+ligneNo+" +++++++++++++++++++");
					myEntries.remove(0);
					insertData();
					resultatRequete.setStatus("succes");
				}else {
					resultatRequete.setStatus("Aucun Code Element trouvé");
					resultatRequete.setIgnore(myEntries.size());
				}
			}
			else {
				resultatRequete.setStatus("Entete invalide");
				resultatRequete.setIgnore(myEntries.size());
			}
		} catch (FileNotFoundException e) {
			resultatRequete.setStatus("Fichier non trouvé");
			resultatRequete.setIgnore(myEntries.size());
		} catch (IOException io) {
			resultatRequete.setStatus("Fichier illisible");
			resultatRequete.setIgnore(myEntries.size());
		}
		lesResultatRequete(resultatRequete);
		return resultatRequete;
	}
	
	private String getProgramme() {
		Programme programme = iprogramme.getOneProgrammeByCode("enrolement");
		if(programme == null) {
			return null;
		}
		return programme.getUid();
	}
	
	public boolean controleEntete(String[] ligne) {
		System.out.println("Entrer dans DataValuesImport - controleEntete");
		
		for (int i = 0; i < ligne.length; i++) {
			
			if (ligne[i].equals("codeSafespace")) {
				codeSafespace = i;
			}
			if (ligne[i].equals("userid")) {
				userid = i;
			}
			if (ligne[i].equals("centre_social")) {
				codeCentreSociale = i;
			}
			
			if (ligne[i].equals("dat_enrol")) {
				dat_enrol = i;
			}
			if (ligne[i].equals("nom")) {
				nom = i;
			}
			if (ligne[i].equals("pren")) {
				pren = i;
			}
			if (ligne[i].equals("dat_nais")) {
				dat_nais = i;
			}
			if (ligne[i].equals("age_enrol")) {
				age_enrol = i;
			}
			if (ligne[i].equals("id_dreams")) {
				id_dreams = i;
			}
			if (ligne[i].equals("tel")) {
				tel = i;
			}

		}
		
		if(codeCentreSociale == -1 || codeSafespace == -1 || dat_enrol == -1 || nom == -1 || dat_nais == -1 || age_enrol == -1 || id_dreams == -1) {
			return false;
		}
		
		return true;
	}

	public void seachElement(String[] ligne) {
		System.out.println("Entrer dans DataValuesImport - seachElement");
		
		
		for (int i = 0; i < ligne.length; i++) {
			if(i != codeCentreSociale &&  i != codeSafespace &&  i != userid) {
				ajoutElement(ligne[i],i);
			}
		}
	}

	private void ajoutElement(String code,int i) {
		System.out.println("Entrer dans DataValuesImport - ajoutElement");
		
		//System.out.println("code Element: "+code+" // colonne: "+i);
		
		Element element = new Element();
		element = ielement.getOneElmentByCode(code);
		
		if(element != null) {
			//System.out.println("ok");
			//System.out.println("Element: "+element.getName()+" // trouvée");
			listElement.add(element);
			listIndex.add(i);
		}else {
			System.err.println("erreur");
			//System.out.println("Element: "+element.getName()+" // non trouver");
			noElement.add(code);
			resultatRequete.getRaisonAutreEchec().add("code Element non trouvé: "+code);
		}
	}
	
	
	private void insertData() {
		
		if(!myEntries.isEmpty()) {
			String[] data = myEntries.get(0);
			ligneNo++;
			System.out.println("++++++++++ ligne = "+ligneNo+": enregistrement d'enrolement +++++++++++++++++++");
			myEntries.remove(0);
			searchMeta(data);
			insertData();
		}else {
			resultatRequete.setStatus("succes");
		}
		
	}


	private void searchMeta(String[] data) {
		System.out.println("Entrer dans DataValuesImport - searchMeta");
		
		//Instance instance = createInstance(data);
		orgID = getOrganisationId(data);
		if(orgID == null) {
			return;
		}
		
		benefID = createBeneficiaire(data,orgID);
		if(benefID == null) {
			return;
		}
		//Beneficiaire beneficiaire = new Beneficiaire();
				
		//beneficiaire = createBeneficiaire(data,orgID);
		/*if(beneficiaire == null) {
			//iinstance.deleteCompleteInstance(instance);
			resultatRequete.getRaisonNonImport().add("information beneficiaire absent");
			resultatRequete.setIgnore(resultatRequete.getIgnore() + 1);
			return;
		}*/
		
		//createBeneficiareInstance(instance,beneficiaire,data);
		ajouterValeur(benefID,data);
		
	}
	
	/*public Instance createInstance(String[] data) {
		System.out.println("Entrer dans DataValuesImport - createInstance");
		InstanceTDO instanceTDO = new InstanceTDO();
		instanceTDO.setProgramme(progId);		
		if(!data[codeSafespace].isEmpty() && data[codeSafespace] != null && !data[codeCentreSociale].isEmpty() && data[codeCentreSociale] != null) {
			Organisation organisation = getOrganisationId(data[codeCentreSociale],data[codeSafespace]);
			if(organisation != null) {
				instanceTDO.setOrganisation(organisation.getUid());
			}else {
				resultatRequete.getRaisonNonImport().add("organisationcode ou organisationid non renseigné");
				resultatRequete.setIgnore(resultatRequete.getIgnore() + 1);
				return null;
			}
		}else {
			resultatRequete.getRaisonNonImport().add("organisationcode ou organisationid non renseigné");
			resultatRequete.setIgnore(resultatRequete.getIgnore() + 1);
			return null;
		}
		
		if(!data[userid].isEmpty() && data[userid] != null) {
			instanceTDO.setUser(data[userid]);
		}
		if(!data[dat_enrol].isEmpty() && data[dat_enrol] != null) {
			instanceTDO.setDateActivite(data[dat_enrol]);
		}else {
			resultatRequete.getRaisonNonImport().add("dat_enrol non renseigné");
			resultatRequete.setIgnore(resultatRequete.getIgnore() + 1);
			return null;
		}
		
		return iinstance.saveInstance(instanceTDO);
		
	}*/
	
	//public Organisation getOrganisationId(String codeCentreSocial,String codeSafeSpace) {String[] data
	public String getOrganisationId(String[] data) {	
		System.out.println("Entrer dans DataValuesImport - getOrganisationId");
		String codeCentreSocial = null;
		String codeSafeSpace = null;
		Organisation safeSpace = null;
		
		if(!data[codeSafespace].isEmpty() && data[codeSafespace] != null && !data[codeCentreSociale].isEmpty() && data[codeCentreSociale] != null) {
			codeCentreSocial = data[codeCentreSociale];
			codeSafeSpace = data[codeSafespace];
			
		}else {
			resultatRequete.getRaisonNonImport().add("ligne "+ligneNo+": codeCentreSociale ou codeSafespace non renseigné");
			resultatRequete.setIgnore(resultatRequete.getIgnore() + 1);
			return null;
		}
		
		Organisation organisation = iorganisation.getOneOrganisationByCode(codeCentreSocial);
		if(organisation != null) {
			//return null;
			if(codeSafeSpace.length() == 1) {
				codeSafeSpace = "0"+codeSafeSpace;
			}
			
			for(int i = 0;i<organisation.getChildrens().size();i++) {
				if(organisation.getChildrens().get(i).getCode().equals(codeSafeSpace)) {
					safeSpace = organisation.getChildrens().get(i);
				}
			}
			if(safeSpace != null) {
				return safeSpace.getUid();
			}
		}
		
		if(organisation == null) {
			resultatRequete.getRaisonNonImport().add("ligne "+ligneNo+": CentreSociale non retrouvé");
			resultatRequete.setIgnore(resultatRequete.getIgnore() + 1);
		}
		if(safeSpace == null) {
			resultatRequete.getRaisonNonImport().add("ligne "+ligneNo+": Safespace non retrouvé");
			resultatRequete.setIgnore(resultatRequete.getIgnore() + 1);
		}
		
		return null;
	}
	
	public String createBeneficiaire(String[] data,String organisation) {
		System.out.println("Entrer dans DataValuesImport - createBeneficiaire");
		BeneficiaireTDO beneficiaireTDO = new BeneficiaireTDO();
				
		if(!data[nom].isEmpty() && data[nom] != null) {
			beneficiaireTDO.setName(data[nom]);
		}else {
			resultatRequete.getRaisonNonImport().add("ligne "+ligneNo+": nom beneficiaire non renseingé");
			resultatRequete.setIgnore(resultatRequete.getIgnore() + 1);
			return null;
		}
		if(!data[pren].isEmpty() && data[pren] != null) {
			beneficiaireTDO.setFirstName(data[pren]);
		}else {
			resultatRequete.getRaisonNonImport().add("ligne "+ligneNo+": pren beneficiaire non renseingé");
			resultatRequete.setIgnore(resultatRequete.getIgnore() + 1);
			return null;
		}
		if(!data[id_dreams].isEmpty() && data[id_dreams] != null) {
			beneficiaireTDO.setId_dreams(data[id_dreams]);
		}else {
			resultatRequete.getRaisonNonImport().add("ligne "+ligneNo+": id_dreams beneficiaire non renseingé");
			resultatRequete.setIgnore(resultatRequete.getIgnore() + 1);
			return null;
		}
		if(!data[tel].isEmpty() && data[tel] != null) {
			beneficiaireTDO.setTelephone(data[tel]);
		}
		
		if(!data[dat_nais].isEmpty() && data[dat_nais] != null) {
			beneficiaireTDO.setDateNaissance(data[dat_nais]);
		}else {
			resultatRequete.getRaisonNonImport().add("ligne "+ligneNo+": dat_nais beneficiaire non renseingé");
			resultatRequete.setIgnore(resultatRequete.getIgnore() + 1);
			return null;
		}
		
		if(!data[dat_enrol].isEmpty() && data[dat_enrol] != null) {
			beneficiaireTDO.setDateEnrolement(data[dat_enrol]);
		}else {
			resultatRequete.getRaisonNonImport().add("ligne "+ligneNo+": dat_enrol beneficiaire non renseingé");
			resultatRequete.setIgnore(resultatRequete.getIgnore() + 1);
			return null;
		}
		
		if(!data[age_enrol].isEmpty() && data[age_enrol] != null) {
			beneficiaireTDO.setAgeEnrolement(data[age_enrol]);
		}else {
			resultatRequete.getRaisonNonImport().add("ligne "+ligneNo+": age_enrol beneficiaire non renseingé");
			resultatRequete.setIgnore(resultatRequete.getIgnore() + 1);
			return null;
		}
		
		if(organisation != null) {
			beneficiaireTDO.setOrganisation(new UidEntitie(organisation));
		}
		
		ResultatRequete requeteBeneficiaire = ibeneficiaire.saveBeneficiaireTDO(beneficiaireTDO);
		if(requeteBeneficiaire.getStatus() == "OK") {
			return data[id_dreams];
		}else {
			resultatRequete.getRaisonNonImport().add("ligne "+ligneNo+": Echec de creation de la beneficiare id_dreams pourrait déjà existé, id_dreams: "+data[id_dreams]);
			resultatRequete.setIgnore(resultatRequete.getIgnore() + 1);
		}
		//return ibeneficiaire.convertBeneficiaireTDO(beneficiaireTDO);
		return null;
	}
	
	public Programme seachProgramme(String prog) {
		System.out.println("Entrer dans DataValuesImport - seachProgramme");
		
		Programme programme = new Programme();
		if(!prog.isEmpty() && prog != null) {
			programme = iprogramme.getOneProgramme(prog);
			if(programme == null) {
				return null;
			}
		}else {
			return null;
		}
		return programme;
	}

	public Instance seachInstance(String inst) {
		System.out.println("Entrer dans DataValuesImport - seachInstance");
		
		Instance instance = new Instance();
		instance = iinstance.getOneInstance(inst);
		if(instance == null) {
			return null;
		}
		return instance;
	}
	
	/*public Organisation seachOrganisation(String id,String code) {
		System.out.println("Entrer dans DataValuesImport - seachOrganisation");
		
		Organisation organisation = new Organisation();
		if(!id.isEmpty() && id != null) {
			organisation = iorganisation.getOneOrganisationById(id);
			if(organisation == null) {
				return null;
			}
		}else if(!code.isEmpty() && code != null) {
			organisation = iorganisation.getOneOrganisationByCode(code);
			if(organisation == null) {
				return null;
			}
		}else {
			return null;
		}
		
		
		return organisation;
	}*/
	
	public UserApp seachUser(String user) {
		System.out.println("Entrer dans DataValuesImport - seachUser");
		
		UserApp user1 = new UserApp();
		user1 = iuser.getUser(user);
		if(user1 == null) {
			//System.err.println("User Organisation non trouver: "+user);
			return null;
		}
		
		return user1;
	}
	
	/*private void createBeneficiareInstance(Instance instance,Beneficiaire beneficiaire,String[] data) {
		System.out.println("Entrer dans DataValuesImport - createBeneficiareInstance");
		InstanceBeneficiaire instanceBeneficiaire = new InstanceBeneficiaire();
		Date dateActivite;
		
		if(!data[dat_enrol].isEmpty() && data[dat_enrol] != null) {
			
				dateActivite = convertDate.getDateParse(data[dat_enrol]);
				if(dateActivite == null) {
					resultatRequete.getRaisonNonImport().add("dat_enrol pas en format Date, dat_enrol: "+data[dat_enrol]);
					resultatRequete.setIgnore(resultatRequete.getIgnore() + 1);
					return;
				}
			
		}else {
			resultatRequete.getRaisonNonImport().add("dat_enrol non renseigné");
			resultatRequete.setIgnore(resultatRequete.getIgnore() + 1);
			return;
		}
		instanceBeneficiaire.setInstance(instance);
		instanceBeneficiaire.setBeneficiaire(beneficiaire);
		instanceBeneficiaire.setDateAction(dateActivite);
		instanceBeneficiaire.setOrdre(1);
		
		beneficiaire.getInstanceBeneficiaires().add(instanceBeneficiaire);
		beneficiaire = ibeneficiaire.saveOneBeneficiaire(beneficiaire);
		if(beneficiaire != null) {
			ajouterValeur(instance,beneficiaire,data);
		}else {
			iinstance.deleteCompleteInstance(instance);
			resultatRequete.getRaisonNonImport().add("Echec de creation de la beneficiare id_dreams pourrait déjà existé, id_dreams: "+data[id_dreams]);
			resultatRequete.setIgnore(resultatRequete.getIgnore() + 1);
		}
			
	}*/
	
	private void ajouterValeur(String beneficiaireID,String[] data) {
		System.out.println("Entrer dans DataValuesImport - ajouterValeur");
		
		DataInstance dataInstance = new DataInstance();
		dataInstance.setDreamsId(beneficiaireID);
		dataInstance.setDateActivite(data[dat_enrol]);
		dataInstance.setOrganisation(orgID);
		dataInstance.setProgramme(progId);
		dataInstance.setOrder(1);
		
		List<DataValueTDO> dataValueTDOs = new ArrayList<DataValueTDO>();
		for(int i =0; i<listElement.size();i++) {
			if(!data[listIndex.get(i)].isEmpty() && data[listIndex.get(i)] != null) {
				DataValueTDO dataValueTDO = new DataValueTDO();
				dataValueTDO.setElement(listElement.get(i).getUid());
				dataValueTDO.setValue(data[listIndex.get(i)]);
				dataValueTDO.setNumero(1);
				dataValueTDOs.add(dataValueTDO);
			}
		}
		
		dataInstance.setDataValue(dataValueTDOs);
		ResultatRequete resultatDataInstance = idataValues.saveDataInstance(dataInstance);
		if(resultatDataInstance.getStatus() == "ok") {
			resultatRequete.setImporte(resultatRequete.getImporte() + 1);
			System.out.println("++++++++++ ligne = "+ligneNo+": evaluer Service +++++++++++++++++++");
			servicesDreams.evaluerService(resultatDataInstance.getId(),data[dat_enrol]);
			System.out.println("++++++++++ ligne = "+ligneNo+": enregistrement DossierBeneficiare +++++++++++++++++++");
			createDossierBeneficiaire.createDossierBeneficiare(resultatDataInstance.getId(),beneficiaireID);
		}
		
		
	
		/*
		List<DataValue> listDataValue = new ArrayList<DataValue>();
		
		for(int i =0; i<listElement.size();i++) {
			
			if(!data[listIndex.get(i)].isEmpty() && data[listIndex.get(i)] != null) {
				//System.out.println("Element :"+listElement.get(i).getName()+" // Element id:"+listElement.get(i).getUid());
				DataValue dataValue = new DataValue();
				dataValue.setInstance(instance);
				dataValue.setUser(instance.getUser());
				dataValue.setElement(listElement.get(i));
				dataValue.setValue(data[listIndex.get(i)]);
				dataValue.setNumero(1);
				dataValue.setDateCreation(new Date());
				dataValue.setDateUpdate(new Date());
				listDataValue.add(dataValue);
				
			}
		}
		
		if(!listDataValue.isEmpty()) {
			listDataValue = idataValues.saveAllDataValue(listDataValue);
			resultatRequete.setImporte(resultatRequete.getImporte() + 1);
			servicesDreams.evaluerService(instance,data[dat_enrol]);
			
			//Instance serviceInstance = servicesDreams.evaluerService(instance,data[dat_enrol]);
			//serviceBeneficiaire(serviceInstance,beneficiaire);
			
			
			createDossierBeneficiaire.createDossierBeneficiare(instance,beneficiaire);
			
		}*/
	}
	
	/*private void serviceBeneficiaire(Instance instance,Beneficiaire beneficiaire) {
		System.out.println("Entrer dans DataValuesImport - serviceBeneficiaire");
		InstanceBeneficiaire instanceBeneficiaire = new InstanceBeneficiaire();
		instanceBeneficiaire.setInstance(instance);
		instanceBeneficiaire.setBeneficiaire(beneficiaire);
		instanceBeneficiaire.setDateAction(instance.getDateActivite());
		instanceBeneficiaire.setOrdre(1);
		
		beneficiaire.getInstanceBeneficiaires().add(instanceBeneficiaire);
		beneficiaire = ibeneficiaire.updateOneBeneficiaire(beneficiaire);
	}*/
	
	private void lesResultatRequete(ResultatRequete requete) {
		System.out.println("entrer dans ResultatRequete");
		System.out.println("requete.id = "+requete.getId());
		System.out.println("requete.status = "+requete.getStatus());
		System.out.println("requete.importe = "+requete.getImporte());
		System.out.println("requete.ignore = "+requete.getIgnore());
		System.out.println("requete.update = "+requete.getUpdate());
		System.out.println("requete.delete = "+requete.getDelete());
		
		System.out.println();
		System.out.println("requete.raisonNonImport = "+requete.getRaisonNonImport().size());
		for(int i = 0;i<requete.getRaisonNonImport().size();i++) {
			System.out.println("raisonNonImport: "+requete.getRaisonNonImport().get(i));
		}
		
		System.out.println();
		System.out.println("requete.raisonAutreEchec = "+requete.getRaisonAutreEchec().size());
		for(int i = 0;i<requete.getRaisonAutreEchec().size();i++) {
			System.out.println("raisonAutreEchec: "+requete.getRaisonAutreEchec().get(i));
		}
		System.out.println("requete.id = "+requete.getId());
		System.out.println("requete.status = "+requete.getStatus());
		System.out.println("requete.importe = "+requete.getImporte());
		System.out.println("requete.ignore = "+requete.getIgnore());
		System.out.println("requete.update = "+requete.getUpdate());
		System.out.println("requete.delete = "+requete.getDelete());
	}
	
}























