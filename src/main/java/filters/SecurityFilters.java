package filters;

import static spark.Spark.halt;

import spark.Filter;
import spark.Request;
import spark.Response;

public class SecurityFilters {
	public static Filter isAuthenticated = (Request req, Response res) -> {
		if (req.session().attribute("currentUser") == null) {
			if (req.pathInfo().equals("/apartments/mine")) {
				res.redirect("/login");
				halt();
			}
			res.redirect("/login?returnPath=" + req.pathInfo());
			halt();
		}
	};
}
