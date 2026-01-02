package sb.fault;

import java.util.List;
import java.util.Set;

public class MutationConfig {

    private final Set<Integer> targetLines;
    private final List<MutationOperator> operators;

    public MutationConfig(
            Set<Integer> targetLines,
            List<MutationOperator> operators
    ) {
        this.targetLines = targetLines;
        this.operators = operators;
    }

    public boolean isTargetLine(int line) {
        return targetLines.contains(line);
    }

    public List<MutationOperator> operators() {
        return operators;
    }
}
