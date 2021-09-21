package com.example.demolauncher.annotation;

import android.util.Log;

import java.lang.annotation.Annotation;

public class AnnotationTest {
    @NewAnnotation(url = "http" , params = "188")
    public void testMethod(String url , String params){

    }

    public void getAnnotation(){
        try { //分别通过实例和类名反射获取方法注解
            Annotation[] annotationsByName = Class.forName("Annotation")
                    .getMethod("testMethod")
                    .getAnnotations();
            Annotation[] annotationsByObject = new AnnotationTest().getClass()
                    .getMethod("testMethod")
                    .getAnnotations();
            for (Annotation an : annotationsByName){ //遍历注解
                if (an instanceof NewAnnotation){ //获取对应注解
                    Log.d("tag" , "params=" + ((NewAnnotation) an).url() +
                            ((NewAnnotation) an).params()); //获取注解元数据
                }
            }
        }catch (ClassNotFoundException c){

        }catch (NoSuchMethodException n){}

    }
}
