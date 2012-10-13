package controllers;

import models.NeedItem;
import models.Page;

import org.joda.time.DateMidnight;

import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.needList;
import views.html.need.formNeed;

public class NeedForm extends Controller{
	
	final static Form<NeedItem> needForm = form(NeedItem.class);
	
	public static Result blank() {
       return ok(formNeed.render(needForm));
    }
	
	@Transactional
	public static Result submit() {
		 Form<NeedItem> filledForm = needForm.bindFromRequest();
		 if (filledForm.hasErrors()){
			 return badRequest(formNeed.render(filledForm));
		 }else{
			 NeedItem created = filledForm.get();
			 if (created.getEndDate() == null){
				 created.setEndDate(DateMidnight.now().toDate());
			 }
		 created.save();
		 Page<NeedItem> page1 = NeedItem
					.getPage(0, 10, "id", "desc", "");
		 return ok(needList.render(page1, "id", "desc", ""));
		 }
	}
	
}
