package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionDeleteResponse;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class QuestionController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private QuestionService questionService;

    @DeleteMapping(path = "/question/delete/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDeleteResponse> delete(@RequestHeader("authorization") final String authorization,
                                                         @PathVariable("questionId") final String questionUuid) throws AuthorizationFailedException, InvalidQuestionException {

        UserEntity userEntity =  authenticationService.validateTokenForDeleteQuestionEndpoint(authorization);
        QuestionEntity questionEntity = questionService.getQuestionByUuid(questionUuid);

        // Removed extra parameter
        questionService.authorize(questionEntity, userEntity);
        questionService.delete(questionEntity);

        QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse().id( questionEntity.getUuid()).status("QUESTION DELETED");
        return new ResponseEntity<QuestionDeleteResponse>( questionDeleteResponse, HttpStatus.ACCEPTED);

    }


}
