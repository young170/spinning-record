package sb.fault;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class FaultInjectionTransformer implements ClassFileTransformer {

    private final MutationConfig config;

    public FaultInjectionTransformer(MutationConfig config) {
        this.config = config;
    }

    @Override
    public byte[] transform(
            ClassLoader loader,
            String className,
            Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain,
            byte[] classfileBuffer
    ) {

        // Bootstrap
        if (loader == null) {
            return null;
        }

        // Skip JDK + agent + ASM
        if (className == null
                || className.startsWith("java/")
                || className.startsWith("jdk/")
                || className.startsWith("sun/")
                || className.startsWith("org/objectweb/asm/")
                || className.startsWith("sb/fault/")) {
            return null;
        }

        // Only mutate application package
        if (!className.startsWith("test_files/")) {
            return null;
        }

        try {
            ClassReader cr = new ClassReader(classfileBuffer);
            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);

            ClassVisitor cv = new ClassVisitor(Opcodes.ASM9, cw) {

                @Override
                public MethodVisitor visitMethod(
                        int access,
                        String name,
                        String descriptor,
                        String signature,
                        String[] exceptions
                ) {
                    MethodVisitor mv = super.visitMethod(
                            access, name, descriptor, signature, exceptions
                    );

                    return new MethodVisitor(Opcodes.ASM9, mv) {

                        private int currentLine = -1;

                        @Override
                        public void visitLineNumber(int line, Label start) {
                            currentLine = line;
                            super.visitLineNumber(line, start);
                        }

                        @Override
                        public void visitInsn(int opcode) {

                            if (config.isTargetLine(currentLine)) {
                                for (MutationOperator op : config.operators()) {
                                    if (op.matches(opcode)) {
                                        System.out.println(
                                                "[FaultInjected] "
                                                + className + "." + name
                                                + " line " + currentLine
                                                + " " + op.name()
                                        );
                                        op.apply(mv);
                                        return;
                                    }
                                }
                            }

                            super.visitInsn(opcode);
                        }
                    };
                }
            };

            cr.accept(cv, ClassReader.EXPAND_FRAMES);
            return cw.toByteArray();

        } catch (Throwable t) {
            return null;
        }
    }
}
