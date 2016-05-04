package cn.xuyingqi.web.util.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import cn.xuyingqi.util.util.ClassUtils;

/**
 * properties文件工具类
 * 
 * @author XuYQ
 *
 */
public class PropertiesUtils {

	// 日志
	private Logger logger = Logger.getLogger(PropertiesUtils.class);

	// 资源文件
	private Properties properties;

	/**
	 * properties文件工具
	 * 
	 * @param filePath
	 *            文件路径
	 */
	public PropertiesUtils(String filePath) {
		InputStream is = PropertiesUtils.class.getResourceAsStream(filePath);
		if (is != null) {
			properties = new Properties();
			try {
				properties.load(is);
			} catch (IOException e) {
				this.logger.warn("properties文件读取异常");
				properties = null;
			}
		}
	}

	/**
	 * 获取properties文件的某项值
	 * 
	 * @param property
	 * @return
	 */
	public String get(String property) {
		return properties == null ? null : properties.getProperty(property);
	}

	/**
	 * 将properties文件的数据加载到JavaBean中
	 * 
	 * @param clazz
	 * @return
	 */
	public <T> T load(Class<T> clazz) {
		T t = null;
		try {
			// 获取对应类的对象
			t = clazz.newInstance();

			// 若properties为空,则直接返回
			if (properties == null) {
				return t;
			} else {
				// 获取类字段
				Set<Field> fieldSet = ClassUtils.getFieldSet(clazz);
				// 遍历类字段
				Iterator<Field> fieldIterator = fieldSet.iterator();
				while (fieldIterator.hasNext()) {
					// 对应字段
					Field field = fieldIterator.next();
					try {
						try {
							ClassUtils.invokeSetMethod(t, field, get(field.getName()));
						} catch (IllegalArgumentException | InvocationTargetException e) {
							this.logger.warn("字段：" + field.getName() + "调用set方法失败");
						}
					} catch (NoSuchMethodException e) {
						this.logger.warn("字段：" + field.getName() + "对应set方法未找到");
					} catch (SecurityException e) {
						e.printStackTrace();
					}
				}

				return t;
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return t;
	}
}
