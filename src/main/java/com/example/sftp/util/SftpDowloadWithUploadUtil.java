package com.example.sftp.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import com.jcraft.jsch.ChannelSftp.LsEntry;

/**
 * sftp 上传下载功能 第一个工具类
 * 
 * @author liuzhihu
 *
 */
public class SftpDowloadWithUploadUtil {

	/**
	 * 登录sftp
	 * 
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @return
	 */
	public static ChannelSftp login(String host, int port, String username, String password) {
		ChannelSftp sftp = null;

		try {
			JSch jsch = new JSch();
			jsch.getSession(username, host, port);
			Session sshSession = jsch.getSession(username, host, port);
			sshSession.setPassword(password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.setTimeout(5000);
			sshSession.connect();
			Channel channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return sftp;
	}

	/**
	 * 断开FTP连接
	 * 
	 * @param sftpClient
	 */
	public static void logout(ChannelSftp sftpClient) {
		if (null != sftpClient) {
			sftpClient.disconnect();

			try {
				if (null != sftpClient.getSession()) {
					sftpClient.getSession().disconnect();
				}
			} catch (JSchException e) {
				e.getMessage();
			}
		}
	}

	/**
	 * 列出指定目录下的文件
	 * 
	 * @param directory
	 * @param sftp
	 * @return
	 * @throws SftpException
	 */
	public static List<String> listFiles(String directory, ChannelSftp sftp) throws SftpException {
		@SuppressWarnings("rawtypes")
		Vector fileList;
		List<String> fileNameList = new ArrayList<String>();

		fileList = sftp.ls(directory);
		@SuppressWarnings("rawtypes")
		Iterator it = fileList.iterator();

		while (it.hasNext()) {
			String fileName = ((LsEntry) it.next()).getFilename();
			if (".".equals(fileName) || "..".equals(fileName)) {
				continue;
			}
			fileNameList.add(fileName);
		}
		return fileNameList;
	}

	/**
	 * 上传到指定目录
	 * 
	 * @param directory
	 * @param sftp
	 * @throws Exception
	 */
	public static void uploadByDirectory(String directory, ChannelSftp sftp) throws Exception {

		String uploadFile = "";
		List<String> uploadFileList = listFiles(directory, sftp);
		Iterator<String> it = uploadFileList.iterator();

		while (it.hasNext()) {
			uploadFile = it.next().toString();
			upload(directory, uploadFile, sftp);
		}
	}

	/**
	 * 上传文件
	 * 
	 * @param directory
	 * @param uploadFile
	 * @param sftp
	 */
	public static void upload(String directory, String uploadFile, ChannelSftp sftp) {
		mkDir(directory, sftp);
		try {
			sftp.cd(directory);
			File file = new File(uploadFile);
			sftp.put(uploadFile, file.getName());
		} catch (SftpException e) {
			e.getMessage();
		}
	}

	/**
	 * 下载单个文件
	 * 
	 * @param directory
	 * @param downloadFile
	 * @param saveDirectory
	 * @param sftp
	 * @throws SftpException
	 */
	public static void download(String directory, String downloadFile, String saveDirectory, ChannelSftp sftp)
			throws SftpException {
		File file = new File(saveDirectory);
		// 如果文件夹不存在则创建
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if (!file.exists() && !file.isDirectory()) {
			file.mkdir();
		}
		String saveFile = saveDirectory + "/" + downloadFile;
		sftp.cd(directory);
		sftp.get(downloadFile, saveFile);
	}

	/**
	 * 批量下载文件
	 * 
	 * @param directory
	 * @param saveDirectory
	 * @param sftp
	 * @throws Exception
	 * @see [类、类#方法、类#成员]
	 */
	public static Boolean downloadByDirectory(String directory, String saveDirectory, ChannelSftp sftp)
			throws SftpException {
		String downloadFile = "";
		List<String> downloadFileList = listFiles(directory, sftp);
		Iterator<String> it = downloadFileList.iterator();
		if (downloadFileList.size() <= 0) {
			return false;
		}
		while (it.hasNext()) {
			downloadFile = it.next().toString();
			if (downloadFile.indexOf(".") < 0) {
				continue;
			}
			download(directory, downloadFile, saveDirectory + getSysTime(), sftp);
		}
		return true;
	}

	/**
	 * 删除文件
	 * 
	 * @param directory
	 * @param deleteFile
	 * @param sftp
	 */
	public static void delete(String directory, String deleteFile, ChannelSftp sftp) {
		try {
			sftp.cd(directory);
			sftp.rm(deleteFile);
		} catch (Exception e) {
			e.getMessage();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 创建指定文件夹
	 * 
	 * @param dirName
	 * @param sftp
	 */
	public static void mkDir(String dirName, ChannelSftp sftp) {
		String[] dirs = dirName.split("/");

		try {
			String now = sftp.pwd();
			sftp.cd("/");
			for (int i = 0; i < dirs.length; i++) {
				if (StringUtils.isNotEmpty(dirs[i])) {
					boolean dirExists = openDir(dirs[i], sftp);
					if (!dirExists) {
						sftp.mkdir(dirs[i]);
						sftp.cd(dirs[i]);
					}
				}
			}
			sftp.cd(now);
		} catch (SftpException e) {
			e.getMessage();
		}
	}

	/**
	 * 打开指定目录
	 * 
	 * @param directory
	 * @param sftp
	 * @return
	 */
	public static boolean openDir(String directory, ChannelSftp sftp) {
		try {
			sftp.cd(directory);
			return true;
		} catch (SftpException e) {
			e.getMessage();
			return false;
		}
	}

	/**
	 * 格式化路径
	 * 
	 * @return
	 */
	public static List<String> formatPath(final String srcPath) {
		List<String> list = new ArrayList<String>(2);
		String dir = "";
		String fileName = "";
		String repSrc = srcPath.replaceAll("\\\\", "/");
		int firstP = repSrc.indexOf("/");
		int lastP = repSrc.lastIndexOf("/");
		fileName = repSrc.substring(lastP + 1);
		dir = repSrc.substring(firstP, lastP);
		dir = (dir.length() == 1 ? dir : (dir + "/"));
		list.add(dir);
		list.add(fileName);
		return list;
	}

	/**
	 * 获取系统时间
	 * 
	 * @return
	 */
	public static String getSysTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
		Date date = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String todayTime = sdf.format(date);
		return todayTime;
	}

	/**
	 * 测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ChannelSftp sftp = login("172.17.26.32", 22, "public", "public");

		System.out.println(sftp.toString());

		logout(sftp);

		System.out.println("登出");
	}

}
