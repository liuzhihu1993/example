package com.example.httpjson.engine;

import org.codehaus.jackson.map.ObjectMapper;

import com.example.httpjson.client.JsonClient;
import com.example.httpjson.domain.StudentReq;
import com.example.httpjson.domain.StudentRsp;

import net.sf.json.JSONObject;

/**
 * �������json��ҵ�����
 * 
 * @author liuzhihu
 *
 */
public class JsonEngine {

	/**
	 * ����json �ĵ�ַ
	 */
	private static final String URL = "http://www.baidu.com";

	/**
	 * ͨ��id ��ѯѧ����Ӧ��Ϣ
	 * 
	 * @return
	 */
	public StudentRsp getStudentById(StudentReq req) {

		StudentRsp rsp = null;
		String requestJSON = null;
		String responseJSON = null;

		try {
			// ������ת����Ϊjson��ʽ��
			requestJSON = JSONObject.fromObject(req).toString();

			// ��ȡ��Ӧֵ
			responseJSON = JsonClient.getInstance().httpPostWithJSON(URL, requestJSON);

			ObjectMapper objectMapper = new ObjectMapper();

			// ��jsonת����ʵ����
			rsp = (StudentRsp) objectMapper.readValue(responseJSON, StudentRsp.class);

			// ע�� һ����Ҫ��rsp�����п� ������Ϊ��ģ��ֱ��ʡ����

		} catch (Exception e) {
			e.getMessage();
		}

		return rsp;

	}
}
