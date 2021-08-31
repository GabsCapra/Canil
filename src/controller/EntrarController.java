package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.ConnectionJDBC;

public class EntrarController {

	@FXML
	private Label ExibMensagemLabel;

	@FXML
	private TextField LoginTextField;

	@FXML
	private PasswordField SenhaPasswordField;

	@FXML
	private Button EnterButton;

	public void EntraAiButtonOnAction(ActionEvent e) {
		if ((LoginTextField.getText().isEmpty()) && (SenhaPasswordField.getText().isEmpty())) {
			ExibMensagemLabel.setText("Login e/ou Senha Inválido.");
		} else {
			if (validaLogin()) {
				vaiLoga();
			} else {
				ExibMensagemLabel.setText("Login e/ou senha inválido."+ "\n" +"Tente novamente.");
			}
		}

	}

	public boolean validaLogin() {
		boolean result = false;

		ConnectionJDBC conect = new ConnectionJDBC();
		Connection conectDB = conect.getConnection();
		
		String verificaLogin = "SELECT count(1) FROM login WHERE email = '" + LoginTextField.getText()
				+ "' AND senha = '" + SenhaPasswordField.getText() + "'";
		
		
		try {
			Statement statement = conectDB.createStatement();
			ResultSet queryResult = statement.executeQuery(verificaLogin);

			while (queryResult.next()) {
				if (queryResult.getInt(1) == 1) {
					result = true;
				} else {
					result = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void vaiLoga() {
		Main.loadScene("/view/TelaCadAtt.fxml", "Cad-Att");
	}

}
