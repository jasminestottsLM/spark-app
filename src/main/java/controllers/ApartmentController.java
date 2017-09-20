package controllers;

import java.util.HashMap;
import java.util.Map;

import models.Apartment;
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
			MustacheRenderer.getInstance();
			return MustacheRenderer.getInstance().render("apartment/details.html", model);
		}
	};
	
	public static final Route newForm = (Request req, Response res) -> {
		return MustacheRenderer.getInstance().render("apartment/newForm.html", null);
	};

	public static final Route create = (Request req, Response res) -> {
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
		try (AutoCloseableDB db = new AutoCloseableDB()) {
			apartment.saveIt();
			res.redirect("/");
			return "";
		}
	};
}
