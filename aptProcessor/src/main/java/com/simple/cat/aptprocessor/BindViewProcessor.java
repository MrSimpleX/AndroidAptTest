package com.simple.cat.aptprocessor;

import com.google.auto.service.AutoService;
import com.simple.cat.aptannotation.BindActivity;
import com.simple.cat.aptannotation.BindView;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
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

/**
 * @author MrSimpleZ
 * @version V1.0
 * @Title: AndroidAptTest
 * @Package com.simple.cat.aptprocessor
 * @Description: (用一句话描述该文件做什么)
 * @date 2018/11/6 5:17 PM
 */
@AutoService(Processor.class)
public class BindViewProcessor extends AbstractProcessor {

		private Messager mMessage;
		private Elements mElements;

		@Override
		public Set<String> getSupportedAnnotationTypes() {
				HashSet<String> annSet = new LinkedHashSet<>();
				annSet.add(BindActivity.class.getCanonicalName());
				return annSet;
		}

		@Override
		public SourceVersion getSupportedSourceVersion() {
				return SourceVersion.latestSupported();
		}

		@Override
		public synchronized void init(ProcessingEnvironment processingEnvironment) {
				super.init(processingEnvironment);
				mMessage = processingEnvironment.getMessager();
				mElements = processingEnvironment.getElementUtils();
		}

		/**
		 * to auto generate code below
		 * public final class DIxxActivity{
		 *      public static void bind(Activity activity){
		 *             activity.view = activity.findviewById(R.id.xx);
		 *      }
		 * }
		 * @param set
		 * @param roundEnvironment
		 * @return
		 */
		@Override
		public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
				System.out.println("--------------apt start");

				Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindActivity.class);
				for(Element element : elements){
						TypeElement typeElement = (TypeElement) element;
						System.out.println("--------------apt " + ((TypeElement) element).getQualifiedName());
						List<? extends Element> elementList = mElements.getAllMembers(typeElement);
						System.out.println("--------------apt " + ClassName.get(typeElement.asType()));
						MethodSpec.Builder bindViewMethod = MethodSpec.methodBuilder("bindView")
							.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
							.returns(TypeName.VOID)
							.addParameter(ClassName.get(typeElement.asType()),"activity");

						for(Element member : elementList){
								BindView view = member.getAnnotation(BindView.class);
								if(view == null){
										continue;
								}
								bindViewMethod.addStatement(String.format("activity.%s = (%s)activity.findViewById(%s)",
									member.getSimpleName(),
									ClassName.get(member.asType()).toString(),
									view.value()));
								System.out.println("--------------apt " + ClassName.get(member.asType()).toString());
						}

						TypeSpec bindActivityType = TypeSpec.classBuilder("DI" + typeElement.getSimpleName())
							.superclass(TypeName.get(typeElement.asType()))
							.addModifiers(Modifier.PUBLIC, Modifier.FINAL)
							.addMethod(bindViewMethod.build())
							.build();
						JavaFile javaFile = JavaFile.builder(mElements.getPackageOf(typeElement).getQualifiedName().toString(), bindActivityType)
							.build();
						try {
								javaFile.writeTo(processingEnv.getFiler());
						} catch (IOException e) {
								e.printStackTrace();
						}
						System.out.println("--------------apt finish");
				}
				return true;
		}
}
