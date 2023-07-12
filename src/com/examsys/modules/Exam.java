package com.examsys.modules;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

public class Exam {
    @JSONField(name = "EXAM_NAME")
    private String examName;
    @JSONField(name = "QUEST_NUM")
    private int questNum;
    @JSONField(name = "QUEST_NUM_NEED_ANS")
    private int questNumNeedAns;
    @JSONField(name = "TIME_LIMIT")
    private int timeLimit;
    @JSONField(name = "IS_PUBLISHED")
    private boolean isPublished;
    @JSONField(name = "QUEST_GROUP")
    private List<Quest> questGroup;
    @JSONField(name = "QUEST_ID_POINT")
    private int questIdPoint;

    public Exam(String examName, int questNumNeedAns, int timeLimit, boolean isPublished, List<Quest> questGroup) {
        this.examName = examName;
//        this.questNum = questNum;
        this.questNumNeedAns = questNumNeedAns;
        this.timeLimit = timeLimit;
        this.isPublished = isPublished;
        this.questGroup = questGroup;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public int getQuestNum() {
        return questNum;
    }

    public void setQuestNum(int questNum) {
        this.questNum = questNum;
    }

    public int getQuestNumNeedAns() {
        return questNumNeedAns;
    }

    public void setQuestNumNeedAns(int questNumNeedAns) {
        this.questNumNeedAns = questNumNeedAns;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public List<Quest> getQuestGroup() {
        return questGroup;
    }

    public void setQuestGroup(List<Quest> questGroup) {
        this.questGroup = questGroup;
    }

    public int getQuestIdPoint() {
        return questIdPoint;
    }

    public void setQuestIdPoint(int questIdPoint) {
        this.questIdPoint = questIdPoint;
    }
}
