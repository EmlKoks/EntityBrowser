package emlkoks.entitybrowser.view;

public enum ViewFile {
    CHOOSE_CONNECTION("/view/chooseConnection.fxml"),
    ENTITY_DETAILS("/view/entityDetails.fxml"),
    MAIN_WINDOW("/view/main/mainWindow.fxml"),
    NEW_DRIVER("/view/newDriver.fxml");

    private String file;

    ViewFile(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }
}
