package main;

import config.ProjectConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import services.CommentServices;

public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(ProjectConfiguration.class);

        var cs1 = context.getBean("commentService", CommentServices.class);
        var cs2 = context.getBean("commentService", CommentServices.class);

        boolean b1 = cs1 == cs2;

        System.out.println(b1);
    }
}
