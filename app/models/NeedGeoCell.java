package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class NeedGeoCell {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String value;
	@ManyToOne
	private NeedItem needItem;

	public NeedGeoCell() {
	}

	public NeedGeoCell(String geoString, NeedItem needItem) {
		this.value = geoString;
		this.needItem = needItem;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public NeedItem getNeedItem() {
		return needItem;
	}

	public void setNeedItem(NeedItem needItem) {
		this.needItem = needItem;
	}
}
