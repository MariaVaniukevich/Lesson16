package org.aston;

import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;

public class RequestEchoTest {

    private final static String URL = "https://postman-echo.com";
    @Test
    public void postFormDataRequestTest() {

        Specification.installSpecification(Specification.requestSpec(URL));
        given()
                .basePath("/post")
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .formParam("foo1", "bar1")
                .formParam("foo2", "bar2")
                .when()
                .post()
                .then().log().body()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("form.foo1", equalTo("bar1"))
                .body("form.foo2", equalTo("bar2"));
    }

    @Test
    public void postRawTextRequestTest() {
        Specification.installSpecification(Specification.requestSpec(URL));
        Map<String,Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("text", "value");

        given()
                .basePath("/post")
                .contentType(ContentType.JSON)
                .body(jsonAsMap)
                .when()
                .post()
                .then().log().all()
                .statusCode(200)
                .body("data.text", equalTo("value"));
    }
    @Test
    public void postmanFirstGetTest() {
        given()
                .baseUri("https://postman-echo.com")
                .when()
                .get("/get?foo1=bar1&foo2=bar2")
                .then()
                .assertThat()
                .statusCode(200)
                .and().body("args.foo2", equalTo("bar2"))
                .and().body("args.foo1", equalTo("bar1"))
                .body("headers", not(emptyArray()))
                .and().body("url", equalTo("https://postman-echo.com/get?foo1=bar1&foo2=bar2"));
    }
    @Test
    public void postmanPutTest() {
        String expectedBody = "This is expected to be sent back as part of response body.";
        String expectedUrl = "https://postman-echo.com/put";
        given()
                .contentType(ContentType.TEXT)
                .body(expectedBody)
                .when()
                .put(expectedUrl)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("data", equalTo(expectedBody))
                .body("json", nullValue())
                .body("url", equalTo(expectedUrl))
                .body("headers", not(emptyArray()));
    }
    @Test
    public void postmanPatchTest() {
        String expectedBody = "This is expected to be sent back as part of response body.";

        given()
                .contentType(ContentType.TEXT)
                .body(expectedBody)
                .when()
                .patch("https://postman-echo.com/patch")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("args", notNullValue())
                .body("data", equalTo(expectedBody))
                .body("files", notNullValue())
                .body("form", notNullValue())
                .body("headers", not(emptyArray()))
                .body("json", nullValue())
                .body("url", equalTo("https://postman-echo.com/patch"));
    }
    @Test
    public void postmanDeleteTest() {
        String expectedBody = "This is expected to be sent back as part of response body.";

        given()
                .contentType(ContentType.TEXT)
                .body(expectedBody)
                .when()
                .delete("https://postman-echo.com/delete")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("args", notNullValue())
                .body("data", equalTo(expectedBody))
                .body("files", notNullValue())
                .body("form", notNullValue())
                .body("headers", not(emptyArray()))
                .body("json", nullValue())
                .body("url", equalTo("https://postman-echo.com/delete"));
    }
}