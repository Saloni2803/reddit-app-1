package controller;


import com.upgrad.reddit.api.RedditApiApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This class contains all the test cases to test the PostController class.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedditApiApplication.class)
@AutoConfigureMockMvc

public class PostControllerTest {

    @Autowired
    private MockMvc mvc;


    //This test case passes when you try to create the post but the JWT token entered does not exist in the database.
    @Test
    public void createPostWithNonExistingAccessToken() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/post/create?content=my_post").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).header("authorization", "non_existing_access_token"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-001"));
    }

    //This test case passes when you try to create the post but the user corresponding to the JWT token entered is signed out of the application.
    @Test
    public void createPostWithSignedOutUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/post/create?content=my_post").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).header("authorization", "database_accesstoken3"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-002"));
    }

    //This test case passes when you try to get the detail of all the posts and the JWT token entered exists in the database and the user corresponding to that JWT token is signed in.
    @Test
    public void getAllPosts() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/post/all").header("authorization", "database_accesstoken1"))
                .andExpect(status().isOk());
    }

    //This test case passes when you try to get the detail of all the posts but the JWT token entered does not exist in the database.
    @Test
    public void getAllPostsWithNonExistingAccessToken() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/post/all").header("authorization", "non_existing_access_token"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-001"));
    }

    //This test case passes when you try to get the detail of all the posts and the JWT token entered exists in the database but the user corresponding to that JWT token is signed out.
    @Test
    public void getAllPostsWithSignedOutUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/post/all").header("authorization", "database_accesstoken3"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-002"));
    }

    //This test case passes when you try to edit the post but the JWT token entered does not exist in the database.
    @Test
    public void editPostWithNonExistingAccessToken() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/post/edit/database_post_uuid?content=edited_post").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).header("authorization", "non_existing_access_token"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-001"));
    }

    //This test case passes when you try to edit the post and the JWT token entered exists in the database but the user corresponding to that JWT token is signed out.
    @Test
    public void editPostWithWithSignedOutUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/post/edit/database_post_uuid?content=edited_post").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).header("authorization", "database_accesstoken3"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-002"));
    }

    //This test case passes when you try to edit the post and the JWT token entered exists in the database and the user corresponding to that JWT token is signed in but the corresponding user is not the owner of the post.
    @Test
    public void editPostWithoutOwnership() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/post/edit/database_post_uuid?content=edited_post").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).header("authorization", "database_accesstoken2"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-003"));
    }

    //This test case passes when you try to edit the post which does not exist in the database.
    @Test
    public void editNonExistingPost() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/post/edit/non_exisitng_post_uuid?content=edited_post").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).header("authorization", "database_accesstoken1"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("POS-001"));
    }

    //This test case passes when you try to delete the post but the JWT token entered does not exist in the database.
    @Test
    public void deletePostWithNonExistingAccessToken() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/post/delete/database_post_uuid").header("authorization", "non_existing_access_token"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-001"));
    }

    //This test case passes when you try to delete the post and the JWT token entered exists in the database but the user corresponding to that JWT token is signed out.
    @Test
    public void deletePostWithSignedOutUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/post/delete/database_post_uuid").header("authorization", "database_accesstoken3"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-002"));
    }

    //This test case passes when you try to delete the post and the JWT token entered exists in the database and the user corresponding to that JWT token is signed in but the corresponding user is not the owner of the post or he is not the admin.
    @Test
    public void deletePostWithoutOwnership() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/post/delete/database_post_uuid").header("authorization", "database_accesstoken2"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-003"));
    }


    //This test case passes when you try to delete the post which does not exist in the database.
    @Test
    public void deleteNoneExistingPost() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/post/delete/non_existing_post_uuid").header("authorization", "database_accesstoken1"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("POS-001"));
    }

    //This test case passes when you try to get all the posts posted by a specific user but the JWT token entered does not exist in the database.
    @Test
    public void getAllPostsByUserWithNonExistingAccessToken() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/post/all/database_uuid1").header("authorization", "non_existing_access_token"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-001"));
    }

    //This test case passes when you try to get all the posts posted by a specific user and the JWT token entered exists in the database but the user corresponding to that JWT token is signed out.
    @Test
    public void getAllPostsByUserWithSignedOutUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/post/all/database_uuid1").header("authorization", "database_accesstoken3"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-002"));
    }

    //This test case passes when you try to get all the posts posted by a specific user which does not exist in the database.
    @Test
    public void getAllPostsForNonExistingUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/post/all/non_existing_user_uuid").header("authorization", "database_accesstoken1"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("USR-001"));
    }


}
