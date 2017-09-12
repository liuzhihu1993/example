package com.example.sftp.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.io.IOUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * sftp �ڶ�������
 * 
 * @author liuzhihu
 *
 */
public class SFTPUtil {

	private ChannelSftp sftp;

	private Session session;
	/** FTP ��¼�û��� */
	private String username;
	/** FTP ��¼���� */
	private String password;
	/** ˽Կ�ļ���·�� */
	private String keyFilePath;
	/** FTP ��������ַIP��ַ */
	private String host;
	/** FTP �˿� */
	private int port;

	/**
	 * �������������֤��sftp����
	 * 
	 * @param userName
	 * @param password
	 * @param host
	 * @param port
	 */
	public SFTPUtil(String username, String password, String host, int port) {
		this.username = username;
		this.password = password;
		this.host = host;
		this.port = port;
	}

	/**
	 * ���������Կ��֤��sftp����
	 * 
	 * @param userName
	 * @param host
	 * @param port
	 * @param keyFilePath
	 */
	public SFTPUtil(String username, String host, int port, String keyFilePath) {
		this.username = username;
		this.host = host;
		this.port = port;
		this.keyFilePath = keyFilePath;
	}

	public SFTPUtil() {
	}

	/**
	 * ����sftp������
	 * 
	 * @throws Exception
	 */
	public void login() throws Exception {
		try {
			JSch jsch = new JSch();
			if (keyFilePath != null) {
				jsch.addIdentity(keyFilePath);// ����˽Կ
			}

			session = jsch.getSession(username, host, port);
			if (password != null) {
				session.setPassword(password);
			}
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");

			session.setConfig(config);
			session.connect();

			Channel channel = session.openChannel("sftp");
			channel.connect();

			sftp = (ChannelSftp) channel;
		} catch (JSchException e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * �ر����� server
	 */
	public void logout() {
		if (sftp != null) {
			if (sftp.isConnected()) {
				sftp.disconnect();
			}
		}
		if (session != null) {
			if (session.isConnected()) {
				session.disconnect();
			}
		}
	}

	/**
	 * ���������������ϴ���sftp��Ϊ�ļ�
	 * 
	 * @param directory
	 *            �ϴ�����Ŀ¼
	 * @param sftpFileName
	 *            sftp���ļ���
	 * @param in
	 *            ������
	 * @throws SftpException
	 * @throws Exception
	 */
	public void upload(String directory, String sftpFileName, InputStream input) throws SftpException {
		try {
			sftp.cd(directory);
		} catch (SftpException e) {
			sftp.mkdir(directory);
			sftp.cd(directory);
		}
		sftp.put(input, sftpFileName);
	}

	/**
	 * �ϴ������ļ�
	 * 
	 * @param directory
	 *            �ϴ���sftpĿ¼
	 * @param uploadFile
	 *            Ҫ�ϴ����ļ�,����·��
	 * @throws FileNotFoundException
	 * @throws SftpException
	 * @throws Exception
	 */
	public void upload(String directory, String uploadFile) throws FileNotFoundException, SftpException {
		File file = new File(uploadFile);
		upload(directory, file.getName(), new FileInputStream(file));
	}

	/**
	 * ��byte[]�ϴ���sftp����Ϊ�ļ���ע��:��String����byte[]�ǣ�Ҫָ���ַ�����
	 * 
	 * @param directory
	 *            �ϴ���sftpĿ¼
	 * @param sftpFileName
	 *            �ļ���sftp�˵�����
	 * @param byteArr
	 *            Ҫ�ϴ����ֽ�����
	 * @throws SftpException
	 * @throws Exception
	 */
	public void upload(String directory, String sftpFileName, byte[] byteArr) throws SftpException {
		upload(directory, sftpFileName, new ByteArrayInputStream(byteArr));
	}

	/**
	 * ���ַ�������ָ�����ַ������ϴ���sftp
	 * 
	 * @param directory
	 *            �ϴ���sftpĿ¼
	 * @param sftpFileName
	 *            �ļ���sftp�˵�����
	 * @param dataStr
	 *            ���ϴ�������
	 * @param charsetName
	 *            sftp�ϵ��ļ��������ַ����뱣��
	 * @throws UnsupportedEncodingException
	 * @throws SftpException
	 * @throws Exception
	 */
	public void upload(String directory, String sftpFileName, String dataStr, String charsetName)
			throws UnsupportedEncodingException, SftpException {
		upload(directory, sftpFileName, new ByteArrayInputStream(dataStr.getBytes(charsetName)));

	}

	/**
	 * �����ļ�
	 * 
	 * @param directory
	 *            ����Ŀ¼
	 * @param downloadFile
	 *            ���ص��ļ�
	 * @param saveFile
	 *            ���ڱ��ص�·��
	 * @throws SftpException
	 * @throws FileNotFoundException
	 * @throws Exception
	 */
	public void download(String directory, String downloadFile, String saveFile)
			throws SftpException, FileNotFoundException {
		if (directory != null && !"".equals(directory)) {
			sftp.cd(directory);
		}
		File file = new File(saveFile);
		sftp.get(downloadFile, new FileOutputStream(file));
	}

	/**
	 * �����ļ�
	 * 
	 * @param directory
	 *            ����Ŀ¼
	 * @param downloadFile
	 *            ���ص��ļ���
	 * @return �ֽ�����
	 * @throws SftpException
	 * @throws IOException
	 * @throws Exception
	 */
	public byte[] download(String directory, String downloadFile) throws SftpException, IOException {
		if (directory != null && !"".equals(directory)) {
			sftp.cd(directory);
		}
		InputStream is = sftp.get(downloadFile);

		byte[] fileData = IOUtils.toByteArray(is);

		return fileData;
	}

	/**
	 * ɾ���ļ�
	 * 
	 * @param directory
	 *            Ҫɾ���ļ�����Ŀ¼
	 * @param deleteFile
	 *            Ҫɾ�����ļ�
	 * @throws SftpException
	 * @throws Exception
	 */
	public void delete(String directory, String deleteFile) throws SftpException {
		sftp.cd(directory);
		sftp.rm(deleteFile);
	}

	/**
	 * �г�Ŀ¼�µ��ļ�
	 * 
	 * @param directory
	 *            Ҫ�г���Ŀ¼
	 * @param sftp
	 * @return
	 * @throws SftpException
	 */
	public Vector<?> listFiles(String directory) throws SftpException {
		return sftp.ls(directory);
	}

	public static void main(String[] args) throws Exception {
		SFTPUtil sftp = new SFTPUtil("public", "public", "172.17.26.32", 22);
		sftp.login();
		byte[] buff = sftp.download("./PortalONE", "tomcat.zip");
		System.out.println(Arrays.toString(buff));
		sftp.logout();
	}

}
