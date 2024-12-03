package api.test;

import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndPoints;
import api.payload.User;
import io.restassured.response.Response;

public class UserTests {

	User userPayload;
	Faker faker;
	

	@BeforeClass
	public void setUp() {
		faker = new Faker();
		userPayload = new User();

		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password(5, 10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());
	}

	@Test(priority = 1)
	public void testPOSTUser() {
		Response response = UserEndPoints.createUser(userPayload);
		response.then().log().all()
		//Assert.assertEquals(response.getStatusCode(), 200);
		
		//verify status code as 200
	      .assertThat().statusCode(200)

	      //verify body
	      .body("Location", Matchers.equalTo("Makinac Island"))

	      //verify header
	      .header("Content-Length" , "57");
		
		
	}
	
	@Test(priority=2)
	public void testGetuserByName() {
		Response response = UserEndPoints.readUser(this.userPayload.getUsername());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
	}
	
	@Test(priority = 3)
	public void testUpdateUser() {
		//update date using payload
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		
		Response response = UserEndPoints.updateUser(this.userPayload.getUsername(), userPayload);
		response.then().log().body();
		Assert.assertEquals(response.getStatusCode(), 200);
		
		//Checking data after update
		Response responseAfterUpdate = UserEndPoints.readUser(this.userPayload.getUsername());
		Assert.assertEquals(responseAfterUpdate.getStatusCode(), 200);
		
	}
	
	@Test(priority = 4)
	public void testDeleteUserByName() {
		Response response = UserEndPoints.deleteUser(this.userPayload.getUsername());
		
		Assert.assertEquals(response.getStatusCode(), 200);
	}

}
