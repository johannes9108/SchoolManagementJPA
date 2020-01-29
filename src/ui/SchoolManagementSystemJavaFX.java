package ui;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;

import domain.Controller;
import domain.Course;
import domain.Education;
import domain.Student;
import domain.Teacher;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafxControllers.CRUDController;
import javafxControllers.DisplayController;
import model.EntityType;
import model.ListItem;

public class SchoolManagementSystemJavaFX extends Application {

	private AnchorPane root;
	private List<Tab> tabGroup;
	private Stage popupStage;
	private static Controller controller;
	private static CRUDController crudController;
	private static DisplayController displayController;
//	private static UpdateController updateController;

//	private static DeleteController deleteController;

	public static void main(String[] args) {
		setUpData();
		controller = new Controller();
		crudController = new CRUDController();
		displayController = new DisplayController();

		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		popupStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainContainer.fxml"));

		root = loader.load();

		TabPane tabPane = (TabPane) root.getChildren().get(0);

		tabPane.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {

			try {
				switch (newValue.intValue()) {
				case 0:
					loadDisplayTab();
					break;
				case 1:
					loadAddTab();
					break;
				case 2:
					loadStatisticTab();
					break;
				default:
					break;
				}
			}

			catch (IOException e) {
				System.out.println("Fel vi inläsning av FXML-filer");
			}

		});

		tabGroup = tabPane.getTabs();

//		TabPane pane = (TabPane) root.getChildren().get(0);
		Scene scene = new Scene(root, 800, 600);

		loadDisplayTab();

		loadAddTab();
		
		loadStatisticTab();

//		loader = new FXMLLoader(getClass().getResource("/fxml/Statistics.fxml"));
//		sp = loader.load();
//		tabGroup.get(4).setContent(sp);

		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public void loadDeleteTab() throws IOException {
		FXMLLoader loader;
		SplitPane sp;
		loader = new FXMLLoader(getClass().getResource("/fxml/DeleteTab.fxml"));
		sp = loader.load();
		tabGroup.get(3).setContent(sp);
	}

	public void loadUpdateTab() throws IOException {
		FXMLLoader loader;
		SplitPane sp;
		loader = new FXMLLoader(getClass().getResource("/fxml/UpdateTab.fxml"));
		sp = loader.load();
		tabGroup.get(2).setContent(sp);
	}

	public void loadAddTab() throws IOException {
		FXMLLoader loader;
		SplitPane sp;
		loader = new FXMLLoader(getClass().getResource("/fxml/AddTab.fxml"));
		loader.setController(crudController);
		sp = loader.load();
		tabGroup.get(1).setContent(sp);
		crudController.setBiDirectional(this);
		crudController.setMainController(controller);
	}

	public void loadDisplayTab() throws IOException {
		FXMLLoader loader;
		SplitPane sp;
		loader = new FXMLLoader(getClass().getResource("/fxml/TheSchool.fxml"));
		loader.setController(displayController);
		sp = loader.load();
		tabGroup.get(0).setContent(sp);
		displayController.setBiDirectional(this);
		displayController.setMainController(controller);
	}

	public void loadStatisticTab() throws IOException{
		FXMLLoader loader;
		VBox vbox;
		loader = new FXMLLoader(getClass().getResource("/fxml/StatsTab.fxml"));
//		loader.setController(displayController);
		
		vbox = loader.load();
		System.out.println(vbox);
		AnchorPane formAnchorPane;
		TextField formTextField;
		int size;
		
		formAnchorPane = (AnchorPane) vbox.getChildren().get(0);
		formTextField = (TextField) formAnchorPane.getChildren().get(1);
		size = controller.getAll(EntityType.TEACHER).size();
		formTextField.setText(""+size);
		
		formAnchorPane = (AnchorPane) vbox.getChildren().get(1);
		formTextField = (TextField) formAnchorPane.getChildren().get(1);
		size = controller.getAll(EntityType.COURSE).size();
		formTextField.setText(""+size);
		
		formAnchorPane = (AnchorPane) vbox.getChildren().get(2);
		formTextField = (TextField) formAnchorPane.getChildren().get(1);
		size = controller.getAll(EntityType.EDUCATION).size();
		formTextField.setText(""+size);
		
		formAnchorPane = (AnchorPane) vbox.getChildren().get(3);
		formTextField = (TextField) formAnchorPane.getChildren().get(1);
		size = controller.getAll(EntityType.STUDENT).size();
		formTextField.setText(""+size);
		
		tabGroup.get(2).setContent(vbox);
		
//		displayController.setBiDirectional(this);
//		displayController.setMainController(controller);
		
	}

	public void insertCorrectAddView(EntityType type) {
		SplitPane sp = (SplitPane) tabGroup.get(1).getContent();
		AnchorPane ap = (AnchorPane) sp.getItems().get(1);

		ObservableList<Node> formContainer = ap.getChildren();
		formContainer.removeIf(t -> !(t instanceof ButtonBar));
		ButtonBar bb = (ButtonBar) formContainer.get(0);
		formContainer.clear();
		FXMLLoader loader;
		Parent root;

		try {
			switch (type) {
			case TEACHER:
				loader = new FXMLLoader(getClass().getResource("/fxml/TeacherForm.fxml"));
				loader.setController(crudController);
				loader.load();
				root = loader.getRoot();
				((Label) ((VBox) root.getChildrenUnmodifiable().get(0)).getChildren().get(0)).setText("New Teacher");

				HBox hbox = new HBox();
				hbox.getChildren().add(root);

				loader = new FXMLLoader(getClass().getResource("/fxml/RelationshipPicker.fxml"));
				loader.setController(crudController);
				loader.load();
				root = loader.getRoot();
				((Label) root.getChildrenUnmodifiable().get(0)).setText("Courses");

				List<ListItem> courses = extractListItemsFromCollection(EntityType.COURSE);

				ListView<ListItem> lw = ((ListView<ListItem>) root.getChildrenUnmodifiable().get(1));
				lw.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
				lw.getItems().addAll(courses);
				crudController.setCourseListView(lw);
				hbox.getChildren().add(root);
				formContainer.add(hbox);

				break;

			case COURSE:
				loader = new FXMLLoader(getClass().getResource("/fxml/CourseForm.fxml"));
				loader.setController(crudController);
				loader.load();
				root = loader.getRoot();
				((Label) ((VBox) root.getChildrenUnmodifiable().get(0)).getChildren().get(0)).setText("New Course");

				TextField points = ((TextField) ((AnchorPane) ((VBox) root.getChildrenUnmodifiable().get(0))
						.getChildren().get(4)).getChildren().get(1));
				points.textProperty().addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue,
							String newValue) {
						if (!newValue.matches("\\d*")) {
							points.setText(newValue.replaceAll("[^\\d]", ""));
						}
					}
				});

