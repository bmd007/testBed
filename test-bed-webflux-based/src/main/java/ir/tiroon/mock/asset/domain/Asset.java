package ir.tiroon.mock.asset.domain;

import java.time.Instant;
import java.util.Objects;

/**
 * Asset domain object, used for persistence.
 */
public class Asset {

    private Long id;
    private Instant createdAt;
    private AssetMetadata assetMetadata;

    private Asset(Builder builder) {
        this.id = builder.id;
        this.createdAt = builder.createdAt;
        this.assetMetadata = builder.assetMetadata;
    }

    public Asset() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public AssetMetadata getAssetMetadata() {
        return assetMetadata;
    }

    public void setAssetMetadata(AssetMetadata assetMetadata) {
        this.assetMetadata = assetMetadata;
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof Asset)) {
            return false;
        }
        Asset castOther = (Asset) other;
        return Objects.equals(id, castOther.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assetMetadata);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Long id;
        private Instant createdAt;
        private AssetMetadata assetMetadata;

        private Builder() {
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withCreatedAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder withAssetMetadata(AssetMetadata assetMetadata) {
            this.assetMetadata = assetMetadata;
            return this;
        }

        public Asset build() {
            return new Asset(this);
        }
    }
}
