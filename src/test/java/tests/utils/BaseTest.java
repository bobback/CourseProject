package tests.utils;

        import com.codeborne.selenide.Configuration;
        import com.codeborne.selenide.Selenide;
        import com.codeborne.selenide.SelenideElement;
        import org.openqa.selenium.By;

        import java.io.IOException;
        import java.io.InputStream;
        import java.util.Base64;
        import java.util.Properties;

        import static com.codeborne.selenide.Condition.visible;
        import static com.codeborne.selenide.Configuration.baseUrl;
        import static com.codeborne.selenide.Configuration.headless;
        import static com.codeborne.selenide.Selenide.$;

public class BaseTest {
    private final SelenideElement usernameInput = $(By.name("username"));
    private final SelenideElement passwordInput = $(By.name("password"));
    private final SelenideElement loginButton = $(By.xpath("//button[contains(text(), 'Sign in')]"));

    protected static String API_URL;
    protected static String API_USERNAME;
    protected static String API_PASSWORD;

    protected void setUp(){
        System.setProperty("webdriver.gecko.driver", "C:/Program Files/geckodriver/geckodriver.exe");

        String browser = System.getProperty("browser");
        if (browser == null) {
            browser = "chrome";
        }

        switch (browser.toLowerCase()) {
            case "chrome" -> Configuration.browser = "chrome";
            case "headlesschrome" -> {
                Configuration.browser = "chrome";
                headless = true;
            }
            case "firefox" -> Configuration.browser = "firefox";
            default -> throw new IllegalArgumentException("Invalid browser specified" + browser);
        }

        Configuration.baseUrl = getAppUrl();
    }

    protected void logInTestPositive(){
        Selenide.open("/login");
        usernameInput.setValue("admin");
        passwordInput.setValue("rdaqa2023#p");
        loginButton.click();

        $(By.xpath("/html/body/header/div[3]/div[2]")).shouldBe(visible);
    }

    protected String getAuthHeader() {
        Selenide.open("http://64.227.74.146/settings/api");

        String token = $(By.xpath("//*[@id=\"config-section\"]/div[2]/div[2]/ul/li[1]/strong")).getText();
        return "Basic " + Base64.getEncoder().encodeToString(("jsonrpc:" + token).getBytes());
    }

    protected void tearDown(){
        Selenide.closeWebDriver();
    }

    private static String getAppUrl(){
        String instanceName = System.getProperty("instanceName");
        if (instanceName == null) {
            throw new IllegalArgumentException("No instanceName provided. Please specify instanceName using -DinstanceName argument.");
        }

        String apiUrl;
        String apiUsername;
        String apiPassword;

        Properties properties = new Properties();
        try (InputStream inputStream = BaseTest.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (instanceName) {
            case "internet" -> {
                apiUrl = properties.getProperty("internet.api.url");
                apiUsername = properties.getProperty("internet.api.username");
                apiPassword = properties.getProperty("internet.api.password");
            }
            case "local" -> {
                apiUrl = properties.getProperty("local.api.url");
                apiUsername = properties.getProperty("local.api.username");
                apiPassword = properties.getProperty("local.api.password");
            }
            default -> throw new IllegalArgumentException("Invalid instanceName specified: " + instanceName);
        }
        API_URL = apiUrl;
        API_USERNAME = apiUsername;
        API_PASSWORD = apiPassword;

        return apiUrl;
    }
}
