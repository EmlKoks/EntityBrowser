package emlkoks.entitybrowser.update;

public enum GithubReleaseProperties {
    ASSETS("assets"),
    ASSETS_CONTENT_TYPE("content_type"),
    ASSETS_DOWNLOAD_URL("browser_download_url"),
    VERSION("tag_name"),
    TITLE("name"),
    CONTENT("body");

    private String value;

    GithubReleaseProperties(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