				hbox = new HBox();
				hbox.getChildren().add(root);

				VBox vbox = new VBox();

				loader = new FXMLLoader(getClass().getResource("/fxml/RelationshipPicker.fxml"));
				loader.setController(crudController);
				loader.load();
				root = loader.getRoot();
				((Label) root.getChildrenUnmodifiable().get(0)).setText("Teachers");

				List<ListItem> teachers = extractListItemsFromCollection(EntityType.TEACHER);

				lw = ((ListView<ListItem>) root.getChildrenUnmodifiable().get(1));
				lw.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
				lw.getItems().addAll(teachers);
				crudController.setTeacherListView(lw);

				vbox.getChildren().add(root);

				loader = new FXMLLoader(getClass().getResource("/fxml/RelationshipPicker.fxml"));
				loader.setController(crudController);
				loader.load();
				root = loader.getRoot();
				((Label) root.getChildrenUnmodifiable().get(0)).setText("Educations");

				List<ListItem> educations = extractListItemsFromCollection(EntityType.EDUCATION);

				lw = ((ListView<ListItem>) root.getChildrenUnmodifiable().get(1));
				lw.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
				lw.getItems().addAll(educations);
				crudController.setEducationListView(lw);

				vbox.getChildren().add(root);
				hbox.getChildren().add(vbox);
				formContainer.add(hbox);
				break;

			case EDUCATION:
				loader = new FXMLLoader(getClass().getResource("/fxml/EducationForm.fxml"));
				loader.setController(crudController);
				loader.load();
				root = loader.getRoot();

				((Label) ((VBox) root.getChildrenUnmodifiable().get(0)).getChildren().get(0)).setText("New Education");

				hbox = new HBox();
				hbox.getChildren().add(root);

				vbox = new VBox();

				loader = new FXMLLoader(getClass().getResource("/fxml/RelationshipPicker.fxml"));
				loader.setController(crudController);
				loader.load();
				root = loader.getRoot();
				((Label) root.getChildrenUnmodifiable().get(0)).setText("Courses");

				courses = extractListItemsFromCollection(EntityType.COURSE);

				lw = ((ListView<ListItem>) root.getChildrenUnmodifiable().get(1));
				lw.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
				lw.getItems().addAll(courses);
				crudController.setCourseListView(lw);

				vbox.getChildren().add(root);

				loader = new FXMLLoader(getClass().getResource("/fxml/RelationshipPicker.fxml"));
				loader.setController(crudController);
				loader.load();
				root = loader.getRoot();
				((Label) root.getChildrenUnmodifiable().get(0)).setText("Students");

				List<ListItem> students = extractListItemsFromCollection(EntityType.STUDENT);

				lw = ((ListView<ListItem>) root.getChildrenUnmodifiable().get(1));
				lw.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
				lw.getItems().addAll(students);
				crudController.setStudentListView(lw);

				vbox.getChildren().add(root);
				hbox.getChildren().add(vbox);
				formContainer.add(hbox);
				break;

			case STUDENT:
				loader = new FXMLLoader(getClass().getResource("/fxml/StudentForm.fxml"));
				loader.setController(crudController);
				loader.load();

				root = loader.getRoot();
				((Label) ((VBox) root.getChildrenUnmodifiable().get(0)).getChildren().get(0)).setText("New Student");

				hbox = new HBox();
				hbox.getChildren().add(root);

				loader = new FXMLLoader(getClass().getResource("/fxml/RelationshipPicker.fxml"));
				loader.setController(crudController);
				loader.load();
				root = loader.getRoot();
				((Label) root.getChildrenUnmodifiable().get(0)).setText("Education");

				educations = extractListItemsFromCollection(EntityType.EDUCATION);

				lw = ((ListView<ListItem>) root.getChildrenUnmodifiable().get(1));
				lw.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
				lw.getItems().addAll(educations);
				crudController.setEducationListView(lw);
				hbox.getChildren().add(root);
				formContainer.add(hbox);

