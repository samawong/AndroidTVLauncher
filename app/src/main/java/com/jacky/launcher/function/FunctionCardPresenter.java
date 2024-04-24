package com.jacky.launcher.function;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.Presenter;

import com.jacky.launcher.R;

/**
 * ImageCard Presenter
 *
 * @author jacky
 * @version v1.0
 * @since 16/7/16
 */
public class FunctionCardPresenter extends Presenter {
    private  int mSelectedBackgroundColor = -1;
    private  int mDefaultBackgroundColor = -1;
    private Context mContext;
    private final int CARD_WIDTH = 313;
    private final int CARD_HEIGHT = 176;
    private Drawable mDefaultCardImage;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        mDefaultBackgroundColor = ContextCompat.getColor(parent.getContext(),R.color.default_background);
        mSelectedBackgroundColor = ContextCompat.getColor(parent.getContext(),R.color.selected_background);
        ImageCardView cardView = new ImageCardView(parent.getContext()) {
            @Override
            public void setSelected(boolean selected) {

                updateCartBackgroundColor(this, selected);
                super.setSelected(selected);
            }
        };
        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        updateCartBackgroundColor(cardView,false);
        return new ViewHolder(cardView);
    }

    private void updateCartBackgroundColor(ImageCardView view, boolean selected){
        int color = selected ? mSelectedBackgroundColor : mDefaultBackgroundColor;

        view.setBackgroundColor(color);
        view.findViewById(androidx.leanback.R.id.info_field).setBackgroundColor(color);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        cardView.setMainImageDimensions(CARD_WIDTH,CARD_HEIGHT);
        FunctionModel functionModel = (FunctionModel) item;
        cardView.setMainImageScaleType(ImageView.ScaleType.CENTER_INSIDE);
        cardView.getMainImageView().setImageResource(functionModel.getIcon());
        cardView.setTitleText(functionModel.getName());
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        cardView.setBadgeImage(null);
        cardView.setMainImage(null);
    }
}
