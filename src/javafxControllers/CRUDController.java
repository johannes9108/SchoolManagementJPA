package javafxControllers;

import java.util.ArrayList;
import java.util.List;

import domain.Controller;
import domain.Course;
import domain.Education;
import domain.Student;
import domain.Teacher;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.EntityType;
import model.ListItem;
import ui.SchoolManagementSystemJavaFX;

public class CRUDController implements SubControllerAPI {

	@FXML
	private ChoiceBox<String> entityList;

	private Controller controller;
	private EntityType currentSelection;

	private SchoolManagementSystemJavaFX mainApp;

	private ArrayList<BooleanProperty> toggleBoxes;

	public ArrayList<BooleanProperty> getToggleBoxes() {
		return toggleBoxes;
	}

	@FXML
	private TextField firstNameField;
	@FXML
	private TextField lastNameField;
	@FXML
	private TextField emailField;
	@FXML
	private DatePicker birthDatePicker;

	public void setFirstNameField(TextField firstNameField) {
		this.firstNameField = firstNameField;
	}

	public void setLastNameField(TextField lastNameField) {
		this.lastNameField = lastNameField;
	}

	public void setEmailField(TextField emailField) {
		this.emailField = emailField;
	}

	public void setBirthDatePicker(DatePicker birthDatePicker) {
		this.birthDatePicker = birthDatePicker;
	}

	public void setNameField(TextField nameField) {
		this.nameField = nameField;
	}

	public void setSubjectField(TextField subjectField) {
		this.subjectField = subjectField;
	}

	public void setDifficultyField(TextField difficultyField) {
		this.difficultyField = difficultyField;
	}

	public void setPointsField(TextField pointsField) {
		this.pointsField = pointsField;
	}

	public void setFacultyField(TextField facultyField) {
		this.facultyField = facultyField;
	}

	public void setStartDate(DatePicker startDate) {
		this.startDate = startDate;
	}

	public void setFinalDate(DatePicker finalDate) {
		this.finalDate = finalDate;
	}

	public void setAddButton(Button addButton) {
		this.addButton = addButton;
	}

	public void setClearButton(Button clearButton) {
		this.clearButton = clearButton;
	}

	
	@FXML
	private TextField nameField;
	@FXML
	private TextField subjectField;
	@FXML
	private TextField difficultyField;
	@FXML
	private TextField pointsField;

	@FXML
	private TextField facultyField;
	@FXML
	private DatePicker startDate;
	@FXML
	private DatePicker finalDate;

	private ListView<ListItem> teacherListView;
	private ListView<ListItem> courseListView;
	private ListView<ListItem> educationListView;
	private ListView<ListItem> studentListView;
	
	public ListView<ListItem> getTeacherListView() {
		return teacherListView;
	}

	public ListView<ListItem> getCourseListView() {
		return courseListView;
	}

	public ListView<ListItem> getEducationListView() {
		return educationListView;
	}

	public ListView<ListItem> getStudentListView() {
		return studentListView;
	}

	public void setTeacherListView(ListView<ListItem> teacherListView) {
		this.teacherListView = teacherListView;
	}

	public void setCourseListView(ListView<ListItem> courseListView) {
		this.courseListView = courseListView;
	}

	public void setEducationListView(ListView<ListItem> educationListView) {
		this.educationListView = educationListView;
	}

	public void setStudentListView(ListView<ListItem> studentListView) {
		this.studentListView = studentListView;
	}

	@FXML
	private ListView<String> relationshipListView;
	

	@FXML
	private Button addButton;

	@FXML
	private Button clearButton;

	private Button updateButton;

	private int currentItemId;
	
