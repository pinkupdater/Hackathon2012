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
import javax.persistence.Transient;

import org.joda.time.DateMidnight;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.Point;

import play.data.format.Formats;
import play.data.validation.Constraints.Required;
import play.db.jpa.JPA;
import models.GeoCellUtil;

@Entity
public class GiveItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Required(message = "This is required!")
	private String name;
	@Enumerated(value = EnumType.STRING)
	private ItemType type = ItemType.FREE;
	private String description;
	@Required(message = "This is required!")
	private String email;
	private String phone;
	@Formats.DateTime(pattern = "MM/dd/yy")
	private Date endDate;
	private Double latitude;
	private Double longitude;
	private boolean showDetails = false;
	@OneToMany(cascade = CascadeType.ALL)
	private List<GiveGeoCell> geoCells;
	private String locationName;
	@Transient
	private String location;

	public GiveItem() {
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
						"select count(c) from GiveItem c where lower(c.name) like ? and c.endDate >= ?")
				.setParameter(1, "%" + filter.toLowerCase() + "%")
				.setParameter(2, DateMidnight.now().toDate()).getSingleResult();
		List<GiveItem> data = JPA
				.em()
				.createQuery(
						"from GiveItem c where lower(c.name) like ? and c.endDate >= ? order by c."
								+ sortBy + " " + order)
				.setParameter(1, "%" + filter.toLowerCase() + "%")
				.setParameter(2, DateMidnight.now().toDate())
				.setFirstResult((page - 1) * pageSize).setMaxResults(pageSize)
				.getResultList();
		return new Page<GiveItem>(data, total, page, pageSize);
	}

	@SuppressWarnings("unchecked")
	public static List<GiveItem> getBestMatching(int pageSize,
			List<String> cells, String filter) {
		if (filter == null) {
			filter = "";
		}
		List<String> ids = JPA
				.em()
				.createQuery(
						"select distinct n.giveItem.id from GiveGeoCell n where n.value in ( :cells ) order by n.giveItem.id desc")
				.setParameter("cells", cells).getResultList();
		List<GiveItem> data = null;

		if (ids != null && !ids.isEmpty()) {
			data = JPA
					.em()
					.createQuery(
							"from GiveItem n where lower(n.name) like :name and n.endDate >= :enddate and n.id in ( :ids ) order by n.id desc")
					.setParameter("name", "%" + filter.toLowerCase() + "%")
					.setParameter("enddate", DateMidnight.now().toDate())
					.setParameter("ids", ids).setMaxResults(pageSize)
					.getResultList();
			if (data == null) {
				data = new ArrayList<GiveItem>();
			}
			if (data.size() < pageSize) {
				List<GiveItem> data2 = JPA
						.em()
						.createQuery(
								"from GiveItem n where lower(n.name) like :name and n.endDate >= :enddate and n.id not in (:ids) order by n.id desc")
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
							"from GiveItem n where lower(n.name) like :name and n.endDate >= :enddate order by n.id desc")
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

	public List<GiveGeoCell> getGeoCells() {
		return geoCells;
	}

	public void setGeoCells(List<GiveGeoCell> geoCells) {
		this.geoCells = geoCells;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getLocation() {
		return location;
	}

	public void setGeoCellsStrings(List<String> geoCellsStrings) {
		this.geoCells = new ArrayList<GiveGeoCell>();
		for (String geoString : geoCellsStrings) {
			geoCells.add(new GiveGeoCell(geoString, this));
		}
	}

	public void setLocation(String location) {
		if (location != null && location.length() >= 2) {
			String[] fields = location.substring(1, location.length() - 1)
					.split(",\\s");
			if (fields.length == 2) {
				try {
					Double latitude = Double.parseDouble(fields[0]);
					Double longitude = Double.parseDouble(fields[1]);
					setPosition(latitude, longitude);
				} catch (NumberFormatException e) {
					// silently ignore and do nothing.
				}
			}
		}
	}

	public static Page<GiveItem> getPage(int page, int i, String sortBy,
			String order, String filter, String location) {
		System.out.println(location);
		if (filter == null) {
			filter = "";
		}

		if (location != null && location.length() >= 2) {
			String[] fields = location.substring(1, location.length() - 1)
					.split(",\\s");
			if (fields.length == 2) {
				try {
					Double latitude = Double.parseDouble(fields[0]);
					Double longitude = Double.parseDouble(fields[1]);
					System.out.println(latitude + " " + longitude);
					List<String> cells = GeoCellUtil.getCells(latitude,
							longitude, 50);
					if (cells != null) {
						return searchInCells(page, i, sortBy, order, filter,
								cells);
					}
				} catch (NumberFormatException e) {
					// silently ignore and do nothing.
				}

			}
		} else {
			return getPage(page, i, sortBy, order, filter);
		}

		return new Page<GiveItem>(new ArrayList<GiveItem>(), 0, page, i);
	}

	@SuppressWarnings("unchecked")
	private static Page<GiveItem> searchInCells(int page, int pageSize,
			String sortBy, String order, String filter, List<String> cells) {
		if (page < 1) {
			page = 1;
		}
		List<String> ids = JPA
				.em()
				.createQuery(
						"select distinct n.giveItem.id from GiveGeoCell n where n.value in ( :cells ) order by n.giveItem.id desc")
				.setParameter("cells", cells).getResultList();
		List<GiveItem> data = null;

		if (ids != null && !ids.isEmpty()) {
			data = JPA
					.em()
					.createQuery(
							"from GiveItem n where lower(n.name) like :name and n.endDate >= :enddate and n.id in ( :ids ) order by n.id desc")
					.setParameter("name", "%" + filter.toLowerCase() + "%")
					.setParameter("enddate", DateMidnight.now().toDate())
					.setParameter("ids", ids)
					.setFirstResult((page - 1) * pageSize)
					.setMaxResults(pageSize).getResultList();
		}
		if (data == null) {
			data = new ArrayList<GiveItem>();
		}
		return new Page<GiveItem>(data, data.size(), page, pageSize);
	}
}
