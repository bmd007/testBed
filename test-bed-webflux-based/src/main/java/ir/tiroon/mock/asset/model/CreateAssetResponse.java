package ir.tiroon.mock.asset.model;

import java.time.Instant;

public class CreateAssetResponse {
    private Long assetId;
    private Instant createdAt;

    private CreateAssetResponse() {
    }

    private CreateAssetResponse(Builder builder) {
        this.assetId = builder.assetId;
        this.createdAt = builder.createdAt;
    }

    public Long getAssetId() {
        return assetId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long assetId;
        private Instant createdAt;

        private Builder() {
        }

        public Builder withAssetId(Long assetId) {
            this.assetId = assetId;
            return this;
        }

        public Builder withCreatedAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public CreateAssetResponse build() {
            return new CreateAssetResponse(this);
        }
    }
}
