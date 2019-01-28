package ci.jsi.importExport.traitement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ci.jsi.entites.beneficiaire.Beneficiaire;
import ci.jsi.entites.beneficiaire.Ibeneficiaire;
import ci.jsi.entites.dataValue.DataValue;
import ci.jsi.entites.dataValue.DataValueTDO;
import ci.jsi.entites.dataValue.IdataValues;
import ci.jsi.entites.element.Element;
import ci.jsi.entites.element.Ielement;
import ci.jsi.entites.instance.Iinstance;
import ci.jsi.entites.instance.Instance;
import ci.jsi.entites.programme.Iprogramme;
import ci.jsi.entites.programme.Programme;

@Service
public class CreateDossierBeneficiaireImport {

	@Autowired
	Ielement ielement;
	@Autowired
	Iprogramme iprogramme;
	@Autowired
	IdataValues idataValues;
	@Autowired
	Iinstance iinstance;
	@Autowired
	Ibeneficiaire ibeneficiaire;
	
	private Element _nomPrenomBenef;
	private Element _contactParent;
	private Element _nomPrenomParent;
	private Element _porteEntree;
	private Element _repereHabitation;
	private Element _id_dreams;
	private Element _age_enrol;
	private Element _contactTeleph;
	private Element _dat_enrol;
	
	public CreateDossierBeneficiaireImport() {
		
	}
	public void genererElement() {
		_nomPrenomBenef = getElementUid("nomPrenomBenef");
		_contactParent = getElementUid("contactParent");
		_nomPrenomParent = getElementUid("nomPrenomParent");
		_porteEntree = getElementUid("porteEntree");
		_repereHabitation = getElementUid("repereHabitation");
		_id_dreams = getElementUid("id_dreams");
		_age_enrol = getElementUid("age_enrol");
		_contactTeleph = getElementUid("contactTeleph");
		_dat_enrol = getElementUid("dat_enrol");
	}