	public void setMainController(Controller controller) {
		
		toggleBoxes = new ArrayList<>();
		this.controller = controller;
		entityList.getItems().addAll("Teacher", "Course", "Education", "Student");
		entityList.getSelectionModel().select(0);
		currentSelection = EntityType.TEACHER;
		insertCorrectAddView(currentSelection);

		entityList.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				EntityType currentType = convertIndexToEnum(newValue.intValue());
				insertCorrectAddView(currentType);
				currentSelection = currentType;
			}
		});

		
	}

	private void insertCorrectAddView(EntityType type) {
		mainApp.insertCorrectAddView(type);
		
//		pointsField.textProperty().addListener(new ChangeListener<String>() {
//		    @Override
//		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
//		        String newValue) {
//		        if (!newValue.matches("\\d*")) {
//		            pointsField.setText(newValue.replaceAll("[^\\d]", ""));
//		        }
//		    }
//		});
	}

	public void setBiDirectional(SchoolManagementSystemJavaFX schoolManagementSystemJavaFX) {
		mainApp = schoolManagementSystemJavaFX;
	}
	private List<Integer> convertItemsToIDs(ListView<ListItem> tmp){
		List<Integer> result = new ArrayList<Integer>();
		tmp.getSelectionModel().getSelectedItems()
		.forEach(t->result.add(t.getId()));
		return result;
		
	}

	public void handleAdd() {
		
		
		
		int id = -1;

		switch (currentSelection) {
		case TEACHER:
			id = controller.add(new Teacher(firstNameField.getText(), lastNameField.getText(),
					birthDatePicker.getValue(), emailField.getText()));
			if(id>=0) {
				List<Integer> ids = convertItemsToIDs(courseListView);
				controller.associate(currentSelection,id,ids,EntityType.COURSE);
			}
			firstNameField.clear();
			lastNameField.clear();
			birthDatePicker.setValue(null);
			emailField.clear();
			courseListView.getSelectionModel().clearSelection();
			break;
		case COURSE:
			id = controller.add(new Course(nameField.getText(), subjectField.getText(), difficultyField.getText(),
					Integer.parseInt(pointsField.getText())));
			if(id>=0) {
				List<Integer> associationIDs = convertItemsToIDs(teacherListView);
				controller.associate(currentSelection,id,associationIDs,EntityType.TEACHER);
				associationIDs = convertItemsToIDs(educationListView);
				controller.associate(currentSelection,id,associationIDs,EntityType.EDUCATION);
			}
			
			nameField.clear();
			subjectField.clear();
			difficultyField.clear();
			pointsField.clear();
			teacherListView.getSelectionModel().clearSelection();
			educationListView.getSelectionModel().clearSelection();

			break;
		case EDUCATION:
			id = controller.add(new Education(nameField.getText(), facultyField.getText(), startDate.getValue(),
					finalDate.getValue()));
			
			if(id>=0) {
				List<Integer> associationIDs = convertItemsToIDs(courseListView);
				controller.associate(currentSelection,id,associationIDs,EntityType.COURSE);
				associationIDs = convertItemsToIDs(studentListView);
				controller.associate(currentSelection,id,associationIDs,EntityType.STUDENT);
			}
			nameField.clear();
			facultyField.clear();
			startDate.setValue(null);
			finalDate.setValue(null);
			
			courseListView.getSelectionModel().clearSelection();
			studentListView.getSelectionModel().clearSelection();
			
			break;
		case STUDENT:
			id = controller.add(new Student(firstNameField.getText(), lastNameField.getText(),
					birthDatePicker.getValue(), emailField.getText()));
			if(id>=0) {
				List<Integer> indicies = convertItemsToIDs(educationListView);
				controller.associate(currentSelection,id,indicies,EntityType.EDUCATION);
			}
			firstNameField.clear();
			lastNameField.clear();
			birthDatePicker.setValue(null);
			emailField.clear();
			educationListView.getSelectionModel().clearSelection();
			break;
		}
		if (id>=0) {
			controller.refreshLocalData(currentSelection);
			mainApp.report("Success when adding!");
			
			
		} else {
			mainApp.report("FAILURE when adding!!!");
		}
	}

	public void handleClear() {

		switch (currentSelection) {
		case TEACHER:
			firstNameField.clear();
			lastNameField.clear();
			birthDatePicker.setValue(null);
			emailField.clear();
			relationshipListView.getSelectionModel().clearSelection();
			break;
		case COURSE:
			nameField.clear();
			subjectField.clear();
			difficultyField.clear();
			pointsField.clear();
			relationshipListView.getSelectionModel().clearSelection();
			break;
		case EDUCATION:
			nameField.clear();
			facultyField.clear();
			startDate.setValue(null);
			finalDate.setValue(null);
			break;
		case STUDENT:
			firstNameField.clear();
			lastNameField.clear();
			birthDatePicker.setValue(null);
			emailField.clear();
			break;
		}
	}

	public void handleUpdate() {
		
		switch(currentSelection) {
		
		case TEACHER:
			System.out.println("UPDATE");
//		
//			firstNameField.getText();
//			emailField.getText();
//			
//			Teacher teacher = controller.getById(ID, EntityType.TEACHER);
//			controller.associate(currentSelection, id, indicies);
			
			
			break;
		}
		
		
	}

	public void setUpdateButton(Button updateButton) {
		this.updateButton = updateButton;
		updateButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				switch (currentSelection) {

				case TEACHER:
					System.out.println(firstNameField.getText());
					System.out.println(lastNameField.getText());
					System.out.println(birthDatePicker.getValue());
					System.out.println(emailField.getText());
					System.out.println(courseListView.getSelectionModel().getSelectedIndices());
//					Teacher teacher = new Teacher(firstName, lastName, birthDate, email)
					System.out.println("ID: " + currentItemId);
					break;

				case COURSE:

					break;

				case EDUCATION:

					break;

				case STUDENT:

					break;
				}
				
				mainApp.close();


			}
			
		});
	}

	public void setCurrentItemId(int id) {
		this.currentItemId = id;
	}

	
	

}
