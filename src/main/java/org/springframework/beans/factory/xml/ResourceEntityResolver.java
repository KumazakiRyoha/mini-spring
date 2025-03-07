package org.springframework.beans.factory.xml;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

/**
 * Spring资源实体解析器，用于解析XML中的实体引用
 */
class ResourceEntityResolver implements EntityResolver {
    private final ResourceLoader resourceLoader;

    public ResourceEntityResolver(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if (systemId != null) {
            Resource resource = this.resourceLoader.getResource(systemId);
            try {
                InputSource source = new InputSource(resource.getInputStream());
                source.setPublicId(publicId);
                source.setSystemId(systemId);
                return source;
            } catch (Exception e) {
                // 如果无法解析，返回空InputSource，忽略实体
                return new InputSource();
            }
        }
        return null; // 使用默认行为
    }
}
