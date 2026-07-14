package com.examly.springapp;

import java.io.File;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;    
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SpringappApplication.class)
@AutoConfigureMockMvc
class SpringappApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    // Test adding a vegan snack through the controller
    @Test
    void test_Add_VeganSnack() throws Exception {
        String veganSnackJson = "{\"snackName\": \"Vegan Delights\",\"snackType\": \"Granola Bars\",\"quantity\": \"200 grams\",\"price\": 500,\"expiryInMonths\": \"6\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/addVeganSnack")
                .contentType(MediaType.APPLICATION_JSON)
                .content(veganSnackJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    // Test adding a vegan snack with negative expiry (exception case)
    @Test
    void test_Return_BadRequest_For_Negative_Expiry() throws Exception {
        // Use valid data but set expiry to a negative value
        String veganSnackJson = "{\"snackName\": \"Tasty Snacks\",\"snackType\": \"Veggie Chips\",\"quantity\": \"300 grams\",\"price\": 350,\"expiryInMonths\": \"-1\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/addVeganSnack")
                .contentType(MediaType.APPLICATION_JSON)
                .content(veganSnackJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) // Expecting a bad request for negative expiry
                .andExpect(jsonPath("$").value("Expiry in months should not be a negative value.")); // Adjust the message based on your exception handling
    }

    // Test fetching all vegan snacks from the controller
    @Test
    void test_Return_AllVeganSnacks() throws Exception {
        mockMvc.perform(get("/getAllVeganSnacks")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andReturn();
    }

    // Verify the controller directory exists
    @Test
    void test_Verify_Controller_Directory_Exists() {
        String directoryPath = "src/main/java/com/examly/springapp/controller";
        File directory = new File(directoryPath);
        assertTrue(directory.exists() && directory.isDirectory(), "Controller directory does not exist.");
    }

    // Verify the VeganSnackController class exists
    @Test
    void test_Verify_VeganSnackController_File_Exists() {
        String filePath = "src/main/java/com/examly/springapp/controller/VeganSnackController.java";
        File file = new File(filePath);
        assertTrue(file.exists() && file.isFile(), "VeganSnackController file does not exist.");
    }

    // Verify the model directory exists
    @Test
    void test_Verify_Model_Directory_Exists() {
        String directoryPath = "src/main/java/com/examly/springapp/model";
        File directory = new File(directoryPath);
        assertTrue(directory.exists() && directory.isDirectory(), "Model directory does not exist.");
    }

    // Verify the VeganSnack model file exists
    @Test
    void test_Verify_VeganSnack_File_Exists() {
        String filePath = "src/main/java/com/examly/springapp/model/VeganSnack.java";
        File file = new File(filePath);
        assertTrue(file.exists() && file.isFile(), "VeganSnack model file does not exist.");
    }

    // Verify the repository directory exists
    @Test
    void test_Verify_Repository_Directory_Exists() {
        String directoryPath = "src/main/java/com/examly/springapp/repository";
        File directory = new File(directoryPath);
        assertTrue(directory.exists() && directory.isDirectory(), "Repository directory does not exist.");
    }

    // Verify the service directory exists
    @Test
    void test_Verify_Service_Directory_Exists() {
        String directoryPath = "src/main/java/com/examly/springapp/service";
        File directory = new File(directoryPath);
        assertTrue(directory.exists() && directory.isDirectory(), "Service directory does not exist.");
    }

    // Verify the VeganSnackService class exists
    @Test
    void test_Verify_VeganSnackService_Class_Exists() {
        checkClassExists("com.examly.springapp.service.VeganSnackService");
    }

    // Verify the VeganSnack model class exists
    @Test
    void test_Verify_VeganSnackModel_Class_Exists() {
        checkClassExists("com.examly.springapp.model.VeganSnack");
    }

    // Check that the VeganSnack model has a 'snackName' field
    @Test
    void test_Verify_VeganSnack_Model_Has_snackName_Field() {
        checkFieldExists("com.examly.springapp.model.VeganSnack", "snackName");
    }

    // Check that the VeganSnack model has a 'snackType' field
    @Test
    void test_Verify_VeganSnack_Model_Has_snackType_Field() {
        checkFieldExists("com.examly.springapp.model.VeganSnack", "snackType");
    }

    // Check that the VeganSnack model has a 'quantity' field
    @Test
    void test_Verify_VeganSnack_Model_Has_quantity_Field() {
        checkFieldExists("com.examly.springapp.model.VeganSnack", "quantity");
    }

    // Check that the VeganSnack model has a 'price' field
    @Test
    void test_Verify_VeganSnack_Model_Has_price_Field() {
        checkFieldExists("com.examly.springapp.model.VeganSnack", "price");
    }

    // Check that the VeganSnackRepo implements JpaRepository
    @Test
    void test_Verify_VeganSnackRepo_Extends_JpaRepository() {
        checkClassImplementsInterface("com.examly.springapp.repository.VeganSnackRepo",
                "org.springframework.data.jpa.repository.JpaRepository");
    }

    // Verify that CORS configuration class exists
    @Test
    void test_Verify_CorsConfiguration_Class_Exists() {
        checkClassExists("com.examly.springapp.configuration.CorsConfiguration");
    }

    // Verify that CORS configuration has the Configuration annotation
    @Test
    void test_Verify_CorsConfiguration_Has_Configuration_Annotation() {
        checkClassHasAnnotation("com.examly.springapp.configuration.CorsConfiguration",
                "org.springframework.context.annotation.Configuration");
    }

    // Verify that InvalidExpiryException class exists
    @Test
    void test_Verify_InvalidExpiryException_Class_Exists() {
        checkClassExists("com.examly.springapp.exception.InvalidExpiryException");
    }

    // Verify that InvalidExpiryException extends RuntimeException
    @Test
    void test_Verify_InvalidExpiryException_Extends_RuntimeException() {
        try {
            Class<?> clazz = Class.forName("com.examly.springapp.exception.InvalidExpiryException");
            assertTrue(RuntimeException.class.isAssignableFrom(clazz), "InvalidExpiryException should extend RuntimeException.");
        } catch (ClassNotFoundException e) {
            fail("InvalidExpiryException class does not exist.");
        }
    }

    // Helper methods (unchanged)
    
    // Helper method to check if a class exists
    private void checkClassExists(String className) {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            fail("Class " + className + " does not exist.");
        }
    }

    // Helper method to check if a field exists in a class
    private void checkFieldExists(String className, String fieldName) {
        try {
            Class<?> clazz = Class.forName(className);
            clazz.getDeclaredField(fieldName);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            fail("Field " + fieldName + " in class " + className + " does not exist.");
        }
    }

    // Helper method to check if a class implements an interface
    private void checkClassImplementsInterface(String className, String interfaceName) {
        try {
            Class<?> clazz = Class.forName(className);
            Class<?> interfaceClazz = Class.forName(interfaceName);
            assertTrue(interfaceClazz.isAssignableFrom(clazz), className + " does not implement " + interfaceName + ".");
        } catch (ClassNotFoundException e) {
            fail("Class " + className + " or interface " + interfaceName + " does not exist.");
        }
    }

    // Helper method to check if a class has a specific annotation
    private void checkClassHasAnnotation(String className, String annotationName) {
        try {
            Class<?> clazz = Class.forName(className);
            @SuppressWarnings("unchecked")
            Class<? extends java.lang.annotation.Annotation> annotationClazz = 
                (Class<? extends java.lang.annotation.Annotation>) Class.forName(annotationName);
            assertTrue(clazz.isAnnotationPresent(annotationClazz),
                    className + " does not have annotation " + annotationName + ".");
        } catch (ClassNotFoundException e) {
            fail("Class " + className + " or annotation " + annotationName + " does not exist.");
        }
    }
}
