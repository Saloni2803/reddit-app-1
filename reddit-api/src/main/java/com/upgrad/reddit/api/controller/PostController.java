package com.upgrad.reddit.api.controller;

import com.upgrad.reddit.api.model.*;
import com.upgrad.reddit.service.business.PostBusinessService;
import com.upgrad.reddit.service.entity.PostEntity;
import com.upgrad.reddit.service.exception.AuthorizationFailedException;
import com.upgrad.reddit.service.exception.InvalidPostException;
import com.upgrad.reddit.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostBusinessService postBusinessService;

    /**
     * A controller method to create a post.
     *
     * @param postRequest   - This argument contains all the attributes required to store post details in the database.
     * @param authorization - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<PostResponse> type object along with Http status CREATED.
     * @throws AuthorizationFailedException
     */
    @PostMapping(value = "/create")
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest, @RequestHeader("authorization") String authorization) throws AuthorizationFailedException {

        PostEntity postEntity = new PostEntity();
        postEntity.setContent(postRequest.getContent());

        postBusinessService.createPost(postEntity, authorization);
        PostResponse response = new PostResponse();
        response.setId(postEntity.getUuid());
        response.setStatus("Post Created");
        return new ResponseEntity<PostResponse>(response, HttpStatus.OK);
    }


    /**
     * A controller method to fetch all the posts from the database.
     *
     * @param authorization - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<List<PostDetailsResponse>> type object along with Http status OK.
     * @throws AuthorizationFailedException
     */
    @GetMapping(value = "/all")
    public ResponseEntity<List<PostDetailsResponse>> getPosts(@RequestHeader("authorization") String authorization) throws AuthorizationFailedException {
        TypedQuery<PostEntity> posts = postBusinessService.getPosts(authorization);
        List<PostDetailsResponse> responses = new ArrayList<>();
        posts.getResultList().forEach(post -> {
            PostDetailsResponse response = new PostDetailsResponse();
            response.setContent(post.getContent());
            response.setId(post.getUuid());
            responses.add(response);
        });
        return new ResponseEntity<List<PostDetailsResponse>>(responses, HttpStatus.OK);
    }

    /**
     * A controller method to edit the post in the database.
     *
     * @param postEditRequest - This argument contains all the attributes required to edit the post details in the database.
     * @param postId          - The uuid of the post to be edited in the database.
     * @param authorization   - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<PostEditResponse> type object along with Http status OK.
     * @throws AuthorizationFailedException
     * @throws InvalidPostException
     */
    @PutMapping(value = "/edit/{postId}")
    public ResponseEntity<PostEditResponse> editPost(@RequestBody PostEditRequest postEditRequest, @PathVariable String postId, @RequestHeader("authorization") String authorization) throws AuthorizationFailedException, InvalidPostException {
        PostEntity postEntity = new PostEntity();
        postEntity.setContent(postEditRequest.getContent());
        postEntity.setUuid(UUID.randomUUID().toString());
        PostEntity post = postBusinessService.editPostContent(postEntity, postId, authorization);
        PostEditResponse response = new PostEditResponse();
        response.setId(post.getUuid());
        response.setStatus("Post Edited");
        return new ResponseEntity<PostEditResponse>(response, HttpStatus.OK);
    }

    /**
     * A controller method to delete the post in the database.
     *
     * @param postId        - The uuid of the post to be deleted in the database.
     * @param authorization - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<PostDeleteResponse> type object along with Http status OK.
     * @throws AuthorizationFailedException
     * @throws InvalidPostException
     */
    @DeleteMapping(value = "/delete/{postId}")
    public ResponseEntity<PostDeleteResponse> deletePost(@PathVariable String postId, @RequestHeader("authorization") String authorization) throws AuthorizationFailedException, InvalidPostException {
        PostEntity postDeleted = postBusinessService.deletePost(postId, authorization);
        PostDeleteResponse response = new PostDeleteResponse();
        response.setId(postDeleted.getUuid());
        response.setStatus("Post Deleted");
        return new ResponseEntity<PostDeleteResponse>(response,HttpStatus.OK);
    }

    /**
     * A controller method to fetch all the posts posted by a specific user.
     *
     * @param userId        - The uuid of the user whose posts are to be fetched from the database.
     * @param authorization - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<List<PostDetailsResponse>> type object along with Http status OK.
     * @throws AuthorizationFailedException
     * @throws UserNotFoundException
     */
    @GetMapping(value = "/all/{userId}")
    public ResponseEntity<List<PostDetailsResponse>> getPostsByUser(@PathVariable String userId, @RequestHeader("authorization") String authorization) throws AuthorizationFailedException, UserNotFoundException {
        TypedQuery<PostEntity> posts = postBusinessService.getPostsByUser(userId, authorization);
        List<PostEntity> postEntities = posts.getResultList();
        List<PostDetailsResponse> responses = new ArrayList<>();
        if (postEntities != null) {
            for(PostEntity post : postEntities){
                PostDetailsResponse response = new PostDetailsResponse();
                response.setId(post.getUuid());
                response.setContent(post.getContent());
                responses.add(response);
            }
        }
        return new ResponseEntity<List<PostDetailsResponse>>(responses,HttpStatus.OK);
    }

}
