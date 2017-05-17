package com.lzx.allenLee.util.fileManager;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.lzx.allenLee.global.Global;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * @function 1.��url��ַ�����ļ�
 * @author allenlee
 * 
 */
public class FileUtil {

	/**
	 * 读取文件的内容
	 * 
	 * @param filePath
	 * @param paramContext
	 * @return
	 */
	public static String getFileContentFromInputStream(String filePath, Context paramContext) {
		String content;

		try {

			InputStream localInputStream = getFileInputStreamFromAssetsDir(filePath, paramContext);
			if (localInputStream == null) {
				content = null;
			} else {
				byte[] arrayOfByte = new byte[localInputStream.available()];
				localInputStream.read(arrayOfByte);
				localInputStream.close();
				content = new String(arrayOfByte);
			}

			// str = convertCodeAndGetText(filePath);
		} catch (Exception localException) {
			localException.printStackTrace();
			content = null;
		}
		return content;
	}

	/**
	 * 从assets文件夹读取输入流
	 * 
	 * @param filePath
	 * @param paramContext
	 * @return
	 */
	public static InputStream getFileInputStreamFromAssetsDir(String filePath, Context paramContext) {
		InputStream in = null;
		try {
			in = paramContext.getResources().getAssets().open(filePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 从资源文件夹读取插件

		return in;

	}

	/**
	 * getFileFromUrl,从服务器读取单个文件到本地文件夹
	 * 
	 * @param url：文件地址
	 * @param localPath：本地路径
	 * @return
	 */
	public static boolean getFileFromUrl(String url, String localPath) {
		FileOutputStream fos = null;
		Boolean result = false;
		byte[] resBytes = null;
		try {
			resBytes = getFileByteArrayFromURL(url);
			if (resBytes != null) {

				try {
					fos = new FileOutputStream(localPath);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				try {
					fos.write(resBytes);
				} catch (IOException e) {
					e.printStackTrace();
				}

				result = true;
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				fos = null;
			}
		}
		return result;
	}

	/**
	 * get File ByteArray From URL with HttpURLConnection
	 * 
	 * @param urlPath
	 * @return
	 */
	public static byte[] getFileByteArrayFromURL(String urlPath) {
		byte[] fileByteArrayData = null;
		InputStream is = null;
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlPath);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			// conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(60000);
			is = conn.getInputStream();
			if (conn.getResponseCode() == 200) {
				fileByteArrayData = readInputStreamAsByteArray(is);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
					is = null;
					conn.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileByteArrayData;
	}

	/**
	 * read InputStream into byte array
	 * 
	 * @param is
	 * @return
	 */
	public static byte[] readInputStreamAsByteArray(InputStream is) {
		byte[] byteArrayData = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = -1;
		try {
			while ((length = is.read(buffer)) != -1) {
				baos.write(buffer, 0, length);
			}
			baos.flush();
		} catch (IOException e) {
			return null;
		}
		byteArrayData = baos.toByteArray();
		try {
			is.close();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return byteArrayData;
	}

	/**
	 * 从sd卡或资源文件中获取位图
	 * 
	 * @return
	 */
	public Bitmap getBitmap(String filePath, Context ctx) {
		Bitmap bitmap = null;
		return bitmap;
	}

	/**
	 * 获取文件的绝对路径
	 * 
	 * @param relativePath
	 * @return
	 */
	public static String getFilePath(String relativePath) {
		String absPath = relativePath.replace('\\', '/'); // 如果\\替换成\
		if (absPath.startsWith(Global.getFileRootPath())) {

		} else {
			// 开头特殊字符处理
			if (absPath.startsWith("."))
				absPath = absPath.substring(1);
			if (absPath.startsWith("/"))
				absPath = absPath.substring(1);
		}
		absPath = Global.getFileRootPath() + absPath;
		return absPath;

	}

	/**
	 * 读取txt内容，解决android读取中文txt的乱码（自动判断文档类型并转码）
	 * 
	 * @param str_filepath
	 * @return
	 * @throws Exception
	 */
	public static String readFileContentByFilePath(String str_filepath) throws FileNotFoundException {// 转码

		File file = new File(str_filepath);
		if (!file.exists()) {
			throw new FileNotFoundException("don't not find key File.");
		}
		BufferedReader reader;
		String text = "";
		try {
			// FileReader f_reader = new FileReader(file);
			// BufferedReader reader = new BufferedReader(f_reader);
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream in = new BufferedInputStream(fis);
			in.mark(4);
			byte[] first3bytes = new byte[3];
			in.read(first3bytes);// 找到文档的前三个字节并自动判断文档类型。
			in.reset();
			if (first3bytes[0] == (byte) 0xEF && first3bytes[1] == (byte) 0xBB && first3bytes[2] == (byte) 0xBF) {// utf-8

				reader = new BufferedReader(new InputStreamReader(in, "utf-8"));

			} else if (first3bytes[0] == (byte) 0xFF && first3bytes[1] == (byte) 0xFE) {

				reader = new BufferedReader(new InputStreamReader(in, "unicode"));
			} else if (first3bytes[0] == (byte) 0xFE && first3bytes[1] == (byte) 0xFF) {

				reader = new BufferedReader(new InputStreamReader(in, "utf-16be"));
			} else if (first3bytes[0] == (byte) 0xFF && first3bytes[1] == (byte) 0xFF) {

				reader = new BufferedReader(new InputStreamReader(in, "utf-16le"));
			} else {

				reader = new BufferedReader(new InputStreamReader(in, "GBK"));
			}
			String str = reader.readLine();

			while (str != null) {
				text = text + str;
				str = reader.readLine();
				if (str != null) {
					text = text + "/n";
				}

			}
			reader.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}

	/** 读取某文件夹下所有的子文件 */
	public static List<File> getSubFiles(String path) {
		List<File> subFiles = new ArrayList<File>();
		// 构建文件对象
		File dir = new File(path);
		// 得到该文件夹下所有文件
		File[] files = dir.listFiles();
		if (files != null) {
			for (File file : files) {
				subFiles.add(file);
			}
		}
		return subFiles;
	}

	/**
	 * 删除路径下的所有文件
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean deleteDirectoryFiles(String filePath) {
		boolean result = true;
		if (filePath == null || filePath.length() == 0) {
			result = false;
		} else {
			if (!filePath.endsWith("/")) {
				filePath += "/";
			}
			try {
				File dir = new File(filePath);
				if (dir.exists()) {
					File[] files = dir.listFiles();
					if (files != null) {
						for (int i = 0; i < files.length; i++) {
							files[i].delete();
						}
					}
				}
			} catch (Exception e) {
				result = false;
			}

		}

		return result;
	}

	/**
	 * 是否存在子文件夹及文件
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean ifContainChildrenFiles(String filePath) {

		boolean result = false;
		if (filePath == null || filePath.length() == 0) {
			result = false;
		} else {
			if (!filePath.endsWith("/")) {
				filePath += "/";
			}
			try {
				File dir = new File(filePath);
				if (dir.exists()) {
					result = true;
				}
			} catch (Exception e) {
				result = false;
			}

		}

		return result;

	}

	/**
	 * 文件是否存在
	 * 
	 * @param file
	 * @return
	 */
	public static boolean hasFile(File file) {
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 创建文件及其路径
	 * 
	 * @author allenlee
	 * @param file
	 * @return
	 */
	public static boolean createFilePath(File file) {
		if (!hasFile(file)) {
			return file.mkdirs();
		} else {
			return true;
		}
	}

	// 写数据到SD中的文件
	public static void writeSdcardFile(String fileName, String write_str) throws IOException {
		try {

			FileOutputStream fout = new FileOutputStream(fileName);
			byte[] bytes = write_str.getBytes();

			fout.write(bytes);
			fout.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
