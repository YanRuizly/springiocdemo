package com.coder.factory;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ClassPathXMLApplicationContext implements ApplicationContext {
    private File file;
    private HashMap map = new HashMap();

    public ClassPathXMLApplicationContext(String config_file) {
        URL url = this.getClass().getClassLoader().getResource(config_file);
        try {
            file = new File(url.toURI());
            XMLParsing();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void XMLParsing() throws Exception {
        SAXReader reader = new SAXReader();
        try {
            /** 通过reader对象的read方法加载xml文件,获取docuemnt对象 */
            Document document = reader.read(file);
            Element beanRoot = document.getRootElement();
            Iterator it = beanRoot.elementIterator();
            while (it.hasNext()) {
                Element bean = (Element)it.next();
                String id = bean.attributeValue("id");
                String cls = bean.attributeValue("class");
                /** 反射获取实例 */
                Object object = Class.forName(cls).newInstance();
                /** 获取方法 */
                Method[] method = object.getClass().getDeclaredMethods();
                List<Element> list = bean.elements("property");

                for (Element element : list){
                    for (int n = 0; n < method.length; n++) {
                        String name = method[n].getName();
                        String temp = null;
                        if (name.startsWith("set")) {
                            temp = name.substring(3, name.length()).toLowerCase();
                            if (element.attributeValue("name") != null) {
                                if (temp.equals(element.attribute("name").getValue())) {
                                    method[n].invoke(object, element.attribute("value").getValue());
                                }
                            } else {
                                method[n].invoke(object, map.get(element.attribute("ref").getValue()));
                            }
                        }
                    }
                }
                map.put(id, object);
            }
        }catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public Object getBean(String name){
        return map.get(name);
    }
}
