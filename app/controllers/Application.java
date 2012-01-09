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

	private static JedisPool pool = null;

	static {
		try {
			String redisEnvURL = System.getenv("REDISTOGO_URL");
			Logger.info("redisEnvURL: " + redisEnvURL);

			URI redisURI = new URI(redisEnvURL);

			if (redisURI.getUserInfo() != null) {
				pool = new JedisPool(new JedisPoolConfig(), redisURI.getHost(),
						redisURI.getPort(), Protocol.DEFAULT_TIMEOUT, redisURI
								.getUserInfo().split(":", 2)[1]);
			} else
				pool = new JedisPool(new JedisPoolConfig(), redisEnvURL);
		} catch (URISyntaxException e) {
			Logger.error("URI couldn't be parsed. Handle exception, %s", e);

		}
	}

	public static void testRedis() {
		Jedis jedis = pool.getResource();
		try {
			jedis.set("foo", "bar");
			String foobar = jedis.get("foo");
			renderJSON(foobar);
		} finally {
			pool.returnResource(jedis);
		}
	}

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