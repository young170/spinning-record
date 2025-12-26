package sb.fault;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class FaultInjectionTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(
            ClassLoader loader,
            String className,
            Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain,
            byte[] classfileBuffer
    ) {

        // 1. Skip bootstrap
        if (loader == null) {
            return null;
        }

        // 2. Skip JDK + agent + ASM
        if (className == null
                || className.startsWith("java/")
                || className.startsWith("jdk/")
                || className.startsWith("sun/")
                || className.startsWith("org/objectweb/asm/")) {
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
                        @Override
                        public void visitInsn(int opcode) {
                            // Fault: replace IADD with ISUB
                            if (opcode == Opcodes.IADD) {
                                System.out.println(
                                        "[FaultInjected] Replacing IADD with ISUB in "
                                        + className + "." + name
                                );
                                super.visitInsn(Opcodes.ISUB);
                            } else {
                                super.visitInsn(opcode);
                            }
                        }
                    };
                }
            };

            cr.accept(cv, 0);
            return cw.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
