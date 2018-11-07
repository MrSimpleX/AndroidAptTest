package com.simple.cat.aptannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author MrSimpleZ
 * @version V1.0
 * @Title: AndroidAptTest
 * @Package com.simple.cat.aptannotation
 * @Description: (用一句话描述该文件做什么)
 * @date 2018/11/6 3:18 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BindView {
		int value() default 0;
}
