package io.redstone.config.couchbean;

import java.util.List;

import io.mercury.persistence.json.JsonParser;
import io.redstone.config.couchbean.base.CouchConnector;
import io.redstone.config.couchbean.base.CouchDocumentEnum;

public class AppConf {

	private int id;

	public int getId() {
		return id;
	}

	public AppConf setId(int id) {
		this.id = id;
		return this;
	}

	public static void main(String[] args) {

		String json = CouchConnector.Singleton.getCouchBeanValue(CouchDocumentEnum.AppConf);

		List<AppConf> jsonToList = JsonParser.toList(json, AppConf.class);

		jsonToList.forEach(appConfig -> {
			System.out.println(appConfig.getId());
		});

	}

}