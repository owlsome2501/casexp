package org.casexp.casserver;

import static org.junit.Assert.*;

import java.util.Arrays;

import javax.naming.InitialContext;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

class GsonList {
	public String state;
	public String[] lists;

	public GsonList() {
	}

	public GsonList(String[] parameters) {
		this.state = parameters[0];
		this.lists = Arrays.copyOfRange(parameters, 1, parameters.length);
	}
}

public class GsonTest {

	private Gson gson;

	@Before
	public void init() {
		gson = new Gson();
	}

	@Test
	public void GoodJosntest() {
		GsonList gsonList = new GsonList(
				new String[] { "good", "arg1", "arg2", "arg3" });
		String json = gson.toJson(gsonList);
		System.out.println("json output: " + json);
	}

	@Test
	public void EmptyListTest() {
		GsonList gsonList = new GsonList(new String[] { "good" });
		if (gsonList.lists == null) {
			System.out.println("gsonList.lists == null");
		} else {
			System.out.println("gsonList length: "
					+ String.valueOf(gsonList.lists.length));
		}
		String json = gson.toJson(gsonList);
		System.out.println("json other output: " + json);
	}

	@Test
	public void EmptyObjectTest() {
		GsonList gsonList = new GsonList();
		String json = gson.toJson(gsonList);
		System.out.println("json bad output: " + json);
	}

	@Test
	public void NullTest() {
		String json = gson.toJson(null);
		System.out.println("json null output: " + json);
	}

}
