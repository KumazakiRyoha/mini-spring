package org.springframework.beans.ioc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.utils.StringUtils;

/**
 * XmlBeanDefinitionReader测试类
 */
public class XmlBeanDefinitionReaderTest {

    private DefaultListableBeanFactory beanFactory;
    private XmlBeanDefinitionReader xmlBeanDefinitionReader;

    @BeforeEach
    public void setUp() {
        // 创建bean工厂
        beanFactory = new DefaultListableBeanFactory();
        // 创建XML bean定义读取器
        xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
    }

    @Test
    public void testLoadBeanDefinitions() throws Exception {
        // 加载测试的XML配置文件
        xmlBeanDefinitionReader.loadBeanDefinitions("classpath:spring-test.xml");

        // 验证bean定义是否被正确加载 - 通过尝试获取Bean实例来间接验证
        Object personBean = beanFactory.getBean("person");
        Assertions.assertNotNull(personBean, "person bean应该被成功创建");
        Assertions.assertEquals("org.springframework.beans.ioc.Person", personBean.getClass().getName());
    }

    @Test
    public void testBeanWithProperties() throws Exception {
        // 加载测试的XML配置文件
        xmlBeanDefinitionReader.loadBeanDefinitions("classpath:spring-test.xml");

        // 获取并验证带有属性的bean
        Object personBean = beanFactory.getBean("person");
        Assertions.assertNotNull(personBean, "person bean应该被成功创建");

        // 使用反射获取属性值并验证
        java.lang.reflect.Field nameField = personBean.getClass().getDeclaredField("name");
        nameField.setAccessible(true);
        String name = (String) nameField.get(personBean);
        Assertions.assertEquals("张三", name, "属性设置不正确");

        java.lang.reflect.Field ageField = personBean.getClass().getDeclaredField("age");
        ageField.setAccessible(true);
        int age = (int) ageField.get(personBean);
        Assertions.assertEquals(18, age, "属性设置不正确");
    }

    @Test
    public void testBeanReference() throws Exception {
        // 加载测试的XML配置文件
        xmlBeanDefinitionReader.loadBeanDefinitions("classpath:spring-test.xml");

        // 验证bean引用是否正确处理 - 通过尝试获取Bean实例来间接验证
        Object carBean = beanFactory.getBean("car");
        Assertions.assertNotNull(carBean, "car bean应该被成功创建");

        // 验证car bean中的owner属性是否正确关联了person bean
        java.lang.reflect.Field ownerField = carBean.getClass().getDeclaredField("owner");
        ownerField.setAccessible(true);
        Object ownerBean = ownerField.get(carBean);
        Assertions.assertNotNull(ownerBean, "car的owner属性不应该为null");
        Assertions.assertEquals("org.springframework.beans.ioc.Person", ownerBean.getClass().getName());
    }

    @Test
    public void testBeanNameGeneration() throws Exception {
        // 创建包含id属性为空的bean的XML文件并加载
        xmlBeanDefinitionReader.loadBeanDefinitions("classpath:spring-no-id.xml");

        // 验证是否使用类名首字母小写作为bean名称
        String expectedBeanName = StringUtils.lowerFirst("SampleBean");

        // 通过尝试获取Bean实例来验证Bean定义存在
        try {
            Object bean = beanFactory.getBean(expectedBeanName);
            Assertions.assertNotNull(bean, "应该能够获取到使用类名首字母小写命名的bean");
        } catch (BeanException e) {
            Assertions.fail("应该存在使用类名首字母小写的bean，但获取失败: " + e.getMessage());
        }
    }

    /**
     * 测试ref属性的Bean注入，验证XML中通过ref引用的Bean是否被正确注入
     */
    @Test
    public void testBeanRefInjection() throws Exception {
        // 加载测试的XML配置文件
        xmlBeanDefinitionReader.loadBeanDefinitions("classpath:spring-ref.xml");

        // 先获取并验证student bean
        Object studentBean = beanFactory.getBean("student");
        Assertions.assertNotNull(studentBean, "student bean应该被成功创建");

        // 获取并验证classroom bean
        Object classroomBean = beanFactory.getBean("classroom");
        Assertions.assertNotNull(classroomBean, "classroom bean应该被成功创建");

        // 验证classroom里的teacher属性是否成功注入
        java.lang.reflect.Field teacherField = classroomBean.getClass().getDeclaredField("teacher");
        teacherField.setAccessible(true);
        Object teacherBean = teacherField.get(classroomBean);
        Assertions.assertNotNull(teacherBean, "classroom的teacher属性不应该为null");
        Assertions.assertEquals("org.springframework.beans.ioc.Teacher", teacherBean.getClass().getName());

        // 验证classroom里的students属性是否成功注入(List类型的ref注入)
        java.lang.reflect.Field studentsField = classroomBean.getClass().getDeclaredField("students");
        studentsField.setAccessible(true);
        java.util.List<?> studentsList = (java.util.List<?>) studentsField.get(classroomBean);
        Assertions.assertNotNull(studentsList, "classroom的students属性不应该为null");
        Assertions.assertFalse(studentsList.isEmpty(), "students列表不应该为空");

        // 验证第一个学生的属性
        Object firstStudent = studentsList.get(0);
        Assertions.assertNotNull(firstStudent, "学生列表的第一个元素不应该为null");
        Assertions.assertEquals("org.springframework.beans.ioc.Student", firstStudent.getClass().getName());
        Assertions.assertSame(studentBean, firstStudent, "studentsList中的对象应该与单独获取的student bean相同");

        // 验证teacher属性的name值
        java.lang.reflect.Field teacherNameField = teacherBean.getClass().getDeclaredField("name");
        teacherNameField.setAccessible(true);
        String teacherName = (String) teacherNameField.get(teacherBean);
        Assertions.assertEquals("李老师", teacherName, "teacher的name属性值不正确");
    }
}
