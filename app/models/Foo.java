package models;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;

import jsondto.JSONDTO;
import jsondto.JSONDTORepresentable;

import play.Logger;
import play.data.binding.TypeBinder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Foo extends RedisModel implements
		JSONDTORepresentable<Foo, Foo.DTO> {

	public String id = UUID.randomUUID().toString();
	public String name;

	public class DTO implements JSONDTO {
		public String id;
		public String name;
	}

	public Foo merge(DTO dto) {
		this.name = dto.name;
		return this;
	}

	public DTO toDTO() {
		DTO dto = new DTO();
		dto.id = id;
		dto.name = name;
		return dto;
	}

	public Foo() {
	}

	public Foo(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public Foo save() {
		execute(new Function<String>() {
			public String execute(Jedis jedis) {
				return jedis.set(id, name);
			}
		});
		return this;
	}

	public void delete() {
		execute(new Function<Long>() {
			public Long execute(Jedis jedis) {
				return jedis.del(id);
			}
		});
	}

	public static Foo get(final String id) {
		return execute(new Function<Foo>() {
			public Foo execute(Jedis jedis) {
				String name = jedis.get(id);
				return new Foo(id, name);
			}
		});
	}

	public static void deleteAll() {
		execute(new Function<Void>() {
			public Void execute(Jedis jedis) {
				Set<String> keys = jedis.keys("*");
				for (String key : keys)
					jedis.del(key);
				return null;
			}
		});
	}

	public static List<Foo> findAll() {
		return execute(new Function<List<Foo>>() {
			public List<Foo> execute(Jedis jedis) {
				List<Foo> foos = new ArrayList<Foo>();
				Set<String> keys = jedis.keys("*");
				for (String id : keys) {
					String name = jedis.get(id);
					foos.add(new Foo(id, name));
				}
				return foos;
			}
		});
	}

}
