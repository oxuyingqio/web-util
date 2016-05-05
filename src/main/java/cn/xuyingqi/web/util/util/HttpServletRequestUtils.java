package cn.xuyingqi.web.util.util;

import javax.servlet.http.HttpServletRequest;

/**
 * HttpServletRequest工具类
 * 
 * @author XuYQ
 *
 */
public class HttpServletRequestUtils {

	/**
	 * 判断浏览器是否支持Gzip
	 * 
	 * @param request
	 * @return
	 */
	public static boolean browserSupportsGzip(HttpServletRequest request) {
		String acceptEncoding = request.getHeader("Accept-Encoding");
		return ((acceptEncoding != null) && (acceptEncoding.indexOf("gzip") != -1));
	}
}
