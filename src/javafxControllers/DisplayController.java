package javafxControllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import domain.Controller;
import domain.Course;
import domain.Education;
import domain.Student;
import domain.Teacher;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import model.EntityType;
import ui.SchoolManagementSystemJavaFX;

public class DisplayController implements SubControllerAPI {

	@FXML
	private ChoiceBox<String> entityList;

	private Controller controller;
	private EntityType currentSelection;

	private HashMap<String, String> currentFilterMap;

	@FXML
	private TableView<?> tw;

	@FXML
	private Button filterButton;

	ObservableMap<String, Node> searchItems;


	private SchoolManagementSystemJavaFX mainApp;

	public void setMainController(Controller controller) {
		this.controller = controller;
		entityList.getItems().addAll("Teacher", "Course", "Education", "Student");
		entityList.getSelectionModel().select(0);
		currentSelection = EntityType.TEACHER;
		insertCorrectDisplayView(currentSelection);
		populate(currentSelection, null);

		entityList.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				EntityType currentType = convertIndexToEnum(newValue.intValue());
				insertCorrectDisplayView(currentType);
				currentSelection = currentType;
				populate(currentSelection, null);
				System.out.println("CurrentSelection=="+currentType);
			}
		});

		tw.setRowFactory(tw -> {
			TableRow row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && (!row.isEmpty())&& currentSelection!=EntityType.STUDENT) {
					mainApp.displayFullInfo(currentSelection, row.getItem());
				}
				if (event.getButton() == MouseButton.SECONDARY) {
					createContextMenu(currentSelection, row,
							row.getParent().getScene().getWindow().getX() + event.getSceneX() + 5,
							row.getParent().getScene().getWindow().getY() + event.getSceneY() + 55);
				}

			});

			return row;
		});

	}

	public void populate(EntityType type, HashMap<String, String> filterMap) {
		tw.getItems().clear();
		List<?> collection = (List<?>) controller.getAll(currentSelection);
		ObservableList<?> displayList = tw.getItems();
		ArrayList tmp = null;


		System.out.println(filterMap);
		if (filterMap != null && !filterMap.isEmpty()) {
			currentFilterMap = filterMap;
			switch (type) {
			case TEACHER:
				System.out.println("Inne");
				Stream<Teacher> teachers = (Stream<Teacher>) collection.stream();
				tmp = (ArrayList) teachers.filter(t -> {
					System.out.println("Checking teacher " + t.getFirstName());

					if (filterMap.containsKey("ID")) {
//						System.out.println("ID");
						if (filterMap.get("ID").compareTo(t.getId() + "") != 0) {
							return false;
						}
					}
					if (filterMap.containsKey("FirstName")) {
						if (!(t.getFirstName().toLowerCase().contains(filterMap.get("FirstName").toLowerCase()))) {
							return false;
						}
						System.out.println("FirstName: " + filterMap.get("FirstName") + ":" + t.getFirstName());
					}
					if (filterMap.containsKey("LastName")) {
						if (!(t.getLastName().toLowerCase().contains(filterMap.get("LastName").toLowerCase()))) {
							return false;
						}
						System.out.println("LastName: " + filterMap.get("LastName") + ":" + t.getLastName());
					}
					if (filterMap.containsKey("Email")) {
						if (!(t.getEmail().contains(filterMap.get("Email").toLowerCase()))) {
							return false;
						}
						System.out.println("Email: " + filterMap.get("Email") + ":" + t.getEmail());
					}
					LocalDate max = LocalDate.MAX, min = LocalDate.MIN;

					if (filterMap.containsKey("BirthDateFrom")) {
						System.out.println("BirthDateFrom: " + filterMap.get("BirthDateFrom") + ":" + t.getBirthDate());
						min = LocalDate.parse(filterMap.get("BirthDateFrom"));

					}
					if (filterMap.containsKey("BirthDateTo")) {
						System.out.println("BirthDateTo: " + filterMap.get("BirthDateTo") + ":" + t.getBirthDate());
						max = LocalDate.parse(filterMap.get("BirthDateTo"));

					}
					if (!(max.isAfter(t.getBirthDate()) && min.isBefore(t.getBirthDate()))) {
						return false;
					}

					return true;

				}).collect(Collectors.toList());
				break;
			case COURSE:
				System.out.println("Inne");
				Stream<Course> courses = (Stream<Course>) collection.stream();
				tmp = (ArrayList) courses.filter(t -> {

					if (filterMap.containsKey("ID")) {
						System.out.println("ID");
						if (filterMap.get("ID").compareTo(t.getId() + "") != 0) {
							return false;
						}
					}
					if (filterMap.containsKey("Name")) {
						if (!(t.getName().toLowerCase().contains(filterMap.get("Name").toLowerCase()))) {
							return false;
						}
						System.out.println("Name: " + filterMap.get("Name") + ":" + t.getName());
					}
					if (filterMap.containsKey("Subject")) {
						if (!(t.getName().toLowerCase().contains(filterMap.get("Subject").toLowerCase()))) {
							return false;
						}
						System.out.println("Subject: " + filterMap.get("Subject") + ":" + t.getName());
					}
					if (filterMap.containsKey("Difficulty")) {
						if (!(t.getDifficulty().toLowerCase().contains(filterMap.get("Difficulty").toLowerCase()))) {
							return false;
						}
						System.out.println("Difficulty: " + t.getDifficulty());
					}
					int max = Integer.MAX_VALUE, min = Integer.MIN_VALUE;

					if (filterMap.containsKey("PointsFrom")) {
						System.out.println("PointsFrom: " + filterMap.get("PointsFrom") + ":" + t.getPoints());
						min = Integer.parseInt(filterMap.get("PointsFrom"));

					}
					if (filterMap.containsKey("PointsTo")) {
						System.out.println("PointsTo: " + filterMap.get("PointsTo") + ":" + t.getPoints());
						max = Integer.parseInt(filterMap.get("PointsTo"));

					}
					if (!(max >= t.getPoints() && min <= (t.getPoints()))) {
						return false;
					}

					return true;

				}).collect(Collectors.toList());
				break;
			case EDUCATION:
				System.out.println("Inne");
				Stream<Education> educations = (Stream<Education>) collection.stream();
				tmp = (ArrayList) educations.filter(t -> {

					if (filterMap.containsKey("ID")) {
						System.out.println("ID");
						if (filterMap.get("ID").compareTo(t.getId() + "") != 0) {
							return false;
						}
					}
					if (filterMap.containsKey("Name")) {
						if (!(t.getName().toLowerCase().contains(filterMap.get("Name").toLowerCase()))) {
							return false;
						}
						System.out.println("Name: " + filterMap.get("Name") + ":" + t.getName());
					}
					if (filterMap.containsKey("Faculty")) {
						if (!(t.getFaculty().toLowerCase().contains(filterMap.get("Faculty").toLowerCase()))) {
							return false;
						}
						System.out.println("Faculty: " + t.getFaculty());
					}

					LocalDate max = LocalDate.MAX, min = LocalDate.MIN;

					if (filterMap.containsKey("StartDateFrom")) {
						System.out.println("StartDateFrom: " + filterMap.get("StartDateFrom") + ":" + t.getStartDate());
						min = LocalDate.parse(filterMap.get("StartDateFrom"));

					}
					if (filterMap.containsKey("StartDateTo")) {
						System.out.println("StartDateTo: " + filterMap.get("StartDateTo") + ":" + t.getStartDate());
						max = LocalDate.parse(filterMap.get("StartDateTo"));

					}
					if (!(max.isAfter(t.getStartDate()) && min.isBefore(t.getStartDate()))) {
						return false;
					}

					max = LocalDate.MAX;
					min = LocalDate.MIN;

					if (filterMap.containsKey("FinalDateFrom")) {
						System.out.println("FinalDateFrom: " + filterMap.get("FinalDateFrom") + ":" + t.getFinalDate());
						min = LocalDate.parse(filterMap.get("FinalDateFrom"));

					}
					if (filterMap.containsKey("FinalDateTo")) {
						System.out.println("FinalDateTo: " + filterMap.get("FinalDateTo") + ":" + t.getFinalDate());
						max = LocalDate.parse(filterMap.get("FinalDateTo"));

					}
					if (!(max.isAfter(t.getFinalDate()) && min.isBefore(t.getFinalDate()))) {
						return false;
					}

					return true;

				}).collect(Collectors.toList());
				break;
			case STUDENT:
				System.out.println("Inne");
				Stream<Student> students = (Stream<Student>) collection.stream();
				tmp = (ArrayList) students.filter(t -> {

					if (filterMap.containsKey("ID")) {
						System.out.println("ID");
						if (filterMap.get("ID").compareTo(t.getId() + "") != 0) {
							return false;
						}
					}
					if (filterMap.containsKey("FirstName")) {
						if (!(t.getFirstName().toLowerCase().contains(filterMap.get("FirstName").toLowerCase()))) {
							return false;
						}
						System.out.println("FirstName: " + filterMap.get("FirstName") + ":" + t.getFirstName());
					}
					if (filterMap.containsKey("LastName")) {
						if (!(t.getLastName().toLowerCase().contains(filterMap.get("LastName").toLowerCase()))) {
							return false;
						}
						System.out.println("LastName: " + t.getLastName());
					}
					if (filterMap.containsKey("Email")) {
						if (!(t.getEmail().contains(filterMap.get("Email").toLowerCase()))) {
							return false;
						}
						System.out.println("Email: " + t.getEmail());
					}
					LocalDate max = LocalDate.MAX, min = LocalDate.MIN;

					if (filterMap.containsKey("BirthDateFrom")) {
						System.out.println("BirthDateFrom: " + filterMap.get("BirthDateFrom") + ":" + t.getBirthDate());
						min = LocalDate.parse(filterMap.get("BirthDateFrom"));

					}
					if (filterMap.containsKey("BirthDateTo")) {
						System.out.println("BirthDateTo: " + filterMap.get("BirthDateTo") + ":" + t.getBirthDate());
						max = LocalDate.parse(filterMap.get("BirthDateTo"));

					}
					if (!(max.isAfter(t.getBirthDate()) && min.isBefore(t.getBirthDate()))) {
						return false;
					}

					return true;

				}).collect(Collectors.toList());
				break;

			default:
				break;
			}

		} else {
			tmp = new ArrayList<>(collection);
		}
		tw.getItems().addAll(tmp);
	}

	private void insertCorrectDisplayView(EntityType type) {
		mainApp.insertCorrectDisplayView(type);
	}

	public void setBiDirectional(SchoolManagementSystemJavaFX schoolManagementSystemJavaFX) {
		mainApp = schoolManagementSystemJavaFX;
	}

	public void handleFiltering() {
		HashMap<String, String> activeSearchItems = new HashMap<>();

		switch (currentSelection) {

		case TEACHER:
			TextField tfCheck = (TextField) searchItems.get("ID");
			if (!tfCheck.getText().isEmpty())
				activeSearchItems.put("ID", tfCheck.getText());

			tfCheck = (TextField) searchItems.get("FirstName");
			if (!tfCheck.getText().isEmpty())
				activeSearchItems.put("FirstName", tfCheck.getText());

			tfCheck = (TextField) searchItems.get("LastName");
			if (!tfCheck.getText().isEmpty())
				activeSearchItems.put("LastName", tfCheck.getText());

			tfCheck = (TextField) searchItems.get("Email");
			if (!tfCheck.getText().isEmpty())
				activeSearchItems.put("Email", tfCheck.getText());

			DatePicker datePicker = (DatePicker) searchItems.get("BirthDateFrom");
			if (datePicker.getValue() != null)
				activeSearchItems.put("BirthDateFrom", datePicker.getValue().toString());

			datePicker = (DatePicker) searchItems.get("BirthDateTo");
			if (datePicker.getValue() != null)
				activeSearchItems.put("BirthDateTo", datePicker.getValue().toString());

			populate(currentSelection, activeSearchItems);

			break;

		case COURSE:

			tfCheck = (TextField) searchItems.get("ID");
			if (!tfCheck.getText().isEmpty())
				activeSearchItems.put("ID", tfCheck.getText());

			tfCheck = (TextField) searchItems.get("Name");
			if (!tfCheck.getText().isEmpty())
				activeSearchItems.put("Name", tfCheck.getText());
			
			tfCheck = (TextField) searchItems.get("Subject");
			if (!tfCheck.getText().isEmpty())
				activeSearchItems.put("Subject", tfCheck.getText());

			tfCheck = (TextField) searchItems.get("Difficulty");
			if (!tfCheck.getText().isEmpty())
				activeSearchItems.put("Difficulty", tfCheck.getText());

			tfCheck = (TextField) searchItems.get("PointsFrom");
			if (!tfCheck.getText().isEmpty())
				activeSearchItems.put("PointsFrom", tfCheck.getText());

			tfCheck = (TextField) searchItems.get("PointsTo");
			if (!tfCheck.getText().isEmpty())
				activeSearchItems.put("PointsTo", tfCheck.getText());

			populate(currentSelection, activeSearchItems);
			break;

		case EDUCATION:
			tfCheck = (TextField) searchItems.get("ID");
			if (!tfCheck.getText().isEmpty())
				activeSearchItems.put("ID", tfCheck.getText());

			tfCheck = (TextField) searchItems.get("Name");
			if (!tfCheck.getText().isEmpty())
				activeSearchItems.put("Name", tfCheck.getText());

			tfCheck = (TextField) searchItems.get("Faculty");
			if (!tfCheck.getText().isEmpty())
				activeSearchItems.put("Faculty", tfCheck.getText());

			datePicker = (DatePicker) searchItems.get("StartDateFrom");
			if (datePicker.getValue() != null)
				activeSearchItems.put("StartDateFrom", datePicker.getValue().toString());

			datePicker = (DatePicker) searchItems.get("StartDateTo");
			if (datePicker.getValue() != null)
				activeSearchItems.put("StartDateTo", datePicker.getValue().toString());

			datePicker = (DatePicker) searchItems.get("FinalDateFrom");
			if (datePicker.getValue() != null)
				activeSearchItems.put("FinalDateFrom", datePicker.getValue().toString());

			datePicker = (DatePicker) searchItems.get("FinalDateTo");
			if (datePicker.getValue() != null)
				activeSearchItems.put("FinalDateTo", datePicker.getValue().toString());

			populate(currentSelection, activeSearchItems);

			break;

		case STUDENT:
			tfCheck = (TextField) searchItems.get("ID");
			if (!tfCheck.getText().isEmpty())
				activeSearchItems.put("ID", tfCheck.getText());

			tfCheck = (TextField) searchItems.get("FirstName");
			if (!tfCheck.getText().isEmpty())
				activeSearchItems.put("FirstName", tfCheck.getText());

			tfCheck = (TextField) searchItems.get("LastName");
			if (!tfCheck.getText().isEmpty())
				activeSearchItems.put("LastName", tfCheck.getText());

			tfCheck = (TextField) searchItems.get("Email");
			if (!tfCheck.getText().isEmpty())
				activeSearchItems.put("Email", tfCheck.getText());

			datePicker = (DatePicker) searchItems.get("BirthDateFrom");
			if (datePicker.getValue() != null)
				activeSearchItems.put("BirthDateFrom", datePicker.getValue().toString());

			datePicker = (DatePicker) searchItems.get("BirthDateTo");
			if (datePicker.getValue() != null)
				activeSearchItems.put("BirthDateTo", datePicker.getValue().toString());

			populate(currentSelection, activeSearchItems);

			break;

		}
	}

	public void setSearchItems(ObservableMap<String, Node> searchItems) {

		this.searchItems = searchItems;
	}

	public void createContextMenu(EntityType currentSelection, TableRow item, double posX, double posY) {
		ContextMenu cm = new ContextMenu();
		MenuItem removeOption = new MenuItem("Remove");
		removeOption.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("Inne i removeOption");
				switch (currentSelection) {

				case TEACHER:
					Teacher removeTeacher = (Teacher) item.getItem();
					int result = controller.removeById(removeTeacher.getId(), currentSelection);
					System.out.println("Teacher with ID: " + result + " has been removed");
					break;
				case COURSE:
					Course removeCourse = (Course) item.getItem();
					result = controller.removeById(removeCourse.getId(), currentSelection);
					System.out.println("Course with ID: " + result + " has been removed");
					break;
				case EDUCATION:
					Education removeEducation = (Education) item.getItem();
					result = controller.removeById(removeEducation.getId(), currentSelection);
					System.out.println("Education with ID: " + result + " has been removed");
					break;
				case STUDENT:
					Student removeStudent = (Student) item.getItem();
					result = controller.removeById(removeStudent.getId(), currentSelection);
					System.out.println("Student with ID: " + result + " has been removed");
					break;

				}
				controller.refreshLocalData(currentSelection);
				populate(currentSelection, currentFilterMap);
			}
		});
		MenuItem updateOption = new MenuItem("Update");
		updateOption.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					mainApp.createUpdatePopup(currentSelection, item.getItem());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		cm.getItems().addAll(removeOption, updateOption);

		cm.show(item.getParent().getScene().getWindow(), posX, posY);
	}

	public void refreshTableView() {
		populate(currentSelection, null);
	}
}
