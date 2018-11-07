package com.simple.cat.aptprocessor;

import com.google.auto.service.AutoService;
import com.simple.cat.aptannotation.BindView;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

@AutoService(Processor.class)
public class AptProcessor extends AbstractProcessor {

		private Messager mMessager;

		private Elements mElements;

		@Override
		public Set<String> getSupportedAnnotationTypes() {
				HashSet<String> mAnnotationSet = new LinkedHashSet<>();
//				mAnnotationSet.add(BindView.class.getCanonicalName());
				return mAnnotationSet;
		}

		@Override
		public SourceVersion getSupportedSourceVersion() {
				return SourceVersion.latestSupported();
		}

		@Override
		public synchronized void init(ProcessingEnvironment processingEnvironment) {
				super.init(processingEnvironment);
				mMessager = processingEnvironment.getMessager();
				mElements = processingEnvironment.getElementUtils();
		}

		@Override
		public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
				//public static void main
				MethodSpec main = MethodSpec.methodBuilder("main")
					.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
					.returns(void.class)
					.addStatement("$T.out.println($S)",System.class,"Hello APt")
					.build();
				TypeSpec helloworld = TypeSpec.classBuilder("HelloWorld")
					.addModifiers(Modifier.PUBLIC, Modifier.FINAL)
					.addMethod(main)
					.build();
				JavaFile javaFile = JavaFile.builder("com.test.helloworld",helloworld)
					.build();
				try {
						javaFile.writeTo(processingEnv.getFiler());
				} catch (IOException e) {
						e.printStackTrace();
				}
				return false;
		}
}
