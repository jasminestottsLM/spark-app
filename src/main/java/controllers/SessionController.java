package controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;

import models.User;
import models.newCSRF;
import spark.Request;
import spark.Response;
import spark.Route;
import utilities.AutoCloseableDB;
import utilities.MustacheRenderer;

public class SessionController {

	public static final Route newForm = (Request req, Response res) -> {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("returnPath", req.queryParams("returnPath"));
		model.put("currentUser", req.session().attribute("currentUser"));
		model.put("noUser", req.session().attribute("currentUser") == null);
		UUID thisCSRF = newCSRF.getNewCSRF();
		model.put("thisCSRF", thisCSRF);
		String StringCSRF = thisCSRF.toString();
		res.cookie("thisCSRF", StringCSRF);
		
		MustacheRenderer.getInstance();

		return MustacheRenderer.getInstance().render("session/newForm.html", model);
	};

	public static final Route create = (Request req, Response res) -> {
		String email = req.queryParams("email");
		String password = req.queryParams("password");

		try (AutoCloseableDB db = new AutoCloseableDB()) {
			User user = User.findFirst("email = ?", email);
			String testCSRF = req.queryParams("thisCSRF"); 
			System.out.println("testCSRF = " + testCSRF);
			String receivedCSRF = req.cookie("thisCSRF");
			System.out.println("newCSRF = " + receivedCSRF);
			
			if (user != null && BCrypt.checkpw(password, user.getPassword()) && testCSRF.equals(receivedCSRF)) {
				req.session().attribute("currentUser", user);
			} else if (user != null) {
				req.session().attribute("message", "Incorrect Password");
			} else {
				req.session().attribute("message", "Username not found");
			}
		}
		
		res.redirect(req.queryParamOrDefault("returnPath", "/"));
		return "";
	};

	public static final Route destroy = (Request req, Response res) -> {
		String email = req.queryParams("email");

		try (AutoCloseableDB db = new AutoCloseableDB()) {
			User user = User.findFirst("email = ?", email);
			req.session().attribute("currentUser", null);
		}
		res.redirect("/");
		return "";
	};

}
