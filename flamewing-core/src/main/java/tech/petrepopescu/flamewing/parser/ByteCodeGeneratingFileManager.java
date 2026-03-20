package tech.petrepopescu.flamewing.parser;

import tech.petrepopescu.flamewing.parser.route.ByteCodeObject;

import javax.tools.*;
import java.util.HashMap;
import java.util.Map;

public class ByteCodeGeneratingFileManager extends ForwardingJavaFileManager<JavaFileManager> {
    private final Map<String, ByteCodeObject> storedBytecodeObjects = new HashMap<>();

    public ByteCodeGeneratingFileManager(JavaFileManager fileManager) {
        super(fileManager);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
       ByteCodeObject object = new ByteCodeObject(className, kind);
       storedBytecodeObjects.put(className, object);
       return object;
    }

    public byte[] getBytecode(String className) {
        return storedBytecodeObjects.get(className).getBytecode().toByteArray();
    }

    public Map<String, ByteCodeObject> getBytecodeObjects() {
        return storedBytecodeObjects;
    }
}