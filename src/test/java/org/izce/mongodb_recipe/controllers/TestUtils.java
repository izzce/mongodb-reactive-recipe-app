package org.izce.mongodb_recipe.controllers;

import com.fasterxml.jackson.databind.json.JsonMapper;

public class TestUtils {
	public static String asJsonString(final Object obj) {
		try {
			return new JsonMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
