package ci.jsi.entites.beneficiaire;

import java.util.List;

import ci.jsi.entites.instance.Instance;
import ci.jsi.initialisation.ResultatRequete;

public interface Ibeneficiaire {

	
	public List<BeneficiaireTDO> SearchBeneficiaireTDOByIdDreams(String idDreams,String organisation);
	
	public List<BeneficiaireTDO> getBeneficiaireTDOByInstance(String instance);
	
	public BeneficiaireTDO getBeneficiaireTDOByIdDreams(String idDreams);
	
	public List<BeneficiaireTDO> getListBeneficiaireTDOByIdDreams(List<String> idDreams);
	
	public ResultatRequete saveBeneficiaireTDO(BeneficiaireTDO beneficiaireTDO);
	
	public Beneficiaire getOneBeneficiaireByUid(String uid);
	public Beneficiaire getOneBeneficiaireByIdDreams(String idDreams);
	public Beneficiaire getOneBeneficiaireByInstance(String instance);
	public Beneficiaire updateOneBeneficiaire(Beneficiaire beneficiaire);
	public Beneficiaire saveOneBeneficiaire(Beneficiaire beneficiaire);
	public Beneficiaire convertBeneficiaireTDO(BeneficiaireTDO beneficiaireTDO);
	
	public Beneficiaire deleteBeneficiaireInstance(Beneficiaire beneficiaire,Instance instance);
	
	public List<BeneficiaireTDO> getBeneficiairePeriode(List<String> organisation,String debut,String fin);
	public List<BeneficiaireTDO> getBeneficiairePreview(List<String> organisation,String fin);
	
	public List<StatusBeneficiaire> getStatusBeneficiaire(List<String> organisation,String debut,String fin);
	
	public List<BeneficiaireOEV> getBeneficiaireOEV(List<String> organisation,String debut,String fin);
	
	public ResultatRequete deleteBeneficiaire(String beneficiaireUid);
}
