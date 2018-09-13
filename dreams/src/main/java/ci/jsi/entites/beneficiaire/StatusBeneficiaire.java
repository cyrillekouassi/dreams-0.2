package ci.jsi.entites.beneficiaire;

import java.util.ArrayList;
import java.util.List;

import ci.jsi.initialisation.UidEntitie;
import ci.jsi.initialisation.UidInstance;

public class StatusBeneficiaire {

	private String id;
	private String numeroOrdre;
	private String name;
	private String firstName;
	private String idDreams;
	private int ageEnrolement;
	private String dateEnrolement;
	private String porteEntree;
	private String telephone;
	private String categorieDreams;
	private String dateNaissance;
	private String quatier;
	private String status;
	private String dateCreation;
	private String dateUpdate;
	
	private UidEntitie organisation;
	private List<UidInstance> instance = new ArrayList<UidInstance>();
	
	public StatusBeneficiaire() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNumeroOrdre() {
		return numeroOrdre;
	}

	public void setNumeroOrdre(String numeroOrdre) {
		this.numeroOrdre = numeroOrdre;
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

	public String getIdDreams() {
		return idDreams;
	}

	public void setIdDreams(String idDreams) {
		this.idDreams = idDreams;
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

	public String getPorteEntree() {
		return porteEntree;
	}

	public void setPorteEntree(String porteEntree) {
		this.porteEntree = porteEntree;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCategorieDreams() {
		return categorieDreams;
	}

	public void setCategorieDreams(String categorieDreams) {
		this.categorieDreams = categorieDreams;
	}

	public String getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(String dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public String getQuatier() {
		return quatier;
	}

	public void setQuatier(String quatier) {
		this.quatier = quatier;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
