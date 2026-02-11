package tech.petrepopescu.flamewing.parser.route;

import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;

public class ByteCodeObject extends SimpleJavaFileObject {

    private ByteArrayOutputStream bytecode;
    public ByteCodeObject(String name, JavaFileObject.Kind kind) {
        super(URI.create("bytecode:///" + name + kind.extension), kind);
    }

    @Override
    public OutputStream openOutputStream() {
        bytecode = new ByteArrayOutputStream();
        return bytecode;
    }

    public ByteArrayOutputStream getBytecode() {
        return bytecode;
    }
}
