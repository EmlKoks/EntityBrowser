package emlkoks.entitybrowser.update;

import emlkoks.entitybrowser.Main;
import emlkoks.entitybrowser.Mode;
import emlkoks.entitybrowser.view.ViewFile;
import emlkoks.entitybrowser.view.controller.UpdateAvailableController;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;

import static emlkoks.entitybrowser.update.GithubReleaseProperties.*;

@Slf4j
public class Updater {
    private static final String RELEASE_URL = "https://api.github.com/repos/EmlKoks/EntityBrowser/releases";
    private static final String JAR_TYPE = "application/x-java-archive";
    private ReleaseInfo releaseInfo;

    public Updater() {
        checkForUpdates();
    }

    public void checkForUpdates() {
        String response;
        try {
            if (Mode.DEBUG.equals(Main.mode)) {
                response = mockedRelease();
            } else {
                response = getLastRelease();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            createReleaseNotes(response);
        } catch (ReleaseAssetNotFound exception) {
            log.info("Not found downloadable asset");
            return;
        }
        log.info("ReleaseInfo {}", releaseInfo);
        if (isNew()) {
            showNewVersionDialog();
        }
    }

    private boolean isNew() {
        return !Main.properties.getVersion().equals(releaseInfo.getLastVersion());
    }

    private void showNewVersionDialog() {
        FXMLLoader loader = new FXMLLoader(Main.getMainController().getClass()
                .getResource(ViewFile.UPDATE_AVAILABLE.getFile()), Main.resources);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = createStage(loader);
        UpdateAvailableController controller = loader.getController();
        controller.showReleaseInfo(this, this.releaseInfo);
        stage.showAndWait();
    }

    private void createReleaseNotes(String response) {
        JSONObject responseJson = new JSONArray(response).getJSONObject(0);
        JSONArray assets = responseJson.getJSONArray(ASSETS.getValue());
        if (assets.isEmpty()) {
            throw new ReleaseAssetNotFound();
        }
        String downloadUrl = assets.toList().stream()
                .map(HashMap.class::cast)
                .filter(asset -> JAR_TYPE.equals(asset.get(ASSETS_CONTENT_TYPE.getValue())))
                .map(asset -> (String) asset.get(ASSETS_DOWNLOAD_URL.getValue()))
                .findFirst()
                .orElseThrow(ReleaseAssetNotFound::new);

        this.releaseInfo = ReleaseInfo.builder()
                .currentVersion(Main.properties.getVersion())
                .downloadUrl(downloadUrl)
                .lastVersion(responseJson.getString(VERSION.getValue()))
                .releaseTitle(responseJson.getString(TITLE.getValue()))
                .releaseNotes(responseJson.getString(CONTENT.getValue()))
                .build();
    }

    private Stage createStage(FXMLLoader loader) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene dialogScene = new Scene(loader.getRoot());
        stage.setTitle(Main.resources.getString("updateAvailable.title"));
        stage.setScene(dialogScene);
        return stage;
    }

    private String getLastRelease() throws IOException {
        URL url = new URL(RELEASE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        StringBuffer content;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            content = new StringBuffer();
            while ((inputLine = reader.readLine()) != null) {
                content.append(inputLine);
            }
        }
        return content.toString();
    }

    private String mockedRelease() throws IOException {
        return "[{\"url\": \"https://api.github.com/repos/EmlKoks/EntityBrowser/releases/25901126\", \"assets_url\": \"https://api.github.com/repos/EmlKoks/EntityBrowser/releases/25901126/assets\", \"upload_url\": \"https://uploads.github.com/repos/EmlKoks/EntityBrowser/releases/25901126/assets{?name,label}\", \"html_url\": \"https://github.com/EmlKoks/EntityBrowser/releases/tag/v0.1-alpha\", \"id\": 25901126, \"node_id\": \"MDc6UmVsZWFzZTI1OTAxMTI2\", \"tag_name\": \"v0.1-alpha\", \"target_commitish\": \"master\", \"name\": \"First release title\", \"draft\": false, \"author\": {\"login\": \"EmlKoks\", \"id\": 18641029, \"node_id\": \"MDQ6VXNlcjE4NjQxMDI5\", \"avatar_url\": \"https://avatars1.githubusercontent.com/u/18641029?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github.com/users/EmlKoks\", \"html_url\": \"https://github.com/EmlKoks\", \"followers_url\": \"https://api.github.com/users/EmlKoks/followers\", \"following_url\": \"https://api.github.com/users/EmlKoks/following{/other_user}\", \"gists_url\": \"https://api.github.com/users/EmlKoks/gists{/gist_id}\", \"starred_url\": \"https://api.github.com/users/EmlKoks/starred{/owner}{/repo}\", \"subscriptions_url\": \"https://api.github.com/users/EmlKoks/subscriptions\", \"organizations_url\": \"https://api.github.com/users/EmlKoks/orgs\", \"repos_url\": \"https://api.github.com/users/EmlKoks/repos\", \"events_url\": \"https://api.github.com/users/EmlKoks/events{/privacy}\", \"received_events_url\": \"https://api.github.com/users/EmlKoks/received_events\", \"type\": \"User\", \"site_admin\": false}, \"prerelease\": true, \"created_at\": \"2020-04-26T19:18:19Z\", \"published_at\": \"2020-04-26T22:06:55Z\", \"assets\": [{\"url\": \"https://api.github.com/repos/EmlKoks/EntityBrowser/releases/assets/20175519\", \"id\": 20175519, \"node_id\": \"MDEyOlJlbGVhc2VBc3NldDIwMTc1NTE5\", \"name\": \"entity-browser-0.1-alpha.jar\", \"label\": null, \"uploader\": {\"login\": \"EmlKoks\", \"id\": 18641029, \"node_id\": \"MDQ6VXNlcjE4NjQxMDI5\", \"avatar_url\": \"https://avatars1.githubusercontent.com/u/18641029?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github.com/users/EmlKoks\", \"html_url\": \"https://github.com/EmlKoks\", \"followers_url\": \"https://api.github.com/users/EmlKoks/followers\", \"following_url\": \"https://api.github.com/users/EmlKoks/following{/other_user}\", \"gists_url\": \"https://api.github.com/users/EmlKoks/gists{/gist_id}\", \"starred_url\": \"https://api.github.com/users/EmlKoks/starred{/owner}{/repo}\", \"subscriptions_url\": \"https://api.github.com/users/EmlKoks/subscriptions\", \"organizations_url\": \"https://api.github.com/users/EmlKoks/orgs\", \"repos_url\": \"https://api.github.com/users/EmlKoks/repos\", \"events_url\": \"https://api.github.com/users/EmlKoks/events{/privacy}\", \"received_events_url\": \"https://api.github.com/users/EmlKoks/received_events\", \"type\": \"User\", \"site_admin\": false}, \"content_type\": \"application/x-java-archive\", \"state\": \"uploaded\", \"size\": 113354, \"download_count\": 0, \"created_at\": \"2020-04-26T22:06:00Z\", \"updated_at\": \"2020-04-26T22:06:01Z\", \"browser_download_url\": \"https://github.com/EmlKoks/EntityBrowser/releases/download/v0.1-alpha/entity-browser-0.1-alpha.jar\"}], \"tarball_url\": \"https://api.github.com/repos/EmlKoks/EntityBrowser/tarball/v0.1-alpha\", \"zipball_url\": \"https://api.github.com/repos/EmlKoks/EntityBrowser/zipball/v0.1-alpha\", \"body\": \"First release body\"}]";
    }

    public void update() {
        //TODO
    }
}
