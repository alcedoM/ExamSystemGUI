package com.examsys.modules;

import com.alibaba.fastjson.annotation.JSONField;

public class Quest {
    //定义题目类型常量
    public final static int CHOICE = 1;
    public final static int Fill_BLANK = 2;
    public final static int MULTI_CHOICE = 3;
    public final static int JUDEG_TF = 4;

    @JSONField(name = "QUEST_ID")
    private int questId;
    @JSONField(name = "QUEST_TYPE")
    private int questType;
    @JSONField(name = "QUEST_STEM")
    private String questStem;//题干
    @JSONField(name = "CHOICE")
    private String[] choice;
    @JSONField(name = "CURRENT_ANS")
    private String currentAns;
    @JSONField(name = "CORRECT_ANS")
    private String correctAns;

    public Quest(int questId, int questType, String questStem, String[] choice, String correctAns) {
        this.questId = questId;
        this.questType = questType;
        this.questStem = questStem;
        this.choice = choice;
        this.correctAns = correctAns;
    }

    public int getQuestId() {
        return questId;
    }

    public void setQuestId(int questId) {
        this.questId = questId;
    }

    public int getQuestType() {
        return questType;
    }

    public void setQuestType(int questType) {
        this.questType = questType;
    }

    public String getQuestStem() {
        return questStem;
    }

    public void setQuestStem(String questStem) {
        this.questStem = questStem;
    }

    public String[] getChoice() {
        return choice;
    }

    public void setChoice(String[] choice) {
        this.choice = choice;
    }

    public String getCurrentAns() {
        return currentAns;
    }

    public void setCurrentAns(String currentAns) {
        this.currentAns = currentAns;
    }

    public String getCorrectAns() {
        return correctAns;
    }

    public void setCorrectAns(String correctAns) {
        this.correctAns = correctAns;
    }
}
