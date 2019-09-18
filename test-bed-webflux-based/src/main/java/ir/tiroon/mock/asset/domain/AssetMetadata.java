package ir.tiroon.mock.asset.domain;

import java.util.HashMap;
import java.util.Map;

public class AssetMetadata {
    private Map<String, Object> data;

    private AssetMetadata() {
    }

    protected AssetMetadata(Builder builder) {
        this.data = builder.data;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Object getData(String key) {
        return data.containsKey(key) ? data.get(key) : null;
    }

    public Object getData(AssetKey key) {
        return data.containsKey(key.getStringValue()) ? data.get(key.getStringValue()) : null;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Map<String, Object> data = new HashMap<>();

        public Builder withData(String key, Object value) {
            data.put(key, value);
            return this;
        }

        public Builder withData(AssetKey key, Object value) {
            data.put(key.getStringValue(), value);
            return this;
        }

        public Builder withData(AssetDefaultMetadata obj) {
            data.put(obj.getKey(), obj.getValue());
            return this;
        }

        public Builder withData(Map<String, Object> map) {
            data.putAll(map);
            return this;
        }

        public AssetMetadata build() {
            return new AssetMetadata(this);
        }
    }
}
