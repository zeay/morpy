package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.AnswerDetailsResponse;
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
public class AnswerController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private QuestionService questionService;

    @GetMapping(path = "/answer/all/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDetailsResponse> getAllAnswersToQuestion(@RequestHeader("authorization") final String authorization,
                                                                         @PathVariable("questionId") final String questionUuid) throws AuthorizationFailedException, InvalidQuestionException {

       UserEntity userEntity = authenticationService.validateTokenForGetAllAnswersEndpoint(authorization);

        QuestionEntity questionEntity =  questionService.getQuestionByUuid(questionUuid);

        //TO DO : add answer content after clarification of query on discussion form
        AnswerDetailsResponse answerDetailsResponse = new AnswerDetailsResponse().id(questionEntity.getUuid())
                .questionContent(questionEntity.getContent());

        return new ResponseEntity<AnswerDetailsResponse>(answerDetailsResponse, HttpStatus.OK);


    }

}
