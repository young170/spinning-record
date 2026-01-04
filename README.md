Reversing APR tools as fault injection tools

### Run

```
$ mvn clean package
```

```
$ java -javaagent:target/bytecode-fault-agent-1.0.jar -cp <target_class_file>
```
