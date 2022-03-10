package com.jdeco.estimationapp.objects;

public class ActionLookUp {
    String actionCode;
    String actionName;

    public ActionLookUp(String actionCode, String actionName) {
        this.actionCode = actionCode;
        this.actionName = actionName;
    }

    public ActionLookUp() {
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    @Override
    public String toString() {
        return actionName;
    }
}
