package com.example.httpswithcertificate.engine;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import com.example.httpswithcertificate.client.HttpsWithCertificateClient;

/**
 * 请求https 带有证书
 * 
 * @author liuzhihu
 *
 */
public class HttpsWithCertificateEngine {

	/**
	 * 请求的https路径
	 */
	private static final String httGetURL = "https://wap.cmgame.com:32310/portalone/MiguPayNotify&productId=003";

	/**
	 * 真正调用https的方法
	 * 
	 * @param url
	 * @return
	 */
	public static String httpGetAppService() {

		HttpGet httpget = new HttpGet(httGetURL);

		// 设置以json的形式接收代码
		httpget.setHeader("Accept", "application/json");

		httpget.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		httpget.setHeader("AUTHORIZATION", "X-WSSE");

		// 直接使用配置的host
		httpget.setHeader("Host", "127.0.0.1");

		// 设置连接超时时间
		int timeout = 2000;

		// 设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
				.build();
		httpget.setConfig(requestConfig);

		String cerPath = "";
		String tmpLoc = System.getProperty("user.home");

		// 这里的url必须加上cer文件名
		String pamentCerUrl = "/PortalONE/tomcat/pamentcer/iSimularClient.cer";
		if (StringUtils.isEmpty(pamentCerUrl)) {
			pamentCerUrl = "/PortalONE/tomcat/pamentcer/iSimularClient.cer";
		}

		// 存放在环境上的cer文件证书目录
		cerPath = tmpLoc + pamentCerUrl;

		if (!pamentCerUrl.startsWith(File.separator)) {
			cerPath = tmpLoc + File.separator + pamentCerUrl;
		}

		// 证书文件格式
		String cerFactoryName = "X.509";
		String keystore = "jks";
		String responseBody = "";

		try {
			// 封装https请求
			responseBody = HttpsWithCertificateClient.get(httpget, cerFactoryName, keystore, cerPath);
			return responseBody;

		} catch (Exception e) {
			e.getMessage();
		}

		return "";
	}

	public static String httpPostAppService(String url, String requestBody) {

		String cerPath = "";
		String tmpLoc = System.getProperty("user.home");

		// 这里的url必须加上cer文件名
		String pamentCerUrl = "/PortalONE/tomcat/pamentcer/iSimularClient.cer";
		if (StringUtils.isEmpty(pamentCerUrl)) {
			pamentCerUrl = "/PortalONE/tomcat/pamentcer/iSimularClient.cer";
		}

		// 存放在环境上的cer文件证书目录
		cerPath = tmpLoc + pamentCerUrl;

		if (!pamentCerUrl.startsWith(File.separator)) {
			cerPath = tmpLoc + File.separator + pamentCerUrl;
		}

		HttpPost httpPost = new HttpPost(url);

		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		httpPost.setHeader("AUTHORIZATION", "X-WSSE");

		// 直接使用配置的host
		httpPost.setHeader("Host", "127.0.0.1");

		// 设置连接超时时间
		int timeout = 2000;

		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
				.build();
		httpPost.setConfig(requestConfig);

		// 证书文件格式
		String cerFactoryName = "X.509";
		String keystore = "jks";

		HttpEntity entity = new StringEntity(requestBody.toString(), "UTF-8");
		httpPost.setEntity(entity);
		String responseBody = "";
		try {
			responseBody = HttpsWithCertificateClient.post(httpPost, cerFactoryName, keystore, cerPath);
			return responseBody;
		} catch (Exception e) {
			e.getMessage();
		}

		return "";

	}

}
