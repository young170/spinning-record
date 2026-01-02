package sb.fault;

import org.objectweb.asm.MethodVisitor;

public interface MutationOperator {

    boolean matches(int opcode);

    void apply(MethodVisitor mv);

    String name();
}
