package cn.xuyingqi.web.util.util;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * 资源文件工具类
 * 
 * @author XuYQ
 *
 */
public class ResourceUtils {

	// URL 前缀 file:
	public static final String URL_PREFIX_FILE = "file:";

	// URL Protocol jar
	public static final String URL_PROTOCOL_JAR = "jar";

	// 系统jar文件分隔符
	public static final String URL_JAR_SEPARATOR = "!/";

	/**
	 * URL的Protocol是否为Jar
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isJarURL(URL url) {
		String protocol = url.getProtocol();
		return URL_PROTOCOL_JAR.equals(protocol);
	}

	/**
	 * 通过jar包内文件的URL提取其jar包的URL
	 * 
	 * @param jarFileURL
	 * @return
	 * @throws MalformedURLException
	 */
	public static URL extractJarFileURL(URL jarFileURL) throws MalformedURLException {
		String jarFile = jarFileURL.getFile();
		int separatorIndex = jarFile.indexOf(URL_JAR_SEPARATOR);
		if (separatorIndex != -1) {
			String jar = jarFile.substring(0, separatorIndex);
			try {
				return new URL(jar);
			} catch (MalformedURLException ex) {
				if (!jar.startsWith("/")) {
					jar = "/" + jar;
				}
				return new URL(URL_PREFIX_FILE + jar);
			}
		} else {
			return jarFileURL;
		}
	}
}
