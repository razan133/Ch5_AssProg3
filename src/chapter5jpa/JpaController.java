/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter5jpa;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import static javax.persistence.Persistence.createEntityManagerFactory;
import javax.persistence.Query;

/**
 * FXML Controller class
 *
 * @author lenovo
 */
public class JpaController implements Initializable {

    @FXML
    private TextField TextFieldId;
    @FXML
    private TextField TextFieldName;
    @FXML
    private TextField TextFieldMajor;
    @FXML
    private TextField TextFieldGrade;
    @FXML
    private TableView<Student> tableV;
    @FXML
    private TableColumn<Student, String> coulmnID;
    @FXML
    private TableColumn<Student, String> coulmnName;
    @FXML
    private TableColumn<Student, String> coulmnMajor;
    @FXML
    private TableColumn<Student, Double> coulmnGrade;
    @FXML
    private Button btnAdd;
    @FXML
    private Button showbtn;
    @FXML
    private Button btnEdite;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnAddC;
    @FXML
    private Button ShowCourse;
    @FXML
    private TextArea Query;
    @FXML
    private Button performQuery;
    @FXML
    private Button updateDelBtn;
    @FXML
    private TextField IdStudent;
    @FXML
    private TextField IdCourse;
    @FXML
    private TextField smester;
    @FXML
    private TableView<Registration> tableCourse;
    @FXML
    private TableColumn<Registration, String> idStdC;
    @FXML
    private TableColumn<Registration, String> idCourseC;
    @FXML
    private TableColumn<Registration, String> smesterC;
    EntityManagerFactory emf;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         coulmnID.setCellValueFactory(new PropertyValueFactory("id"));
        coulmnName.setCellValueFactory(new PropertyValueFactory("name"));
        coulmnMajor.setCellValueFactory(new PropertyValueFactory("major"));
        coulmnGrade.setCellValueFactory(new PropertyValueFactory("grade"));
        idStdC.setCellValueFactory(new PropertyValueFactory("studentid"));
        idCourseC.setCellValueFactory(new PropertyValueFactory("courseid"));
        smesterC.setCellValueFactory(new PropertyValueFactory("smester"));
        this.emf=Persistence.createEntityManagerFactory("Chapter5JPAPU");  
      
    }    

    @FXML
    private void buttonAddHandle(ActionEvent event) {
        
        Student s = new Student();
        s.setId(TextFieldId.getText());
        s.setName(TextFieldName.getText());
        s.setMajor(TextFieldMajor.getText());
        s.setGrade(Double.parseDouble(TextFieldGrade.getText()));
        
        EntityManager em = this.emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(s);
        em.getTransaction().commit();
        em.close();
    }

    @FXML
    private void buttonShowHandle(ActionEvent event) {
        EntityManager em=this.emf.createEntityManager();
        List<Student> std=em.createNamedQuery("StudentQueryFindALL").getResultList();
        tableV.getItems().clear();
        tableV.getItems().setAll(std);
        em.close();
    }

    @FXML
    private void buttonEditeHandle(ActionEvent event) {
        EntityManager em = this.emf.createEntityManager();
         em.getTransaction().begin();
       Query query = em.createQuery("UPDATE Student s SET s.name= :name ,s.grade= :grade,s.major= :major WHERE s.id= :id");
            query.setParameter("name",TextFieldName.getText());
            query.setParameter("grade", Double.parseDouble(TextFieldGrade.getText()));
            query.setParameter("major", TextFieldMajor.getText());
            query.setParameter("id", TextFieldId.getText());
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }

    @FXML
    private void buttonDeleteHandle(ActionEvent event) {
           EntityManager em = this.emf.createEntityManager();
         em.getTransaction().begin();
       Query query = em.createQuery("DELETE FROM Student s WHERE s.id= :id");
           query.setParameter("id", TextFieldId.getText());
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }

    @FXML
    private void buttonAddCHandle(ActionEvent event) {
        boolean check=false;
        boolean check1=false;
        boolean t=false;
        Registration re=new Registration();
        re.setStudentid(IdStudent.getText());
        re.setCourseid(IdCourse.getText());
        re.setSmester(smester.getText());
        EntityManager em1=this.emf.createEntityManager();
        List<String> s1=em1.createQuery("SELECT s.id FROM Student s ").getResultList();
        List<String> s2=em1.createQuery("SELECT c.id FROM course c ").getResultList();
        List<String> r= em1.createQuery("SELECT r.courseid  FROM Registration r WHERE r.studentid= :studentid")
                .setParameter("studentid", re.getStudentid()).getResultList();
        List<String> r1= em1.createQuery("SELECT r.smester  FROM Registration r WHERE r.studentid= :studentid")
                .setParameter("studentid", re.getStudentid()).getResultList();
        
        //check if student id exist in student table
        if(s1.contains(re.getStudentid())){
            check=true;
        }
        //check if course id exist in course id
        if(s2.contains(re.getCourseid())){
            check1=true;
        }
        
        //check if student registration course more thane once
        //smester and course id not null
        if((!r.equals(null)&&!r1.equals(null))&&(r.contains(re.getCourseid())&&r1.contains(re.getSmester()))){
            t=true;
        }
       
        //if user regestration another once or ifo not exist in tables
        if((check==true &&check1==true)==false||t==true){
       Alert alert = new Alert(Alert.AlertType.ERROR);
       alert.setTitle("Error Dialog");
       alert.setHeaderText("Look, an Error Dialog");
       alert.setContentText("student id or course id not exist// or student regestration this course befor !");
       alert.showAndWait();  
        
    }else{
      EntityManager em=emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(re);
        em.getTransaction().commit();
        em.close();
        }  }
   

    @FXML
    private void ShowCourseHandle(ActionEvent event) {
        EntityManager em=this.emf.createEntityManager();
        List<Registration> reg=em.createNamedQuery("regQueryFindALL").getResultList();
        tableCourse.getItems().clear();
        tableCourse.getItems().setAll(reg);
        em.close();
    }

    @FXML
    private void PerformBtnHandle(ActionEvent event) {
        String sql=Query.getText();
       EntityManager em=this.emf.createEntityManager();
        List<Student> s=em.createQuery(sql).getResultList();
        tableV.getItems().clear();
        tableV.getItems().setAll(s);
        em.close();
        
    }

    @FXML
    private void updateDelHandle(ActionEvent event) {
        String sql=Query.getText();
        EntityManager em=emf.createEntityManager();
        em.getTransaction().begin();
        Query query = em.createQuery(sql);
         query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
    

    @FXML
    private void showRegInf(ActionEvent event) {
         EntityManager em = this.emf.createEntityManager();
        try{
        Registration r = (Registration) em.createNamedQuery("regQueryFindByID")
                .setParameter("studentid",(IdStudent.getText()))
                .getSingleResult();
        IdStudent.setText(r.getStudentid());
        IdCourse.setText(r.getCourseid());
        smester.setText(r.getSmester()+"");
        }catch(NoResultException ex){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error Retrieving");
            alert.setContentText("No records found");
            alert.showAndWait();
        }
        em.close();
    }

    @FXML
    private void showStdInfoHandler(ActionEvent event) {
    
     EntityManager em = this.emf.createEntityManager();
        try{
        Student s = (Student) em.createNamedQuery("StudentQueryFindByID")
                .setParameter("id", (TextFieldId.getText()))
                .getSingleResult();
        TextFieldName.setText(s.getName());
        TextFieldId.setText(s.getId());
        TextFieldMajor.setText(s.getMajor());
        TextFieldGrade.setText(String.valueOf(s.getGrade()));
        }catch(NoResultException ex){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error Retrieving");
            alert.setContentText("No records found");
            alert.showAndWait();
        }
        em.close();

    }
}
    

