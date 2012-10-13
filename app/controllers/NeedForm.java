package controllers;

import models.NeedItem;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.need.formNeed;

public class NeedForm extends Controller{
	
	final static Form<NeedItem> needForm = form(NeedItem.class);
	
	public static Result blank() {
        return ok(formNeed.render(needForm));
    }
	
	@Transactional
	public static Result submit() {
		 Form<NeedItem> filledForm = needForm.bindFromRequest();
		 NeedItem created = filledForm.get();
		 created.save();
		 return null;
	}
	
	/*public static Result formNeed(){
		return ok(formNeed.render("salut"));
	}*/

}
