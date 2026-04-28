package es.kitti.adoption.intake.resource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.jwt.Claim;
import io.quarkus.test.security.jwt.JwtSecurity;
import io.restassured.http.ContentType;
import es.kitti.adoption.test.KafkaTestResource;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
@QuarkusTestResource(KafkaTestResource.class)
class IntakeRequestResourceTest {

    private static final String CREATE_BODY = """
            {
                "targetOrganizationId": 200,
                "catName": "Mishi",
                "catAge": 3,
                "region": "Santa Cruz de Tenerife",
                "city": "La Orotava",
                "vaccinated": true,
                "description": "Friendly cat"
            }
            """;

    @Test
    void createUnauthorized() {
        given()
                .contentType(ContentType.JSON)
                .body(CREATE_BODY)
                .when()
                .post("/intake-requests")
                .then()
                .statusCode(401);
    }

    @Test
    @TestSecurity(user = "100", roles = "User")
    @JwtSecurity(claims = {
            @Claim(key = "sub", value = "100"),
            @Claim(key = "email", value = "user@kitti.es")
    })
    void createAsUser_success() {
        given()
                .contentType(ContentType.JSON)
                .body(CREATE_BODY)
                .when()
                .post("/intake-requests")
                .then()
                .statusCode(201)
                .body("status", equalTo("Pending"))
                .body("userId", equalTo(100))
                .body("targetOrganizationId", equalTo(200))
                .body("catName", equalTo("Mishi"));
    }

    @Test
    @TestSecurity(user = "200", roles = "Organization")
    @JwtSecurity(claims = {
            @Claim(key = "sub", value = "200"),
            @Claim(key = "email", value = "org@kitti.es")
    })
    void createAsOrganization_forbidden() {
        given()
                .contentType(ContentType.JSON)
                .body(CREATE_BODY)
                .when()
                .post("/intake-requests")
                .then()
                .statusCode(403);
    }

    @Test
    @TestSecurity(user = "100", roles = "User")
    @JwtSecurity(claims = {
            @Claim(key = "sub", value = "100"),
            @Claim(key = "email", value = "user@kitti.es")
    })
    void createInvalidBody_returns400() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "targetOrganizationId": 200,
                            "catName": "",
                            "catAge": -1,
                            "region": "",
                            "city": "",
                            "vaccinated": null
                        }
                        """)
                .when()
                .post("/intake-requests")
                .then()
                .statusCode(400);
    }

    @Test
    @TestSecurity(user = "100", roles = "User")
    @JwtSecurity(claims = {
            @Claim(key = "sub", value = "100"),
            @Claim(key = "email", value = "user@kitti.es")
    })
    void findMine_returnsCreatedItem() {
        given()
                .contentType(ContentType.JSON)
                .body(CREATE_BODY)
                .when()
                .post("/intake-requests")
                .then()
                .statusCode(201);

        given()
                .when()
                .get("/intake-requests/mine")
                .then()
                .statusCode(200)
                .body("size()", org.hamcrest.Matchers.greaterThanOrEqualTo(1));
    }

    @Test
    @TestSecurity(user = "200", roles = "Organization")
    @JwtSecurity(claims = {
            @Claim(key = "sub", value = "200"),
            @Claim(key = "email", value = "org@kitti.es")
    })
    void findByOrganization_asOrganization_returns200() {
        given()
                .when()
                .get("/intake-requests/organization")
                .then()
                .statusCode(200);
    }

    @Test
    @TestSecurity(user = "100", roles = "User")
    @JwtSecurity(claims = {
            @Claim(key = "sub", value = "100"),
            @Claim(key = "email", value = "user@kitti.es")
    })
    void findByOrganization_asUser_forbidden() {
        given()
                .when()
                .get("/intake-requests/organization")
                .then()
                .statusCode(403);
    }

    @Test
    @TestSecurity(user = "200", roles = "Organization")
    @JwtSecurity(claims = {
            @Claim(key = "sub", value = "200"),
            @Claim(key = "email", value = "org@kitti.es")
    })
    void approveNotFound_returns404() {
        given()
                .when()
                .patch("/intake-requests/999999/approve")
                .then()
                .statusCode(404);
    }

    @Test
    @TestSecurity(user = "200", roles = "Organization")
    @JwtSecurity(claims = {
            @Claim(key = "sub", value = "200"),
            @Claim(key = "email", value = "org@kitti.es")
    })
    void rejectNotFound_returns404() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        { "reason": "no capacity" }
                        """)
                .when()
                .patch("/intake-requests/999999/reject")
                .then()
                .statusCode(404);
    }

    @Test
    void approveUnauthenticated_returns401() {
        given()
                .when()
                .patch("/intake-requests/1/approve")
                .then()
                .statusCode(401);
    }
}
