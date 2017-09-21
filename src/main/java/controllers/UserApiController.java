package controllers;

import static spark.Spark.notFound;

import java.util.Map;

import org.javalite.common.JsonHelper;

import models.Apartment;
import models.User;
import spark.Request;
import spark.Response;
import spark.Route;
import utilities.AutoCloseableDB;

public class UserApiController {

	public static final Route details = (Request req, Response res) -> {

		try (AutoCloseableDB db = new AutoCloseableDB()) {
			String idAsString = req.params("id");
			int id = Integer.parseInt(idAsString);
			User user = User.findById(id);
			Apartment apartment = Apartment.findById(id);
			if (user != null) {
				res.header("Content-Type", "application/json");
				return apartment.toJson(true);
			}
			notFound("Could not find that user.");
			return "/";
		}

	};

	public static final Route create = (Request req, Response res) -> {
		String json = req.body();
		Map map = JsonHelper.toMap(json);
		User user = new User();
		user.fromMap(map);

		try (AutoCloseableDB db = new AutoCloseableDB()) {
			user.saveIt();
			res.status(201);
			return user.toJson(true);
		}
	};
	

}
