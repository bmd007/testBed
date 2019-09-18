package ir.tiroon.mock.asset.domain;


public class AssetData {
    private byte[] data;
    private String mimeType;

    private AssetData(Builder builder) {
        this.data = builder.data;
        this.mimeType = builder.mimeType;
    }

    public byte[] getData() {
        return data;
    }

    public String getMimeType() {
        return mimeType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private byte[] data;
        private String mimeType;

        private Builder() {
        }

        public Builder withData(byte[] data) {
            this.data = data;
            return this;
        }

        public Builder withMimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        public AssetData build() {
            return new AssetData(this);
        }
    }
}
