package controllers;

import static spark.Spark.notFound;

import java.util.Map;

import org.javalite.common.JsonHelper;

import models.Apartment;
import spark.Request;
import spark.Response;
import spark.Route;
import utilities.AutoCloseableDB;

public class ApartmentApiController {

	public static final Route details = (Request req, Response res) -> {

		try (AutoCloseableDB db = new AutoCloseableDB()) {
			String idAsString = req.params("id");
			int id = Integer.parseInt(idAsString);
			Apartment apartment = Apartment.findById(id);
			if (apartment != null) {
				res.header("Content-Type", "application/json");
				return apartment.toJson(true);
			}
			notFound("Could not find that apartment.");
			return "";
		}

	};

	public static final Route create = (Request req, Response res) -> {
		String json = req.body();
		Map map = JsonHelper.toMap(json);
		Apartment apartment = new Apartment();
		apartment.fromMap(map);

		try (AutoCloseableDB db = new AutoCloseableDB()) {
			apartment.saveIt();
			res.status(201);
			return apartment.toJson(true);
		}
	};
}