package org.springframework.beans.factory.xml;

import java.io.InputStream;

import org.springframework.beans.BeanException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.utils.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.io.IOException;

/**
 * Default implementation of the {@link BeanDefinitionReader} interface that
 * reads bean definitions from XML files.
 */

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public static final String BEAN_ELEMENT = "bean";
    public static final String PROPERTY_ELEMENT = "property";
    public static final String ID_ATTRIBUTE = "id";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String CLASS_ATTRIBUTE = "class";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String REF_ATTRIBUTE = "ref";

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        super(registry, resourceLoader);
    }

    @Override
    public void loadBeanDefinitions(String location) throws BeanException {
        ResourceLoader resourceLoader = getResourceLoader();
        Resource resource = resourceLoader.getResource(location);
        loadBeanDefinitions(resource);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) throws BeanException {
        try {
            InputStream inputStream = resource.getInputStream();
            try {
                doLoadBeanDefinitions(inputStream);
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            throw new BeanException("IOException parsing XML document from " + resource, e);
        }
    }

    protected void doLoadBeanDefinitions(InputStream inputStream) throws BeanException {

        Document document = doLoadDocument(inputStream);

        Element root = document.getDocumentElement();

        NodeList childNodes = root.getChildNodes();

        for (int i = 0; i < childNodes.getLength(); i++) {
            if (childNodes.item(i) instanceof Element) {
                if (BEAN_ELEMENT.equals(((Element) childNodes.item(i)).getNodeName())) {
                    // 解析<bean>元素
                    Element beanElement = (Element) childNodes.item(i);
                    String id = beanElement.getAttribute(ID_ATTRIBUTE);
                    String name = beanElement.getAttribute(NAME_ATTRIBUTE);
                    String className = beanElement.getAttribute(CLASS_ATTRIBUTE);

                    // 加载Bean的类
                    Class<?> beanClass = null;
                    try {
                        beanClass = Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        throw new BeanException("Bean class not found: " + className, e);
                    }

                    // id优先于name
                    String beanName = StringUtils.isNotEmpty(id) ? id : name;
                    if (StringUtils.isEmpty(beanName)) {
                        // 如果id和name都为空，将类名的第一个字母转为小写后作为bean的名称
                        beanName = StringUtils.lowerFirst(beanClass.getSimpleName());
                    }

                    // 创建BeanDefinition对象
                    BeanDefinition beanDefinition = new BeanDefinition(beanClass);

                    // 解析<property>元素
                    for (int j = 0; j < beanElement.getChildNodes().getLength(); j++) {
                        if (beanElement.getChildNodes().item(j) instanceof Element) {
                            if (PROPERTY_ELEMENT
                                    .equals(((Element) beanElement.getChildNodes().item(j)).getNodeName())) {
                                // 解析<property>元素
                                Element propertyElement = (Element) beanElement.getChildNodes().item(j);
                                String nameAttribute = propertyElement.getAttribute(NAME_ATTRIBUTE);
                                String valueAttribute = propertyElement.getAttribute(VALUE_ATTRIBUTE);
                                String refAttribute = propertyElement.getAttribute(REF_ATTRIBUTE);

                                if (StringUtils.isEmpty(nameAttribute)) {
                                    throw new BeanException("<property> must contain name attribute");
                                }

                                Object value = valueAttribute;
                                if (StringUtils.isNotEmpty(refAttribute)) {
                                    value = new BeanReference(refAttribute);
                                }
                                PropertyValue propertyValue = new PropertyValue(nameAttribute, value);
                                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);

                            }
                        }
                    }
                    if (getRegistry().containsBeanDefinition(beanName)) {
                        throw new BeanException("Duplicate beanName [" + beanName + "] in bean definitions");

                    }
                    getRegistry().registerBeanDefinition(beanName, beanDefinition);
                }
            }
        }

    }

    /**
     * 从输入流中加载XML文档
     *
     * @param inputStream XML文件输入流
     * @return 文档对象
     * @throws BeanException 如果加载或解析XML出错
     */
    protected Document doLoadDocument(InputStream inputStream) throws BeanException {
        try {
            // 创建DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // 禁用DTD验证
            factory.setValidating(false);
            // 禁用XML外部实体解析，防止XXE攻击
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

            // 创建DocumentBuilder
            DocumentBuilder builder = factory.newDocumentBuilder();
            // 设置实体解析器
            builder.setEntityResolver(new ResourceEntityResolver(getResourceLoader()));

            // 解析XML文档
            Document document = builder.parse(inputStream);
            return document;
        } catch (ParserConfigurationException e) {
            throw new BeanException("解析XML文档时出错: " + e.getMessage(), e);
        } catch (SAXException e) {
            throw new BeanException("无效的XML文档: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new BeanException("读取XML文档时出错: " + e.getMessage(), e);
        }
    }

}
