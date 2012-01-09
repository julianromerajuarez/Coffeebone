package models;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisModel {

	public transient static JedisPool pool = null;

	public static <T> T execute(Function<T> function) {
		Jedis jedis = pool.getResource();
		try {
			return function.execute(jedis);
		} finally {
			pool.returnResource(jedis);
		}
	}

	protected static abstract class Function<T> {

		public abstract T execute(Jedis jedis);

	}

}
