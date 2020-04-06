package emlkoks.entitybrowser.view;

public enum ViewFile {
    ENTITY_DETAILS("/view/entityDetails.fxml"),
    MAIN_WINDOW("/view/mainWindow.fxml"),
    NEW_CONNECTION("/view/newConnection.fxml"),
    NEW_DRIVER("/view/newDriver.fxml"),
    NEW_SESSION("/view/newSession.fxml");

    private String file;

    ViewFile(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }
}
