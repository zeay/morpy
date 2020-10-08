package com.upgrad.quora.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name = "answer")
public class AnswerEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="id")
    private int id;

    @Column(name = "uuid")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name ="ans")
    @Size(max = 255)
    private String ans;

    @Column(name ="date")
    private ZonedDateTime date;

    @OneToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "question_id")
    private AnswerEntity questionId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn( name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public AnswerEntity getQuestionId() {
        return questionId;
    }

    public void setQuestionId(AnswerEntity questionId) {
        this.questionId = questionId;
    }

    public UserEntity getUserId() {
        return userId;
    }

    public void setUserId(UserEntity userId) {
        this.userId = userId;
    }
}
