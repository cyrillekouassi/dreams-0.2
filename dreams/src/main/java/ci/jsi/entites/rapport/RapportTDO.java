package ci.jsi.entites.rapport;

public class RapportTDO {
	
	private String organisation;
	private String element;
	private String periode;
	private double valeurs;
	
	
	public RapportTDO() {
		
	}


	public String getOrganisation() {
		return organisation;
	}

	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

	public String getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = element;
	}

	public String getPeriode() {
		return periode;
	}

	public void setPeriode(String periode) {
		this.periode = periode;
	}


	public double getValeurs() {
		return valeurs;
	}


	public void setValeurs(double valeurs) {
		this.valeurs = valeurs;
	}
	
	
}
