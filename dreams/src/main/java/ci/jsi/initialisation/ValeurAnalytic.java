package ci.jsi.initialisation;

public class ValeurAnalytic {

	private String indicator;
	private String tranche;
	private String periode;
	private double valeurNouveau;
	private double valeurAncien;
	
	public ValeurAnalytic() {
		
	}

	public String getPeriode() {
		return periode;
	}

	public void setPeriode(String periode) {
		this.periode = periode;
	}

	

	

	public String getIndicator() {
		return indicator;
	}

	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}

	public String getTranche() {
		return tranche;
	}

	public void setTranche(String tranche) {
		this.tranche = tranche;
	}

	public double getValeurNouveau() {
		return valeurNouveau;
	}

	public void setValeurNouveau(double valeurNouveau) {
		this.valeurNouveau = valeurNouveau;
	}

	public double getValeurAncien() {
		return valeurAncien;
	}

	public void setValeurAncien(double valeurAncien) {
		this.valeurAncien = valeurAncien;
	}
	
	
}
