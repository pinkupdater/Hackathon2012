package controllers;

import org.joda.time.DateMidnight;

import models.GiveItem;
import models.Page;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.giveList;
import views.html.give.formGive;


public class GiveForm extends Controller{
	
final static Form<GiveItem> giveForm = form(GiveItem.class);
	
	public static Result blank() {
        return ok(formGive.render(giveForm));
    }
	
	@Transactional
	public static Result submit() {
		 Form<GiveItem> filledForm = giveForm.bindFromRequest();
		 if (filledForm.hasErrors()){
			 return badRequest(formGive.render(filledForm));
		 }else{
			 GiveItem created = filledForm.get();
			 if (created.getEndDate() == null){
				 created.setEndDate(DateMidnight.now().toDate());
			 }
		 created.save();
		 Page<GiveItem> page1 = GiveItem
					.getPage(0, 10, "id", "desc", "");
		 return ok(giveList.render(page1, "id", "desc", ""));
		 }
	}
}
