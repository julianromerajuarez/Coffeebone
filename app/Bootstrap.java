import java.net.URI;
import java.net.URISyntaxException;

import models.Foo;

import play.Logger;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

@OnApplicationStart
public class Bootstrap extends Job<Void> {

	@Override
	public void doJob() throws Exception {
		Logger.info("Bootstrap.doJob");

		try {
			JedisPool pool = null;
			String redisEnvURL = System.getenv("REDISTOGO_URL");
			Logger.info("redisEnvURL: " + redisEnvURL);

			URI redisURI = new URI(redisEnvURL);

			if (redisURI.getUserInfo() != null) {
				pool = new JedisPool(new JedisPoolConfig(), redisURI.getHost(),
						redisURI.getPort(), Protocol.DEFAULT_TIMEOUT, redisURI
								.getUserInfo().split(":", 2)[1]);
			} else
				pool = new JedisPool(new JedisPoolConfig(), redisEnvURL);

			Foo.pool = pool;

			Logger.info("Redis pool configured successfully.");

		} catch (URISyntaxException e) {
			Logger.error("URI couldn't be parsed. Handle exception, %s", e);
		}
	}
}
