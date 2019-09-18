package ir.tiroon.mock.asset.domain;

import java.net.URL;

public class AssetReference {
    private URL url;

    private Long assetId;

    private AssetReference(Builder builder) {
        this.url = builder.url;
        this.assetId = builder.assetId;
    }

    public URL getUrl() {
        return url;
    }

    public Long getAssetId() {
        return assetId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private URL url;
        private Long assetId;

        private Builder() {
        }

        public Builder withUrl(URL url) {
            this.url = url;
            return this;
        }

        public Builder withAssetId(Long assetId) {
            this.assetId = assetId;
            return this;
        }

        public AssetReference build() {
            return new AssetReference(this);
        }
    }
}
