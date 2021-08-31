package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;

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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Cachorro;
import model.ConnectionJDBC;

public class ListaController implements Initializable {

	@FXML
	private Label MensagemLabel;

	@FXML
	private TableView<Cachorro> Tabela;

	@FXML
	private TableColumn LIDTable;

	@FXML
	private TableColumn LPedigreeTable;

	@FXML
	private TableColumn LNomeTable;

	@FXML
	private TableColumn LRacaTable;

	@FXML
	private TableColumn LSexoTable;

	@FXML
	private TableColumn LDNTable;

	@FXML
	private TableColumn LNMaeTable;

	@FXML
	private TableColumn LNPaiTable;

	@FXML
	private TextField NomePet;

	@FXML
	private TextField PedigreePet;

	@FXML
	private TextField PaiPet;

	@FXML
	private TextField MaePet;

	@FXML
	private ComboBox SexoPet;

	private List<Sexo> sexos = new ArrayList<>();

	private ObservableList<Sexo> obsSexos;

	@FXML
	private ComboBox RacaPet;

	private List<String> racas = new ArrayList<>();

	private ObservableList<String> obsRacas;

	@FXML
	private Button VoltarButton;

	@FXML
	private Button EditButton;

	@FXML
	private Button ExcluirButton;
	
	@FXML
	private Button ReloadButton;

	@FXML
	private Button AtualizarButton;

	@FXML
	private DatePicker DNPet;

	@FXML
	private void VoltaOnAction() {
		Main.loadScene("/view/TelaCadAtt.fxml", "Cad-Att");
	}
	@FXML
	private void ReloadOn() {
		Main.loadScene("/view/Lista.fxml", "Lista");
	}

	public void carregaSexoPet() {

		obsSexos = FXCollections.observableArrayList(sexos);

		SexoPet.setItems(obsSexos);
	}

	public void carregaRacaPet() {

		obsRacas = FXCollections.observableArrayList(racas);
		RacaPet.setItems(obsRacas);
	}

	private List<Cachorro> listaCao = new ArrayList<>();

	private ObservableList<Cachorro> obsLista;

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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		buildData();
		carregaLista();
		carregaSexoPet();
		carregaRacaPet();
	}

	public void carregaLista() {

		LIDTable.setCellValueFactory(new PropertyValueFactory<>("ID"));
		LNomeTable.setCellValueFactory(new PropertyValueFactory<>("Nome"));
		LPedigreeTable.setCellValueFactory(new PropertyValueFactory<>("Pedigree"));
		LRacaTable.setCellValueFactory(new PropertyValueFactory<>("Raca"));
		LSexoTable.setCellValueFactory(new PropertyValueFactory<>("Sexo"));
		LDNTable.setCellValueFactory(new PropertyValueFactory<>("DN"));
		LNPaiTable.setCellValueFactory(new PropertyValueFactory<>("NMae"));
		LNMaeTable.setCellValueFactory(new PropertyValueFactory<>("NPai"));

		ConnectionJDBC conect = new ConnectionJDBC();
		Connection conectDB = conect.getConnection();

		String SELECT = "SELECT * FROM olhatdcachorros;";
		try {

			Statement statement = conectDB.createStatement();
			ResultSet queryResult = statement.executeQuery(SELECT);

			while (queryResult.next()) {
				Cachorro dog = new Cachorro(queryResult.getInt("id"), queryResult.getString("pedigree"),
						queryResult.getString("nome"),
						queryResult.getInt("raca_id"),
						queryResult.getInt("raca_id") + " - " + queryResult.getString("raca"),
						queryResult.getString("nMae"), queryResult.getString("nPai"), queryResult.getDate("dn"),
						queryResult.getString("sexo"));

				listaCao.add(dog);

			}
			obsLista = FXCollections.observableArrayList(listaCao);

		} catch (Exception e1) {
			e1.printStackTrace();
		}

		Tabela.setItems(obsLista);
		Tabela.setEditable(true);
	}

	@FXML
	private void EditOnAction() {
		if (Tabela.getSelectionModel().getSelectedItem() == null) {
			MensagemLabel.setText("Selecione na lista ou então volte ao menu anterior");
		} else {
			
			Cachorro idCao = Tabela.getSelectionModel().getSelectedItem();

			try {
				NomePet.setText(idCao.getNome());
				PedigreePet.setText(idCao.getPedigree());
				PaiPet.setText(idCao.getNPai());
				MaePet.setText(idCao.getNMae());
				
				Date date = new SimpleDateFormat("dd/MM/yyyy").parse(idCao.getDN());
				DNPet.setValue(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				
				SexoPet.setValue(idCao.getSexo());
				RacaPet.setValue(idCao.getRaca_id() + " - " + idCao.getRaca());
				System.out.println();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
		}
	}

	@FXML
	private void ExcluirOnAction() {
		ConnectionJDBC conect = new ConnectionJDBC();
		Connection conectDB = conect.getConnection();

		Cachorro idCao = Tabela.getSelectionModel().getSelectedItem();

		String DELETE = "DELETE FROM cachorro WHERE id = " + idCao.getID() + ";";

		try {
			Statement statement = conectDB.createStatement();
			int queryResult = statement.executeUpdate(DELETE);

			NomePet.clear();
			PedigreePet.clear();
			PaiPet.clear();
			MaePet.clear();
			SexoPet.getItems().clear();
			RacaPet.getItems().clear();
			SexoPet.setPromptText("Sexo");
			RacaPet.setPromptText("Raça");
			DNPet.getEditor().clear();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		setMsg(MensagemLabel, "Cachorro Deletado");
	}

	@FXML
	private void AttOnAction() {

		Cachorro idCao = Tabela.getSelectionModel().getSelectedItem();

		if ((NomePet.getText().isEmpty()) && (PedigreePet.getText().isEmpty()) && MaePet.getText().isEmpty()
				&& PaiPet.getText().isEmpty()) {
		} else {

			ConnectionJDBC conect = new ConnectionJDBC();
			Connection conectDB = conect.getConnection();

			String raca;
			try {
				raca = ((String) RacaPet.getSelectionModel().getSelectedItem()).split(" - ")[0];
			} catch (Exception e) {
				raca = RacaPet.getPromptText().split(" - ")[0];
			}
			String sexo;
			try {
				sexo = (String) SexoPet.getSelectionModel().getSelectedItem().toString();
			} catch (Exception e) {
				sexo = SexoPet.getPromptText();
			}
			Date date = java.sql.Date.valueOf(DNPet.getValue());

			String UPDATE = "UPDATE cachorro SET pedigree = " + PedigreePet.getText() + ", nome = '" + NomePet.getText()
					+ "', raca_id = " + raca + ", dn = '" + date + "', sexo = '" + sexo + "', nMae = '"
					+ MaePet.getText() + "', nPai = '" + PaiPet.getText() + "' WHERE id = " + idCao.getID() + ";";
			try {
				Statement statement = conectDB.createStatement();
				int queryResult = statement.executeUpdate(UPDATE);
				NomePet.clear();
				PedigreePet.clear();
				PaiPet.clear();
				MaePet.clear();
				SexoPet.getItems().clear();
				RacaPet.getItems().clear();
				SexoPet.setPromptText("Sexo");
				RacaPet.setPromptText("Raça");
				DNPet.getEditor().clear();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		setMsg(MensagemLabel, "Dados atualizados!");
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