				break;
			}
		} catch (IOException ex) {
			System.out.println("Couldn't find the file");
		}
		formContainer.add(bb);
	}

	public List<ListItem> extractListItemsFromCollection(EntityType type) {
		List<ListItem> convertedCollection = new ArrayList<ListItem>();
		controller.getAll(type).forEach(item -> {
			switch (type) {

			case TEACHER:
				Teacher t = (Teacher) item;
				convertedCollection.add(new ListItem(t.getFirstName() + ":" + t.getLastName(), t.getId()));
				break;
			case COURSE:
				Course c = (Course) item;
				convertedCollection.add(new ListItem(c.getName(), c.getId()));
				break;

			case EDUCATION:
				Education e = (Education) item;
				convertedCollection.add(new ListItem(e.getName(), e.getId()));
				break;

			case STUDENT:
				Student s = (Student) item;
				convertedCollection.add(new ListItem(s.getFirstName() + ":" + s.getLastName(), s.getId()));
				break;
			}

		});
		return convertedCollection;
	}

	public void insertCorrectDisplayView(EntityType type) {
		// This code reaches the TableView
		LocalDate fromDefault = LocalDate.of(1950, 5, 1), toDefault = LocalDate.of(2015, 12, 28);
		SplitPane sp = (SplitPane) tabGroup.get(0).getContent();
		TableView<Teacher> teacherTW = (TableView) ((AnchorPane) sp.getItems().get(1)).getChildren().get(0);
		TableView<Student> studentTW = (TableView) ((AnchorPane) sp.getItems().get(1)).getChildren().get(0);
		TableView<Education> educationTW = (TableView) ((AnchorPane) sp.getItems().get(1)).getChildren().get(0);
		TableView<Course> courseTW = (TableView) ((AnchorPane) sp.getItems().get(1)).getChildren().get(0);

		VBox sideControllVbox = (VBox) ((AnchorPane) sp.getItems().get(0)).getChildren().get(0);
		ObservableMap<String, Node> searchItems = FXCollections.observableMap(new HashMap<>());
		ObservableList<Node> sideControllChildren = sideControllVbox.getChildren();
		searchItems.put("Label", sideControllChildren.get(0));
		searchItems.put("ChoiceBox", sideControllChildren.get(1));

		sideControllChildren
				.removeIf((Node t) -> !(t instanceof Label || t instanceof ChoiceBox<?> || t instanceof Button));

		Button filterButton = (Button) sideControllChildren.remove(2);
		filterButton.setId("filterButton");

		TextField searchID = new TextField();
		searchID.setPromptText("ID");
		searchID.setPrefWidth(150);
		TextField searchFirstName = new TextField();
		searchFirstName.setPromptText("First name");
		searchFirstName.setPrefWidth(150);
		searchFirstName.setId("searchFirstName");
		TextField searchLastName = new TextField();
		searchLastName.setPromptText("Last name");
		searchLastName.setPrefWidth(150);

		TextField searchEmail = new TextField();
		searchEmail.setPromptText("Email");
		searchEmail.setPrefWidth(150);
		DatePicker searchBirthDateFrom = new DatePicker();
		searchBirthDateFrom.setPromptText("BirthDate From");
		searchBirthDateFrom.setPrefWidth(150);
		DatePicker searchBirthDateTo = new DatePicker();
		searchBirthDateTo.setPromptText("BirthDate To");
		searchBirthDateTo.setPrefWidth(150);

		TextField searchName = new TextField();
		searchName.setPromptText("Name");
		searchName.setPrefWidth(150);
		TextField searchSubject = new TextField();
		searchSubject.setPromptText("Subject");
		searchSubject.setPrefWidth(150);
		TextField searchDifficulty = new TextField();
		searchDifficulty.setPromptText("Difficulty");
		searchDifficulty.setPrefWidth(150);

		TextField searchFaculty = new TextField();
		searchFaculty.setPromptText("Faculty");
		searchFaculty.setPrefWidth(150);

		TextField searchPointsFrom = new TextField();
		searchPointsFrom.setPromptText("Points Min");
		searchPointsFrom.setPrefWidth(150);
		TextField searchPointsTo = new TextField();
		searchPointsTo.setPromptText("Points Max");
		searchPointsTo.setPrefWidth(150);

		DatePicker searchStartDateFrom = new DatePicker();
		searchStartDateFrom.setPromptText("StartDate From");
		searchStartDateFrom.setPrefWidth(150);
		DatePicker searchStartDateTo = new DatePicker();
		searchStartDateTo.setPromptText("StartDate To");
		searchStartDateTo.setPrefWidth(150);
		DatePicker searchFinalDateFrom = new DatePicker();
		searchFinalDateFrom.setPromptText("FinalDate From");
		searchFinalDateFrom.setPrefWidth(150);
		DatePicker searchFinalDateTo = new DatePicker();
		searchFinalDateTo.setPromptText("FinalDate To");
		searchFinalDateTo.setPrefWidth(150);

		TextField education = new TextField();
		education.setPromptText("Education");
		education.setPrefWidth(150);

		TableColumn<Teacher, Integer> teacherId = new TableColumn<>("ID");
		teacherId.setCellValueFactory(new PropertyValueFactory<>("id"));
		TableColumn<Teacher, String> teacherFirstName = new TableColumn<>("First Name");
		teacherFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		TableColumn<Teacher, String> teacherLastName = new TableColumn<>("Last Name");
		teacherLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		TableColumn<Teacher, LocalDate> teacherBirthDate = new TableColumn<>("BirthDate");
		teacherBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		TableColumn<Teacher, String> teacherEmail = new TableColumn<>("Email");
		teacherEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

		TableColumn<Course, Integer> courseId = new TableColumn<>("ID");
		courseId.setCellValueFactory(new PropertyValueFactory<>("id"));
		TableColumn<Course, String> courseName = new TableColumn<>("Name");
		courseName.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumn<Course, String> courseSubject = new TableColumn<>("Subject");
		courseSubject.setCellValueFactory(new PropertyValueFactory<>("subject"));
		TableColumn<Course, String> courseDifficulty = new TableColumn<>("Difficulty");
		courseDifficulty.setCellValueFactory(new PropertyValueFactory<>("difficulty"));

		TableColumn<Course, Integer> coursePoints = new TableColumn<>("Points");
		coursePoints.setCellValueFactory(new PropertyValueFactory<>("points"));

		TableColumn<Education, Integer> educationId = new TableColumn<>("ID");
		educationId.setCellValueFactory(new PropertyValueFactory<>("id"));
		TableColumn<Education, String> educationName = new TableColumn<>("Name");
		educationName.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumn<Education, String> educationFaculty = new TableColumn<>("Faculty");
		educationFaculty.setCellValueFactory(new PropertyValueFactory<>("faculty"));
		TableColumn<Education, LocalDate> educationStartDate = new TableColumn<>("StartDate");
		educationStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
		TableColumn<Education, LocalDate> educationFinalDate = new TableColumn<>("FinalDate");
		educationFinalDate.setCellValueFactory(new PropertyValueFactory<>("finalDate"));

		TableColumn<Student, Integer> studentId = new TableColumn<>("ID");
		studentId.setCellValueFactory(new PropertyValueFactory<>("id"));
		TableColumn<Student, String> studentFirstName = new TableColumn<>("First Name");
		studentFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		TableColumn<Student, String> studentLastName = new TableColumn<>("Last Name");
		studentLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		TableColumn<Student, LocalDate> studentBirthDate = new TableColumn<>("Birth Date");
		studentBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		TableColumn<Student, String> studentEmail = new TableColumn<>("Email");
		studentEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		TableColumn<Student, String> studentEducation = new TableColumn<>("Education");
		studentEducation.setCellValueFactory(new PropertyValueFactory<>("education"));

		switch (type) {
		case TEACHER: // Teacher
			teacherTW.getColumns().clear();
			teacherTW.getColumns().add(teacherId);
			teacherTW.getColumns().add(teacherFirstName);
			teacherTW.getColumns().add(teacherLastName);
			teacherTW.getColumns().add(teacherEmail);
			teacherTW.getColumns().add(teacherBirthDate);

			searchItems.put("ID", searchID);
			searchItems.put("FirstName", searchFirstName);
			searchItems.put("LastName", searchLastName);
			searchItems.put("Email", searchEmail);
			searchItems.put("BirthDateFrom", searchBirthDateFrom);
			searchItems.put("BirthDateTo", searchBirthDateTo);

			sideControllChildren.add(searchID);
			sideControllChildren.add(searchFirstName);
			sideControllChildren.add(searchLastName);
			sideControllChildren.add(searchEmail);
			sideControllChildren.add(searchBirthDateFrom);
			sideControllChildren.add(searchBirthDateTo);

			break;
		case COURSE: // Course
			courseTW.getColumns().clear();
			courseTW.getColumns().add(courseId);
			courseTW.getColumns().add(courseName);
			courseTW.getColumns().add(courseSubject);
			courseTW.getColumns().add(courseDifficulty);
			courseTW.getColumns().add(coursePoints);

			searchItems.put("ID", searchID);
			searchItems.put("Name", searchName);
			searchItems.put("Subject", searchSubject);
			searchItems.put("Difficulty", searchDifficulty);
			searchItems.put("PointsFrom", searchPointsFrom);
			searchItems.put("PointsTo", searchPointsTo);

			sideControllChildren.add(searchID);
			sideControllChildren.add(searchName);
			sideControllChildren.add(searchSubject);
			sideControllChildren.add(searchDifficulty);
			sideControllChildren.add(searchPointsFrom);
			sideControllChildren.add(searchPointsTo);
			break;
		case EDUCATION: // Education
			educationTW.getColumns().clear();
			educationTW.getColumns().add(educationId);
			educationTW.getColumns().add(educationName);
			educationTW.getColumns().add(educationFaculty);
			educationTW.getColumns().add(educationStartDate);
			educationTW.getColumns().add(educationFinalDate);

			searchItems.put("ID", searchID);
			searchItems.put("Name", searchName);
			searchItems.put("Faculty", searchFaculty);
			searchItems.put("StartDateFrom", searchStartDateFrom);
			searchItems.put("StartDateTo", searchStartDateTo);
			searchItems.put("FinalDateFrom", searchFinalDateFrom);
			searchItems.put("FinalDateTo", searchFinalDateTo);

			sideControllChildren.add(searchID);
			sideControllChildren.add(searchName);
			sideControllChildren.add(searchFaculty);
			sideControllChildren.add(searchStartDateFrom);
			sideControllChildren.add(searchStartDateTo);
			sideControllChildren.add(searchFinalDateFrom);
			sideControllChildren.add(searchFinalDateTo);
			break;

		case STUDENT: // Student
			studentTW.getColumns().clear();
			studentTW.getColumns().add(studentId);
			studentTW.getColumns().add(studentFirstName);
			studentTW.getColumns().add(studentLastName);
			studentTW.getColumns().add(studentEmail);
			studentTW.getColumns().add(studentBirthDate);
			studentTW.getColumns().add(studentEducation);

			searchItems.put("ID", searchID);
			searchItems.put("FirstName", searchFirstName);
			searchItems.put("LastName", searchLastName);
			searchItems.put("Email", searchEmail);
			searchItems.put("BirthDateFrom", searchBirthDateFrom);
			searchItems.put("BirthDateTo", searchBirthDateTo);

			sideControllChildren.add(searchID);
			sideControllChildren.add(searchFirstName);
			sideControllChildren.add(searchLastName);
			sideControllChildren.add(searchEmail);
			sideControllChildren.add(searchBirthDateFrom);
			sideControllChildren.add(searchBirthDateTo);
			break;
		}

		searchItems.put("filterButton", filterButton);
		sideControllChildren.add(filterButton);
		displayController.setSearchItems(searchItems);
	}

	public static void setUpData() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		// Students owned by Education

		Student student1 = new Student("Johannes", "Döpare", LocalDate.of(1980, 5, 31), "johannes@gmail.com");
		Student student2 = new Student("Nicolas", "Loggins", LocalDate.of(1965, 3, 17), "nicolas@gmail.com");
		Student student3 = new Student("Robert", "Dräpare", LocalDate.of(1983, 12, 25), "rober@gmail.com");
		Student student4 = new Student("Anna", "Andersson", LocalDate.of(1990, 3, 4), "anna@gmail.com");
		Student student5 = new Student("John", "Stig", LocalDate.of(1987, 2, 27), "john@gmail.com");
		Student student6 = new Student("Lukas", "Myren", LocalDate.of(1955, 4, 20), "lukas@gmail.com");
		Student student7 = new Student("Sofia", "Hansen", LocalDate.of(2005, 3, 3), "sofia@gmail.com");
		Student student8 = new Student("Zara", "Larsson", LocalDate.of(1930, 10, 12), "zara@gmail.com");
		Student student9 = new Student("Wilma", "Jackson", LocalDate.of(1966, 12, 29), "wilma@gmail.com");

		// Teachers owned by Courses
		Teacher teacher1 = new Teacher("Bita", "Jabbari", LocalDate.of(1968, 9, 13), "bita@gmail.com");
		Teacher teacher2 = new Teacher("Ulf", "Bilting", LocalDate.of(1955, 8, 14), "ulf@gmail.com");
		Teacher teacher3 = new Teacher("Ali", "Hemez", LocalDate.of(1976, 7, 15), "ali@gmail.com");
		Teacher teacher4 = new Teacher("Nypan", "Hök", LocalDate.of(1945, 6, 16), "nypan@gmail.com");
		Teacher teacher5 = new Teacher("Wim", "Koppear", LocalDate.of(1988, 5, 17), "wim@gmail.com");
		Teacher teacher6 = new Teacher("Kesse", "Mannez", LocalDate.of(1995, 4, 18), "kesse@gmail.com");
		Teacher teacher7 = new Teacher("Siv", "Namme", LocalDate.of(1950, 3, 19), "siv@gmail.com");
		Teacher teacher8 = new Teacher("Linda", "Inton", LocalDate.of(1960, 2, 20), "linda@gmail.com");
		Teacher teacher9 = new Teacher("Tomas", "Safari", LocalDate.of(1970, 1, 21), "tomas@gmail.com");

		// Courses own both Education/Teachers
		Course course1 = new Course("Spanska A", "SprÃ¥k", "junior", 5);
		Course course2 = new Course("Spanska B", "SprÃ¥k", "senior", 10);
		Course course3 = new Course("Engelska A", "SprÃ¥k", "junior", 5);
		Course course4 = new Course("Matematik C", "SprÃ¥k", "senior", 10);
		Course course5 = new Course("Filosofi GrundKurs", "SprÃ¥k", "junior", 5);
		Course course6 = new Course("Biologi B", "SprÃ¥k", "senior", 10);

		// Education owns students but are owned by Course
		Education education1 = new Education("Språkvetare", "Språkvetenskap", LocalDate.of(2015, 01, 31),
				LocalDate.of(2019, 7, 1));
		Education education2 = new Education("Systemutvecklare", "Coding", LocalDate.of(2016, 01, 31),
				LocalDate.of(2018, 6, 28));
		Education education3 = new Education("Naturvetare", "Naturvetenskap", LocalDate.of(2017, 01, 31),
				LocalDate.of(2022, 6, 25));
		Education education4 = new Education("Lektor", "Humaniora", LocalDate.of(2016, 01, 31),
				LocalDate.of(2020, 6, 28));

		education1.addStudent(student1);
		education1.addStudent(student2);
		education2.addStudent(student3);
		education2.addStudent(student4);
		education3.addStudent(student5);
		education3.addStudent(student6);
		education4.addStudent(student7);
		education4.addStudent(student8);

		// THESE ARENT NEEDED SINCE COURSE HAS CASCADEPERSIST
