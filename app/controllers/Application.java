package controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import jsondto.JSONDTOUtil;

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

	public static void listFoos() throws Exception {
		renderJSON(Foo.findAll());
	}

	public static void createFoo(Foo.DTO dto) {
		Foo foo = new Foo().merge(dto).save();
		Logger.info("Created new Foo with id=%s", foo.id);
		renderJSON(foo);
	}

	public static void updateFoo(String id, Foo.DTO dto) {
		Foo foo = Foo.get(id).merge(dto).save();
		Logger.info("Updated a Foo with id=%s, new name=%s", foo.id, foo.name);
		renderJSON(foo);
	}

	public static void deleteFoo(String id) {
		Foo foo = Foo.get(id);
		Logger.info("Deleted Foo with id=%s", foo.id);
		foo.delete();
	}
}