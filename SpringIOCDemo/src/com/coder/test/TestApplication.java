package com.coder.test;

import com.coder.entity.Student;
import com.coder.factory.ApplicationContext;
import com.coder.factory.ClassPathXMLApplicationContext;
import com.coder.service.StudentSrv;

public class TestApplication {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXMLApplicationContext("applicationContext.xml");
        StudentSrv stuServ = (StudentSrv) context.getBean("StudentSrv");
        stuServ.getStudent().selfIntro();

        Student student = (Student)context.getBean("Student");
        student.setName("Huster");
        student.setAdd("Hust");
        stuServ.getStudent().selfIntro();

    }
}
