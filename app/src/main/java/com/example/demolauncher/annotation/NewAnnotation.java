package com.example.demolauncher.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) //此注解用于描述注解，表示自定义注解保存到运行时
@Target(ElementType.METHOD) //此注解用于秒速注解，表示自定义注解只能作用于方法
@Documented // 用于描述注解，表示自定义注解可被文档提取
@Inherited //用于描述注解，表示自定义注解可被使用它的类的子类继承

public @interface NewAnnotation { //自定义注解
    String url();
    String params();
}
