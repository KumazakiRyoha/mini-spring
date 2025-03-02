package org.springframework.core.io;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 默认的资源加载器实现
 */
public class DefaultResourceLoader implements ResourceLoader {

    /**
     * 类路径资源前缀
     */
    public static final String CLASSPATH_URL_PREFIX = "classpath:";

    @Override
    public Resource getResource(String location) {
        if (location == null) {
            throw new IllegalArgumentException("Location must not be null");
        }

        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            // 处理类路径资源
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()));
        } else if (location.startsWith("/")) {
            // 处理以 / 开头的路径
            return new FileSystemResource(location);
        } else if (isUrl(location)) {
            // 处理URL格式的资源
            try {
                URL url = new URL(location);
                return new UrlResource(url);
            } catch (MalformedURLException ex) {
                throw new IllegalArgumentException("Invalid URL: " + location, ex);
            }
        } else {
            // 处理相对路径，认为是文件系统资源
            return new FileSystemResource(location);
        }
    }

    /**
     * 判断给定的位置是否是URL
     * 
     * @param location 待检查的位置
     * @return 如果位置是URL则返回true
     */
    protected boolean isUrl(String location) {
        if (location == null) {
            return false;
        }

        // 检查是否包含常见的URL协议前缀
        if (location.startsWith("http:") ||
                location.startsWith("https:") ||
                location.startsWith("file:") ||
                location.startsWith("jar:") ||
                location.startsWith("wsjar:") ||
                location.startsWith("war:") ||
                location.startsWith("ftp:")) {
            return true;
        }

        // 尝试更严格的URL检测
        try {
            new URL(location);
            return true;
        } catch (MalformedURLException ex) {
            return false;
        }
    }
}
