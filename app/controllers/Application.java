package controllers;

import models.Foo;
import play.mvc.Controller;
import play.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class Application extends Controller {

	public static void index() {
		render();
	}
	
	public static void clear() {
		Foo.deleteAll();
		index();
	}

	public static void listFoos() {
		renderJSON(Foo.findAll());
	}

	public static void createFoo(JsonElement body) {
		Foo foo = new Gson().fromJson(body, Foo.class).save();
		Logger.info("Created new Foo with id=%s", foo.id);
		renderJSON(foo);
	}

	public static void updateFoo(JsonElement body, Long id) {
		Foo foo = new Gson().fromJson(body, Foo.class).mergeSave();
		Logger.info("Updated existing Foo with id=%s, new name=%s", foo.id,
				foo.name);
		renderJSON(foo);
	}

	public static void deleteFoo(Long id) {
		Foo foo = Foo.findById(id);
		Logger.info("Deleted Foo with id=%s", foo.id);
		foo.delete();
	}
}