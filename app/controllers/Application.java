package controllers;

import java.util.Date;

import models.GiveItem;
import models.ItemType;
import models.NeedItem;
import models.Page;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class Application extends Controller {

	private static boolean initialized = false;

	@Transactional
	private static void initialize() {
		for (int i = 0; i < 10; i++) {
			NeedItem needItem = new NeedItem();
			needItem.setName("I need a tv.");
			needItem.setEmail("aaa@bbb.com");
			needItem.setEndDate(new Date());
			needItem.save();

			needItem = new NeedItem();
			needItem.setName("I need a laptop.");
			needItem.setEmail("aaa@bbb.com");
			needItem.setEndDate(new Date());
			needItem.save();
		}

		for (int i = 0; i < 10; i++) {
			GiveItem giveItem = new GiveItem();
			giveItem.setName("I give a tv.");
			giveItem.setEmail("aaa@bbb.com");
			giveItem.setEndDate(new Date());
			giveItem.setPhone("0123 444 555");
			giveItem.setType(ItemType.PAID);
			giveItem.setDescription("ana are mere.");
			giveItem.save();

			giveItem = new GiveItem();
			giveItem.setName("I give a laptop.");
			giveItem.setEmail("aaa@bbb.com");
			giveItem.setEndDate(new Date());
			giveItem.save();
		}

		initialized = true;
	}

	@Transactional
	public static Result index() {
		if (!initialized) {
			initialize();
		}

		Page<NeedItem> page = NeedItem.getPage(1, 3, "id", "asc", "tv");
		Page<NeedItem> page2 = NeedItem.getPage(1, 5, "id", "desc", "laptop");

		return ok(index.render("first 3 tv needs: " + page.getList()
				+ "              last 5 laptop needs: " + page2.getList()));
		
	}

	@Transactional
	public static Result needList(int page, String sortBy, String order,
			String filter) {
		if (!initialized) {
			initialize();
		}

		Page<NeedItem> page1 = NeedItem
				.getPage(page, 10, sortBy, order, filter);
		return ok(needList.render(page1, sortBy, order, filter));
	}

	@Transactional
	public static Result giveList(int page, String sortBy, String order,
			String filter) {
		if (!initialized) {
			initialize();
		}

		Page<GiveItem> page1 = GiveItem
				.getPage(page, 10, sortBy, order, filter);
		return ok(giveList.render(page1, sortBy, order, filter));
	}
}
