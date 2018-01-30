package cn.xuyingqi.web.util.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import cn.xuyingqi.util.util.ListFactory;

/**
 * HttpClient工具类
 * 
 * @author XuYQ
 *
 */
public class HttpClientUtils {

	/**
	 * 默认编码集
	 */
	private static final String CHARSET = "UTF-8";

	/**
	 * HttpClient连接池
	 */
	private static PoolingHttpClientConnectionManager phccm;

	/**
	 * 私有构造方法
	 */
	private HttpClientUtils() {

	}

	/**
	 * 初始化
	 */
	private static synchronized void init() {

		// 判断HttpClient连接池是否为空
		if (phccm == null) {

			// 实例化HttpClient连接池
			phccm = new PoolingHttpClientConnectionManager();
			// 设置连接池最大连接数
			phccm.setMaxTotal(50);
			// 设置每路由最大连接数.默认值是2
			phccm.setDefaultMaxPerRoute(5);
		}
	}

	/**
	 * 通过连接池获取HttpClient
	 * 
	 * @return
	 */
	private static CloseableHttpClient getHttpClient() {

		// 初始化
		init();

		// 返回HttpClient
		return HttpClients.custom().setConnectionManager(phccm).build();
	}

	/**
	 * HttpGet请求
	 * 
	 * @param url
	 * @return
	 * @throws URISyntaxException
	 */
	public static String httpGetRequest(String url) throws URISyntaxException {

		return httpGetRequest(url, new HashMap<String, Object>());
	}

	/**
	 * HttpGet请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws URISyntaxException
	 */
	public static String httpGetRequest(String url, Map<String, Object> params) throws URISyntaxException {

		return httpGetRequest(url, new HashMap<String, Object>(), params);
	}

	/**
	 * HttpGet请求
	 * 
	 * @param url
	 * @param headers
	 * @param params
	 * @return
	 * @throws URISyntaxException
	 */
	public static String httpGetRequest(String url, Map<String, Object> headers, Map<String, Object> params)
			throws URISyntaxException {

		// 地址,参数
		URIBuilder ub = new URIBuilder();
		ub.setPath(url);
		ub.setParameters(covertParams2NVPS(params));

		// HttpGet
		HttpGet httpGet = new HttpGet(ub.build());

		// 实例化代理
		HttpHost proxy = new HttpHost("192.168.70.124", 808);
		// 设置代理
		httpGet.setConfig(RequestConfig.custom().setProxy(proxy).build());

		// 遍历头
		for (Map.Entry<String, Object> param : headers.entrySet()) {

			httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
		}

		// 获取结果
		return getResult(httpGet);
	}

	/**
	 * HttpPost请求
	 * 
	 * @param url
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String httpPostRequest(String url) throws UnsupportedEncodingException {

		return httpPostRequest(url, new HashMap<String, Object>());
	}

	/**
	 * HttpPost请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String httpPostRequest(String url, Map<String, Object> params) throws UnsupportedEncodingException {

		return httpPostRequest(url, params, CHARSET);
	}

	/**
	 * HttpPost请求
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String httpPostRequest(String url, Map<String, Object> params, String charset)
			throws UnsupportedEncodingException {

		return httpPostRequest(url, new HashMap<String, Object>(), params, charset);
	}

	/**
	 * HttpPost请求
	 * 
	 * @param url
	 * @param headers
	 * @param params
	 * @param charset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String httpPostRequest(String url, Map<String, Object> headers, Map<String, Object> params,
			String charset) throws UnsupportedEncodingException {

		// HttpPost
		HttpPost httpPost = new HttpPost(url);

		// // 实例化代理
		// HttpHost proxy = new HttpHost("192.168.70.124", 808);
		// // 设置代理
		// httpPost.setConfig(RequestConfig.custom().setProxy(proxy).build());

		// 遍历头
		for (Map.Entry<String, Object> param : headers.entrySet()) {

			httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
		}

		// 设置请求参数,及编码格式
		httpPost.setEntity(new UrlEncodedFormEntity(covertParams2NVPS(params), charset));

		// 获取结果
		return getResult(httpPost);
	}

	/**
	 * 参数转换
	 * 
	 * @param params
	 * @return
	 */
	private static List<NameValuePair> covertParams2NVPS(Map<String, Object> params) {

		// 转换参数
		List<NameValuePair> nvps = ListFactory.newInstance();

		// 遍历参数
		for (Map.Entry<String, Object> param : params.entrySet()) {

			nvps.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
		}

		return nvps;
	}

	/**
	 * 获取Http请求处理结果
	 * 
	 * @param request
	 * @return
	 */
	private static String getResult(HttpRequestBase request) {

		// 获取HttpClient
		CloseableHttpClient httpClient = getHttpClient();

		try {

			// 获取响应结果
			CloseableHttpResponse response = httpClient.execute(request);
			// 判断响应结果
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				// 获取响应数据
				HttpEntity entity = response.getEntity();
				if (entity != null) {

					String result = EntityUtils.toString(entity);
					response.close();

					return result;
				}
			} else {

				// 抛出错误异常
				throw new RuntimeException(response.getStatusLine().toString());
			}
		} catch (ClientProtocolException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} finally {

		}

		return "";
	}

	/**
	 * Main函数测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
	}
}