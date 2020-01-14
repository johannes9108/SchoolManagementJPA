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
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.EntityType;
import ui.SchoolManagementSystemJavaFX;

public class AddController implements SubControllerAPI {

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

	@FXML
	private ListView<String> teacherListView;
	@FXML
	private ListView<String> courseListView;
	@FXML
	private ListView<String> educationListView;
	@FXML
	private ListView<String> studentListView;
	
	@FXML
	private ListView<String> relationshipListView;
	

	@FXML
	private Button addButton;

	@FXML
	private Button clearButton;

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
	private List<Integer> convertItemsToIDs(ListView<String> tmp){
		List<Integer> result = new ArrayList<Integer>();
		tmp.getSelectionModel().getSelectedItems()
		.forEach(t->result.add(extractId(t)));
		
		return result;
		
	}

	private int extractId(String listEntry) {
		String[] listItems = listEntry.split("\tID\t");
		return Integer.parseInt(listItems[1]);
	}
	
	public void handleAdd() {

		if(relationshipListView == null) {
			System.out.println("TLW existerar EJ");
			System.exit(0);
		}
		
		
		int result = -1;

		switch (currentSelection) {
		case TEACHER:
			result = controller.add(new Teacher(firstNameField.getText(), lastNameField.getText(),
					birthDatePicker.getValue(), emailField.getText()));
			if(result>=0) {
				List<Integer> indicies = convertItemsToIDs(relationshipListView);
				controller.associate(currentSelection,result,indicies);
			}
			break;
		case COURSE:
			result = controller.add(new Course(nameField.getText(), subjectField.getText(), difficultyField.getText(),
					Integer.parseInt("1")));
			
//			result = controller.add(new Course(nameField.getText(), subjectField.getText(), difficultyField.getText(),
//					Integer.parseInt(pointsField.getText())));
			break;
		case EDUCATION:
			result = controller.add(new Education(nameField.getText(), facultyField.getText(), startDate.getValue(),
					finalDate.getValue()));
			break;
		case STUDENT:
			result = controller.add(new Student(firstNameField.getText(), lastNameField.getText(),
					birthDatePicker.getValue(), emailField.getText()));
			break;
		}
		if (result>0) {
			controller.refreshLocalData(currentSelection);
			mainApp.report("Success when adding!");
			
			
		} else {
			mainApp.report("FAILURE when adding!!!");
		}
	}

	public void handleClear() {
		System.out.println("Tryckte på CLEAR");

		switch (currentSelection) {
		case TEACHER:
			System.out.println("Clearar Teachers osv");
			break;
		case COURSE:

			break;
		case EDUCATION:

			break;
		case STUDENT:

			break;
		}
	}

	
	

}
