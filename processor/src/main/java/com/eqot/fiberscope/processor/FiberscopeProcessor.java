package com.eqot.fiberscope.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.eqot.fiberscope.processor.Fiberscope", "com.eqot.fiberscope.processor.Accessible"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class FiberscopeProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        StringBuilder builder = new StringBuilder()
                .append("package com.stablekernel.annotationprocessor.generated;\n\n")
                .append("public class GeneratedClass {\n\n") // open class
                .append("\tpublic String getMessage() {\n") // open method
                .append("\t\treturn \"");

        for (Element element : roundEnv.getElementsAnnotatedWith(Fiberscope.class)) {
            String objectType = element.getSimpleName().toString();

            final Messager messager = processingEnv.getMessager();

            processClass(element);

//            final Accessible accessible = element.getAnnotation(Accessible.class);
//            final String target = accessible.value();
//            messager.printMessage(Diagnostic.Kind.NOTE, target);

            builder.append(objectType).append(" says hello!\\n");
        }

        builder.append("\";\n") // end return
                .append("\t}\n") // close method
                .append("}\n"); // close class

        try {
            JavaFileObject source = processingEnv.getFiler().createSourceFile("com.stablekernel.annotationprocessor.generated.GeneratedClass");

            Writer writer = source.openWriter();
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
        }

        return true;
    }

    private void processClass(Element klass) {
        final String packageName = processingEnv.getElementUtils().getPackageOf(klass).getQualifiedName().toString();
        final ClassName className = ClassName.get(packageName, klass.getSimpleName().toString());
        final ClassName newClass = ClassName.get(
                packageName, klass.getSimpleName().toString() + "Fiberscope2");

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(newClass.simpleName())
                .addModifiers(Modifier.PUBLIC);

        classBuilder
                .addField(className, "mInstance", Modifier.PRIVATE, Modifier.FINAL);

        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("mInstance = new $T()", className);

        for (Element element : klass.getEnclosedElements()) {
            if (element.getAnnotation(Accessible.class) == null) {
                continue;
            }

            final String methodName = element.getSimpleName().toString();

            constructorBuilder
                    .beginControlFlow("try")
                    .addStatement("$N = $T.class.getDeclaredMethod($S, int.class, int.class)",
                            "_" + methodName, className, methodName)
                    .addStatement("$N.setAccessible(true)", "_" + methodName)
                    .endControlFlow("catch (Exception e) {}");

            MethodSpec methodSpec = MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(int.class)
                    .addParameter(int.class, "value1")
                    .addParameter(int.class, "value2")
                    .addStatement("int result = 0")
                    .beginControlFlow("try")
                    .addStatement("result = (int) $N.invoke(mInstance, value1, value2)",
                            "_" + methodName)
                    .endControlFlow("catch (Exception e) {}")
                    .addStatement("return result")
                    .build();

            classBuilder
                    .addField(Method.class, "_" + methodName, Modifier.PRIVATE)
                    .addMethod(methodSpec);
        }

        classBuilder
                .addMethod(constructorBuilder.build());

        TypeSpec classSpec = classBuilder.build();

        try {
            JavaFileObject source = processingEnv.getFiler().createSourceFile(newClass.toString());
            Writer writer = source.openWriter();

            JavaFile.builder(packageName, classSpec)
                .build()
                .writeTo(writer);
//                .writeTo(System.out);

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
