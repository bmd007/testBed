package ir.tiroon.mock.asset.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.util.Date;

public class AssetModel {
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    private String documentType;

    private String fleetName;

    private Long partnerId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date paymentDate;

    private String url;

    private AssetModel() {
    }

    private AssetModel(Builder builder) {
        this.id = builder.id;
        this.createdAt = builder.createdAt;
        this.documentType = builder.documentType;
        this.fleetName = builder.fleetName;
        this.partnerId = builder.partnerId;
        this.paymentDate = builder.paymentDate;
        this.url = builder.url;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getUrl() {
        return url;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public String getFleetName() {
        return fleetName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public static class Builder {

        private Long id;
        private Instant createdAt;
        private String url;
        private String documentType;
        private String fleetName;
        private Long partnerId;
        private Date paymentDate;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withCreatedAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder withUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder withDocumentType(String documentType) {
            this.documentType = documentType;
            return this;
        }

        public Builder withFleetName(String fleetName) {
            this.fleetName = fleetName;
            return this;
        }

        public Builder withPartnerId(Long partnerId) {
            this.partnerId = partnerId;
            return this;
        }

        public Builder withPaymentDay(Date paymentDate) {
            this.paymentDate = paymentDate;
            return this;
        }


        public AssetModel build() {
            return new AssetModel(this);
        }
    }
}