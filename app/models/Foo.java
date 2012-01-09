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

public class Foo implements JSONDTORepresentable<Foo.DTO> {

	public class DTO implements JSONDTO {
		public String id;
		public String name;
	}

	public void merge(DTO dto) {
		this.name = dto.name;
	}

	public DTO toDTO() {
		DTO dto = new DTO();
		dto.id = id;
		dto.name = name;
		return dto;
	}

	public transient static JedisPool pool = null;

	public String id = UUID.randomUUID().toString();

	public String name;

	public Foo() {
	}

	public Foo(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public Foo save() {
		Jedis jedis = pool.getResource();
		try {
			jedis.set(id, name);
		} finally {
			pool.returnResource(jedis);
		}
		return this;
	}

	public void delete() {
		Jedis jedis = pool.getResource();
		try {
			jedis.del(id);
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static Foo get(String id) {
		Jedis jedis = pool.getResource();
		try {
			String name = jedis.get(id);
			return new Foo(id, name);
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static void deleteAll() {
		Jedis jedis = pool.getResource();
		try {
			Set<String> keys = jedis.keys("*");
			for (String key : keys)
				jedis.del(key);
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static List<Foo> findAll() {
		List<Foo> foos = new ArrayList<Foo>();
		Jedis jedis = pool.getResource();
		try {
			Set<String> keys = jedis.keys("*");
			for (String id : keys) {
				String name = jedis.get(id);
				foos.add(new Foo(id, name));
			}

		} finally {
			pool.returnResource(jedis);
		}
		return foos;
	}

}
