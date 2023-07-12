package com.examsys.modules;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class Answer {
    @JSONField(name = "EXAM_NAME")
    private String examName;
    @JSONField(name = "STUDENT_ID")
    private String studentID;
    @JSONField(name = "QUEST_GROUP")
    private List<Quest> questGroup;
    @JSONField(name = "SCORE")
    private int score;

    public Answer(String examName, String studentID, List<Quest> questGroup, int score) {
        this.examName = examName;
        this.studentID = studentID;
        this.questGroup = questGroup;
        this.score = score;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public List<Quest> getQuestGroup() {
        return questGroup;
    }

    public void setQuestGroup(List<Quest> questGroup) {
        this.questGroup = questGroup;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
