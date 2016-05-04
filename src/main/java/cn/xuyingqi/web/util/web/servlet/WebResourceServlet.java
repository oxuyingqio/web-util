package cn.xuyingqi.web.util.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import cn.xuyingqi.web.util.util.HttpServletRequestUtils;
import cn.xuyingqi.web.util.util.PropertiesUtils;
import cn.xuyingqi.web.util.util.ResourceFile;
import cn.xuyingqi.web.util.vo.WebResourceProperties;

/**
 * Web资源文件统一处理Servlet
 * 
 * @author Administrator
 *
 */
public class WebResourceServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 日志
	private Logger logger = Logger.getLogger(WebResourceServlet.class);

	// Web资源路径标志
	private static final String WEB_RESOURCE_SIGN = "/~/";

	// Web资源配置
	private WebResourceProperties webResourceProperties;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		webResourceProperties = new PropertiesUtils("/webResource.properties").load(WebResourceProperties.class);
		this.logger.info("是否使用浏览器缓存：" + webResourceProperties.isUseExplorerCache());
		this.logger.info("浏览器缓存时间(单位秒)：" + webResourceProperties.getExplorerCacheTime());
		this.logger.info("是否使用Gzip压缩" + webResourceProperties.isUseGzip());
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 获取请求的requestURI
		String requestURI = request.getRequestURI();
		// 获取请求URI的MimeType,并设置response的ContentType
		String mimeType = request.getSession().getServletContext().getMimeType(requestURI);
		if (!StringUtils.isBlank(mimeType)) {
			response.setContentType(mimeType);
		}

		// 获取去除标志后的文件路径信息
		String filePath = "/"
				+ requestURI.substring(requestURI.indexOf(WEB_RESOURCE_SIGN) + WEB_RESOURCE_SIGN.length());
		// 输入流
		InputStream is = null;
		// 获取资源文件
		ResourceFile resource = new ResourceFile(filePath);
		// 获取资源文件的输入流
		is = resource.getInputStream();
		if (is == null) {
			// 404未找到文件
			response.sendError(404);
			this.logger.warn("Web资源文件：" + filePath + "，未找到");
			return;
		}

		// 是否使用浏览器缓存
		if (webResourceProperties.isUseExplorerCache()) {
			// 获取请求的最后时间
			long requestLastModified = request.getDateHeader("If-Modified-Since");
			// 获取资源文件的最后修改时间
			long curentLastModified = resource.getLastModified();
			// 判断服务器资源文件是否晚于请求文件的时间
			if (curentLastModified - requestLastModified >= 1000) {
				// 服务器有更新,设置当前更新时间
				response.addDateHeader("Last-Modified", curentLastModified);
				// 指定max-age,单位:秒.此段时间内不会重新访问服务器
				response.setHeader("Cache-Control", "max-age=" + webResourceProperties.getExplorerCacheTime());
			} else {
				// 资源文件未变更,使用缓存
				response.setStatus(304);
				this.logger.info("Web资源文件：" + filePath + "未变更，使用缓存");
				return;
			}
		}

		// 获取response的输出流
		OutputStream os = response.getOutputStream();
		if (webResourceProperties.isUseGzip() && HttpServletRequestUtils.browserSupportsGzip(request)) {
			os = new GZIPOutputStream(os);
			response.setHeader("Content-Encoding", "gzip");
		}

		// 输出文件
		int temp;
		while ((temp = is.read()) != -1) {
			os.write(temp);
		}
		is.close();
		os.close();
	}
}
