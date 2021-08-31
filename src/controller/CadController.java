package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Timer;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import application.Main;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import model.ConnectionJDBC;

public class CadController implements Initializable {

	// cadastro
	@FXML
	private TextField nPedigreeTextField;

	@FXML
	private TextField nomeTextField;

	@FXML
	private DatePicker DNDatePicker;

	@FXML
	private TextField nPaiTextField;

	@FXML
	private TextField nMaeTextField;

	@FXML
	private Label CadReLabel;

	@FXML
	private ComboBox<String> RacaComboBox;

	private List<String> racas = new ArrayList<>();

	private ObservableList<String> obsRacas;

	@FXML
	private Button CadastroButton;

	@FXML
	private Button ListaButton;

	@FXML
	private void ListaOnAction() {
		Main.loadScene("/view/Lista.fxml", "Lista");
	}

	@FXML
	private void VamoCadastrarButton() {

		ConnectionJDBC conect = new ConnectionJDBC();
		Connection conectDB = conect.getConnection();
		
		Date date = java.sql.Date.valueOf(DNDatePicker.getValue());

		String insert = "INSERT INTO cachorro (pedigree, nome, raca_id, dn, sexo, nMae, nPai) VALUES ('"
				+ nPedigreeTextField.getText() + "','" + nomeTextField.getText() + "',"
				+ RacaComboBox.getSelectionModel().getSelectedItem().split(" - ")[0] + ",'" + date + "','"
				+ SexoComboBox.getSelectionModel().getSelectedItem() + "','" + nMaeTextField.getText() + "','"
				+ nPaiTextField.getText() + "');";
		try {

			Statement statement = conectDB.createStatement();
			statement.executeUpdate(insert);
			setMsg(CadReLabel, "Cadastro realizado com \n sucesso!");
			nPedigreeTextField.clear();
			nomeTextField.clear();
			nMaeTextField.clear();
			nPaiTextField.clear();
			RacaComboBox.getItems().clear();
			SexoComboBox.getItems().clear();
			DNDatePicker.getEditor().clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private ComboBox<Sexo> SexoComboBox;

	private List<Sexo> sexos = new ArrayList<>();

	private ObservableList<Sexo> obsSexos;

	
	//autocomplete
	private AutoCompletionBinding<String> completaId;
	
	private List<String> _possiveisCompId = new ArrayList<>();

	private Set<String> possiveisId = new HashSet<String>(_possiveisCompId);

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		buildData();
		carregaSexoComboBoxCad();
		carregaRacaComboBoxCad();
		carregaSexoComboBoxAtt();
		carregaRacaComboBoxAtt();
		autoCompletaId();
		TextFields.bindAutoCompletion(ProcuraIdTextField,
				_possiveisCompId.toString().replace("[", "").replace("]", "").split(", "));

	}

	public void carregaSexoComboBoxCad() {

		obsSexos = FXCollections.observableArrayList(sexos);

		SexoComboBox.setItems(obsSexos);
	}

	public void carregaRacaComboBoxCad() {

		obsRacas = FXCollections.observableArrayList(racas);
		RacaComboBox.setItems(obsRacas);
	}

	private void autoCompletaId() {
		ConnectionJDBC conect = new ConnectionJDBC();
		Connection conectDB = conect.getConnection();

		String SELECTIDCOMP = "SELECT id FROM showcachorros;";

		try {
			Statement statement = conectDB.createStatement();
			ResultSet queryResult = statement.executeQuery(SELECTIDCOMP);

			while (queryResult.next()) {
				_possiveisCompId.add(queryResult.getString("id"));

			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	// pesquisar e atualizar

	@FXML
	private Label MensagemErroLabel;

	public boolean validaId(String value) {
		boolean result = true;
		String[] splited = value.split("");
		for (String ch : splited) {
			if (!Character.isDigit(ch.charAt(0))) {
				result = false;
			}
		}
		return result;

	}

	@FXML
	private void ProcuraOCaoOnAction() {

		if (validaId(ProcuraIdTextField.getText())) {
			ConnectionJDBC conect = new ConnectionJDBC();
			Connection conectDB = conect.getConnection();

			String SELECT = "SELECT * FROM olhatdcachorros WHERE id = " + ProcuraIdTextField.getText() + ";";
			try {

				Statement statement = conectDB.createStatement();
				ResultSet queryResult = statement.executeQuery(SELECT);

				if (!queryResult.next()) {
					setMsg(MensagemErroLabel, "Não existe ID");
				} else {
					attNomeTextField.setText(queryResult.getString("nome"));
					nPedigreeAttTextField.setText(queryResult.getString("pedigree"));
					NPaiAttTextField.setText(queryResult.getString("nPai"));
					NMaeAttTextField.setText(queryResult.getString("nMae"));
					attDataDatePicker.setValue(queryResult.getDate("dn").toLocalDate());
					attSexoComboBox.setPromptText(queryResult.getString("sexo"));
					attRacaComboBox
							.setPromptText(queryResult.getString("raca_id") + " - " + queryResult.getString("raca"));
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else {
			setMsg(MensagemErroLabel, "Somente números!");
		}
	}

	String inputCache = "";

	public void buildData() {
		Sexo sexo1 = new Sexo("F");
		Sexo sexo2 = new Sexo("M");

		sexos.add(sexo1);
		sexos.add(sexo2);

		ConnectionJDBC conect = new ConnectionJDBC();
		Connection conectDB = conect.getConnection();

		try {
			Statement statement = conectDB.createStatement();
			ResultSet queryResult = statement.executeQuery("SELECT * FROM Raca;");

			while (queryResult.next()) {
				racas.add(queryResult.getInt("id") + " - " + queryResult.getString("raca"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void carregaSexoComboBoxAtt() {

		obsSexos = FXCollections.observableArrayList(sexos);

		attSexoComboBox.setItems(obsSexos);
	}

	public void carregaRacaComboBoxAtt() {

		obsRacas = FXCollections.observableArrayList(racas);
		attRacaComboBox.setItems(obsRacas);
	}

	@FXML
	private TextField ProcuraIdTextField;

	@FXML
	private TextField nPedigreeAttTextField;

	@FXML
	private TextField attNomeTextField;

	@FXML
	private DatePicker attDataDatePicker;

	@FXML
	private TextField NPaiAttTextField;

	@FXML
	private TextField NMaeAttTextField;

	@FXML
	private Label TxLabel;

	@FXML
	private ComboBox attSexoComboBox;

	private List<Sexo> sexos1 = new ArrayList<>();

	private ObservableList<Sexo> obsSexos1;

	@FXML
	private ComboBox attRacaComboBox;

	private List<String> racas1 = new ArrayList<>();

	private ObservableList<String> obsRacas1;

	@FXML
	private Button ExcluirButton;

	@FXML
	private void DeletaCaoOnAction() {

		ConnectionJDBC conect = new ConnectionJDBC();
		Connection conectDB = conect.getConnection();

		String DELETE = "DELETE FROM cachorro WHERE pedigree = " + nPedigreeAttTextField.getText() + ";";
		try {
			Statement statement = conectDB.createStatement();
			int queryResult = statement.executeUpdate(DELETE);
			nPedigreeAttTextField.clear();
			ProcuraIdTextField.clear();
			attNomeTextField.clear();
			NMaeAttTextField.clear();
			NPaiAttTextField.clear();
			attSexoComboBox.getItems().clear();
			attRacaComboBox.getItems().clear();
			attDataDatePicker.getEditor().clear();
			setMsg(MensagemErroLabel, "Cachorro Deletado");
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@FXML
	private Button attButton;

	@FXML
	private void AttAiOnAction() {
		if ((attNomeTextField.getText().isEmpty()) && (nPedigreeAttTextField.getText().isEmpty())
				&& NMaeAttTextField.getText().isEmpty() && NPaiAttTextField.getText().isEmpty()) {
		} else {

			ConnectionJDBC conect = new ConnectionJDBC();
			Connection conectDB = conect.getConnection();

			String raca;
			try {
				raca = ((String) attRacaComboBox.getSelectionModel().getSelectedItem()).split(" - ")[0];
			} catch (Exception e) {
				raca = attRacaComboBox.getPromptText().split(" - ")[0];
			}
			String sexo;
			try {
				sexo = (String) attSexoComboBox.getSelectionModel().getSelectedItem().toString();
			} catch (Exception e) {
				sexo = attSexoComboBox.getPromptText();
			}
			Date date = java.sql.Date.valueOf(attDataDatePicker.getValue());

			String UPDATE = "UPDATE cachorro SET pedigree = " + nPedigreeAttTextField.getText() + ", nome = '" + attNomeTextField.getText()
			+ "', raca_id = " + raca + ", dn = '" + date + "', sexo = '" + sexo + "', nMae = '"
			+ NMaeAttTextField.getText() + "', nPai = '" + NPaiAttTextField.getText() + "' WHERE id = " + ProcuraIdTextField.getText() + ";";

			try {
				Statement statement = conectDB.createStatement();
				int queryResult = statement.executeUpdate(UPDATE);
				ProcuraOCaoOnAction();
				nPedigreeAttTextField.clear();
				ProcuraIdTextField.clear();
				attNomeTextField.clear();
				NMaeAttTextField.clear();
				NPaiAttTextField.clear();
				attSexoComboBox.getItems().clear();
				attRacaComboBox.getItems().clear();
				attRacaComboBox.setPromptText("Raça");
				attSexoComboBox.setPromptText("Sexo");
				attDataDatePicker.getEditor().clear();
				setMsg(MensagemErroLabel, "Atualizado com \n sucesso!");
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}
	}

	public void setMsg(Label labelAlvo, String msg) {
		labelAlvo.setText(msg);
		Timer t = new java.util.Timer();
		t.schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> {
					labelAlvo.setText("");
				});
				t.cancel();
			}
		}, 5000);
	}

}