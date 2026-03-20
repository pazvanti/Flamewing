package tech.petrepopescu.flamewing.parser.compiler;

import org.springframework.stereotype.Component;

@Component
public class DynamicClassLoader extends ClassLoader {
    public DynamicClassLoader() {
        super(Thread.currentThread().getContextClassLoader());
    }

    public DynamicClassLoader(ClassLoader parent) {
        super(parent);
    }
    public Class<?> loadClass(String className, byte[] bytecode) {
        Class<?> clazz = findLoadedClass(className);
        if (clazz == null) {
            clazz = defineClass(className, bytecode, 0, bytecode.length);
        }
        return clazz;
    }
}
