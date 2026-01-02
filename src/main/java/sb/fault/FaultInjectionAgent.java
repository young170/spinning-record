package sb.fault;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FaultInjectionAgent {

    public static void premain(String agentArgs, Instrumentation inst) {

        System.out.println("[Agent] Fault Injection Agent loaded");

        // Data ///////////
        Set<Integer> targetLines = new HashSet<>();
        targetLines.add(8);

        List<MutationOperator> operators = new ArrayList<>();
        operators.add(new MutatorArithmetricOperator());
        ///////////////////

        MutationConfig config = new MutationConfig(targetLines, operators);

        inst.addTransformer(new FaultInjectionTransformer(config), true);
    }
}
