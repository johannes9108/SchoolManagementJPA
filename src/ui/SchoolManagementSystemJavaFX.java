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

				default:
					break;
				}
			}

			catch (IOException e) {
				System.out.println("Fel vi inl�sning av FXML-filer");
			}

		});

		tabGroup = tabPane.getTabs();

//		TabPane pane = (TabPane) root.getChildren().get(0);
		Scene scene = new Scene(root, 800, 600);

		loadDisplayTab();

		loadAddTab();

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

				List<ListItem> courses = extractStringFromCollection(EntityType.COURSE);

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
				
				TextField points = ((TextField)((AnchorPane)((VBox) root.getChildrenUnmodifiable().get(0)).getChildren().get(4))
						.getChildren().get(1));
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

				List<ListItem> teachers = extractStringFromCollection(EntityType.TEACHER);

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

				List<ListItem> educations = extractStringFromCollection(EntityType.EDUCATION);

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

				courses = extractStringFromCollection(EntityType.COURSE);

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

				List<ListItem> students = extractStringFromCollection(EntityType.STUDENT);

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

				educations = extractStringFromCollection(EntityType.EDUCATION);

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

	public List<ListItem> extractStringFromCollection(EntityType type) {
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
		searchBirthDateTo.setValue(toDefault);

		TextField searchName = new TextField();
		searchName.setPromptText("Name");
		searchName.setPrefWidth(150);
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
		searchStartDateTo.setValue(toDefault);
		DatePicker searchFinalDateFrom = new DatePicker();
		searchFinalDateFrom.setPromptText("FinalDate From");
		searchFinalDateFrom.setPrefWidth(150);
		DatePicker searchFinalDateTo = new DatePicker();
		searchFinalDateTo.setPromptText("FinalDate To");
		searchFinalDateTo.setPrefWidth(150);
		searchFinalDateTo.setValue(toDefault);

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

		// COURSES missing from teacher

		TableColumn<Course, Integer> courseId = new TableColumn<>("ID");
		courseId.setCellValueFactory(new PropertyValueFactory<>("id"));
		TableColumn<Course, String> courseName = new TableColumn<>("Name");
		courseName.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumn<Course, String> courseDifficulty = new TableColumn<>("Difficulty");
		courseDifficulty.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
		TableColumn<Course, Integer> coursePoints = new TableColumn<>("Points");
		coursePoints.setCellValueFactory(new PropertyValueFactory<>("points"));
		// TEACHERS/EDUCATIONS missing from Course

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
		// STUDENTS/COURSE missing from Education

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
		// Missing Education

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
			courseTW.getColumns().add(courseDifficulty);
			courseTW.getColumns().add(coursePoints);

			searchItems.put("ID", searchID);
			searchItems.put("Name", searchName);
			searchItems.put("Difficulty", searchDifficulty);
			searchItems.put("PointsFrom", searchPointsFrom);
			searchItems.put("PointsTo", searchPointsTo);

			sideControllChildren.add(searchID);
			sideControllChildren.add(searchName);
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

		Student student = new Student("Johannes", "Döpare", LocalDate.of(1999, 01, 31), "johannes@gmail.com");
		Student student1 = new Student("Nicolas", "Johansson", LocalDate.of(1987, 03, 17), "nicolas@gmail.com");
		Student student2 = new Student("Robert", "Drönare", LocalDate.of(1983, 12, 25), "rob@gmail.com");

		em.persist(student);
		em.persist(student1);
		em.persist(student2);

		Teacher t1 = new Teacher("Bita", "Jabbari", LocalDate.of(1980, 07, 13), "bita@gmail.com");
		Teacher t2 = new Teacher("Ulf", "Snulf ", LocalDate.of(1913, 07, 13), "ulf@gmail.com");
		Teacher t3 = new Teacher("Bita", "Jabbari", LocalDate.of(1980, 07, 13), "bita@gmail.com");
		Teacher t4 = new Teacher("Ulf", "Snulf ", LocalDate.of(1913, 07, 13), "ulf@gmail.com");
		Teacher t5 = new Teacher("Bita", "Jabbari", LocalDate.of(1980, 07, 13), "bita@gmail.com");
		Teacher t6 = new Teacher("Ulf", "Snulf ", LocalDate.of(1913, 07, 13), "ulf@gmail.com");

		em.persist(t1);
		em.persist(t2);
		em.persist(t3);
		em.persist(t4);
		em.persist(t5);
		em.persist(t6);

		Course c1 = new Course("Spanska A", "Språk", "junior", 5);
		Course c2 = new Course("Spanska B", "Språk", "senior", 10);

		em.persist(c1);
		em.persist(c2);

		Education e1 = new Education("Språkexpert", "Språkvetenskap", LocalDate.of(1999, 01, 31),
				LocalDate.of(1999, 02, 28));
		Education e2 = new Education("Javaexpert", "Coding", LocalDate.of(1999, 01, 31), LocalDate.of(1999, 02, 28));

		em.persist(e1);
		em.persist(e2);

		t1.addCourse(c1);
		t1.addCourse(c2);

		t2.addCourse(c1);

		e1.addCourse(c2);

		e2.addCourse(c2);
		e2.addCourse(c1);

		e1.addStudent(student);
		e1.addStudent(student1);

		e2.addStudent(student1);
		e2.addStudent(student2);

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
				relatedNames.add(t.getFirstName() + " " +  t.getLastName());
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
					System.out.println("ANROPA Associate med valda v�rden");
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

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TeacherForm.fxml"));
		loader.setController(crudController);
		loader.load();
		AnchorPane ap = (AnchorPane) loader.getRoot();
		vbox.getChildren().add(ap);

		loader = new FXMLLoader(getClass().getResource("/fxml/RelationshipMap.fxml"));
		loader.setController(crudController);
		loader.load();
		BorderPane bp = (BorderPane) loader.getRoot();
		vbox.getChildren().add(bp);
		ObservableList<Node> formItems = ap.getChildren();
		ObservableList<Node> relationshipItems = bp.getChildren();
		VBox vboxContainer = ((VBox) formItems.get(0));
		Label formLabel = (Label) vboxContainer.getChildren().get(0);
		Label relationshipLabel = (Label) relationshipItems.get(0);

		ListView lw = (ListView) relationshipItems.get(1);
		lw.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		switch (type) {
		case TEACHER:
			Teacher tmpTeacher = (Teacher) item;
			formLabel.setText("Teacher");
			TextField firstName = ((TextField) ((AnchorPane) vboxContainer.getChildren().get(1)).getChildren().get(1));
					firstName.setText(tmpTeacher.getFirstName());
			TextField lastName = ((TextField) ((AnchorPane) vboxContainer.getChildren().get(2)).getChildren().get(1));
					lastName.setText(tmpTeacher.getLastName());
			DatePicker birthDate = ((DatePicker) ((AnchorPane) vboxContainer.getChildren().get(3)).getChildren().get(1));
					birthDate.setValue(tmpTeacher.getBirthDate());
			TextField email = ((TextField) ((AnchorPane) vboxContainer.getChildren().get(4)).getChildren().get(1));
					email.setText(tmpTeacher.getEmail());

			relationshipLabel.setText("Courses");

			List<ListItem> courses = extractStringFromCollection(EntityType.COURSE);
			lw.getItems().addAll(courses);
			ArrayList<Integer> selectedIndicies = extractIndiciesFromCollection(tmpTeacher.getCourses(), courses,
					EntityType.TEACHER);
			
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

			break;

		case EDUCATION:

			break;

		case STUDENT:

			break;
		}

		Button updateButton = new Button("Update");
		crudController.setUpdateButton(updateButton);
		vbox.getChildren().add(updateButton);

		Scene popupScene = new Scene(vbox);
		
		popupStage.setScene(popupScene);
		popupStage.show();

	}

	private ArrayList<Integer> extractIndiciesFromCollection(List<?> relationshipCollection, List<ListItem> fullList,
			EntityType type) {
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
				while(fullItr.hasNext()) {
					ListItem li = fullItr.next();
					
					if(li.getId() == c.getId()) {
						System.out.println("Index: " + n);
						System.out.println("Match: " + c.getName() + " with ID: " + c.getId());
						indicies.add(n);
						
					}
					
					n++;
				}

			}
			System.out.println();
			break;
		case COURSE:

			break;
		case EDUCATION:

			break;
		case STUDENT:

			break;

		default:
			break;
		}

		return indicies;

	}

	public void close() {
		if(popupStage!=null) {
			popupStage.close();
			
		}
	}

}
