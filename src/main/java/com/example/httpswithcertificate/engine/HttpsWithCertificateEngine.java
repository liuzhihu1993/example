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
 * ����https ����֤��
 * 
 * @author liuzhihu
 *
 */
public class HttpsWithCertificateEngine {

	/**
	 * �����https·��
	 */
	private static final String httGetURL = "https://wap.cmgame.com:32310/portalone/MiguPayNotify&productId=003";

	/**
	 * ��������https�ķ���
	 * 
	 * @param url
	 * @return
	 */
	public static String httpGetAppService() {

		HttpGet httpget = new HttpGet(httGetURL);

		// ������json����ʽ���մ���
		httpget.setHeader("Accept", "application/json");

		httpget.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		httpget.setHeader("AUTHORIZATION", "X-WSSE");

		// ֱ��ʹ�����õ�host
		httpget.setHeader("Host", "127.0.0.1");

		// �������ӳ�ʱʱ��
		int timeout = 2000;

		// ��������ʹ��䳬ʱʱ��
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
				.build();
		httpget.setConfig(requestConfig);

		String cerPath = "";
		String tmpLoc = System.getProperty("user.home");

		// �����url�������cer�ļ���
		String pamentCerUrl = "/PortalONE/tomcat/pamentcer/iSimularClient.cer";
		if (StringUtils.isEmpty(pamentCerUrl)) {
			pamentCerUrl = "/PortalONE/tomcat/pamentcer/iSimularClient.cer";
		}

		// ����ڻ����ϵ�cer�ļ�֤��Ŀ¼
		cerPath = tmpLoc + pamentCerUrl;

		if (!pamentCerUrl.startsWith(File.separator)) {
			cerPath = tmpLoc + File.separator + pamentCerUrl;
		}

		// ֤���ļ���ʽ
		String cerFactoryName = "X.509";
		String keystore = "jks";
		String responseBody = "";

		try {
			// ��װhttps����
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

		// �����url�������cer�ļ���
		String pamentCerUrl = "/PortalONE/tomcat/pamentcer/iSimularClient.cer";
		if (StringUtils.isEmpty(pamentCerUrl)) {
			pamentCerUrl = "/PortalONE/tomcat/pamentcer/iSimularClient.cer";
		}

		// ����ڻ����ϵ�cer�ļ�֤��Ŀ¼
		cerPath = tmpLoc + pamentCerUrl;

		if (!pamentCerUrl.startsWith(File.separator)) {
			cerPath = tmpLoc + File.separator + pamentCerUrl;
		}

		HttpPost httpPost = new HttpPost(url);

		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		httpPost.setHeader("AUTHORIZATION", "X-WSSE");

		// ֱ��ʹ�����õ�host
		httpPost.setHeader("Host", "127.0.0.1");

		// �������ӳ�ʱʱ��
		int timeout = 2000;

		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
				.build();
		httpPost.setConfig(requestConfig);

		// ֤���ļ���ʽ
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
