package ci.jsi.entites.rapport;

import java.util.List;

public interface Irapport {

	public List<RapportTDO> getRapport(String organisation,List<String> elements,List<String> periode);
	public List<RapportTDO> getRapportOptionRapportTDOCodeNull(String organisation,String element,String periode);
	public List<Rapport> getRapportOptionRapportCodeNull(List<String> organisation,String element,String periode);
	public List<Rapport> getRapportOptionRapportCodeNull(String organisation,String element,String periode);
	public String saveRapportTDO(RapportTDO rapportTDO);
	public String saveRapport(List<Rapport> rapport);
}
