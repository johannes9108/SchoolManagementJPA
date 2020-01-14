package javafxControllers;

import java.util.ArrayList;
import java.util.List;

import domain.Controller;
import domain.Course;
import domain.Education;
import domain.Student;
import domain.Teacher;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import model.EntityType;
import ui.SchoolManagementSystemJavaFX;

public class DisplayController implements SubControllerAPI{

	@FXML
	private ChoiceBox<String> entityList;

	private Controller controller;
	private EntityType currentSelection;

	@FXML
	private TableView<?> tw;

	private SchoolManagementSystemJavaFX mainApp;

	public void setMainController(Controller controller) {
		this.controller = controller;
		entityList.getItems().addAll("Teacher", "Course", "Education", "Student");
		entityList.getSelectionModel().select(0);
		currentSelection = EntityType.TEACHER;
		insertCorrectDisplayView(currentSelection);
		populate(currentSelection);

		entityList.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				EntityType currentType = convertIndexToEnum(newValue.intValue());
				insertCorrectDisplayView(currentType);
				currentSelection = currentType;
				populate(currentSelection);
			}
		});

		tw.setRowFactory(tw -> {
			TableRow row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					mainApp.displayFullInfo(currentSelection,row.getItem());
				}
			});

			return row;
		});

	}


	public void populate(EntityType type) {
		tw.getItems().clear();
		List<?> collection = (List<?>) controller.getAll(currentSelection);
		ObservableList<?> displayList = tw.getItems();
		ArrayList tmp = new ArrayList<>();
		tmp.addAll(collection);
		tw.getItems().addAll(tmp);

//		List<?> set = controller.getAll(currentSelection);
//		set.forEach(System.out::println);
	}

	private void insertCorrectDisplayView(EntityType type) {
		mainApp.insertCorrectDisplayView(type);
	}

	public void setBiDirectional(SchoolManagementSystemJavaFX schoolManagementSystemJavaFX) {
		mainApp = schoolManagementSystemJavaFX;
	}


	

}
