package ci.jsi.entites.beneficiaire;

import java.util.ArrayList;
import java.util.List;

import ci.jsi.initialisation.UidEntitie;
import ci.jsi.initialisation.UidInstance;

public class BeneficiaireOEV {
	
	private String id;
	private String idDreams;
	private String codeOEV;
	private String name;
	private String firstName;
	private int ageEnrolement;
	private String dateEnrolement;
	private String dateIdentification;
	private String statutVIH;
	private String telephone;
	private String dateNaissance;
	private String dateCreation;
	private String dateUpdate;
	
	private UidEntitie organisation;
	private List<UidInstance> instance = new ArrayList<UidInstance>();
	
	
	public BeneficiaireOEV() {
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdDreams() {
		return idDreams;
	}
	public void setIdDreams(String idDreams) {
		this.idDreams = idDreams;
	}
	public String getCodeOEV() {
		return codeOEV;
	}
	public void setCodeOEV(String codeOEV) {
		this.codeOEV = codeOEV;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public int getAgeEnrolement() {
		return ageEnrolement;
	}
	public void setAgeEnrolement(int ageEnrolement) {
		this.ageEnrolement = ageEnrolement;
	}
	public String getDateEnrolement() {
		return dateEnrolement;
	}
	public void setDateEnrolement(String dateEnrolement) {
		this.dateEnrolement = dateEnrolement;
	}
	public String getDateIdentification() {
		return dateIdentification;
	}
	public void setDateIdentification(String dateIdentification) {
		this.dateIdentification = dateIdentification;
	}
	public String getStatutVIH() {
		return statutVIH;
	}
	public void setStatutVIH(String statutVIH) {
		this.statutVIH = statutVIH;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getDateNaissance() {
		return dateNaissance;
	}
	public void setDateNaissance(String dateNaissance) {
		this.dateNaissance = dateNaissance;
	}
	public String getDateCreation() {
		return dateCreation;
	}
	public void setDateCreation(String dateCreation) {
		this.dateCreation = dateCreation;
	}
	public String getDateUpdate() {
		return dateUpdate;
	}
	public void setDateUpdate(String dateUpdate) {
		this.dateUpdate = dateUpdate;
	}
	public UidEntitie getOrganisation() {
		return organisation;
	}
	public void setOrganisation(UidEntitie organisation) {
		this.organisation = organisation;
	}
	public List<UidInstance> getInstance() {
		return instance;
	}
	public void setInstance(List<UidInstance> instance) {
		this.instance = instance;
	}
	

}
