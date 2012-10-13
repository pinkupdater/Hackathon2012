package controllers;

import models.User;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

	@Transactional
	public static Result index() {
		User user = JPA.em().find(User.class, 1L);
		String message = null;
		if (user == null) {
			message = "No user in DB with id = 1.";
		} else {
			message = "User in DB with id = 1: " + user.getUsername();
		}
		return ok(index.render(message));
	}
}