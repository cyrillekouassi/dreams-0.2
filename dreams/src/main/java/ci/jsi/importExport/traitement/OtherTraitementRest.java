package ci.jsi.importExport.traitement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ci.jsi.entites.beneficiaire.Ibeneficiaire;
import ci.jsi.entites.instance.Iinstance;
import ci.jsi.entites.rapport.TraitementIndicateur;
import ci.jsi.entites.rapport.TraitementIndicateurMAJ180620191;
import ci.jsi.initialisation.ResultatRequete;

@RestController
public class OtherTraitementRest {

	@Autowired
	Iinstance iinstance;
	@Autowired
	Ibeneficiaire ibeneficiaire;
	@Autowired
	CreateDossierBeneficiaire createDossierBeneficiaire;
	@Autowired
	ServicesDreams servicesDreams;
	@Autowired
	TraitementIndicateur traitementIndicateur;
	
	@Autowired
	TraitementIndicateurMAJ180620191 traitementIndicateurMAJ180620191;
	
	
	@RequestMapping(value="api/genererBesoinAndDossier", method=RequestMethod.GET)
	public ResultatRequete genererBesoinAndDossier(@RequestParam(name="instance") String instanceID,@RequestParam(name="beneficiaireID") String beneficiaireId,@RequestParam(name="dateEnrolement") String dateEnrolement){
		System.out.println("entrer dans genererBesoinAndDossier");
		ResultatRequete resultatRequete = new ResultatRequete();
		
		createDossierBeneficiaire.createDossierBeneficiare(instanceID, beneficiaireId);
		servicesDreams.genererService();
		servicesDreams.evaluerService(instanceID,dateEnrolement);

		resultatRequete.setStatus("ok");
		resultatRequete.setId(instanceID);
		return resultatRequete;
		
	}
	
	@RequestMapping(value="api/executeRapport", method=RequestMethod.GET)
	public ResultatRequete executeRapport(@RequestParam(name="action") String action){
		System.out.println("entrer dans executeRapport");
		/*ResultatRequete resultatRequete = new ResultatRequete();
		traitementIndicateur.genereRapport(action);
		resultatRequete.setStatus("ok");*/
		//return traitementIndicateur.genereRapport(action);
		return traitementIndicateurMAJ180620191.genereRapport(action);
		
	}
	
	/*private void serviceBeneficiaire(Instance instance,Beneficiaire beneficiaire) {
		InstanceBeneficiaire instanceBeneficiaire = new InstanceBeneficiaire();
		instanceBeneficiaire.setInstance(instance);
		instanceBeneficiaire.setBeneficiaire(beneficiaire);
		instanceBeneficiaire.setDateAction(instance.getDateActivite());
		instanceBeneficiaire.setOrdre(1);
		
		beneficiaire.getInstanceBeneficiaires().add(instanceBeneficiaire);
		beneficiaire = ibeneficiaire.updateOneBeneficiaire(beneficiaire);
	}*/
	
}
