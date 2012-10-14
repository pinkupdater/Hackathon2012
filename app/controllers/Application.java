package controllers;

import models.GiveItem;
import models.NeedItem;
import models.Page;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.giveList;
import views.html.index;
import views.html.needList;

public class Application extends Controller {

	private static boolean initialized = false;

	@Transactional
	private static void initialize() {
		initialized = true;
	}

	@Transactional
	public static Result index() {
		if (!initialized) {
			initialize();
		}

		Page<NeedItem> page1 = NeedItem.getPage(0, 4, "id", "desc", "");
		Page<GiveItem> page2 = GiveItem.getPage(0, 4, "id", "desc", "");
		return ok(index.render(page1, "id", "desc", "", page2));
	}

	@Transactional
	public static Result needList(int page, String sortBy, String order,
			String filter, String location) {
		if (!initialized) {
			initialize();
		}

		System.out.println(location);

		Page<NeedItem> page1 = NeedItem.getPage(page, 10, sortBy, order,
				filter, location);
		return ok(needList.render(page1, sortBy, order, filter));
	}

	@Transactional
	public static Result giveList(int page, String sortBy, String order,
			String filter, String location) {
		if (!initialized) {
			initialize();
		}

		Page<GiveItem> page1 = GiveItem.getPage(page, 10, sortBy, order,
				filter, location);
		return ok(giveList.render(page1, sortBy, order, filter));
	}

}
