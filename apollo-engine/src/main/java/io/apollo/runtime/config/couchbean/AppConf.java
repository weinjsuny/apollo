package io.apollo.runtime.config.couchbean;

import java.util.List;

import io.apollo.runtime.config.couchbean.base.CouchConnector;
import io.apollo.runtime.config.couchbean.base.CouchDocumentEnum;
import io.mercury.serialization.json.JsonUtil;

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

		List<AppConf> jsonToList = JsonUtil.toList(json, AppConf.class);

		jsonToList.forEach(appConfig -> {
			System.out.println(appConfig.getId());
		});

	}

}
