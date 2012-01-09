package controllers;

import java.net.URI;
import java.net.URISyntaxException;

import models.Foo;
import play.mvc.Controller;
import play.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class Application extends Controller {

	public static void jasmine() {
		render();
	}

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

	public static void updateFoo(JsonElement body, String id) {
		Foo newFoo = new Gson().fromJson(body, Foo.class);
		Foo oldFoo = Foo.get(id);
		oldFoo.name = newFoo.name;
		oldFoo.save();

		Logger.info("Updated existing Foo with id=%s, new name=%s", oldFoo.id,
				oldFoo.name);
		renderJSON(oldFoo);
	}

	public static void deleteFoo(String id) {
		Foo foo = Foo.get(id);
		Logger.info("Deleted Foo with id=%s", foo.id);
		foo.delete();
	}
}