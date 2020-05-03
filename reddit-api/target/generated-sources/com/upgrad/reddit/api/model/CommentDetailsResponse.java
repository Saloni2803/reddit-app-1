package com.upgrad.reddit.api.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * CommentDetailsResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-04-11T05:59:49.291+05:30")

public class CommentDetailsResponse   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("postContent")
  private String postContent = null;

  @JsonProperty("commentContent")
  private String commentContent = null;

  public CommentDetailsResponse id(String id) {
    this.id = id;
    return this;
  }

  /**
   * comment uuid
   * @return id
  **/
  @ApiModelProperty(required = true, value = "comment uuid")
  @NotNull


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public CommentDetailsResponse postContent(String postContent) {
    this.postContent = postContent;
    return this;
  }

  /**
   * Content of the post
   * @return postContent
  **/
  @ApiModelProperty(required = true, value = "Content of the post")
  @NotNull


  public String getPostContent() {
    return postContent;
  }

  public void setPostContent(String postContent) {
    this.postContent = postContent;
  }

  public CommentDetailsResponse commentContent(String commentContent) {
    this.commentContent = commentContent;
    return this;
  }

  /**
   * Comment content
   * @return commentContent
  **/
  @ApiModelProperty(required = true, value = "Comment content")
  @NotNull


  public String getCommentContent() {
    return commentContent;
  }

  public void setCommentContent(String commentContent) {
    this.commentContent = commentContent;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CommentDetailsResponse commentDetailsResponse = (CommentDetailsResponse) o;
    return Objects.equals(this.id, commentDetailsResponse.id) &&
        Objects.equals(this.postContent, commentDetailsResponse.postContent) &&
        Objects.equals(this.commentContent, commentDetailsResponse.commentContent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, postContent, commentContent);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CommentDetailsResponse {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    postContent: ").append(toIndentedString(postContent)).append("\n");
    sb.append("    commentContent: ").append(toIndentedString(commentContent)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

