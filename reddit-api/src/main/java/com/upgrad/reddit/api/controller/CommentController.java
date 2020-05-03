package com.upgrad.reddit.api.controller;

import com.upgrad.reddit.api.model.*;
import com.upgrad.reddit.service.business.CommentBusinessService;
import com.upgrad.reddit.service.entity.CommentEntity;
import com.upgrad.reddit.service.entity.PostEntity;
import com.upgrad.reddit.service.exception.AuthorizationFailedException;
import com.upgrad.reddit.service.exception.CommentNotFoundException;
import com.upgrad.reddit.service.exception.InvalidPostException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.TypedQuery;
import javax.xml.stream.events.Comment;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/")
public class CommentController {


    @Autowired
    private CommentBusinessService commentBusinessService;

    /**
     * A controller method to post an comment to a specific post.
     *
     * @param commentRequest - This argument contains all the attributes required to store comment details in the database.
     * @param postId    - The uuid of the post whose comment is to be posted in the database.
     * @param authorization - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<CommentResponse> type object along with Http status CREATED.
     * @throws AuthorizationFailedException
     * @throws InvalidPostException
     */
    @PostMapping(value = "/post/{postId}/comment/create")
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest commentRequest, @PathVariable String postId, @RequestHeader("authorization") String authorization)throws AuthorizationFailedException, InvalidPostException{
        PostEntity postEntity = commentBusinessService.getPostByUuid(postId);
        if(postEntity == null){
            throw new InvalidPostException("POS-001","The post entered is invalid");
        }
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setComment(commentRequest.getComment());
        commentEntity.setPost(postEntity);
        CommentEntity comment = commentBusinessService.createComment(commentEntity,authorization);
        CommentResponse response = new CommentResponse();
        response.setId(comment.getUuid());
        response.setStatus("Comment Created");
        return new ResponseEntity<CommentResponse>(response,HttpStatus.OK);
    }

    /**
     * A controller method to edit an comment in the database.
     *
     * @param commentEditRequest - This argument contains all the attributes required to store edited comment details in the database.
     * @param commentId          - The uuid of the comment to be edited in the database.
     * @param authorization     - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<CommentEditResponse> type object along with Http status OK.
     * @throws AuthorizationFailedException
     * @throws CommentNotFoundException
     */
    @PutMapping(value = "/comment/edit/{commentId}")
    public ResponseEntity<CommentEditResponse> editComment(@RequestBody CommentEditRequest commentEditRequest,@PathVariable String commentId,@RequestHeader("authorization") String authorization)throws AuthorizationFailedException,CommentNotFoundException{
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setComment(commentEditRequest.getContent());
        CommentEntity comment = commentBusinessService.editCommentContent(commentEntity,commentId,authorization);
        CommentEditResponse response = new CommentEditResponse();
        response.setId(comment.getUuid());
        response.setStatus("Comment Edited");
        return new ResponseEntity<CommentEditResponse>(response,HttpStatus.OK);
    }

    /**
     * A controller method to delete an comment in the database.
     *
     * @param commentId      - The uuid of the comment to be deleted in the database.
     * @param authorization - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<CommentDeleteResponse> type object along with Http status OK.
     * @throws AuthorizationFailedException
     * @throws CommentNotFoundException
     */
    @DeleteMapping(value = "/comment/delete/{commentId}")
    public ResponseEntity<CommentDeleteResponse> deleteComment(@PathVariable String commentId,@RequestHeader("authorization") String authorization)throws AuthorizationFailedException,CommentNotFoundException{
        CommentEntity comment = commentBusinessService.deleteComment(commentId,authorization);
        CommentDeleteResponse response = new CommentDeleteResponse();
        response.setId(comment.getUuid());
        response.setStatus("Comment Deleted");
        return new ResponseEntity<CommentDeleteResponse>(response,HttpStatus.OK);
    }

    /**
     * A controller method to fetch all the comments for a specific post in the database.
     *
     * @param postId    - The uuid of the post whose comments are to be fetched from the database.
     * @param authorization - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<List<CommentDetailsResponse>> type object along with Http status OK.
     * @throws AuthorizationFailedException
     * @throws InvalidPostException
     */
    @GetMapping(value = "comment/all/{postId}")
    public ResponseEntity<List<CommentDetailsResponse>> getCommentsByPost(@PathVariable String postId,@RequestHeader("authorization") String authorization)throws AuthorizationFailedException,InvalidPostException{
        TypedQuery<CommentEntity> commentEntities = commentBusinessService.getCommentsByPost(postId,authorization);
        List<CommentEntity> comments = commentEntities.getResultList();
        List<CommentDetailsResponse> responses = new ArrayList<>();
        if(comments != null){
            for(CommentEntity comment: comments){
                CommentDetailsResponse response = new CommentDetailsResponse();
                response.setId(comment.getUuid());
                response.setCommentContent(comment.getComment());
                response.setPostContent(comment.getPost().getContent());
                responses.add(response);
            }
        }
        return new ResponseEntity<List<CommentDetailsResponse>>(responses,HttpStatus.OK);
    }

}
