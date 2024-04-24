package com.jacky.launcher.detail;

/**
 * @author jacky
 * @version v1.0
 * @since 16/8/28
 */

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter;

public class MediaDetailsDescriptionPresenter extends AbstractDetailsDescriptionPresenter {

    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object itemData) {

        if(itemData instanceof MediaModel mediaModel){
            viewHolder.getSubtitle().setText(mediaModel.getTitle());
            viewHolder.getBody().setText(mediaModel.getContent());
        }
    }
}