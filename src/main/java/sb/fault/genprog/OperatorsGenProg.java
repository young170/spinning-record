package sb.fault.genprog;

import java.util.Map;

import jdk.internal.org.objectweb.asm.Opcodes;

public final class OperatorsGenProg {

    private static final Map<Integer, Integer> REPLACE = Map.of(
            Opcodes.IADD, Opcodes.ISUB,
            Opcodes.ISUB, Opcodes.IADD
    );

    public static boolean isSupported(int opcode) {
        return REPLACE.containsKey(opcode);
    }

    public static int mutate(int opcode) {
        return REPLACE.get(opcode);
    }
}
