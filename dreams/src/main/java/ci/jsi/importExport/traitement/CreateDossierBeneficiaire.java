package ci.jsi.importExport.traitement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ci.jsi.entites.beneficiaire.Beneficiaire;
import ci.jsi.entites.dataValue.DataInstance;
import ci.jsi.entites.dataValue.DataValueTDO;
import ci.jsi.entites.dataValue.IdataValues;
import ci.jsi.entites.element.Element;
import ci.jsi.entites.element.Ielement;
import ci.jsi.entites.instance.Iinstance;
import ci.jsi.entites.instance.Instance;
import ci.jsi.entites.programme.Iprogramme;
import ci.jsi.entites.programme.Programme;
import ci.jsi.initialisation.ResultatRequete;

@Service
public class CreateDossierBeneficiaire {

	@Autowired
	Ielement ielement;
	@Autowired
	Iprogramme iprogramme;
	@Autowired
	IdataValues idataValues;
	@Autowired
	Iinstance iinstance;
	
	public CreateDossierBeneficiaire() {
		
	}

	public Instance createDossierBeneficiare(Instance instance,Beneficiaire beneficiaire) {
		DataInstance dataInstance = new DataInstance();
		List<DataValueTDO> dataValueTDOs = new ArrayList<DataValueTDO>();
		//DataValueTDO dataValueTDO = new DataValueTDO();
		ResultatRequete resultatRequete = null;
		Programme dossierProg = iprogramme.getOneProgrammeByCode("dossierBeneficiare");
		if(dossierProg == null) {
			return null;
		}
		
		dataInstance.setProgramme(dossierProg.getUid());
		dataInstance.setOrganisation(instance.getOrganisation().getUid());
		if(instance.getUser() != null) {
			dataInstance.setUser(instance.getUser().getUid());
		}
		dataInstance.setDreamsId(beneficiaire.getId_dreams());
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String dateActivite = formatter.format(instance.getDateActivite());
		//dataInstance.setDateActivite(instance.getDateActivite().toString());
        dataInstance.setDateActivite(dateActivite);
		dataInstance.setOrder(1);
		
		// ajouter benef name
		DataValueTDO nomPrenomBenef = new DataValueTDO();
		nomPrenomBenef.setElement(getElementUid("nomPrenomBenef"));
		nomPrenomBenef.setNumero(1);
		nomPrenomBenef.setValue(beneficiaire.getName()+" "+beneficiaire.getFirstName());
		dataValueTDOs.add(nomPrenomBenef);
		
		// ajouter benef age
		DataValueTDO age_enrol = new DataValueTDO();
		age_enrol.setElement(getElementUid("age_enrol"));
		age_enrol.setNumero(1);
		age_enrol.setValue(Integer.toString(beneficiaire.getAgeEnrolement()));
		dataValueTDOs.add(age_enrol);
		
		// ajouter benef contact
		DataValueTDO contactTeleph = new DataValueTDO();
		contactTeleph.setElement(getElementUid("contactTeleph"));
		contactTeleph.setNumero(1);
		contactTeleph.setValue(beneficiaire.getTelephone());
		dataValueTDOs.add(contactTeleph);
		
		// ajouter benef date enrolement
		DataValueTDO dat_enrol = new DataValueTDO();
		dat_enrol.setElement(getElementUid("dat_enrol"));
		dat_enrol.setNumero(1);
		String dateEnrol = formatter.format(beneficiaire.getDateEnrolement());
		dat_enrol.setValue(dateEnrol);
		dataValueTDOs.add(dat_enrol);
		
		// ajouter benef code id_dreams
		DataValueTDO id_dreams = new DataValueTDO();
		id_dreams.setElement(getElementUid("id_dreams"));
		id_dreams.setNumero(1);
		id_dreams.setValue(beneficiaire.getId_dreams());
		dataValueTDOs.add(id_dreams);
		
		// ajouter benef repereHabitation
		DataValueTDO repereHabitation = new DataValueTDO();
		repereHabitation.setElement(getElementUid("repereHabitation"));
		repereHabitation.setNumero(1);
		repereHabitation.setValue(benefElementValue(instance,"repere"));
		dataValueTDOs.add(repereHabitation);
		
		// ajouter benef porteEntree
		DataValueTDO porteEntree = new DataValueTDO();
		porteEntree.setElement(getElementUid("porteEntree"));
		porteEntree.setNumero(1);
		porteEntree.setValue(benefElementValue(instance, "porte_entre_bene"));
		dataValueTDOs.add(porteEntree);
		
		// ajouter benef nomPrenomParent
		DataValueTDO nomPrenomParent = new DataValueTDO();
		nomPrenomParent.setElement(getElementUid("nomPrenomParent"));
		nomPrenomParent.setNumero(1);
		nomPrenomParent.setValue(benefElementValue(instance, "nom_pren_charg_benef"));
		dataValueTDOs.add(nomPrenomParent);
		
		// ajouter benef contactParent
		DataValueTDO contactParent = new DataValueTDO();
		contactParent.setElement(getElementUid("contactParent"));
		contactParent.setNumero(1);
		contactParent.setValue(benefElementValue(instance, "tel_charg_benef"));
		dataValueTDOs.add(contactParent);
		
		
		dataInstance.setDataValue(dataValueTDOs);
		resultatRequete = idataValues.saveDataInstance(dataInstance);
		if(resultatRequete.getStatus().equals("ok")) {
			return iinstance.getOneInstance(resultatRequete.getId());
		}
		
		return null;
	}
	
	
	private String getElementUid(String code) {
		Element element = ielement.getOneElmentByCode(code);
		return element.getUid();
	}
	
	private String benefElementValue(Instance instance,String code) {
		DataValueTDO dataValueTDO = idataValues.getDataValueTDO(instance.getUid(), code);
		if(dataValueTDO != null) {
			return dataValueTDO.getValue();
		}
		
		return null;
	}
	
}