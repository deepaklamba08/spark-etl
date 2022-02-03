package org.sp.etl.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.sp.etl.common.util.DataUtils;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

public class JsonConfiguration implements Serializable, Configuration {
    protected transient JsonNode dataNode;

    public JsonConfiguration() {
    }

    public static JsonConfiguration fromString(String content) throws JsonProcessingException {
        return new JsonConfiguration(DataUtils.getObjectMapper().readTree(content));
    }

    public JsonConfiguration(JsonNode dataNode) {
        this.dataNode = dataNode;
    }


    @Override
    public void importFrom(Configuration dataObject) {
        this.dataNode = ((JsonConfiguration) dataObject).dataNode;
    }


    @Override
    public String getStringValue(String fieldName) {
        return this.checkAndGet(fieldName, String.class);
    }


    @Override
    public JsonConfiguration getAttribute(String fieldName) {
        return this.checkAndGet(fieldName, JsonConfiguration.class);
    }

    public JsonNode getDataNode() {
        return dataNode;
    }

    @Override
    @JsonIgnore
    public Iterator<String> getFields() {
        return this.dataNode != null && !this.dataNode.isNull() ? this.dataNode.fieldNames() : null;
    }


    @Override
    public Map<String, String> getValueMap(String fieldName) {
        JsonNode node = this.dataNode.get(fieldName);
        return DataUtils.getObjectMapper().convertValue(node, new TypeReference<Map<String, String>>() {
        });
    }

    @Override
    public void setAttribute(String fieldName, Configuration attribute) {
        this.checkAndAdd(fieldName, attribute);
    }


    @Override
    public void setAttribute(String fieldName, String attribute) {
        this.checkAndAdd(fieldName, attribute);
    }


    @Override
    public void setAttribute(String fieldName, long attribute) {
        this.checkAndAdd(fieldName, attribute);
    }

    @Override
    @JsonIgnore

    public boolean isArray() {
        return dataNode != null && dataNode.isArray();
    }

    @Override
    @JsonIgnore

    public boolean isArray(String fieldName) {
        JsonNode value = this.dataNode != null ? dataNode.get(fieldName) : null;
        return value != null && value.isArray();
    }

    @Override
    @JsonIgnore

    public boolean isObject() {
        return dataNode != null && dataNode.isObject();
    }

    @Override
    @JsonIgnore

    public boolean isObject(String fieldName) {
        JsonNode value = this.dataNode != null ? dataNode.get(fieldName) : null;
        return value != null && value.isObject();

    }

    @Override
    @JsonIgnore

    public boolean isNull() {
        return this.dataNode == null || this.dataNode.isNull();
    }


    public String toString() {
        return dataNode != null ? dataNode.toString() : null;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        if (in.readBoolean()) {
            InputStream inStream = new ObjectInputStream(in);
            this.dataNode = DataUtils.getObjectMapper().configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false).readValue(inStream,
                    JsonNode.class);
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        if (this.dataNode == null) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            OutputStream outStream = new ObjectOutputStream(out);
            DataUtils.getObjectMapper().configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false).writeValue(outStream, this.dataNode);
        }
    }

    private <T> T checkAndGet(String fieldName, Class<T> type) {
        if (this.dataNode == null || this.dataNode.isNull()) {
            return null;
        }
        JsonNode value = this.dataNode.get(fieldName);
        if (value == null || value.isNull()) {
            return null;
        }
        if (String.class.equals(type)) {
            return (T) value.asText();
        } else if (JsonConfiguration.class.equals(type)) {
            return (T) new JsonConfiguration(value);
        }

        return null;
    }

    private void checkAndAdd(String fieldName, Object value) {

        if (this.dataNode == null || this.dataNode.isNull()) {
            throw new IllegalStateException("Can not add attribute to null data.");
        }

        if (this.isObject()) {
            ObjectNode objectNode = (ObjectNode) this.dataNode;
            this.dataNode = this.addAttributeToObject(objectNode, fieldName, value);
        } else if (this.isArray()) {

        }
    }

    private ObjectNode addAttributeToObject(ObjectNode object, String fieldName, Object value) {
        if (value == null) {
            object.put(fieldName, (String) null);
        } else if (value instanceof String) {
            object.put(fieldName, value.toString());
        } else if (value instanceof Long) {
            object.put(fieldName, (long) value);
        } else if (value instanceof JsonConfiguration) {
            JsonConfiguration dataObject = (JsonConfiguration) value;
            object.set(fieldName, dataObject.getDataNode());
        } else {

        }

        return object;
    }
}
