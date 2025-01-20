package org.example;

import Config.Parrot;
import Config.ProjectConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(ProjectConfig.class);

        Parrot parrot1 = context.getBean("parrot1", Parrot.class);
        Parrot parrot2 = context.getBean("parrot2", Parrot.class);
        Parrot parrot3 = context.getBean("parrot3", Parrot.class);

        System.out.println(parrot1.getName());
        System.out.println(parrot2.getName());
        System.out.println(parrot3.getName());

        String hello = context.getBean(String.class);
        System.out.println(hello);

        Integer ten = context.getBean(Integer.class);
        System.out.println(ten);
    }
}
