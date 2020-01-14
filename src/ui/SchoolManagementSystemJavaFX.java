package ui;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafxControllers.AddController;
import javafxControllers.DisplayController;
import model.EntityType;

public class SchoolManagementSystemJavaFX extends Application {

	private AnchorPane root;
	private List<Tab> tabGroup;
	private Stage displayInfoPopup;
	private static Controller controller;
	private static AddController addController;
	private static DisplayController displayController;
//	private static UpdateController updateController;

//	private static DeleteController deleteController;

	public static void main(String[] args) {
		setUpData();
		controller = new Controller();
		addController = new AddController();
		displayController = new DisplayController();

		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		displayInfoPopup = new Stage();
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
					loadUpdateTab();
					break;
				case 3:
					loadDeleteTab();
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

		loadUpdateTab();

		loadDeleteTab();

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
		loader.setController(addController);
		sp = loader.load();
		tabGroup.get(1).setContent(sp);
		addController.setBiDirectional(this);
		addController.setMainController(controller);
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
				loader.setController(addController);
				loader.load();
				root = loader.getRoot();
				((Label) ((VBox) root.getChildrenUnmodifiable().get(0)).getChildren().get(0)).setText("New Teacher");

				HBox hbox = new HBox();
				hbox.getChildren().add(root);

				loader = new FXMLLoader(getClass().getResource("/fxml/RelationshipPicker.fxml"));
				loader.setController(addController);
				loader.load();
				root = loader.getRoot();
				((Label) root.getChildrenUnmodifiable().get(0)).setText("Courses");

				List<String> courses = new ArrayList<String>();
				controller.getAll(EntityType.COURSE).forEach(t -> {
					Course tmp = (Course)t;
					courses.add(tmp.getName() + "\tID\t"  + tmp.getId());
					});

				ListView<String> lw = ((ListView<String>) root.getChildrenUnmodifiable().get(1));
				lw.setId("teacherListView");
				lw.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
				lw.getItems().addAll(courses);
				hbox.getChildren().add(root);
				formContainer.add(hbox);

				break;

			case COURSE:
				loader = new FXMLLoader(getClass().getResource("/fxml/CourseForm.fxml"));
				loader.setController(addController);
				loader.load();
				root = loader.getRoot();
				((Label) ((VBox) root.getChildrenUnmodifiable().get(0)).getChildren().get(0)).setText("New Course");

				hbox = new HBox();
				hbox.getChildren().add(root);
				
				VBox vbox = new VBox();

				loader = new FXMLLoader(getClass().getResource("/fxml/RelationshipPicker.fxml"));
				loader.setController(addController);
				loader.load();
				root = loader.getRoot();
				((Label) root.getChildrenUnmodifiable().get(0)).setText("Teachers");

				

				
				
				List<String> teachers = new ArrayList<String>();
				controller.getAll(EntityType.TEACHER).forEach(t -> {
					Teacher tmp = (Teacher)t;
					teachers.add(tmp.getFirstName() +":" + tmp.getLastName() + "\tID\t"  + tmp.getId());
					});

				lw = ((ListView<String>) root.getChildrenUnmodifiable().get(1));
				lw.setId("teacherListView");
				lw.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
				lw.getItems().addAll(teachers);
				
				vbox.getChildren().add(root);
				
				loader = new FXMLLoader(getClass().getResource("/fxml/RelationshipPicker.fxml"));
				loader.setController(addController);
				loader.load();
				root = loader.getRoot();
				((Label) root.getChildrenUnmodifiable().get(0)).setText("Educations");

				List<String> educations = new ArrayList<String>();
				controller.getAll(EntityType.EDUCATION).forEach(t -> {
					Education tmp = (Education)t;
					educations.add(tmp.getName() + "\tID\t"  + tmp.getId());
					System.out.println("Inne i EduLoop: " + t);
					});

				ListView<String> lw2 = ((ListView<String>) root.getChildrenUnmodifiable().get(1));
				lw2.setId("educationListView");
				lw2.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
				lw2.getItems().addAll(educations);
				
				vbox.getChildren().add(root);
				hbox.getChildren().add(vbox);
				formContainer.add(hbox);
				break;

			case EDUCATION:
				loader = new FXMLLoader(getClass().getResource("/fxml/EducationForm.fxml"));
				loader.setController(addController);
				loader.load();
				root = loader.getRoot();
				formContainer.add(root);
				break;

			case STUDENT:
				loader = new FXMLLoader(getClass().getResource("/fxml/StudentForm.fxml"));
				loader.setController(addController);
				loader.load();
				root = loader.getRoot();
				formContainer.add(root);
				break;
			}
		} catch (IOException ex) {
			System.out.println("Couldn't find the file");
		}
		formContainer.add(bb);
	}
	
	public void insertCorrectUpdateView(int currentSelection) {

	}

	public void insertCorrectDisplayView(EntityType type) {
		// This code reaches the TableView
		SplitPane sp = (SplitPane) tabGroup.get(0).getContent();
		TableView<Teacher> teacherTW = (TableView) ((AnchorPane) sp.getItems().get(1)).getChildren().get(0);
		TableView<Student> studentTW = (TableView) ((AnchorPane) sp.getItems().get(1)).getChildren().get(0);
		TableView<Education> educationTW = (TableView) ((AnchorPane) sp.getItems().get(1)).getChildren().get(0);
		TableView<Course> courseTW = (TableView) ((AnchorPane) sp.getItems().get(1)).getChildren().get(0);

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
//		TableColumn<Student, String> studentEducation = new TableColumn<>("Education");
//		studentEducation.setCellValueFactory(new PropertyValueFactory<>("education"));
		// Missing Education

		switch (type) {
		case TEACHER: // Teacher
			teacherTW.getColumns().clear();
			teacherTW.getColumns().add(teacherId);
			teacherTW.getColumns().add(teacherFirstName);
			teacherTW.getColumns().add(teacherLastName);
			teacherTW.getColumns().add(teacherEmail);
			teacherTW.getColumns().add(teacherBirthDate);
			break;
		case COURSE: // Course
			courseTW.getColumns().clear();
			courseTW.getColumns().add(courseId);
			courseTW.getColumns().add(courseName);
			courseTW.getColumns().add(courseDifficulty);
			courseTW.getColumns().add(coursePoints);
			break;
		case EDUCATION: // Education
			educationTW.getColumns().clear();
			educationTW.getColumns().add(educationId);
			educationTW.getColumns().add(educationName);
			educationTW.getColumns().add(educationFaculty);
			educationTW.getColumns().add(educationStartDate);
			educationTW.getColumns().add(educationFinalDate);
			break;

		case STUDENT: // Student
			studentTW.getColumns().clear();
			studentTW.getColumns().add(studentId);
			studentTW.getColumns().add(studentFirstName);
			studentTW.getColumns().add(studentLastName);
			studentTW.getColumns().add(studentBirthDate);
			studentTW.getColumns().add(studentEmail);
//			studentTW.getColumns().add(studentEducation);

			break;
		}

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

		em.persist(t1);
		em.persist(t2);

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
			displayInfoPopup.setScene(new Scene(popUpRoot));
			displayInfoPopup.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public void report(String message) {
		JOptionPane.showMessageDialog(null, message);
	}

}
