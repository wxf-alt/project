package javase.reflection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// 注意 如果需要通过反射获取注解上的信息，改注解上的生命周期必须是 RUNTIME
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnn {
    String name();
}
