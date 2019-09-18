package ir.tiroon.mock.asset.domain;

public enum AssetDefaultMetadata {
    ASSET_MIME_TYPE_XML(AssetKey.ASSET_MIME_TYPE, "application/xml"),
    ASSET_MIME_TYPE_PDF(AssetKey.ASSET_MIME_TYPE, "application/pdf"),
    ASSET_MIME_TYPE_JPG(AssetKey.ASSET_MIME_TYPE, "image/jpeg"),

    ASSET_FILE_EXTENSION_PDF(AssetKey.ASSET_FILE_EXTENSION, ".pdf"),
    ASSET_FILE_EXTENSION_XML(AssetKey.ASSET_FILE_EXTENSION, ".xml"),
    ASSET_FILE_EXTENSION_JPG(AssetKey.ASSET_FILE_EXTENSION, ".jpg"),

    ASSET_TYPE_SETTLEMENT(AssetKey.ASSET_TYPE, "settlement"),
    ASSET_TYPE_NEWS(AssetKey.ASSET_TYPE, "news"),
    ASSET_TYPE_SCHOOL_TRANSPORT(AssetKey.ASSET_TYPE, "schoolTransport"),
    ASSET_TYPE_AMADEUS(AssetKey.ASSET_TYPE, "amadeusPnr");

    private AssetKey key;
    private Object value;

    AssetDefaultMetadata(AssetKey key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key.getStringValue();
    }

    public Object getValue() {
        return value;
    }
}
