package com.wei.omini.annotation;

import com.wei.omini.constants.Constants;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by wangqiaobin on 2016/11/15.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Component
public @interface Remote {
    String cmd();

    String sub();

    String version() default Constants.DEFAULT_VERSION;
}