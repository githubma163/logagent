package com.max.log.agent.javassist;

import com.max.log.agent.constant.Constant;
import com.max.log.agent.util.ConfigUtil;
import com.max.log.agent.util.StringUtils;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.Modifier;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class CostTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        className = className.replaceAll("/", ".");
        String exclude = ConfigUtil.CONFIG_MAP.get(Constant.EXCLUDE);
        String include = ConfigUtil.CONFIG_MAP.get(Constant.INCLUDE);
        if (StringUtils.isNotBlank(exclude)) {
            if (className.startsWith(exclude)) {
                return classfileBuffer;
            }
        }
        if (className.contains("$$EnhancerBySpringCGLIB$$")) {
            return classfileBuffer;
        }
        if (className.contains("$$FastClassBySpringCGLIB$")) {
            return classfileBuffer;
        }
        if (!className.startsWith(include)) {
            return classfileBuffer;
        }
        System.out.println(className);
        CtClass clazz = null;
        try {
            //ClassPool classPool = ClassPool.getDefault();
            ClassPool classPool = new ClassPool(true);
            //classPool.appendClassPath("/test.jar");
            classPool.appendClassPath(new LoaderClassPath(loader));
            clazz = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
            //javassist.CannotCompileException: no method body
            if (clazz.isInterface() || clazz.isEnum()) {
                return classfileBuffer;
            }
            if (Modifier.isAbstract(clazz.getModifiers())) {
                return classfileBuffer;
            }
            for (CtMethod method : clazz.getDeclaredMethods()) {
                System.out.println(method.getLongName());
                CtClass[] parameterTypes = method.getParameterTypes();
                StringBuilder parameterSb = new StringBuilder();
                //MethodInfo methodInfo = method.getMethodInfo();
                //CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
                //LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
                for (int i = 1, l = parameterTypes.length; i <= l; i++) {
                    //原始类型到包装类型的转换java.lang.VerifyError: Bad type on operand stack
                    String paramName = parameterTypes[i - 1].getName();
                    System.out.println(paramName);
                    if (paramName.equals("int")) {
                        parameterSb.append("java.lang.Integer.valueOf(");
                        parameterSb.append("$");
                        parameterSb.append(i);
                        parameterSb.append(")");
                    } else if (paramName.equals("long")) {
                        parameterSb.append("java.lang.Long.valueOf(");
                        parameterSb.append("$");
                        parameterSb.append(i);
                        parameterSb.append(")");
                    } else {
                        parameterSb.append("$");
                        parameterSb.append(i);
                    }
                    parameterSb.append(",");
                }
                // 定义属性
                method.addLocalVariable("parameterValues", classPool.get(Object[].class.getName()));
                if (parameterSb.length() > 0) {
                    String param = parameterSb.substring(0, parameterSb.length() - 1);
                    System.out.println(param);
                    // 方法前加强
                    method.insertBefore("{ parameterValues = new Object[]{" + param + "}; }");
                } else {
                    method.insertBefore("{ parameterValues = new Object[0]; }");
                }
                // 所有方法，统计耗时；请注意，需要通过`addLocalVariable`来声明局部变量
                method.addLocalVariable("start", CtClass.longType);
                method.insertBefore("start = System.currentTimeMillis();");
                String methodName = method.getLongName();
                method.insertAfter("System.out.println(\"" + methodName + "cost: \" + (System.currentTimeMillis() - start));");
                method.insertAfter("com.max.log.agent.service.LogService.produce(\"" + clazz.getName() + "\",\"" + methodName + "\",parameterValues,(System.currentTimeMillis()-start));");
            }
            byte[] transformed = clazz.toBytecode();
            return transformed;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Object[] parameterValues = new Object[0];
        return classfileBuffer;
    }
}
