package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    public QuestionEntity getQuestionByUuid(String questionUuid) throws InvalidQuestionException {
        QuestionEntity questionEntity = questionDao.getQuestionByUuid(questionUuid);
        if(questionEntity == null){
            throw new InvalidQuestionException("QUES-001", "The question with entered uuid whose details are to be seen does not exist");
        }
        return questionEntity;
    }

     /**
   * Creates question in the DB if the accessToken is valid.
   *
   * @param accessToken accessToken of the user for valid authentication.
   * @throws AuthorizationFailedException ATHR-001 - if user token is not present in DB. ATHR-002 if
   *     the user has already signed out.
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public QuestionEntity createQuestion(QuestionEntity questionEntity, final String accessToken)
      throws AuthorizationFailedException {
    UserAuthEntity userAuthEntity = userAuthDao.getUserAuthByToken(accessToken);
    if (userAuthEntity == null) {
      throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    } else if (userAuthEntity.getLogoutAt() != null) {
      throw new AuthorizationFailedException(
          "ATHR-002", "User is signed out.Sign in first to post a question");
    }
    questionEntity.setDate(ZonedDateTime.now());
    questionEntity.setUuid(UUID.randomUUID().toString());
    questionEntity.setUserEntity(userAuthEntity.getUserEntity());
    return questionDao.createQuestion(questionEntity);
  }
}
