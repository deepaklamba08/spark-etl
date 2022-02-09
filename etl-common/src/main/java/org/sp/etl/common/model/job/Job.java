package org.sp.etl.common.model.job;


import org.sp.etl.common.model.Configuration;
import org.sp.etl.common.model.Id;
import org.sp.etl.common.model.Identifiable;
import org.sp.etl.common.model.step.Step;

import java.util.ArrayList;
import java.util.List;

public class Job implements Identifiable {

    private Id id;
    private String name;
    private String description;
    private boolean isActive;
    private List<String> targets;
    private List<Step> steps;
    private Configuration configuration;

    private Job(Id id, String name, String description, boolean isActive, List<String> targets, List<Step> steps, Configuration configuration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.targets = targets;
        this.steps = steps;
        this.configuration = configuration;
    }

    public List<String> getTargets() {
        return targets;
    }

    public List<Step> getSteps() {
        return steps;
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

    public Configuration getConfiguration() {
        return configuration;
    }

    public String getConfigValue(String key) {
        return this.configuration != null ? this.configuration.getStringValue(key) : null;
    }

    public static class Builder {
        private Id id;
        private String name;
        private String description;
        private boolean isActive;
        private List<String> targets;
        private List<Step> steps;
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

        public Builder withStep(Step step) {
            if (this.steps == null) {
                this.steps = new ArrayList<>();
            }
            this.steps.add(step);
            return this;
        }

        public Builder withTarget(String target) {
            if (this.targets == null) {
                this.targets = new ArrayList<>();
            }
            this.targets.add(target);
            return this;
        }

        public Builder withConfiguration(Configuration configuration) {
            this.configuration = configuration;
            return this;
        }

        public Job build() {
            return new Job(this.id, this.name, this.description, this.isActive, this.targets, this.steps, this.configuration);
        }
    }
}
