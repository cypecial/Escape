public class Main {
    public static void main(String[] args) {
        Model model = new Model();
        MainFrame mainFrame = new MainFrame(model);
        MenuView menuView = new MenuView(model);
        GameView gameView = new GameView(model);
        HelpView helpView = new HelpView(model);

        EditorModel editorModel = new EditorModel();
        EditorFrame editorFrame =  new EditorFrame(editorModel);
        EditorView editorView = new EditorView(editorModel);

        model.addEditorFrame(editorFrame);
        model.setScreen("mainMenu");
        mainFrame.setVisible(true);
        editorFrame.setVisible(false);
    }
}