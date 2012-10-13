package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class GiveGeoCell {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String value;
	@ManyToOne
	private GiveItem giveItem;

	public GiveGeoCell() {
	}

	public GiveGeoCell(String geoString, GiveItem giveItem) {
		this.value = geoString;
		this.giveItem = giveItem;
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

	public GiveItem getGiveItem() {
		return giveItem;
	}

	public void setGiveItem(GiveItem giveItem) {
		this.giveItem = giveItem;
	}
}
