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
		String redisURI = System.getenv("REDISTOGO_URL");
		Logger.info("redisURI: " + redisURI);
		pool = new JedisPool(new JedisPoolConfig(), redisURI);
		
//		try {
//			URI redisURI = new URI();
//			Logger.info("redisURI: " + redisURI);
//			
//			Logger.info("redisURI: " + redisURI);
//			Logger.info("redisURI.getHost: " + redisURI.getHost());
//			Logger.info("redisURI.getPort: " + redisURI.getPort());
//			Logger.info("redisURI.getUserInfo: " + redisURI.getUserInfo());
//			
//			pool = new JedisPool(new JedisPoolConfig(), redisURI.getHost(),
//					redisURI.getPort(), Protocol.DEFAULT_TIMEOUT, redisURI
//							.getUserInfo().split(":", 2)[1]);
//		} catch (URISyntaxException e) {
//			Logger.error("Redis URI couldn't be parsed. Handle exception, %s",
//					e);
//		}
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