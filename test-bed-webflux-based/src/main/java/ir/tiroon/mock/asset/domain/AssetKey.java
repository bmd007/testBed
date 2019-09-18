package ir.tiroon.mock.asset.domain;

public enum AssetKey {
    ASSET_MIME_TYPE("asset_mime_type"),
    ASSET_FILE_EXTENSION("asset_file_extension"),
    ASSET_TYPE("asset_type"),
    ASSET_OBJECT_KEY("asset_object_key"),       //  "/some/path/filename.ext"

    PARTNER_ID("partner_id"),
    PARTNER_DOCUMENT_TYPE("partner_document_type"),
    PARTNER_FLEET_NAME("partner_fleet_name"),
    PARTNER_PAYMENT_DATE("partner_payment_date");

    private String key;

    AssetKey(String key) {
        this.key = key;
    }

    public String getStringValue() {
        return key;
    }
}
