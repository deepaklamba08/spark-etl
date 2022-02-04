package org.sp.etl.common.model.step;

import org.sp.etl.common.model.Configuration;
import org.sp.etl.common.model.Id;
import org.sp.etl.common.model.Identifiable;
import org.sp.etl.function.EtlFunction;

import java.util.ArrayList;
import java.util.List;

public class Step implements Identifiable {

    private Id id;
    private String name;
    private String description;
    private boolean isActive;
    private int stepIndex;
    private List<String> sources;
    private String outputSourceName;
    private String outputSourceAlias;
    private List<EtlFunction> etlFunctions;
    private Configuration configuration;

    private Step(Id id, String name, String description, boolean isActive, int stepIndex, List<String> sources, String outputSourceName, String outputSourceAlias, List<EtlFunction> etlFunctions, Configuration configuration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.stepIndex = stepIndex;
        this.sources = sources;
        this.outputSourceName = outputSourceName;
        this.outputSourceAlias = outputSourceAlias;
        this.etlFunctions = etlFunctions;
        this.configuration = configuration;
    }

    public int getStepIndex() {
        return stepIndex;
    }

    public String getOutputSourceName() {
        return outputSourceName;
    }

    public String getOutputSourceAlias() {
        return outputSourceAlias;
    }

    public List<String> getSources() {
        return sources;
    }

    public List<EtlFunction> getEtlFunctions() {
        return etlFunctions;
    }

    @Override
    public boolean isActive() {
        return this.isActive;
    }

    @Override
    public Id getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    public static class Builder {
        private Id id;
        private String name;
        private String description;
        private boolean isActive;
        private int stepIndex;
        private List<String> sources;
        private String outputSourceName;
        private String outputSourceAlias;
        private List<EtlFunction> etlFunctions;
        private Configuration configuration;

        public Builder withId(Id id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withSource(String source) {
            if (this.sources == null) {
                this.sources = new ArrayList<>();
            }
            this.sources.add(source);
            return this;
        }

        public Builder withActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder makeActive() {
            this.isActive = true;
            return this;
        }

        public Builder makeInActive() {
            this.isActive = false;
            return this;
        }

        public Builder withStepIndex(int stepIndex) {
            this.stepIndex = stepIndex;
            return this;
        }

        public Builder withOutputSourceName(String outputSourceName) {
            this.outputSourceName = outputSourceName;
            return this;
        }

        public Builder withOutputSourceAlias(String outputSourceAlias) {
            this.outputSourceAlias = outputSourceAlias;
            return this;
        }

        public Builder withEtlFunction(EtlFunction etlFunction) {
            if (this.etlFunctions == null) {
                this.etlFunctions = new ArrayList<>();
            }
            this.etlFunctions.add(etlFunction);
            return this;
        }

        public Builder withConfiguration(Configuration configuration) {
            this.configuration = configuration;
            return this;
        }

        public Step build() {
            return new Step(this.id, this.name, this.description, this.isActive, this.stepIndex, this.sources, this.outputSourceName, this.outputSourceAlias, this.etlFunctions, this.configuration);
        }

    }
}
