package es.kiwi.freemarker.test;

import es.kiwi.freemarker.FreemarkerApplication;
import es.kiwi.freemarker.entity.Student;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@SpringBootTest(classes = FreemarkerApplication.class)
@RunWith(SpringRunner.class)
public class FreemarkerTest {
    @Autowired
    private Configuration configuration;

    /**
     * 使用freemarker原生Api将页面生成html文件
     * @throws IOException
     * @throws TemplateException
     */
    @Test
    public void test() throws IOException, TemplateException {
        Template template = configuration.getTemplate("02-list.ftl");
        /*
        合成方法：第一个参数：模型数据；第二个参数：输出流
         */
        template.process(getData(), new FileWriter("D:\\ideawork\\list.html"));
    }

    private Map<String, Object> getData() {
        Map<String, Object> model = new HashMap<>();
        //------------------------------------
        Student stu1 = new Student();
        stu1.setName("Caqui");
        stu1.setAge(18);
        stu1.setMoney(1000.86f);
        stu1.setBirthday(new Date());

        //小红对象模型数据
        Student stu2 = new Student();
        stu2.setName("Mango");
        stu2.setMoney(200.1f);
        stu2.setAge(19);

        //将两个对象模型数据存放到List集合中
        List<Student> stus = new ArrayList<>();
        stus.add(stu1);
        stus.add(stu2);

        //向model中存放List集合数据
        model.put("stus",stus);

        //------------------------------------

        //创建Map数据
        HashMap<String,Student> stuMap = new HashMap<>();
        stuMap.put("stu1",stu1);
        stuMap.put("stu2",stu2);
        // 3.1 向model中存放Map数据
        model.put("stuMap", stuMap);

        //日期
        model.put("today", new Date());

        //长数值类型
        model.put("point", 38473897438743L);

        return model;
    }

}
