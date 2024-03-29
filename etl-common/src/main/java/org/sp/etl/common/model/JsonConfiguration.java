package org.sp.etl.common.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.sp.etl.common.util.DataUtils;
import org.sp.etl.common.util.Preconditions;

import java.io.*;
import java.util.*;
import java.util.function.Function;

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
    public boolean hasField(String fieldName) {
        this.checkNull();
        Iterator<String> fields = this.dataNode.fieldNames();
        boolean hasField = false;
        while (!hasField && fields.hasNext()) {
            hasField = fields.next().equals(fieldName);
        }
        return hasField;
    }


    @Override
    public String getStringValue(String fieldName) {
        return this.checkAndGet(fieldName, String.class, this.dataNode);
    }

    @Override
    public Date getDateValue(String fieldName) {
        long value = this.getLongValue(fieldName);
        return new Date(value);
    }

    @Override
    public Date getDateValue(String fieldName, Date defaultValue) {
        if(this.hasField(fieldName)){
            return this.getDateValue(fieldName);
        }else{
            return defaultValue;
        }
    }

    @Override
    public String getStringValue(String fieldName, String defaultValue) {
        String value = this.checkAndGet(fieldName, String.class, this.dataNode);
        return value != null ? value : defaultValue;
    }

    @Override
    public boolean getBooleanValue(String fieldName) {
        return this.checkAndGet(fieldName, Boolean.class, this.dataNode);
    }

    @Override
    public boolean getBooleanValue(String fieldName, boolean defaultValue) {
        Boolean value = this.checkAndGet(fieldName, Boolean.class, this.dataNode);
        return value != null ? value : defaultValue;
    }

    @Override
    public List<String> getListValue(String fieldName) {
        checkNull();
        if (this.isArray(fieldName)) {
            List<String> values = new ArrayList<>();
            ArrayNode jsonNodes = (ArrayNode) this.dataNode.get(fieldName);
            Iterator<JsonNode> elements = jsonNodes.elements();
            while (elements.hasNext()) {
                values.add(elements.next().asText());
            }
            return values;
        } else {
            throw new IllegalStateException("value for key - " + fieldName + " is not an array");
        }
    }

    @Override
    public List<String> getListValue(String fieldName, List<String> defaultValue) {
        List<String> values = this.getListValue(fieldName);
        return values != null ? values : defaultValue;
    }

    @Override
    public int getIntValue(String fieldName) {
        return this.checkAndGet(fieldName, Integer.class, this.dataNode);
    }

    @Override
    public long getLongValue(String fieldName) {
        return this.checkAndGet(fieldName, Long.class, this.dataNode);
    }


    @Override
    public JsonConfiguration getAttribute(String fieldName) {
        return this.checkAndGet(fieldName, JsonConfiguration.class, this.dataNode);
    }


    @Override
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
    public boolean isArray() {
        return dataNode != null && dataNode.isArray();
    }

    @Override
    public boolean isArray(String fieldName) {
        JsonNode value = this.dataNode != null ? dataNode.get(fieldName) : null;
        return value != null && value.isArray();
    }

    @Override
    public boolean isObject() {
        return dataNode != null && dataNode.isObject();
    }

    @Override
    public boolean isObject(String fieldName) {
        JsonNode value = this.dataNode != null ? dataNode.get(fieldName) : null;
        return value != null && value.isObject();

    }

    @Override
    public boolean isNull() {
        return this.dataNode == null || this.dataNode.isNull();
    }

    @Override
    public void merge(Configuration other) {
        if (!(other instanceof JsonConfiguration)) {
            throw new IllegalArgumentException("configuration is not of type - " + JsonConfiguration.class);
        }
        JsonConfiguration otherConfig = (JsonConfiguration) other;
        if (this.isArray() && otherConfig.isArray()) {
            ArrayNode currentNode = this.dataNode.deepCopy();
            ArrayNode otherNode = (ArrayNode) otherConfig.dataNode;
            otherNode.forEach(element -> currentNode.add(element));
            this.dataNode = currentNode;
        } else if (this.isObject() && otherConfig.isObject()) {
            ObjectNode currentNode = this.dataNode.deepCopy();
            ObjectNode otherNode = (ObjectNode) otherConfig.dataNode;
            otherNode.fieldNames().forEachRemaining(field -> {
                currentNode.put(field, otherNode.get(field));
            });
            this.dataNode = currentNode;
        } else {
            throw new IllegalStateException("configurations have different structure");
        }

    }

    /*@Override
    public void remove(Configuration other) {
        if (!(other instanceof JsonConfiguration)) {
            throw new IllegalArgumentException("configuration is not of type - " + JsonConfiguration.class);
        }
        JsonConfiguration otherConfig = (JsonConfiguration) other;
        if (this.isArray()) {
            ArrayNode currentNode = this.dataNode.deepCopy();
        }

    }*/

    @Override
    public void toArray() {
        if (this.isObject()) {
            ArrayNode node = DataUtils.getObjectMapper().createArrayNode();
            node.add(this.dataNode);
            this.dataNode = node;
        }
    }

    @Override
    public List<Configuration> getAsList() {
        if (this.isArray()) {
            Iterator<JsonNode> elements = this.dataNode.elements();
            List<Configuration> values = new ArrayList<>();
            while (elements.hasNext()) {
                values.add(new JsonConfiguration(elements.next()));
            }
            return values;
        } else {
            throw new IllegalStateException("vale is not an array");
        }
    }

    @Override
    public Map<String, String> getAsMap() {
        if (this.isObject()) {
            Iterator<String> fields = this.dataNode.fieldNames();
            Map<String, String> values = new HashMap<>();
            while (fields.hasNext()) {
                String field = fields.next();
                values.put(field, this.dataNode.get(field).asText());
            }
            return values;
        } else {
            throw new IllegalStateException("vale is not an object");
        }
    }

    @Override
    public <T> Map<String, T> getAsMap(Function<Configuration, T> mapper) {
        if (this.isObject()) {
            Iterator<String> fields = this.dataNode.fieldNames();
            Map<String, T> values = new HashMap<>();
            while (fields.hasNext()) {
                String field = fields.next();
                values.put(field, mapper.apply(new JsonConfiguration(this.dataNode.get(field))));
            }
            return values;
        } else {
            throw new IllegalStateException("vale is not an object");
        }
    }

    @Override
    public Configuration getConfiguration(String fieldName) {
        return new JsonConfiguration(this.dataNode.get(fieldName));
    }

    @Override
    public Configuration getConfiguration(String fieldName, Configuration defaultValue) {
        JsonNode value = this.dataNode.get(fieldName);
        return value != null ? new JsonConfiguration(value) : defaultValue;
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

    private <T> T checkAndGet(String fieldName, Class<T> type, JsonNode dataNode) {
        if (dataNode == null || dataNode.isNull()) {
            return null;
        }
        JsonNode value = dataNode.get(fieldName);
        if (value == null || value.isNull()) {
            return null;
        }
        if (String.class.equals(type)) {
            return (T) value.asText();
        } else if (JsonConfiguration.class.equals(type)) {
            return (T) new JsonConfiguration(value);
        } else if (Integer.class.equals(type)) {
            return (T) new Integer(value.asInt());
        }  else if (Long.class.equals(type)) {
            return (T) new Long(value.asLong());
        } else if (Boolean.class.equals(type)) {
            return (T) new Boolean(value.asBoolean());
        } else {
            throw new IllegalStateException("not implemented yet for type - " + type);
        }
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

    public JsonNode getDataNode() {
        return this.dataNode;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public Id getId() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    private void checkNull() {
        Preconditions.checkNull(this.dataNode, "data node is null");
    }
}
