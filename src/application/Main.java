package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
	public static Stage primaryStage;
	public static Class thisClass;
	
	public Main() {
		thisClass = getClass();
	}
		
	@Override
	public void start(Stage primaryStage) throws Exception{
		Main.primaryStage = primaryStage;
		loadScene("/view/Login.fxml", "Login");
	}
	public static void loadScene (String path, String title) {
		Parent root;
		Scene scene;
		try { 
			root = FXMLLoader.load(thisClass.getClass().getResource(path));
			scene = new Scene(root);
			Image image = new Image("/images/dog-training.png");
			primaryStage.setTitle(title);
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(image);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}
