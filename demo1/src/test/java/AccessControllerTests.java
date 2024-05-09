/**
 * @program:demo1
 * @description
 * @author:Xieshuyang
 * @create:
 **/

import com.example.MyApp;
import com.example.bean.UserAccess;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *@description:
 **/


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MyApp.class)
@WebAppConfiguration
public class AccessControllerTests {
    private MockMvc mockMvc;

    public AccessControllerTests() {
    }

    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Test
    public void testAddUserAccess_Success() throws Exception {
        // 模拟管理员请求
        String adminAuthorization = encodeAuthorization("{\"userId\": 123456, \"role\": \"admin\"}");
        UserAccess request = new UserAccess(123456, Arrays.asList("resourceA", "resourceB"));

        mockMvc.perform(post("/admin/addUser")
                .header("Authorization", adminAuthorization)
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());

    }

    @Test
    public void testAddUserAccess_Unauthorized() throws Exception {
        // 模拟非管理员请求
        String userAuthorization = encodeAuthorization("{\"userId\": 789, \"role\": \"user\"}");
        UserAccess request = new UserAccess(789, Arrays.asList("resourceA", "resourceB"));

        mockMvc.perform(post("/admin/addUser")
                .header("Authorization", userAuthorization)
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testAccessResource_Success() throws Exception {
        // 模拟具有访问权限的用户请求
        String userAuthorization = encodeAuthorization("{\"userId\": 123456, \"role\": \"user\"}");

        mockMvc.perform(get("/user/resourceA")
                .header("Authorization", userAuthorization))
                .andExpect(status().isOk());
    }

    @Test
    public void testAccessResource_Forbidden() throws Exception {
        // 模拟没有访问权限的用户请求
        String userAuthorization = encodeAuthorization("{\"userId\": 789, \"role\": \"user\"}");

        mockMvc.perform(get("/user/resourceA")
                .header("Authorization", userAuthorization))
                .andExpect(status().isForbidden());
    }

    private String encodeAuthorization(String authorization) {
        return Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8));
    }
}
