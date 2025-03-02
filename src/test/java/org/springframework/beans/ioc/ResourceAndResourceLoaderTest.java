package org.springframework.beans.ioc;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class ResourceAndResourceLoaderTest {

    @Test
    public void testResourceLoader() throws Exception {
        // 创建资源加载器
        ResourceLoader resourceLoader = new DefaultResourceLoader();

        // 加载classpath下的资源
        Resource resource = resourceLoader.getResource("classpath:hello.txt");
        InputStream inputStream = resource.getInputStream();
        // open inputStream use UTF-8
        InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(reader);
        String content = bufferedReader.readLine();
        System.out.println("Resource content: " + content);
        assertThat(content).isEqualTo("hello world");
        bufferedReader.close();
        reader.close();
        inputStream.close();

        // 加载文件系统资源
        resource = resourceLoader.getResource("src/test/resources/hello.txt");
        assertThat(resource instanceof FileSystemResource).isTrue();
        inputStream = resource.getInputStream();
        reader = new InputStreamReader(inputStream, "UTF-8");
        bufferedReader = new BufferedReader(reader);
        content = bufferedReader.readLine();
        System.out.println("Resource content: " + content);
        assertThat(content).isEqualTo("hello world");
        bufferedReader.close();

        // 加载URL资源
        resource = resourceLoader.getResource("https://www.youtube.com");
        inputStream = resource.getInputStream();
        reader = new InputStreamReader(inputStream, "UTF-8");
        bufferedReader = new BufferedReader(reader);
        content = bufferedReader.readLine();
        System.out.println("Resource content: " + content);
        assertThat(content).contains("<!DOCTYPE html>");
        bufferedReader.close();

    }
}
