package buct.tzx.servicemanager_processer;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;

import buct.tzx.servicemanager_annotaion.ServiceFactory;

@AutoService(Processor.class)
@SupportedAnnotationTypes("buct.tzx.servicemanager_annotaion.ServiceFactory")
public class process extends AbstractProcessor {
    private Types mTypeUtils;
    private Elements mElementUtils;
    private Filer mFiler;
    private Messager mMessager;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mTypeUtils = processingEnvironment.getTypeUtils();
        mElementUtils = processingEnvironment.getElementUtils();
        mFiler = processingEnvironment.getFiler();
        mMessager = processingEnvironment.getMessager();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        System.out.println(ServiceFactory.class.getName());
        annotations.add(ServiceFactory.class.getName());
        return annotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.println("??????????????????");
        System.out.println("???????????????"+roundEnvironment.getElementsAnnotatedWith(ServiceFactory.class).size()+"???");
        for(Element s:roundEnvironment.getElementsAnnotatedWith(ServiceFactory.class)){
            if(s.getKind()!= ElementKind.CLASS){
                throw new IllegalArgumentException("???????????????");
            }
            System.out.println("??????????????????????????????"+s.toString());
            Annotation realannotation = s.getAnnotation(ServiceFactory.class);
            System.out.println("???????????????????????????"+" " +((ServiceFactory)realannotation).protool());
            System.out.println("????????????????????????");
            FieldSpec FACTORYNAME = FieldSpec.builder(String.class, "FACTORYNAME")
                    .addModifiers(Modifier.PRIVATE, Modifier.FINAL,Modifier.STATIC)
                    .initializer("$S", s.toString())
                    .build();
            String [] names = ((ServiceFactory)realannotation).protool().split("\\.");
            StringBuilder packagename= new StringBuilder();
            for(int i=0;i<names.length-1;i++){
                packagename.append(names[i]).append(".");
            }
            String realpackagename = packagename.substring(0,packagename.length()-1);
            TypeSpec FACNAME = TypeSpec.classBuilder(names[names.length-1]+"SFC")
                    .addModifiers(Modifier.PUBLIC)
                    .addField(FACTORYNAME)
                    .build();
            System.out.println("???????????????"+realpackagename+" "+"???????????????"+FACNAME.name);
            JavaFile javaFile = JavaFile.builder(realpackagename, FACNAME)
                    .build();
            String targetSrc=FACNAME.name;
            System.out.println("????????????"+javaFile.packageName.replace(".","/")+"/srcbuild/"+FACNAME.name+".java");
            System.out.println("?????????"+targetSrc);

            try {
//                File file1=new File("srcbuild/com/example/aptprocesser");
//                if(!file1.exists()){//????????????????????????
//                    file1.mkdir();//???????????????
//                }
//                File file =  new File("srcbuild/"+FACNAME.name+".java");
//                if(file.exists()){
//                    file.delete();
//                    file =  new File("srcbuild/"+FACNAME.name+".java");
//                }
//                System.out.println(file.createNewFile());
//                System.out.println(file.getAbsolutePath());
            JavaFileObject source = mFiler.createSourceFile(targetSrc);
//                javaFile.writeTo(new File(""));
                Writer writer = source.openWriter();
                writer.write(javaFile.toString());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

}