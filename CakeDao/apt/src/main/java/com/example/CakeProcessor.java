package com.example;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * Created by lizhaoxuan on 16/5/21.
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class CakeProcessor extends AbstractProcessor {

    private Messager messager;
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        //获取proxyMap
        Map<String, ProxyInfo> proxyMap = getProxyMap(env);

        //遍历proxyMap，并生成代码
        for (String key : proxyMap.keySet()) {
            ProxyInfo proxyInfo = proxyMap.get(key);
            writeCode(proxyInfo);
        }

        for (TypeElement type : annotations) {
            generateClassMaoCode(proxyMap, type);
            break;
        }
        return true;
    }

    private Map<String, ProxyInfo> getProxyMap(RoundEnvironment roundEnv) {
        Map<String, ProxyInfo> proxyMap = new HashMap<>();
        for (Element element : roundEnv.getElementsAnnotatedWith(IdProperty.class)) {
            String fullClassName = getFullClassName(element);

            ProxyInfo proxyInfo = proxyMap.get(fullClassName);
            if (proxyInfo != null) {
                proxyInfo.setIdProperty(getPropertyInfo(element, true));
            } else {
                proxyInfo = getProxyInfo(element);
                proxyInfo.setIdProperty(getPropertyInfo(element, true));
                proxyMap.put(fullClassName, proxyInfo);
            }
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(DataProperty.class)) {
            String fullClassName = getFullClassName(element);
            ProxyInfo proxyInfo = proxyMap.get(fullClassName);
            if (proxyInfo != null) {
                proxyInfo.addPropertyInfos(getPropertyInfo(element, false));
            } else {
                proxyInfo = getProxyInfo(element);
                proxyInfo.addPropertyInfos(getPropertyInfo(element, false));
                proxyMap.put(fullClassName, proxyInfo);
            }
        }

        return proxyMap;
    }

    /**
     * 取得注解所在类名
     */
    private String getFullClassName(Element element) {
        TypeElement classElement = (TypeElement) element
                .getEnclosingElement();
        return classElement.getQualifiedName().toString();
    }

    /**
     * 获得类基础信息。
     *
     * @param element
     * @return
     */
    private ProxyInfo getProxyInfo(Element element) {
        //target相同只能强转。不同使用getEnclosingElement
        VariableElement variableElement = (VariableElement) element;
        TypeElement classElement = (TypeElement) element
                .getEnclosingElement();
        PackageElement packageElement = elementUtils.getPackageOf(classElement);
        String className = classElement.getSimpleName().toString();
        String packageName = packageElement.getQualifiedName().toString();
        String variableName = variableElement.getSimpleName().toString();

        print("variableName: " + variableName);

        ProxyInfo proxyInfo = new ProxyInfo(packageName, className);
        proxyInfo.setTypeElement(classElement);
        return proxyInfo;
    }

    /**
     * 获得类成员基础信息
     */
    private PropertyInfo getPropertyInfo(Element element, boolean isPrimary) {
        VariableElement variableElement = (VariableElement) element;
        String variableName = variableElement.getSimpleName().toString();
        TypeMirror typeMirror = variableElement.asType();

        PropertyInfo propertyInfo = null;
        try {
            propertyInfo = new PropertyInfo(variableName, typeMirror.toString(), isPrimary);
        } catch (CakeDaoException e) {
            error(element,
                    "The use of irregular %s: %s",
                    element, e.getMessage());
        }
        return propertyInfo;
    }

    private void writeCode(ProxyInfo proxyInfo) {
        try {
            writeCode(proxyInfo.getProxyClassFullName(), proxyInfo.getTypeElement(), proxyInfo.generateJavaCode());
        } catch (CakeDaoException e) {
            error(proxyInfo.getTypeElement(),
                    "The use of irregular %s: %s",
                    proxyInfo.getTypeElement(), e.getMessage());
        }
    }

    private void writeCode(String classFullName, Element element, String code) {
        try {
            JavaFileObject jfo = processingEnv.getFiler().createSourceFile(
                    classFullName, element);
            Writer writer = jfo.openWriter();
            writer.write(code);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            error(element,
                    "Unable to write injector for type %s: %s",
                    element, e.getMessage());
        }
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(IdProperty.class.getCanonicalName());
        types.add(DataProperty.class.getCanonicalName());
        return types;
    }

    private void print(String message) {
        messager.printMessage(Diagnostic.Kind.NOTE, message);
    }

    private void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        messager.printMessage(Diagnostic.Kind.ERROR, message, element);
    }

    private void generateClassMaoCode(Map<String, ProxyInfo> proxyMap, TypeElement element) {

        StringBuilder builder = new StringBuilder();
        builder.append("// Generated code from CakeDao. Do not modify!\n");
        builder.append("package com.zhaoxuan.cakedao; \n\n")
                .append("import android.database.sqlite.SQLiteDatabase;\n")
                .append("import com.zhaoxuan.cakedao.AbstractCakeDao;\n")
                .append("import com.zhaoxuan.cakedao.AbstractClassMap;\n")
                .append("import java.util.HashMap;\n")
                .append("import java.util.Map;\n");

        builder.append("public class ClassMap extends AbstractClassMap { \n")
                .append("private Map<Class, AbstractCakeDao> abstractCakeDaoMap; \n");

        builder.append("public <T> AbstractCakeDao<T> getAbstractCakeDao(Class<T> clazz) {\n")
                .append("return abstractCakeDaoMap.get(clazz);\n }");

        builder.append("public Map<Class, AbstractCakeDao> getMap(){\n")
                .append("if(abstractCakeDaoMap == null){ \n")
                .append("abstractCakeDaoMap = new HashMap<Class, AbstractCakeDao>(); \n");
        Collection<ProxyInfo> list = proxyMap.values();
        for (ProxyInfo info : list) {
            builder.append("abstractCakeDaoMap.put(")
                    .append(info.getFullClassName())
                    .append(".class, new ")
                    .append(info.getProxyClassFullName())
                    .append("(db)); \n");
        }
        builder.append("} \n");
        builder.append("return abstractCakeDaoMap;\n }");

        builder.append("} \n");
        writeCode("com.zhaoxuan.cakedao.ClassMap", element, builder.toString());

    }
}
