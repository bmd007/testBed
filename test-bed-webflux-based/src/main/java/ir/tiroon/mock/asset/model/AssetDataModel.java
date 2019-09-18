package ir.tiroon.mock.asset.model;

public class AssetDataModel {

    private byte[] data;
    private String mimeType;

    private AssetDataModel() {
    }

    private AssetDataModel(Builder builder) {
        this.data = builder.data;
        this.mimeType = builder.mimeType;
    }

    public byte[] getData() {
        return this.data;
    }

    public String getMimeType() {
        return this.mimeType;
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

        public AssetDataModel build() {
            return new AssetDataModel(this);
        }
    }
}
