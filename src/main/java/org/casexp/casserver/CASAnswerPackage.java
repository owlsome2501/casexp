package org.casexp.casserver;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;

class CASAnswerPackage {
	public static String pack(CASAnswerPackage anspkg) {
		return new Gson().toJson(anspkg);
	}

	public String pack() {
		return pack(this);
	}

	public CASAnswerPackage() {
		this.answers = new ArrayList<CASAnswer>();
	}

	public String account;
	public ZonedDateTime date;
	public List<CASAnswer> answers;
}

class CASAnswer {
	public String queryUUID;
	public String state;
	public String data;

	public static CASAnswer ERROR(String data) {
		CASAnswer answer = new CASAnswer();
		answer.queryUUID = "0";
		answer.state = "ERROR";
		answer.data = data;
		return answer;
	}

	public static CASAnswer OK(UUID uuid, String data) {
		CASAnswer answer = new CASAnswer();
		answer.queryUUID = uuid.toString();
		answer.state = "OK";
		answer.data = data;
		return answer;
	}
}