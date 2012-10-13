package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.joda.time.DateMidnight;

import play.data.validation.Constraints;
import play.data.validation.Constraints.Required;
import play.data.format.*;
import play.db.jpa.JPA;

@Entity
public class NeedItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Required(message="Title is required!")
	private String name;
	@Enumerated(value = EnumType.STRING)
	private ItemType type = ItemType.FREE;
	private String description;
	@Required(message="Email is required!")
	private String email;
	private String phone;
	@Formats.DateTime(pattern="MM/dd/yy")
	private Date endDate;
	private Double locationLat;
	private Double locationLng;
	private boolean showDetails = false;

	public NeedItem() {
	}

	public void save() {
		JPA.em().persist(this);
	}

	public void update() {
		JPA.em().merge(this);
	}

	public void delete() {
		JPA.em().remove(this);
	}

	public static NeedItem findById(Long id) {
		return JPA.em().find(NeedItem.class, id);
	}

	@SuppressWarnings("unchecked")
	public static Page<NeedItem> getPage(int page, int pageSize, String sortBy,
			String order, String filter) {
		if (page < 1) {
			page = 1;
		}
		Long total = (Long) JPA
				.em()
				.createQuery(
						"select count(c) from NeedItem c where lower(c.name) like ? and c.endDate >= ?")
				.setParameter(1, "%" + filter.toLowerCase() + "%")
				.setParameter(2, DateMidnight.now().toDate())
				.getSingleResult();
		List<NeedItem> data = JPA
				.em()
				.createQuery(
						"from NeedItem c where lower(c.name) like ? and c.endDate >= ? order by c."
								+ sortBy + " " + order)
				.setParameter(1, "%" + filter.toLowerCase() + "%")
				.setParameter(2, DateMidnight.now().toDate())
				.setFirstResult((page - 1) * pageSize).setMaxResults(pageSize)
				.getResultList();
		return new Page<NeedItem>(data, total, page, pageSize);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ItemType getType() {
		return type;
	}

	public void setType(ItemType type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Double getLocationLat() {
		return locationLat;
	}

	public void setLocationLat(Double locationLat) {
		this.locationLat = locationLat;
	}
	
	public Double getLocationLng() {
		return locationLng;
	}

	public void setLocationLng(Double locationLng) {
		this.locationLng = locationLng;
	}

	public boolean isShowDetails() {
		return showDetails;
	}

	public void setShowDetails(boolean showDetails) {
		this.showDetails = showDetails;
	}

	@Override
	public String toString() {
		return "NeedItem [id=" + id + ", name=" + name + "]";
	}
}
