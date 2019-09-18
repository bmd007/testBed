package ir.tiroon.mock.asset.model;

public class AssetReferenceModel {

    private static final long serialVersionUID = 1L;

    private String url;

    private Long assetId;

    private AssetReferenceModel() {
    }

    private AssetReferenceModel(Builder builder) {
        this.url = builder.url;
        this.assetId = builder.assetId;
    }

    public String getUrl() {
        return url;
    }

    public Long getAssetId() {
        return assetId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String url;
        private Long assetId;

        private Builder() {
        }

        public Builder withUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder withAssetId(Long assetId) {
            this.assetId = assetId;
            return this;
        }

        public AssetReferenceModel build() {
            return new AssetReferenceModel(this);
        }
    }
}
