package com.libertymutual.goforcode.spark.app;

import static spark.Spark.*;

import java.util.HashMap;
import java.util.Map;

import controllers.ApartmentController;
import controllers.SessionController;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import controllers.HomeController;

public class Application {

	public static void main(String[] args) {
//		get("/", HomeController.index);
		get("/", HomeController.index2);
//		get("/", (req, res) -> {
//			Map<String, Object> model = new HashMap<>();
//			
//			return new VelocityTemplateEngine().render(new ModelAndView(model, "/templates/home/index2.vm"));
//		});
		get("/apartments/:id", ApartmentController.details);
		get("/login", SessionController.newForm);
		post("/login", SessionController.create);
	}

}
