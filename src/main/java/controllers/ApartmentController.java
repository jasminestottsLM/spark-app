package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.LazyList;

import models.Apartment;
import models.User;
import spark.Request;
import spark.Response;
import spark.Route;
import utilities.AutoCloseableDB;
import utilities.MustacheRenderer;

public class ApartmentController {

	public static final Route details = (Request req, Response res) -> {
		String idAsString = req.params("id");
		int id = Integer.parseInt(idAsString);

		try (AutoCloseableDB db = new AutoCloseableDB()) {
			Apartment apartment = Apartment.findById(id);
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("apartment", apartment);
			model.put("currentUser", req.session().attribute("currentUser"));
			model.put("noUser", req.session().attribute("currentUser") == null);
			MustacheRenderer.getInstance();
			return MustacheRenderer.getInstance().render("apartment/details.html", model);
		}
	};
	
	public static final Route newForm = (Request req, Response res) -> {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("currentUser", req.session().attribute("currentUser"));
		model.put("noUser", req.session().attribute("currentUser") == null);
		return MustacheRenderer.getInstance().render("apartment/newForm.html", model);
	};

	public static final Route create = (Request req, Response res) -> {
		try (AutoCloseableDB db = new AutoCloseableDB()) {
		Apartment apartment = new Apartment(
				Integer.parseInt(req.queryParams("rent")),
				Integer.parseInt(req.queryParams("number_of_br")),
				Double.parseDouble(req.queryParams("number_of_ba")),
				Integer.parseInt(req.queryParams("square_footage")),
				req.queryParams("address"),
				req.queryParams("city"),
				req.queryParams("state"),
				req.queryParams("zip_code")
				);
		
			User user = req.session().attribute("currentUser");
			user.add(apartment);
			apartment.saveIt();
			res.redirect("/");
			return "";
		}
	};

	public final static Route index = (Request req, Response res) -> {
		User currentUser = req.session().attribute("currentUser");
		long id = (long) currentUser.getId();
				
		try (AutoCloseableDB db = new AutoCloseableDB()) {
//			List<Apartment> apartments = Apartment.where("user_id = ?", id);
			List<Apartment> apartments = currentUser.getAll(Apartment.class);
// multiple filters:  List<Apartment> apartments = currentUser.get(Apartment.class, "state = ?", selectedState)
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("apartments", apartments);
			model.put("currentUser", req.session().attribute("currentUser"));
			model.put("noUser", req.session().attribute("currentUser") == null);
			return MustacheRenderer.getInstance().render("apartment/index.html", model);
		}
	};
	
	
}
