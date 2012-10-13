package controllers;

import java.util.Date;
import java.util.List;

import models.GeoCellUtil;
import models.GiveItem;
import models.ItemType;
import models.NeedItem;
import models.Page;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.giveList;
import views.html.index;
import views.html.needList;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.BoundingBox;
import com.beoui.geocell.model.Point;

public class Application extends Controller {

	private static boolean initialized = false;

	@Transactional
	private static void initialize() {
		for (int i = 0; i < 10; i++) {
			NeedItem needItem = new NeedItem();
			needItem.setName("I need a tv.");
			needItem.setEmail("aaa@bbb.com");
			needItem.setEndDate(new Date());
			needItem.setPosition(10d, 20d);
			needItem.save();

			needItem = new NeedItem();
			needItem.setName("I need a laptop.");
			needItem.setEmail("aaa@bbb.com");
			needItem.setEndDate(new Date());
			needItem.setPosition(46.778, 23.699);
			needItem.save();
		}

		List<String> cells = GeoCellUtil.getCells(47.666, 23.583, 50);
		System.out.println(cells);

		NeedItem.getBestMatching(100, cells, null);

		List<String> cells = GeoCellUtil.getCells(47.666, 23.583, 50);
		System.out.println(cells);

		NeedItem.getBestMatching(100, cells, null);

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


		Page<NeedItem> page1 = NeedItem
				.getPage(0, 4, "id", "desc", "");
		Page<GiveItem> page2 = GiveItem
				.getPage(0, 4, "id", "desc", "");
		return ok(index.render(page1, "id", "desc", "", page2));
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
