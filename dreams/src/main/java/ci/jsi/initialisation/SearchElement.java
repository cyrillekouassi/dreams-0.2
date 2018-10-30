package ci.jsi.initialisation;

import java.util.List;

public class SearchElement {

	String organisation;
	String programme;
	private List<ValueSearch> valueSearchs;
	
	int page;
	int size;
	
	public SearchElement() {
		
	}

	public String getOrganisation() {
		return organisation;
	}

	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

	public String getProgramme() {
		return programme;
	}

	public void setProgramme(String programme) {
		this.programme = programme;
	}

	public List<ValueSearch> getValueSearchs() {
		return valueSearchs;
	}

	public void setValueSearchs(List<ValueSearch> valueSearchs) {
		this.valueSearchs = valueSearchs;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	
	
	
}
