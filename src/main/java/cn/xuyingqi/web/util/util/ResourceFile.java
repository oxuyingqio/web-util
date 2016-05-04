package cn.xuyingqi.web.util.util;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * 获取资源文件信息
 * 
 * @author Administrator
 *
 */
public class ResourceFile {

	// 资源文件路径
	private String filePath;

	// 资源文件
	private File resourceFile;

	/**
	 * 获取资源文件信息
	 * 
	 * @param filePath
	 *            资源文件路径
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	public ResourceFile(String filePath) {
		this.filePath = filePath;

		try {
			this.init();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化
	 * 
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	private void init() throws MalformedURLException, URISyntaxException {
		URL url = ResourceFile.class.getResource(filePath);

		if (url != null) {
			if (ResourceUtils.isJarURL(url)) {
				url = ResourceUtils.extractJarFileURL(url);
			}

			resourceFile = new File(new URI(url.toString()).getSchemeSpecificPart());
		}
	}

	/**
	 * 获取资源文件最后修改时间
	 * 
	 * @return
	 */
	public long getLastModified() {
		return this.resourceFile.lastModified();
	}

	/**
	 * 获取资源文件输入流
	 * 
	 * @return
	 */
	public InputStream getInputStream() {
		return ResourceFile.class.getResourceAsStream(filePath);
	}
}
