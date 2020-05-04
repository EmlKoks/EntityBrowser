package emlkoks.entitybrowser.update;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReleaseInfo {
    private String currentVersion;
    private String lastVersion;
    private String releaseTitle;
    private String releaseNotes;
    private String downloadUrl;
}
