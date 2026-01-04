start with mutations

https://github.com/mrwilson/byte-monkey

try fault injection

apply techniques from APR tools

can APR tools fix the bugs they made? or are they their worst enemies? what can we learn from this? are certain types better at 'catching' others? if the performance is poor, does this mean anything? does this apply for cases when they perform well?

how? need to design a pipeline that flips that of APR. then systematically apply to AMAP

bug input -> generation -> validation

APR-guided fault injection for benchmarking repair and localization tools

- sums up what im trying to do here

this topic can be split into two parts:

1. self-consistency evaluation of APR tools
2. repair-bias on specific-APR-generated faults

### first one

lets start with the first one.

this asks the question: do APR tools behave consistently with the fault models they implicitly assume?

by **fault models** i mean the edits, locations, and the granularity each tools posesses.

APR tools

- hypothesize and target faults using their _fault models_
  - this is because of the search space problem, limited resources
- evaluated on faults external benchmarks
  - which may or may not have been generated using their fault model

then what is self-consistency and how is it measured?

- following the steps of the general APR process: FL -> GEN -> RANK -> VAL
- was the APR-generated fault (1) localized(preferably highly ranked)? (2) fix generated using similar edit operations? (3) used as a patch candidate(preferably highly ranked)?

#### implications & possible outcomes

- repair-prioritizations may have a negative effect on self-consistency
- heuristic-based APR tools perform poorly due to localizations not being able to be invertible
- instead of blindly focusing on: repair rate, correctness, and (recently) overfitting, are the tools itself consistent?

#### (possible) RQs

- does the APR tool repair its own faults at a higher rate than random mutation faults?
  - !!! how can this be connected to benchmarks such as D4J?
- how close is the repair patch to the injected fault edit?
  - use AST edit dist or other methods based on APR-tools' granularity
  - for example, the repair should be in the same scope as the injected fault
  - use closely-invertible edits as possible, but some may not be possible
- was the injected fault prioritized?
  - should be the most clear of all
  - check the ranking in between each step
  - might even be out of the search space itself
    - would this happen? since they assume the same space
