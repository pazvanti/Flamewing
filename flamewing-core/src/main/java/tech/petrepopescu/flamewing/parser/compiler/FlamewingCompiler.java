package tech.petrepopescu.flamewing.parser.compiler;

import javax.tools.JavaFileObject;
import java.util.List;

public abstract class FlamewingCompiler {
    public abstract void compileAndLoad(List<JavaFileObject> javaFileObjects);

    public abstract boolean isJdkPresent();
}
