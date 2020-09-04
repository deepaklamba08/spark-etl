package org.sp.etl.common.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

public class JsonDataObject implements Serializable {
    protected transient JsonNode dataNode;

    public JsonDataObject() {
    }

    public static JsonDataObject emptyArray() {
        return new JsonDataObject(JsonDataUtils.getObjectMapper().createArrayNode());
    }

    public static JsonDataObject emptyObject() {
        return new JsonDataObject(JsonDataUtils.getObjectMapper().createObjectNode());
    }

    public JsonDataObject(JsonNode dataNode) {
        this.dataNode = dataNode;
    }


    public void importFrom(JsonDataObject dataObject) {
        this.dataNode = ((JsonDataObject) dataObject).dataNode;
    }


    public String getStringValue(String fieldName) {
        return this.checkAndGet(fieldName, String.class);
    }


    public JsonDataObject getAttribute(String fieldName) {
        return this.checkAndGet(fieldName, JsonDataObject.class);
    }

    public JsonNode getDataNode() {
        return dataNode;
    }

    @JsonIgnore
    public Iterator<String> getFields() {
        return this.dataNode != null && !this.dataNode.isNull() ? this.dataNode.fieldNames() : null;
    }


    public Map<String, String> getValueMap(String fieldName) {
        JsonNode node = this.dataNode.get(fieldName);
        return JsonDataUtils.getObjectMapper().convertValue(node, new TypeReference<Map<String, String>>() {
        });
    }

    public void setAttribute(String fieldName, JsonDataObject attribute) {
        this.checkAndAdd(fieldName, attribute);
    }


    public void setAttribute(String fieldName, String attribute) {
        this.checkAndAdd(fieldName, attribute);
    }


    public void setAttribute(String fieldName, long attribute) {
        this.checkAndAdd(fieldName, attribute);
    }

    @JsonIgnore

    public boolean isArray() {
        return dataNode != null && dataNode.isArray();
    }

    @JsonIgnore

    public boolean isArray(String fieldName) {
        JsonNode value = this.dataNode != null ? dataNode.get(fieldName) : null;
        return value != null && value.isArray();
    }

    @JsonIgnore

    public boolean isObject() {
        return dataNode != null && dataNode.isObject();
    }

    @JsonIgnore

    public boolean isObject(String fieldName) {
        JsonNode value = this.dataNode != null ? dataNode.get(fieldName) : null;
        return value != null && value.isObject();

    }

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
            this.dataNode = JsonDataUtils.getObjectMapper().configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false).readValue(inStream,
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
            JsonDataUtils.getObjectMapper().configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false).writeValue(outStream, this.dataNode);
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
        } else if (JsonDataObject.class.equals(type)) {
            return (T) new JsonDataObject(value);
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
        } else if (value instanceof JsonDataObject) {
            JsonDataObject dataObject = (JsonDataObject) value;
            object.set(fieldName, dataObject.getDataNode());
        } else {

        }

        return object;
    }
}
