package com.example;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * Created by lizhaoxuan on 16/5/21.
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class CakeProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        Messager messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE,"annotations size :"+ annotations.size());

        for (TypeElement element : annotations){
            System.out.println("System.out.println :"+ element.getSimpleName().toString());
            messager.printMessage(Diagnostic.Kind.OTHER,"Diagnostic.Kind.OTHER :"+ element.getSimpleName().toString());
            messager.printMessage(Diagnostic.Kind.NOTE,"Diagnostic.Kind.NOTE :"+ element.getSimpleName().toString());
        }

//        messager.printMessage(Diagnostic.Kind.MANDATORY_WARNING,"Diagnostic.Kind.MANDATORY_WARNING");
//        messager.printMessage(Diagnostic.Kind.WARNING,"Diagnostic.Kind.WARNING");
//        messager.printMessage(Diagnostic.Kind.ERROR,"Diagnostic.Kind.ERROR");
        return true;
    }


//        for (Element element : env.getElementsAnnotatedWith(GetMsg.class)) {
//            //获取该注解所在类的包名
//
////            String packageName =elementUtils.getPackageOf(type).getQualifiedName().toString();
//            TypeElement classElement = (TypeElement) element;
//            //获取该注解所在类的类名
//            String className = classElement.getSimpleName().toString();
//            //获取该注解所在类的全类名
//            String fullClassName = classElement.getQualifiedName().toString();
//            //获取该注解的值
//            int id = classElement.getAnnotation(GetMsg.class).id();
//            String name = classElement.getAnnotation(GetMsg.class).name();
////            messager.printMessage(Diagnostic.Kind.NOTE,
////                    "Annotation class : packageName = " + packageName);
//            messager.printMessage(Diagnostic.Kind.NOTE,
//                    "Annotation class : className = " + className);
//            messager.printMessage(Diagnostic.Kind.NOTE,
//                    "Annotation class : fullClassName = " + fullClassName);
//            messager.printMessage(Diagnostic.Kind.NOTE,
//                    "Annotation class : id = " + id + "  name = " + name);
//        }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(GetMsg.class.getCanonicalName());
        return types;
    }
}
