package sample.subproject.hibernate;

import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import sample.subproject.hibernate.models.Test;
import sample.subproject.hibernate.models.TestEntity;
import sample.window.Popup;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;

public class DbViewer
{
    Stage stage;
    VBox root;
    HBox rootHBox;

    public DbViewer()
    {
        stage = new Stage();
        root = new VBox();
        rootHBox = new HBox();

        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        SeparatorMenuItem seperator = new SeparatorMenuItem();
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(actionEvent -> commandExit());
        exitItem.setAccelerator(new KeyCodeCombination(KeyCode.F10));
        fileMenu.getItems().addAll(seperator, exitItem);

        Menu toolMenu = new Menu("Tool");
        MenuItem testItem = new MenuItem("test query");
        testItem.setOnAction(actionEvent -> {
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("NewPersistenceUnit");
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            TestEntity testEntity = new TestEntity();
            testEntity.setId(4);
            testEntity.setTitle("test 4");
            entityManager.persist(testEntity);
            entityManager.getTransaction().commit();
            entityManager.close();
            entityManagerFactory.close();

        });
        MenuItem queryItem = new MenuItem("Query");
        toolMenu.getItems().addAll(testItem, queryItem);

        Menu aboutMenu = new Menu("About");
        MenuItem aboutItemMenuItem = new MenuItem("About");
        aboutItemMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F1));
        aboutItemMenuItem.setOnAction(actionEvent -> new Popup().show("Hibernate DB viewer"));
        aboutMenu.getItems().add(aboutItemMenuItem);

        menuBar.getMenus().addAll(fileMenu, toolMenu, aboutMenu);

        root.getChildren().addAll(menuBar, rootHBox);

        Scene scene = new Scene(root, 1024, 768);
        stage.setScene(scene);
        stage.setTitle("Hibernate DbView");
        stage.show();
        stage.setOnCloseRequest(windowEvent -> commandExit());
    }

    public void commandExit()
    {
        System.out.println("exit hibernate dbviewer");
        stage.close();
    }
}