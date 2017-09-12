package com.example.fileDownloadOrUpload.test;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.fileDownloadOrUpload.utils.FileUtils;

/**
 * ���ԵĴ���
 * 
 * @author liuzhihu
 *
 */
@SuppressWarnings("serial")
public class FileUtilsTest extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		/**
		 * �����ļ�
		 */
		FileUtils.download(req, resp, "files/ѧ����Ϣ.xls");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// �ϴ��ļ�
		List<String> filePaths = FileUtils.upload(req, "upload/", ".xls .xlsx");
		System.out.println(filePaths);
	}

}
