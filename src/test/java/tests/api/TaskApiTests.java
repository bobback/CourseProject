package tests.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.*;
import tests.utils.BaseTest;


@Test(groups = "api")
public class TaskApiTests extends BaseTest {
    public static final String API_URL = "http://64.227.74.146/jsonrpc.php";
    private String authHeader;


    private Integer projectId;
    private Integer taskId;


    @BeforeClass
    public void setUpBeforeTest(){
        setUp();
        logInTestPositive();
        authHeader = getAuthHeader();


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
                .post(API_URL);

        createProjectResponse.then().statusCode(200);
        createProjectResponse.prettyPrint();

        projectId = createProjectResponse.then().extract().path("result");
    }


    @Test(priority = 1)
    public void createTaskTest(){
        String createTaskRequest = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"createTask\",\n" +
                "    \"id\": \"1176509098\",\n" +
                "    \"params\": {\n" +
                "        \"title\": \"Bobaktask\",\n" +
                "        \"project_id\":" + projectId +"\n" +
                "    }\n" +
                "}";

        Response createTaskResponse = RestAssured.given()
                .header("Authorization", authHeader)
                .contentType(ContentType.JSON)
                .body(createTaskRequest)
                .post(API_URL);

        createTaskResponse.then().statusCode(200);
        createTaskResponse.prettyPrint();

        taskId = createTaskResponse.then().extract().path("result");
    }


    @Test(priority = 2)
    public void deleteTaskTest(){
        String deleteTaskRequest = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"removeTask\",\n" +
                "    \"id\": \"1176509098\",\n" +
                "    \"params\": {\n" +
                "        \"task_id\": " + "\"" + taskId + "\"\n" +
                "    }\n" +
                "}";

        Response deleteTaskResponse = RestAssured.given()
                .header("Authorization", authHeader)
                .contentType(ContentType.JSON)
                .body(deleteTaskRequest)
                .post(API_URL);

        deleteTaskResponse.then().statusCode(200);
        deleteTaskResponse.prettyPrint();
    }


    @AfterClass
    public void cleanUp(){
        String deleteProjectRequest = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"removeProject\",\n" +
                "    \"id\": \"46285125\",\n" +
                "    \"params\": {\n" +
                "        \"project_id\": " + "\"" + projectId + "\"\n" +
                "    }\n" +
                "}";

        Response deleteProjectResponse = RestAssured.given()
                .header("Authorization", authHeader)
                .contentType(ContentType.JSON)
                .body(deleteProjectRequest)
                .post(API_URL);

        deleteProjectResponse.then().statusCode(200);
        deleteProjectResponse.prettyPrint();

            tearDown();
    }
}

