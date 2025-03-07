package org.springframework.beans.ioc;

/**
 * 用于测试自动生成bean名称的类
 */
public class SampleBean {
    private String value;

    public SampleBean() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SampleBean{" +
                "value='" + value + '\'' +
                '}';
    }
}
