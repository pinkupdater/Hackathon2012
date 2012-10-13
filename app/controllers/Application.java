package controllers;

import java.util.Date;

import models.NeedItem;
import models.Page;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

	@Transactional
	public static Result index() {
		NeedItem needItem;

		// save 20 NeedItems:
		for (int i = 0; i < 10; i++) {
			needItem = new NeedItem();
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

		Page<NeedItem> page = NeedItem.getPage(1, 3, "endDate", "asc", "tv");
		Page<NeedItem> page2 = NeedItem.getPage(1, 5, "endDate", "desc",
				"laptop");

		return ok(index.render("first 3 tv needs: " + page.getList()
				+ "              last 5 laptop needs: " + page2.getList()));
	}
	
	
}