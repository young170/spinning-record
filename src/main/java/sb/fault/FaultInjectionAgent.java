package sb.fault;

import java.lang.instrument.Instrumentation;

public class FaultInjectionAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("[Agent] Fault Injection Agent loaded");

        inst.addTransformer(new FaultInjectionTransformer(), true);
    }
}
