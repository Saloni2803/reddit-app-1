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
 * This class contains all the test cases to test the CommentController class.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedditApiApplication.class)
@AutoConfigureMockMvc

public class CommentControllerTest {

    @Autowired
    private MockMvc mvc;


    //This test case passes when you try to create the comment but the JWT token entered does not exist in the database.
    @Test
    public void createCommentWithNonExistingAccessToken() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/post/database_post_uuid/comment/create?comment=my_comment").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).header("authorization", "non_existing_access_token"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-001"));
    }

    //This test case passes when you try to create the comment but the user corresponding to the JWT token entered is signed out of the application.
    @Test
    public void createCommentWithSignedOutUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/post/database_post_uuid/comment/create?comment=my_comment").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).header("authorization", "database_accesstoken3"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-002"));
    }

    //This test case passes when you try to create the comment for the post which does not exist in the database.
    @Test
    public void createCommentForNonExistingPost() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/post/non_existing_post_uuid/comment/create?comment=my_comment").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).header("authorization", "database_accesstoken"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("POS-001"));
    }

    //This test case passes when you try to edit the comment but the JWT token entered does not exist in the database.
    @Test
    public void editCommentWithNonExistingAccessToken() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/comment/edit/database_comment_uuid?content=edited_comment").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).header("authorization", "non_existing_access_token"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-001"));
    }

    //This test case passes when you try to edit the comment and the JWT token entered exists in the database but the user corresponding to that JWT token is signed out.
    @Test
    public void editCommentWithSignedOutUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/comment/edit/database_comment_uuid?content=edited_comment").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).header("authorization", "database_accesstoken3"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-002"));
    }

    //This test case passes when you try to edit the comment which does not exist in the database.
    @Test
    public void editNonExistingComment() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/comment/edit/non_existing_comment_uuid?content=edited_comment").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).header("authorization", "database_accesstoken1"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("COM-001"));
    }

    //This test case passes when you try to edit the comment and the JWT token entered exists in the database and the user corresponding to that JWT token is signed in but the corresponding user is not the owner of the comment.
    @Test
    public void editCommentWithoutOwnership() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/comment/edit/database_comment_uuid?content=edited_comment").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).header("authorization", "database_accesstoken2"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-003"));
    }

    //This test case passes when you try to delete the comment but the JWT token entered does not exist in the database.
    @Test
    public void deleteCommentWithNonExistingAccessToken() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/comment/delete/database_comment_uuid").header("authorization", "non_existing_access_token"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-001"));
    }

    //This test case passes when you try to delete the comment and the JWT token entered exists in the database but the user corresponding to that JWT token is signed out.
    @Test
    public void deleteCommentWithSignedOutUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/comment/delete/database_comment_uuid").header("authorization", "database_accesstoken3"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-002"));
    }

    //This test case passes when you try to delete the comment which does not exist in the database.
    @Test
    public void deleteNonExistingComment() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/comment/delete/non_existing_comment_uuid").header("authorization", "database_accesstoken1"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("COM-001"));
    }

    //This test case passes when you try to delete the comment and the JWT token entered exists in the database and the user corresponding to that JWT token is signed in but the corresponding user is not the owner of the comment or he is not the admin.
    @Test
    public void deleteCommentWithoutOwnership() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/comment/delete/database_comment_uuid").header("authorization", "database_accesstoken2"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-003"));
    }

    //This test case passes when you try to get all the comments posted for a specific post but the JWT token entered does not exist in the database.
    @Test
    public void getAllCommentsToPostWithNonExistingAccessToken() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/comment/all/database_post_uuid").header("authorization", "non_existing_access_token"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-001"));
    }

    //This test case passes when you try to get all the comments posted for a specific post and the JWT token entered exists in the database but the user corresponding to that JWT token is signed out.
    @Test
    public void getAllCommentsToPostWithSignedOutUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/comment/all/database_post_uuid").header("authorization", "database_accesstoken3"))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("ATHR-002"));
    }

    //This test case passes when you try to get all the comments posted for a specific post which does not exist in the database.
    @Test
    public void getAllCommentsToNonExistingPost() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/comment/all/non_existing_post_uuid").header("authorization", "database_accesstoken"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("POS-001"));
    }


}