//		em.persist(education1);
//		em.persist(education2);
//		em.persist(education3);
//		em.persist(education4);

		em.persist(student9);

		teacher1.addCourse(course1);
		teacher1.addCourse(course2);
		teacher1.addCourse(course3);
		teacher1.addCourse(course4);

		teacher2.addCourse(course3);
		teacher2.addCourse(course4);
		teacher2.addCourse(course5);

		teacher3.addCourse(course6);
		teacher4.addCourse(course2);
		teacher5.addCourse(course4);
		teacher6.addCourse(course5);
		teacher7.addCourse(course6);

		teacher8.addCourse(course6);
		teacher8.addCourse(course5);
		teacher8.addCourse(course1);

		teacher9.addCourse(course6);
		teacher9.addCourse(course2);

		// Connect educations with course so they'll be persisted along with the courses

		course1.addEducation(education1);
		course1.addEducation(education2);
		course1.addEducation(education3);

		course2.addEducation(education2);
		course2.addEducation(education3);
		course2.addEducation(education4);

		course3.addEducation(education1);
		course4.addEducation(education4);

		course5.addEducation(education3);
		course6.addEducation(education1);
		course5.addEducation(education1);
		course6.addEducation(education3);

		em.persist(course1);
		em.persist(course2);
		em.persist(course3);
		em.persist(course4);
		em.persist(course5);
		em.persist(course6);

		tx.commit();
	}

	public <T> void displayFullInfo(EntityType currentSelection, T item) {
		List<String> relatedNames = new ArrayList<String>();
		switch (currentSelection) {
		case TEACHER:
			relatedNames.add("COURSES");
			for (Course c : ((Teacher) item).getCourses()) {
				relatedNames.add(c.getName());
			}
			break;
		case COURSE:
			relatedNames.add("TEACHERS");
			for (Teacher t : ((Course) item).getTeachers()) {
				relatedNames.add(t.getFirstName() + " " + t.getLastName());
			}
			relatedNames.add("EDUCATIONS");
			for (Education e : ((Course) item).getEducations()) {
				relatedNames.add(e.getName());
			}
			break;
		case EDUCATION:
			relatedNames.add("COURSES");
			for (Course c : ((Education) item).getCourses()) {
				relatedNames.add(c.getName());
			}
			relatedNames.add("STUDENTS");
			for (Student s : ((Education) item).getStudents()) {
				relatedNames.add(s.getFirstName() + " " + s.getLastName());
			}
			break;
		case STUDENT:
			break;
		}

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RelationshipMap.fxml"));
			BorderPane popUpRoot = loader.load();
			ListView<String> lw = (ListView<String>) popUpRoot.getChildren().get(1);
			lw.getItems().addAll(relatedNames);
			popupStage.setScene(new Scene(popUpRoot));
			popupStage.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public <T> void displayAssociateView(EntityType currentSelection, T item) {
		List<String> relatedNames = new ArrayList<String>();
		switch (currentSelection) {
		case TEACHER:
			relatedNames.add("COURSES");
			for (Course c : ((Teacher) item).getCourses()) {
				relatedNames.add(c.getName());
			}
			break;
		case COURSE:
			relatedNames.add("EDUCATIONS");
			for (Education e : ((Course) item).getEducations()) {
				relatedNames.add(e.getName());
			}
			break;
		case EDUCATION:
			relatedNames.add("COURSES");
			for (Course c : ((Education) item).getCourses()) {
				relatedNames.add(c.getName());
			}
			relatedNames.add("STUDENTS");
			for (Student s : ((Education) item).getStudents()) {
				relatedNames.add(s.getFirstName() + "" + s.getLastName());
			}
			break;
		case STUDENT:
			break;
		}

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RelationshipMap.fxml"));
			BorderPane popUpRoot = loader.load();
			ListView<String> lw = (ListView<String>) popUpRoot.getChildren().get(1);
			lw.getItems().addAll(relatedNames);
			lw.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

			Button associate = new Button("Associate");
			associate.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
//					controller.associate(currentSelection, id, indicies);
					System.out.println("ANROPA Associate med valda värden");
				}
			});

			popUpRoot.setBottom(associate);

			popupStage.setScene(new Scene(popUpRoot));
			popupStage.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public void report(String message) {
		JOptionPane.showMessageDialog(null, message);
	}

	public <T> void createUpdatePopup(EntityType type, T item) throws Exception {
		popupStage = new Stage();
		VBox vbox = new VBox(10);
		vbox.setAlignment(Pos.CENTER);

		FXMLLoader loader;

		AnchorPane ap;

		BorderPane bp;
		ObservableList<Node> formItems;
		ObservableList<Node> relationshipItems;
		ObservableList<Node> relationshipItems2;
		VBox vboxContainer;
		Label formLabel;
		Label relationshipLabel;
		Label relationshipLabel2;

		ListView lw, lw2;

		switch (type) {
		case TEACHER:
			loader = new FXMLLoader(getClass().getResource("/fxml/TeacherForm.fxml"));
			loader.setController(crudController);
			loader.load();
			ap = (AnchorPane) loader.getRoot();
			vbox.getChildren().add(ap);

			loader = new FXMLLoader(getClass().getResource("/fxml/RelationshipMap.fxml"));
			loader.setController(crudController);
			loader.load();
			bp = (BorderPane) loader.getRoot();
			vbox.getChildren().add(bp);

			formItems = ap.getChildren();
			relationshipItems = bp.getChildren();
			vboxContainer = ((VBox) formItems.get(0));
			formLabel = (Label) vboxContainer.getChildren().get(0);
			relationshipLabel = (Label) relationshipItems.get(0);

			lw = (ListView) relationshipItems.get(1);
			lw.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

			Teacher tmpTeacher = (Teacher) item;
			formLabel.setText("Teacher");
			TextField firstName = ((TextField) ((AnchorPane) vboxContainer.getChildren().get(1)).getChildren().get(1));
			firstName.setText(tmpTeacher.getFirstName());
			TextField lastName = ((TextField) ((AnchorPane) vboxContainer.getChildren().get(2)).getChildren().get(1));
			lastName.setText(tmpTeacher.getLastName());
			DatePicker birthDate = ((DatePicker) ((AnchorPane) vboxContainer.getChildren().get(3)).getChildren()
					.get(1));
			birthDate.setValue(tmpTeacher.getBirthDate());
			TextField email = ((TextField) ((AnchorPane) vboxContainer.getChildren().get(4)).getChildren().get(1));
			email.setText(tmpTeacher.getEmail());

			relationshipLabel.setText("Courses");

			List<ListItem> courses = extractListItemsFromCollection(EntityType.COURSE);
			lw.getItems().addAll(courses);
			ArrayList<Integer> selectedIndicies = extractIndiciesFromCollection(tmpTeacher.getCourses(),
					EntityType.COURSE, courses, type);

			for (Integer integer : selectedIndicies) {
				lw.getSelectionModel().select(integer.intValue());
			}
			crudController.setFirstNameField(firstName);
			crudController.setLastNameField(lastName);
			crudController.setBirthDatePicker(birthDate);
			crudController.setEmailField(email);
			crudController.setCourseListView(lw);
			crudController.setCurrentItemId(tmpTeacher.getId());

			break;

		case COURSE:

			loader = new FXMLLoader(getClass().getResource("/fxml/CourseForm.fxml"));
			loader.setController(crudController);
			loader.load();
			ap = (AnchorPane) loader.getRoot();
			vbox.getChildren().add(ap);

			loader = new FXMLLoader(getClass().getResource("/fxml/RelationshipMap.fxml"));
			loader.setController(crudController);
			loader.load();
			bp = (BorderPane) loader.getRoot();
			vbox.getChildren().add(bp);

			relationshipItems = bp.getChildren();

			loader = new FXMLLoader(getClass().getResource("/fxml/RelationshipMap.fxml"));
			loader.setController(crudController);
			loader.load();
			bp = (BorderPane) loader.getRoot();
			vbox.getChildren().add(bp);

			relationshipItems2 = bp.getChildren();
			formItems = ap.getChildren();

			vboxContainer = ((VBox) formItems.get(0));
			formLabel = (Label) vboxContainer.getChildren().get(0);
			relationshipLabel = (Label) relationshipItems.get(0);
			relationshipLabel2 = (Label) relationshipItems2.get(0);

			lw = (ListView) relationshipItems.get(1);
			lw.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			lw2 = (ListView) relationshipItems2.get(1);
			lw2.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

			Course tmpCourse = (Course) item;
			formLabel.setText("Course");
			TextField name = ((TextField) ((AnchorPane) vboxContainer.getChildren().get(1)).getChildren().get(1));
			name.setText(tmpCourse.getName());
			TextField subject = ((TextField) ((AnchorPane) vboxContainer.getChildren().get(2)).getChildren().get(1));
			subject.setText(tmpCourse.getSubject());
			TextField difficulty = ((TextField) ((AnchorPane) vboxContainer.getChildren().get(3)).getChildren().get(1));
			difficulty.setText(tmpCourse.getDifficulty());
			TextField points = ((TextField) ((AnchorPane) vboxContainer.getChildren().get(4)).getChildren().get(1));
			points.setText("" + tmpCourse.getPoints());

			relationshipLabel.setText("Teachers");

			List<ListItem> teachers = extractListItemsFromCollection(EntityType.TEACHER);
			lw.getItems().addAll(teachers);
			selectedIndicies = extractIndiciesFromCollection(tmpCourse.getTeachers(), EntityType.TEACHER, teachers,
					type);

			for (Integer integer : selectedIndicies) {
				lw.getSelectionModel().select(integer.intValue());
			}

			relationshipLabel2.setText("Educations");
			List<ListItem> educations = extractListItemsFromCollection(EntityType.EDUCATION);
			lw2.getItems().addAll(educations);
			selectedIndicies = extractIndiciesFromCollection(tmpCourse.getEducations(), EntityType.EDUCATION,
					educations, type);

			for (Integer integer : selectedIndicies) {
				lw2.getSelectionModel().select(integer.intValue());
			}
			crudController.setNameField(name);
			crudController.setSubjectField(subject);
			crudController.setDifficultyField(difficulty);
			crudController.setPointsField(points);
			crudController.setTeacherListView(lw);
			crudController.setEducationListView(lw2);
			crudController.setCurrentItemId(tmpCourse.getId());

			break;

		case EDUCATION:

			loader = new FXMLLoader(getClass().getResource("/fxml/EducationForm.fxml"));
			loader.setController(crudController);
			loader.load();
			ap = (AnchorPane) loader.getRoot();
			vbox.getChildren().add(ap);

			loader = new FXMLLoader(getClass().getResource("/fxml/RelationshipMap.fxml"));
			loader.setController(crudController);
			loader.load();
			bp = (BorderPane) loader.getRoot();
			vbox.getChildren().add(bp);

			relationshipItems = bp.getChildren();

			loader = new FXMLLoader(getClass().getResource("/fxml/RelationshipMap.fxml"));
			loader.setController(crudController);
			loader.load();
			bp = (BorderPane) loader.getRoot();
			vbox.getChildren().add(bp);

			relationshipItems2 = bp.getChildren();
			formItems = ap.getChildren();

			vboxContainer = ((VBox) formItems.get(0));
			formLabel = (Label) vboxContainer.getChildren().get(0);
			relationshipLabel = (Label) relationshipItems.get(0);
			relationshipLabel2 = (Label) relationshipItems2.get(0);

			lw = (ListView) relationshipItems.get(1);
			lw.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			lw2 = (ListView) relationshipItems2.get(1);
			lw2.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

			Education tmpEducation = (Education) item;
			formLabel.setText("Education");
			name = ((TextField) ((AnchorPane) vboxContainer.getChildren().get(1)).getChildren().get(1));
			name.setText(tmpEducation.getName());
			TextField faculty = ((TextField) ((AnchorPane) vboxContainer.getChildren().get(2)).getChildren().get(1));
			faculty.setText(tmpEducation.getFaculty());
			DatePicker startDate = ((DatePicker) ((AnchorPane) vboxContainer.getChildren().get(3)).getChildren()
					.get(1));
			startDate.setValue(tmpEducation.getStartDate());
			DatePicker finaDate = ((DatePicker) ((AnchorPane) vboxContainer.getChildren().get(4)).getChildren().get(1));
			finaDate.setValue(tmpEducation.getFinalDate());

			relationshipLabel.setText("Course");

			courses = extractListItemsFromCollection(EntityType.COURSE);
			lw.getItems().addAll(courses);
			selectedIndicies = extractIndiciesFromCollection(tmpEducation.getCourses(), EntityType.COURSE, courses,
					type);

			for (Integer integer : selectedIndicies) {
				lw.getSelectionModel().select(integer.intValue());
			}

			relationshipLabel2.setText("Students");
			List<ListItem> students = extractListItemsFromCollection(EntityType.STUDENT);
			lw2.getItems().addAll(students);
			selectedIndicies = extractIndiciesFromCollection(tmpEducation.getStudents(), EntityType.STUDENT, students,
					type);
			System.out.println(selectedIndicies);
			for (Integer integer : selectedIndicies) {
				lw2.getSelectionModel().select(integer.intValue());
			}
			crudController.setNameField(name);
			crudController.setFacultyField(faculty);
			crudController.setStartDate(startDate);
			crudController.setFinalDate(finaDate);
			crudController.setCourseListView(lw);
			crudController.setStudentListView(lw2);
			crudController.setCurrentItemId(tmpEducation.getId());

			break;

		case STUDENT:
			loader = new FXMLLoader(getClass().getResource("/fxml/StudentForm.fxml"));
			loader.setController(crudController);
			loader.load();
			ap = (AnchorPane) loader.getRoot();
			vbox.getChildren().add(ap);

			
			Label educationLabel = new Label("Education");
			ChoiceBox<ListItem> educationChoiceBox = new ChoiceBox<>();
			educationChoiceBox.setPrefWidth(150);
			crudController.setEducationChoiceBox(educationChoiceBox);

			educations = extractListItemsFromCollection(EntityType.EDUCATION);
			educationChoiceBox.getItems().add(new ListItem("", -1));
			educationChoiceBox.getItems().addAll(educations);
			
			
			vbox.getChildren().add(educationLabel);
			vbox.getChildren().add(educationChoiceBox);

			formItems = ap.getChildren();
			
			vboxContainer = ((VBox) formItems.get(0));
			formLabel = (Label) vboxContainer.getChildren().get(0);

			Student tmpStudent= (Student) item;
			formLabel.setText("Student");
			firstName = ((TextField) ((AnchorPane) vboxContainer.getChildren().get(1)).getChildren().get(1));
				firstName.setText(tmpStudent.getFirstName());
			lastName = ((TextField) ((AnchorPane) vboxContainer.getChildren().get(2)).getChildren().get(1));
				lastName.setText(tmpStudent.getLastName());
			birthDate = ((DatePicker) ((AnchorPane) vboxContainer.getChildren().get(3)).getChildren()
					.get(1));
			birthDate.setValue(tmpStudent.getBirthDate());
			email = ((TextField) ((AnchorPane) vboxContainer.getChildren().get(4)).getChildren().get(1));
				email.setText(tmpStudent.getEmail());
			if(tmpStudent.getEducation()!=null) {
			int index = extractIndexFromID(tmpStudent.getEducation().getId(),educationChoiceBox);
				educationChoiceBox.getSelectionModel().select(index);
			}
			crudController.setFirstNameField(firstName);
			crudController.setLastNameField(lastName);
			crudController.setBirthDatePicker(birthDate);
			crudController.setEmailField(email);
			crudController.setEducationChoiceBox(educationChoiceBox);
			crudController.setCurrentItemId(tmpStudent.getId());
//			Teacher tmpTeacher = (Teacher) item;
//			formLabel.setText("Teacher");
//			TextField firstName = ((TextField) ((AnchorPane) vboxContainer.getChildren().get(1)).getChildren().get(1));
//			firstName.setText(tmpTeacher.getFirstName());
//			TextField lastName = ((TextField) ((AnchorPane) vboxContainer.getChildren().get(2)).getChildren().get(1));
//			lastName.setText(tmpTeacher.getLastName());
//			DatePicker birthDate = ((DatePicker) ((AnchorPane) vboxContainer.getChildren().get(3)).getChildren()
//					.get(1));
//			birthDate.setValue(tmpTeacher.getBirthDate());
//			TextField email = ((TextField) ((AnchorPane) vboxContainer.getChildren().get(4)).getChildren().get(1));
//			email.setText(tmpTeacher.getEmail());
//
//			relationshipLabel.setText("Courses");
//
//			List<ListItem> courses = extractListItemsFromCollection(EntityType.COURSE);
//			lw.getItems().addAll(courses);
//			ArrayList<Integer> selectedIndicies = extractIndiciesFromCollection(tmpTeacher.getCourses(),
//					EntityType.COURSE, courses, type);
//
//			for (Integer integer : selectedIndicies) {
//				lw.getSelectionModel().select(integer.intValue());
//			}
//			crudController.setFirstNameField(firstName);
//			crudController.setLastNameField(lastName);
//			crudController.setBirthDatePicker(birthDate);
//			crudController.setEmailField(email);
//			crudController.setCourseListView(lw);
//			crudController.setCurrentItemId(tmpTeacher.getId());
			
			break;
		}

		crudController.setCurrentSelection(type);
		Button updateButton = new Button("Update");
		crudController.setUpdateButton(updateButton);
		vbox.getChildren().add(updateButton);

		Scene popupScene = new Scene(vbox);

		popupStage.setScene(popupScene);
		popupStage.show();

	}

	private int extractIndexFromID(int id, ChoiceBox<ListItem> educationChoiceBox) {
		int n = 0;
		for (ListItem item : educationChoiceBox.getItems()) {
			if(item.getId() == id) {
				return n;
			}
			n++;
		}
		
		
		return -1;
	}

	private ArrayList<Integer> extractIndiciesFromCollection(List<?> relationshipCollection, EntityType associatedType,
			List<ListItem> fullList, EntityType type) {
		ArrayList<Integer> indicies = new ArrayList<>();

		Iterator<ListItem> fullItr = null;
		switch (type) {
		case TEACHER:
			Iterator<Course> courses = (Iterator<Course>) relationshipCollection.listIterator();
			while (courses.hasNext()) {
				Course c = courses.next();
				System.out.println(c);
				fullItr = fullList.listIterator();
				int n = 0;
				while (fullItr.hasNext()) {
					ListItem li = fullItr.next();

					if (li.getId() == c.getId()) {
//						System.out.println("Index: " + n);
//						System.out.println("Match: " + c.getName() + " with ID: " + c.getId());
						indicies.add(n);

					}

					n++;
				}

			}
			System.out.println();
			break;
		case COURSE:

			switch (associatedType) {

			case TEACHER:
				Iterator<Teacher> teachers = (Iterator<Teacher>) relationshipCollection.listIterator();
				while (teachers.hasNext()) {
					Teacher t = teachers.next();
					System.out.println(t);
					fullItr = fullList.listIterator();
					int n = 0;
					while (fullItr.hasNext()) {
						ListItem li = fullItr.next();

						if (li.getId() == t.getId()) {
							indicies.add(n);

						}

						n++;
					}

				}

				break;
			case EDUCATION:
				Iterator<Education> educations = (Iterator<Education>) relationshipCollection.listIterator();
				while (educations.hasNext()) {
					Education e = educations.next();
					System.out.println(e);
					fullItr = fullList.listIterator();
					int n = 0;
					while (fullItr.hasNext()) {
						ListItem li = fullItr.next();

						if (li.getId() == e.getId()) {
							indicies.add(n);

						}

						n++;
					}

				}
				break;
			}

			System.out.println();
			break;
		case EDUCATION:

			switch (associatedType) {

			case COURSE:
				Iterator<Course> Courses = (Iterator<Course>) relationshipCollection.listIterator();
				while (Courses.hasNext()) {
					Course c = Courses.next();
					System.out.println(c);
					fullItr = fullList.listIterator();
					int n = 0;
					while (fullItr.hasNext()) {
						ListItem li = fullItr.next();

						if (li.getId() == c.getId()) {
							indicies.add(n);

						}

						n++;
					}

				}

				break;
			case STUDENT:
				Iterator<Student> students = (Iterator<Student>) relationshipCollection.listIterator();
				while (students.hasNext()) {
					Student s = students.next();
					System.out.println(s);
					fullItr = fullList.listIterator();
					int n = 0;
					while (fullItr.hasNext()) {
						ListItem li = fullItr.next();

						if (li.getId() == s.getId()) {
							indicies.add(n);

						}

						n++;
					}

				}
				break;
			}

			System.out.println();
			break;
		case STUDENT:
			Iterator<Education> educations = (Iterator<Education>) relationshipCollection.listIterator();
			while (educations.hasNext()) {
				Education e = educations.next();
				System.out.println(e);
				fullItr = fullList.listIterator();
				int n = 0;
				while (fullItr.hasNext()) {
					ListItem li = fullItr.next();

					if (li.getId() == e.getId()) {
						indicies.add(n);
					}

					n++;
				}

			}
			System.out.println();
			break;

		default:
			break;
		}

		return indicies;

	}

	public void close() {
		if (popupStage != null) {
			popupStage.close();

		}
	}

	public void refreshTableView() {
		displayController.refreshTableView();
	}

}
