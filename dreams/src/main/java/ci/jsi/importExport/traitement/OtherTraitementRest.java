package ci.jsi.importExport.traitement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ci.jsi.entites.beneficiaire.Beneficiaire;
import ci.jsi.entites.beneficiaire.Ibeneficiaire;
import ci.jsi.entites.beneficiaire.InstanceBeneficiaire;
import ci.jsi.entites.instance.Iinstance;
import ci.jsi.entites.instance.Instance;
import ci.jsi.entites.rapport.TraitementIndicateur;
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
	
	
	@RequestMapping(value="api/genererBesoinAndDossier", method=RequestMethod.GET)
	public ResultatRequete genererBesoinAndDossier(@RequestParam(name="instance") String instanceID,@RequestParam(name="beneficiaireID") String beneficiaireId,@RequestParam(name="dateEnrolement") String dateEnrolement){
		System.out.println("entrer dans genererBesoinAndDossier");
		ResultatRequete resultatRequete = new ResultatRequete();
		Instance instance = iinstance.getOneInstance(instanceID);
		Beneficiaire beneficiaire = ibeneficiaire.getOneBeneficiaireByIdDreams(beneficiaireId);
		createDossierBeneficiaire.createDossierBeneficiare(instance, beneficiaire);
		servicesDreams.genererService();
		servicesDreams.evaluerService(instance,dateEnrolement);
		
		//Instance serviceInstance = servicesDreams.evaluerService(instance,dateEnrolement);
		//serviceBeneficiaire(serviceInstance,beneficiaire);
		resultatRequete.setStatus("ok");
		resultatRequete.setId(instance.getUid());
		return resultatRequete;
		
	}
	
	@RequestMapping(value="api/executeRapport", method=RequestMethod.GET)
	public ResultatRequete executeRapport(){
		System.out.println("entrer dans executeRapport");
		ResultatRequete resultatRequete = new ResultatRequete();
		traitementIndicateur.genereRapport();
		resultatRequete.setStatus("ok");
		return resultatRequete;
		
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
