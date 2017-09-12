package com.example.httpjson.engine;

import org.codehaus.jackson.map.ObjectMapper;

import com.example.httpjson.client.JsonClient;
import com.example.httpjson.domain.StudentReq;
import com.example.httpjson.domain.StudentRsp;

import net.sf.json.JSONObject;

/**
 * 请求调用json的业务代码
 * 
 * @author liuzhihu
 *
 */
public class JsonEngine {

	/**
	 * 请求json 的地址
	 */
	private static final String URL = "http://www.baidu.com";

	/**
	 * 通过id 查询学生响应信息
	 * 
	 * @return
	 */
	public StudentRsp getStudentById(StudentReq req) {

		StudentRsp rsp = null;
		String requestJSON = null;
		String responseJSON = null;

		try {
			// 将请求转换成为json格式的
			requestJSON = JSONObject.fromObject(req).toString();

			// 获取响应值
			responseJSON = JsonClient.getInstance().httpPostWithJSON(URL, requestJSON);

			ObjectMapper objectMapper = new ObjectMapper();

			// 将json转换成实体类
			rsp = (StudentRsp) objectMapper.readValue(responseJSON, StudentRsp.class);

			// 注意 一般需要对rsp进行判空 这里因为是模拟直接省略了

		} catch (Exception e) {
			e.getMessage();
		}

		return rsp;

	}
}
