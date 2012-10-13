package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.need.formNeed;

public class NeedForm extends Controller{
	
	public static Result formNeed(){
		return ok(formNeed.render("salut"));
	}

}
