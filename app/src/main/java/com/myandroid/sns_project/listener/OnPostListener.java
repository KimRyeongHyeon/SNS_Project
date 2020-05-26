package com.myandroid.sns_project.listener;

import com.myandroid.sns_project.PostInfo;

public interface OnPostListener {
    void onDelete(PostInfo postInfo);
    void onModify();
}
