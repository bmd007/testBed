package ir.tiroon.mock.asset.model;

public class CreateAssetResponseModel {

    private Long assetId;

    private String message;

    private CreateAssetResponseModel() {
    }

    private CreateAssetResponseModel(Builder builder) {
        this.assetId = builder.assetId;
        this.message = builder.message;
    }

    public Long getAssetId() {
        return assetId;
    }

    public String getMessage() {
        return message;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long assetId;
        private String message;

        public Builder withAssetId(Long assetId) {
            this.assetId = assetId;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public CreateAssetResponseModel build() {
            return new CreateAssetResponseModel(this);
        }
    }
}
