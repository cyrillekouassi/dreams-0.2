package ci.jsi.entites.beneficiaire;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ci.jsi.entites.organisation.OrganisationTDO;
import ci.jsi.initialisation.ResultatRequete;


@RestController
@RequestMapping(value="/beneficiaire")
public class BeneficiaireRestService {
	
	@Autowired
	Ibeneficiaire ibeneficiaire;

	@RequestMapping(method=RequestMethod.GET)
	public List<BeneficiaireTDO> searchBeneficiaireTDO(@RequestParam(name="organisation",defaultValue="") String organisation,@RequestParam(name="idDreams",defaultValue="") String idDreams){
		//return idataValues.SearchDataValueTDO(programme, organisation, element, valeur);
		return ibeneficiaire.SearchBeneficiaireTDOByIdDreams(idDreams, organisation);
	}
	@RequestMapping(value="/status",method=RequestMethod.GET)
	public List<StatusBeneficiaire> statusBeneficiaire(@RequestParam(name="org") List<String> organisation, @RequestParam(name="debut")String debut, @RequestParam(name="fin")String fin){
		return ibeneficiaire.getStatusBeneficiaire(organisation,debut,fin);
	}
	@RequestMapping(method=RequestMethod.POST)
	public ResultatRequete SaveBeneficiaireTDO(@RequestBody BeneficiaireTDO beneficiaireTDO){
		//return idataValues.SearchDataValueTDO(programme, organisation, element, valeur);
		return ibeneficiaire.saveBeneficiaireTDO(beneficiaireTDO);
	}
	@RequestMapping(value="/oev",method=RequestMethod.GET)
	public List<BeneficiaireOEV> beneficiaireOEV(@RequestParam(name="org") List<String> organisation, @RequestParam(name="debut")String debut, @RequestParam(name="fin")String fin){
		return ibeneficiaire.getBeneficiaireOEV(organisation,debut,fin);
	}
	
	@RequestMapping(value="instance/{id}", method=RequestMethod.GET)
	public List<BeneficiaireTDO> getListBeneficiaireTDO(@PathVariable(name="id")String instance) {
		return ibeneficiaire.getBeneficiaireTDOByInstance(instance);
	}
	
	@RequestMapping(value="/id", method=RequestMethod.GET)
	public BeneficiaireTDO getOneBeneficiaireTDO(@RequestParam(name="idDreams")String idDreams) {
		return ibeneficiaire.getBeneficiaireTDOByIdDreams(idDreams);
	}
	
	@RequestMapping(value="/iddreams", method=RequestMethod.GET)
	public List<BeneficiaireTDO> getListBeneficiaireTDO(@RequestParam(name="id") List<String> idDreams) {
		return ibeneficiaire.getListBeneficiaireTDOByIdDreams(idDreams);
	}
	
	@RequestMapping(method=RequestMethod.DELETE)
	public ResultatRequete deleteOneInstanceTDO(@RequestParam(name="dossierInstance")String dossierInstance) {
		return ibeneficiaire.deleteBeneficiaireByDossier(dossierInstance);
	}
	
}
