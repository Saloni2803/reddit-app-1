package com.upgrad.reddit.api.controller;

import com.upgrad.reddit.api.model.SigninResponse;
import com.upgrad.reddit.api.model.SignoutResponse;
import com.upgrad.reddit.api.model.SignupUserRequest;
import com.upgrad.reddit.api.model.SignupUserResponse;
import com.upgrad.reddit.service.business.UserBusinessService;
import com.upgrad.reddit.service.entity.UserAuthEntity;
import com.upgrad.reddit.service.entity.UserEntity;
import com.upgrad.reddit.service.exception.AuthenticationFailedException;
import com.upgrad.reddit.service.exception.SignOutRestrictedException;
import com.upgrad.reddit.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserBusinessService userBusinessService;

    /**
     * A controller method for user signup.
     *
     * @param signupUserRequest - This argument contains all the attributes required to store user details in the database.
     * @return - ResponseEntity<SignupUserResponse> type object along with Http status CREATED.
     * @throws SignUpRestrictedException
     */
    @PostMapping(value = "/signup")
    public ResponseEntity<SignupUserResponse> signUp(@RequestBody SignupUserRequest signupUserRequest)throws SignUpRestrictedException{
        UserEntity user = userBusinessService.signup(convertToUserEntity(signupUserRequest));
        SignupUserResponse result = new SignupUserResponse();
        result.setId(user.getUuid());
        result.setStatus("User SuccessfullyRegistered");
        return  new ResponseEntity<SignupUserResponse>(result, HttpStatus.OK);
    }
    /**
     * A controller method for user authentication.
     *
     * @param authorization - A field in the request header which contains the user credentials as Basic authentication.
     * @return - ResponseEntity<SigninResponse> type object along with Http status OK.
     * @throws AuthenticationFailedException
     */
    @PostMapping(value = "/signin")
    public ResponseEntity<SigninResponse> authenticate(@RequestHeader("authorization") String authorization) throws  AuthenticationFailedException{
        String[] credentials = getCredentials(authorization);
        UserAuthEntity userAuthEntity = userBusinessService.authenticate(credentials[0],credentials[1]);
        SigninResponse result = new SigninResponse();
        result.setId(userAuthEntity.getUuid());
        result.setMessage("SIGNED IN SUCCESSFULLY");
        HttpHeaders headers = new HttpHeaders();
        headers.add("access_token", userAuthEntity.getAccessToken());
        return  new ResponseEntity<SigninResponse>(result,headers,HttpStatus.OK);
    }

    /**
     * A controller method for user signout.
     *
     * @param authorization - A field in the request header which contains the JWT token.
     * @return - ResponseEntity<SignoutResponse> type object along with Http status OK.
     * @throws SignOutRestrictedException
     */
    @PostMapping(value="/signout")
    public  ResponseEntity<SignoutResponse> signOut(@RequestHeader("authorization") String authorization) throws SignOutRestrictedException{
        UserAuthEntity userAuthEntity = userBusinessService.signout(authorization);
        SignoutResponse result = new SignoutResponse();
        result.setId(userAuthEntity.getUuid());
        result.setMessage("SIGNED OUT SUCCESSFULLY");
        return new ResponseEntity<SignoutResponse>(result,HttpStatus.OK);
    }

    private UserEntity convertToUserEntity(SignupUserRequest signupUserRequest){
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(signupUserRequest.getFirstName());
        userEntity.setLastName(signupUserRequest.getLastName());
        userEntity.setUserName(signupUserRequest.getUserName());
        userEntity.setPassword(signupUserRequest.getPassword());
        userEntity.setEmail(signupUserRequest.getEmailAddress());
        userEntity.setCountry(signupUserRequest.getCountry());
        userEntity.setContactNumber(signupUserRequest.getContactNumber());
        userEntity.setAboutMe(signupUserRequest.getAboutMe());
        userEntity.setDob(signupUserRequest.getDob());
        return userEntity;
    }

    private String[] getCredentials(String authorisation) throws AuthenticationFailedException{
        String[] credentials = new String[2];
        if(authorisation==null || (authorisation != null && !authorisation.startsWith("Basic "))){
            throw new AuthenticationFailedException("ATH-003","Use Basic username:password for credentials");
        }
        if(authorisation != null){
            String[] userNamePassword = authorisation.split(" ");
            if(userNamePassword != null && userNamePassword[1] != null){
                credentials = userNamePassword[1].split(":");
            }
        }
        return credentials;
    }
}
