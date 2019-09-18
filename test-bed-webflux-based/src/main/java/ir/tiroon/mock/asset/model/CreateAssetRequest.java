package ir.tiroon.mock.asset.model;

import ir.tiroon.mock.asset.domain.AssetMetadata;

import java.time.Instant;

public class CreateAssetRequest {
    private Instant createdAt;
    private AssetMetadata assetMetadata;

    private CreateAssetRequest() {
    }

    private CreateAssetRequest(Builder builder) {
        this.createdAt = builder.createdAt;
        this.assetMetadata = builder.assetMetadata;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public AssetMetadata getAssetMetadata() {
        return assetMetadata;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Instant createdAt;
        private AssetMetadata assetMetadata;

        public Builder withCreatedAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder withAssetMetadata(AssetMetadata assetMetadata) {
            this.assetMetadata = assetMetadata;
            return this;
        }

        public CreateAssetRequest build() {
            return new CreateAssetRequest(this);
        }
    }
}
