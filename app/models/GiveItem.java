package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.data.validation.Constraints;
import play.db.jpa.JPA;

@Entity
public class GiveItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Constraints.Required
	private String name;
	@Enumerated(value = EnumType.STRING)
	private ItemType type = ItemType.FREE;
	private String description;
	@Constraints.Required
	@Constraints.Email
	private String email;
	private String phone;
	private Date endDate;
	private String location;
	private boolean showDetails = false;

	private GiveItem() {
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

	public static GiveItem findById(Long id) {
		return JPA.em().find(GiveItem.class, id);
	}

	@SuppressWarnings("unchecked")
	public static Page<GiveItem> getPage(int page, int pageSize, String sortBy,
			String order, String filter) {
		if (page < 1) {
			page = 1;
		}
		Long total = (Long) JPA
				.em()
				.createQuery(
						"select count(c) from GiveItem c where lower(c.name) like ?")
				.setParameter(1, "%" + filter.toLowerCase() + "%")
				.getSingleResult();
		List<GiveItem> data = JPA
				.em()
				.createQuery(
						"from GiveItem c where lower(c.name) like ? order by c."
								+ sortBy + " " + order)
				.setParameter(1, "%" + filter.toLowerCase() + "%")
				.setFirstResult((page - 1) * pageSize).setMaxResults(pageSize)
				.getResultList();
		return new Page<GiveItem>(data, total, page, pageSize);
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public boolean isShowDetails() {
		return showDetails;
	}

	public void setShowDetails(boolean showDetails) {
		this.showDetails = showDetails;
	}
}