	public Instance createDossierBeneficiare(Instance enrolInstance,Beneficiaire beneficiaire) {
		System.out.println("CreateDossierBeneficiaireImport - createDossierBeneficiare");
		
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		List<DataValue> listDataValue = new ArrayList<DataValue>();
		
		Instance instance = createInstance(enrolInstance);
		if(instance == null) {
			return null;
		}
		
		// ajouter benef name firstname
		DataValue nomPrenomBenef = new DataValue();
		nomPrenomBenef.setInstance(instance);
		nomPrenomBenef.setUser(instance.getUser());
		nomPrenomBenef.setElement(_nomPrenomBenef);
		nomPrenomBenef.setValue(beneficiaire.getName()+" "+beneficiaire.getFirstName());
		nomPrenomBenef.setNumero(1);
		nomPrenomBenef.setDateCreation(new Date());
		nomPrenomBenef.setDateUpdate(new Date());
		listDataValue.add(nomPrenomBenef);
		
		
		// ajouter benef age
		DataValue age_enrol = new DataValue();
		age_enrol.setInstance(instance);
		age_enrol.setUser(instance.getUser());
		age_enrol.setElement(_age_enrol);
		age_enrol.setValue(Integer.toString(beneficiaire.getAgeEnrolement()));
		age_enrol.setNumero(1);
		age_enrol.setDateCreation(new Date());
		age_enrol.setDateUpdate(new Date());
		listDataValue.add(age_enrol);
		
			
		// ajouter benef contact
		DataValue contactTeleph = new DataValue();
		contactTeleph.setInstance(instance);
		contactTeleph.setUser(instance.getUser());
		contactTeleph.setElement(_contactTeleph);
		contactTeleph.setValue(beneficiaire.getTelephone());
		contactTeleph.setNumero(1);
		contactTeleph.setDateCreation(new Date());
		contactTeleph.setDateUpdate(new Date());
		listDataValue.add(contactTeleph);
		
			
		// ajouter benef date enrolement
		DataValue dat_enrol = new DataValue();
		dat_enrol.setInstance(instance);
		dat_enrol.setUser(instance.getUser());
		dat_enrol.setElement(_dat_enrol);
		String dateEnrol = formatter.format(beneficiaire.getDateEnrolement());
		dat_enrol.setValue(dateEnrol);
		dat_enrol.setNumero(1);
		dat_enrol.setDateCreation(new Date());
		dat_enrol.setDateUpdate(new Date());
		listDataValue.add(dat_enrol);
		
		
		// ajouter benef code id_dreams
		DataValue id_dreams = new DataValue();
		id_dreams.setInstance(instance);
		id_dreams.setUser(instance.getUser());
		id_dreams.setElement(_id_dreams);
		id_dreams.setValue(beneficiaire.getId_dreams());
		id_dreams.setNumero(1);
		id_dreams.setDateCreation(new Date());
		id_dreams.setDateUpdate(new Date());
		listDataValue.add(id_dreams);
		
		
		// ajouter benef repereHabitation
		DataValue repereHabitation = new DataValue();
		repereHabitation.setInstance(instance);
		repereHabitation.setUser(instance.getUser());
		repereHabitation.setElement(_repereHabitation);
		repereHabitation.setValue(benefElementValue(instance,"repere"));
		repereHabitation.setNumero(1);
		repereHabitation.setDateCreation(new Date());
		repereHabitation.setDateUpdate(new Date());
		listDataValue.add(repereHabitation);
		
		
		// ajouter benef porteEntree
		DataValue porteEntree = new DataValue();
		porteEntree.setInstance(instance);
		porteEntree.setUser(instance.getUser());
		porteEntree.setElement(_porteEntree);
		porteEntree.setValue(benefElementValue(instance, "porte_entre_bene"));
		porteEntree.setNumero(1);
		porteEntree.setDateCreation(new Date());
		porteEntree.setDateUpdate(new Date());
		listDataValue.add(porteEntree);
		
			
		// ajouter benef nomPrenomParent
		DataValue nomPrenomParent = new DataValue();
		nomPrenomParent.setInstance(instance);
		nomPrenomParent.setUser(instance.getUser());
		nomPrenomParent.setElement(_nomPrenomParent);
		nomPrenomParent.setValue(benefElementValue(instance, "nom_pren_charg_benef"));
		nomPrenomParent.setNumero(1);
		nomPrenomParent.setDateCreation(new Date());
		nomPrenomParent.setDateUpdate(new Date());
		listDataValue.add(nomPrenomParent);
		
		
		// ajouter benef contactParent
		DataValue contactParent = new DataValue();
		contactParent.setInstance(instance);
		contactParent.setUser(instance.getUser());
		contactParent.setElement(_contactParent);
		contactParent.setValue(benefElementValue(instance, "tel_charg_benef"));
		contactParent.setNumero(1);
		contactParent.setDateCreation(new Date());
		contactParent.setDateUpdate(new Date());
		listDataValue.add(contactParent);
		
		
		if(!listDataValue.isEmpty()) {
			listDataValue = idataValues.saveAllDataValue(listDataValue);
		}
		
				
		return instance;
	}
	
	
	private Element getElementUid(String code) {
		return ielement.getOneElmentByCode(code);
		//return element.getUid();
		/*Element element = ielement.getOneElmentByCode(code);
		return element.getUid();*/
	}
	
	private String benefElementValue(Instance instance,String code) {
		DataValueTDO dataValueTDO = idataValues.getDataValueTDO(instance.getUid(), code);
		if(dataValueTDO != null) {
			return dataValueTDO.getValue();
		}
		
		return null;
	}
	
	
	public Instance createInstance(Instance instance) {
		System.out.println("Entrer dans CreateDossierBeneficiaireImport - createInstance");
		
		Instance dossierInstance = new Instance();
		Programme dossierProg = iprogramme.getOneProgrammeByCode("dossierBeneficiare");
		
		dossierInstance.setOrganisation(instance.getOrganisation());
		
		if (instance.getUser() != null) {
			dossierInstance.setUser(instance.getUser());
		}
		dossierInstance.setProgramme(dossierProg);
		dossierInstance.setDateActivite(instance.getDateActivite());
		return iinstance.saveInstance(dossierInstance);
		
	}
	
}
