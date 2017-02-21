package com.fblog.core.dao.entity;

public class Category extends BaseEntity {
    private String name;
    private int leftv;
    private int rightv;
    /* 是否显示 */
    private boolean visible = true;
    private String imgPath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLeftv() {
        return leftv;
    }

    public void setLeftv(int leftv) {
        this.leftv = leftv;
    }

    public int getRightv() {
        return rightv;
    }

    public void setRightv(int rightv) {
        this.rightv = rightv;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getImgPath() {
        if ("android".equalsIgnoreCase(name)) {
            return "/resource/img/android.svg";
        } else if ("ios".equalsIgnoreCase(name)) {
            return "/resource/img/ios.svg";
        } else if ("web".equalsIgnoreCase(name)) {
            return "/resource/img/web.svg";
        }else if ("server".equalsIgnoreCase(name)) {
            return "/resource/img/server.svg";
        }
        return "/resource/img/note.svg";
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
