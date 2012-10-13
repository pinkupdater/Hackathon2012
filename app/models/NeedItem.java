package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.joda.time.DateMidnight;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.Point;

import play.data.validation.Constraints;
import play.db.jpa.JPA;

@Entity
public class NeedItem {

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
	private Double latitude;
	private Double longitude;
	private boolean showDetails = false;
	@OneToMany(cascade = CascadeType.ALL)
	private List<NeedGeoCell> geoCells;

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
				.setParameter(2, DateMidnight.now().toDate()).getSingleResult();
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

	@SuppressWarnings("unchecked")
	public static List<NeedItem> getBestMatching(int pageSize,
			List<String> cells, String filter) {
		if (filter == null) {
			filter = "";
		}
		List<String> ids = JPA
				.em()
				.createQuery(
						"select distinct n.needItem.id from NeedGeoCell n where n.value in ( :cells ) order by n.needItem.id desc")
				.setParameter("cells", cells).getResultList();
		List<NeedItem> data = null;

		if (ids != null && !ids.isEmpty()) {
			data = JPA
					.em()
					.createQuery(
							"from NeedItem n where lower(n.name) like :name and n.endDate >= :enddate and n.id in ( :ids ) order by n.id desc")
					.setParameter("name", "%" + filter.toLowerCase() + "%")
					.setParameter("enddate", DateMidnight.now().toDate())
					.setParameter("ids", ids).setMaxResults(pageSize)
					.getResultList();
			if (data == null) {
				data = new ArrayList<NeedItem>();
			}
			if (data.size() < pageSize) {
				List<NeedItem> data2 = JPA
						.em()
						.createQuery(
								"from NeedItem n where lower(n.name) like :name and n.endDate >= :enddate and n.id not in (:ids) order by n.id desc")
						.setParameter("name", "%" + filter.toLowerCase() + "%")
						.setParameter("enddate", DateMidnight.now().toDate())
						.setParameter("ids", ids).setFirstResult(0)
						.setMaxResults(pageSize - ids.size()).getResultList();

				if (data2 != null) {
					data.addAll(data2);
				}
			}

		} else {
			data = JPA
					.em()
					.createQuery(
							"from NeedItem n where lower(n.name) like :name and n.endDate >= :enddate order by n.id desc")
					.setParameter("name", "%" + filter.toLowerCase() + "%")
					.setParameter("enddate", DateMidnight.now().toDate())
					.setFirstResult(0).setMaxResults(pageSize - ids.size())
					.getResultList();
		}
		return data;
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

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public void setPosition(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		Point p = new Point(latitude, longitude);
		List<String> cells = GeocellManager.generateGeoCell(p);
		setGeoCellsStrings(cells);
	}

	public boolean isShowDetails() {
		return showDetails;
	}

	public void setShowDetails(boolean showDetails) {
		this.showDetails = showDetails;
	}

	public List<NeedGeoCell> getGeoCells() {
		return geoCells;
	}

	public void setGeoCells(List<NeedGeoCell> geoCells) {
		this.geoCells = geoCells;
	}

	public void setGeoCellsStrings(List<String> geoCellsStrings) {
		this.geoCells = new ArrayList<NeedGeoCell>();
		for (String geoString : geoCellsStrings) {
			geoCells.add(new NeedGeoCell(geoString, this));
		}
	}

	@Override
	public String toString() {
		return "NeedItem [id=" + id + ", name=" + name + "]";
	}
}
