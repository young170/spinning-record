package sb.fault;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MutatorArithmetricOperator implements MutationOperator {

    @Override
    public boolean matches(int opcode) {
        return opcode == Opcodes.IADD;
    }

    @Override
    public void apply(MethodVisitor mv) {
        mv.visitInsn(Opcodes.ISUB);
    }

    @Override
    public String name() {
        return "IADD→ISUB";
    }
}
