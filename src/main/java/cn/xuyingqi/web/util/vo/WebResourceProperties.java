package cn.xuyingqi.web.util.vo;

/**
 * Web资源配置
 * 
 * @author XuYQ
 *
 */
public class WebResourceProperties {
	// 默认使用浏览器缓存
	private static final boolean DEFAULT_USE_EXPLORER_CACHE = true;
	// 默认浏览器缓存时间,单位秒
	private static final int DEFAULT_EXPLORER_CACHE_TIME = 604800;
	// 默认使用Gzip压缩
	private static final boolean DEFAULT_USE_GZIP = true;

	// 是否使用浏览器缓存
	private boolean useExplorerCache = DEFAULT_USE_EXPLORER_CACHE;
	// 浏览器缓存时间
	private int explorerCacheTime = DEFAULT_EXPLORER_CACHE_TIME;
	// 是否使用Gzip压缩
	private boolean useGzip = DEFAULT_USE_GZIP;

	public boolean isUseExplorerCache() {
		return useExplorerCache;
	}

	public void setUseExplorerCache(boolean useExplorerCache) {
		this.useExplorerCache = useExplorerCache;
	}

	public int getExplorerCacheTime() {
		return explorerCacheTime;
	}

	public void setExplorerCacheTime(int explorerCacheTime) {
		this.explorerCacheTime = explorerCacheTime;
	}

	public boolean isUseGzip() {
		return useGzip;
	}

	public void setUseGzip(boolean useGzip) {
		this.useGzip = useGzip;
	}
}
