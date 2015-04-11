package jibe.tools.bdd.core;

import com.google.common.collect.Lists;
import jibe.tools.bdd.api.DescriptiveType;
import jibe.tools.bdd.api.Execution;
import jibe.tools.bdd.api.ExecutionsHolder;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class DefaultExecutionsHolder implements ExecutionsHolder {
    private String description;
    private List<Execution> executions;
    private DescriptiveType descriptiveType;

    public DefaultExecutionsHolder(DescriptiveType descriptiveType, String description, Execution... executions) {
        this.descriptiveType = descriptiveType;
        this.description = description;
        this.executions = Lists.newArrayList(executions);
    }

    @Override
    public List<Execution> executions() {
        return Collections.unmodifiableList(executions);
    }

    @Override
    public DescriptiveType descriptiveType() {
        return descriptiveType;
    }

    @Override
    public String describe() {
        String description = String.format("%s: %s\n", descriptiveType, this.description);
        StringBuilder sb = new StringBuilder(description);
        Iterator<Execution> iterator = executions.iterator();
        while (iterator.hasNext()) {
            sb.append("\t").append(iterator.next().describe()).append("\n");
        }

        return sb.toString();
    }
}
