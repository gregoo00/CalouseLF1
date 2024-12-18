package view;

import controller.ItemController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.User;

public class UploadItemView {
	
	private Stage stage;
	private ItemController itemController;
	private User seller; 
	private Scene scene;
	
	public UploadItemView(Stage stage, User seller) {
        this.stage = stage;
        this.seller = seller;
        this.itemController = new ItemController();
        initializeUploadView();
    }
	private void initializeUploadView() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(20));
        
        // Header
        VBox headerBox = createHeaderBox();
        borderPane.setTop(headerBox);
        
        // Form
        GridPane formGrid = createFormGrid();
        borderPane.setCenter(formGrid);
        
        scene = new Scene(borderPane, 400, 500);
        stage.setScene(scene);
        stage.setTitle("Upload Item");
        stage.show();
    }
    
    private VBox createHeaderBox() {
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(0, 0, 20, 0));
        
        Label titleLabel = new Label("Upload New Item");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        headerBox.getChildren().add(titleLabel);
        return headerBox;
    }
    
    private GridPane createFormGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        
        // Item Name
        Label nameLabel = new Label("Item Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Min. 3 characters");
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        
        // Category
        Label categoryLabel = new Label("Category:");
        TextField categoryField = new TextField();
        categoryField.setPromptText("Min. 3 characters");
        grid.add(categoryLabel, 0, 1);
        grid.add(categoryField, 1, 1);
        
        // Size
        Label sizeLabel = new Label("Size:");
        ComboBox<String> sizeBox = new ComboBox<>();
        sizeBox.getItems().addAll("XS", "S", "M", "L", "XL", "XXL");
        grid.add(sizeLabel, 0, 2);
        grid.add(sizeBox, 1, 2);
        
        // Price
        Label priceLabel = new Label("Price:");
        TextField priceField = new TextField();
        priceField.setPromptText("Enter price (numbers only)");
        grid.add(priceLabel, 0, 3);
        grid.add(priceField, 1, 3);
        
        // Upload Button
        Button uploadBtn = new Button("Upload Item");
        uploadBtn.setPrefWidth(200);
        uploadBtn.setOnAction(e -> handleUpload(
            nameField.getText(),
            categoryField.getText(),
            sizeBox.getValue(),
            priceField.getText()
        ));
        
        HBox buttonBox = new HBox(uploadBtn);
        buttonBox.setAlignment(Pos.CENTER);
        grid.add(buttonBox, 0, 4, 2, 1);
        
        return grid;
    }
    
    private void handleUpload(String name, String category, String size, String priceStr) {
        // Validasi input
        if(name.isEmpty() || category.isEmpty() || size == null || priceStr.isEmpty()) {
            showAlert("Error", "All fields must be filled!", Alert.AlertType.ERROR);
            return;
        }
        
        // Validasi nama dan kategori
        if(name.length() < 3) {
            showAlert("Error", "Item name must be at least 3 characters long!", Alert.AlertType.ERROR);
            return;
        }
        
        if(category.length() < 3) {
            showAlert("Error", "Category must be at least 3 characters long!", Alert.AlertType.ERROR);
            return;
        }
        
        // Validasi harga
        double price;
        try {
            price = Double.parseDouble(priceStr);
            if(price <= 0) {
                showAlert("Error", "Price must be greater than 0!", Alert.AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Price must be a valid number!", Alert.AlertType.ERROR);
            return;
        }
        
        // Upload item
        if(itemController.uploadItem(seller.getUserId(), name, category, size, price)) {
            showAlert("Success", "Item uploaded successfully! Waiting for admin approval.", Alert.AlertType.INFORMATION);
            stage.close();
        } else {
            showAlert("Error", "Failed to upload item!", Alert.AlertType.ERROR);
        }
    }
    
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
	
}
