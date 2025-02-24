package org.springframework.beans;

import java.util.ArrayList;
import java.util.List;


public class PropertyValues {

    /**
     * 存储PropertyValue的列表
     */
    private final List<PropertyValue> propertyValueList = new ArrayList<>();

    /**
     * 添加一个属性值对象
     * @param pv 要添加的PropertyValue对象
     */
    public void addPropertyValue(PropertyValue pv) {
        this.propertyValueList.add(pv);
    }

    /**
     * 获取所有的属性值对象
     * @return PropertyValue对象数组
     */
    public PropertyValue[] getPropertyValues() {
        return this.propertyValueList.toArray(new PropertyValue[0]);
    }

    /**
     * 根据属性名称查找对应的PropertyValue对象
     * @param propertyName 属性名称
     * @return 如果找到则返回对应的PropertyValue对象，否则返回null
     */
    public PropertyValue getPropertyValue(String propertyName) {
        for (int i = 0; i < this.propertyValueList.size(); i++) {
            PropertyValue pv = this.propertyValueList.get(i);
            if (pv.getName().equals(propertyName)) {
                return pv;
            }
        }
        return null;
    }
}
