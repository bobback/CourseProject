package tests.ui;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.testng.annotations.*;
import tests.utils.BaseTest;

import java.util.Base64;

import static com.codeborne.selenide.Selenide.$;

@Test(groups = "ui")
public class CreateCommentUiApiTest extends BaseTest {
    public static final String API_ENDPOINT = "http://64.227.74.146/jsonrpc.php";
    private String authHeader;

    public static final String userName = "bobak";
    public static final String password = "123456";
    private final SelenideElement usernameInput = $(By.name("username"));
    private final SelenideElement passwordInput = $(By.name("password"));
    private final SelenideElement loginButton = $(By.xpath("//button[contains(text(), 'Sign in')]"));
    private int taskId;
    private int userId;
    private int projectId;

    @BeforeClass
    public void setUpBeforeTest(){
        setUp();
        logInTestPositive();

        Selenide.open("/settings/api");

        String API_TOKEN = $(By.xpath("//*[@id=\"config-section\"]/div[2]/div[2]/ul/li[1]/strong")).getText();
        authHeader = "Basic " + Base64.getEncoder().encodeToString(("jsonrpc:" + API_TOKEN).getBytes());

        String createUserRequest = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"createUser\",\n" +
                "    \"id\": 1518863034,\n" +
                "    \"params\": {\n" +
                "        \"username\": " + "\"" + userName + "\",\n" +
                "        \"password\": " + "\"" + password + "\",\n" +
                "        \"role\": \"app-manager\"\n" +
                "    }\n" +
                "}";


        Response createUserResponse = RestAssured.given()
                .header("Authorization", authHeader)
                .contentType(ContentType.JSON)
                .body(createUserRequest)
                .post(API_ENDPOINT);

        createUserResponse.then().statusCode(200);
        createUserResponse.prettyPrint();

        userId = createUserResponse.then().extract().path("result");

        String createProjectRequest = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"createProject\",\n" +
                "    \"id\": \"1797076613\",\n" +
                "    \"params\": {\n" +
                "        \"name\": \"Bobak Project\"\n" +
                "    }\n" +
                "}";

        Response createProjectResponse = RestAssured.given()
                .header("Authorization", authHeader)
                .contentType(ContentType.JSON)
                .body(createProjectRequest)
                .post(API_ENDPOINT);

        createProjectResponse.then().statusCode(200);
        createProjectResponse.prettyPrint();

        projectId = createProjectResponse.then().extract().path("result");


        String addUserToProjectRequest = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"addProjectUser\",\n" +
                "    \"id\": \"1294688355\",\n" +
                "    \"params\": [\n" +
                "\"" + projectId + "\",\n" +
                "\"" + userId + "\",\n" +
                "        \"project-manager\"\n" +
                "    ]\n" +
                "}";

        Response addUserToProjectResponse = RestAssured.given()
                .header("Authorization", authHeader)
                .contentType(ContentType.JSON)
                .body(addUserToProjectRequest)
                .post(API_ENDPOINT);

        addUserToProjectResponse.then().statusCode(200);
        addUserToProjectResponse.prettyPrint();

        String createTaskRequest = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"createTask\",\n" +
                "    \"id\": \"1176509098\",\n" +
                "    \"params\": {\n" +
                "        \"owner_id\": " + "\"" + userId + "\",\n" +
                "        \"creator_id\": " + "\"" + userId + "\",\n" +
                "        \"date_due\": \"\",\n" +
                "        \"description\": \"\",\n" +
                "        \"category_id\": \"0\",\n" +
                "        \"score\": \"0\",\n" +
                "        \"title\": \"Bobaktask\",\n" +
                "        \"project_id\": " + "\"" + projectId + "\"\n" +
                "    }\n" +
                "}";

        Response createTaskResponse = RestAssured.given()
                .header("Authorization", authHeader)
                .contentType(ContentType.JSON)
                .body(createTaskRequest)
                .post(API_ENDPOINT);

        createTaskResponse.then().statusCode(200);
        createTaskResponse.prettyPrint();

        taskId = createTaskResponse.then().extract().path("result");

        tearDown();
    }

    @Test
    public void createCommentTest(){
        setUp();
        Selenide.open("/login");
        usernameInput.setValue(userName);
        passwordInput.setValue(password);
        loginButton.click();

        Selenide.open("/task/" + taskId);
        $(By.xpath("//*[@id=\"task-view\"]/div[1]/ul[2]/li[6]/a")).click();
        String commentText = "Bobak's Comment";
        $(By.xpath("//*[@id=\"modal-content\"]/form/div[1]/div/div[2]/textarea")).setValue(commentText);
        $(By.xpath("//*[@id=\"modal-content\"]/form/div[2]/div/button")).click();
        $(By.id("comments")).shouldHave(Condition.text(commentText));

        tearDown();
    }


    @AfterClass
    public void cleanUp(){
        String removeUserRequest = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"removeUser\",\n" +
                "    \"id\": 1518863034,\n" +
                "    \"params\": {\n" +
                "        \"user_id\": " + "\"" + userId + "\"\n" +
                "    }\n" +
                "}";

        Response removeUserResponse = RestAssured.given()
                .header("Authorization", authHeader)
                .contentType(ContentType.JSON)
                .body(removeUserRequest)
                .post(API_ENDPOINT);

        removeUserResponse.then().statusCode(200);
        removeUserResponse.prettyPrint();

        String removeProjectRequest = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"removeProject\",\n" +
                "    \"id\": \"46285125\",\n" +
                "    \"params\": {\n" +
                "        \"project_id\": " + "\"" + projectId + "\"\n" +
                "    }\n" +
                "}";

        Response removeProjectResponse = RestAssured.given()
                .header("Authorization", authHeader)
                .contentType(ContentType.JSON)
                .body(removeProjectRequest)
                .post(API_ENDPOINT);

        removeProjectResponse.then().statusCode(200);
        removeProjectResponse.prettyPrint();
    }

}
