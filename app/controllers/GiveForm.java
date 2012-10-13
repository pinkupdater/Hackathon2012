package controllers;

import models.GiveItem;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.give.formGive;


public class GiveForm extends Controller{
	
final static Form<GiveItem> giveForm = form(GiveItem.class);
	
	public static Result blank() {
        return ok(formGive.render(giveForm));
    }
	
	@Transactional
	public static Result submit() {
		 Form<GiveItem> filledForm = giveForm.bindFromRequest();
		 GiveItem created = filledForm.get();
		 created.save();
		 return null;
	}
}
