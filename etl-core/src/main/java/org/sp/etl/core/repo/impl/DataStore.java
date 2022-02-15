package org.sp.etl.core.repo.impl;

import org.sp.etl.common.exception.EtlExceptions;
import org.sp.etl.common.model.Configuration;
import org.sp.etl.common.model.ConfigurationType;
import org.sp.etl.common.model.Id;
import org.sp.etl.common.util.ConfigurationFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DataStore<T> {
    private final File dataFile;
    private final ConfigurationType configurationType;
    private Map<Id, T> elements;
    private boolean shouldRefresh = false;
    private Object lock = new Object();

    private final Function<T, Id> identifier;
    private final Function<Configuration, T> mapper;

    public DataStore(ConfigurationType configurationType, File dataFile, Function<Configuration, T> mapper, Function<T, Id> identifier) {
        this.configurationType = configurationType;
        this.dataFile = dataFile;
        this.mapper = mapper;
        this.identifier = identifier;
    }

    public T lookupElement(Id key) throws EtlExceptions.InvalidConfigurationException {
        if (this.shouldRefresh || this.elements == null) {
            this.elements = this.readDataFile();
        }
        return this.elements.get(key);
    }

    private Map<Id, T> readDataFile() throws EtlExceptions.InvalidConfigurationException {
        Configuration configuration = ConfigurationFactory.parse(this.dataFile, this.configurationType);
        return configuration.getAsList().stream().map(mapper).collect(Collectors.toMap(identifier, Function.identity()));
    }

    private void saveToFile(Map<Id, T> elements) throws EtlExceptions.InvalidConfigurationException {
        elements.values();
        //ConfigurationFactory.save(this.configurationType, null, this.dataFile, true);
    }

    public void saveElement(T element) throws EtlExceptions.InvalidConfigurationException {
        synchronized (lock) {
            Map<Id, T> existingElements = new HashMap<>(this.readDataFile());
            T existingElement = existingElements.get(this.identifier.apply(element));
            if (existingElement != null) {

            }
            existingElements.put(this.identifier.apply(element), element);
            this.saveToFile(existingElements);
            this.shouldRefresh = true;
        }
    }

    public void updateElement(T element) throws EtlExceptions.InvalidConfigurationException {
        synchronized (lock) {
            Map<Id, T> existingElements = new HashMap<>(this.readDataFile());
            T existingElement = existingElements.get(this.identifier.apply(element));
            if (existingElement == null) {

            }
            existingElements.put(this.identifier.apply(element), element);
            this.saveToFile(existingElements);
            this.shouldRefresh = true;
        }
    }

}