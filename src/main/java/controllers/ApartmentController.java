package controllers;

import java.util.HashMap;
import java.util.Map;

import models.Apartment;
import spark.Request;
import spark.Response;
import spark.Route;
import utilities.MustacheRenderer;

public class ApartmentController {

	public static final Route details = (Request req, Response res) -> {
		String idAsString = req.params("id");
		int id = Integer.parseInt(idAsString);
		Apartment apartment = Apartment.findById(id);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("apartment", apartment);
		MustacheRenderer.getInstance();
		return MustacheRenderer.getInstance().render("apartment/details.html", model);
	};
}
