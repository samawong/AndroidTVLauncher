package com.jacky.launcher.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.DetailsSupportFragment;
import androidx.leanback.widget.Action;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.DetailsOverviewLogoPresenter;
import androidx.leanback.widget.DetailsOverviewRow;
import androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnActionClickedListener;
import androidx.leanback.widget.SparseArrayObjectAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.jacky.launcher.video.VideoActivity;

/**
 * @author jacky
 * @version v1.0
 * @since 16/8/28
 */
public class MediaDetailsFragment extends DetailsSupportFragment {

    private ArrayObjectAdapter  mRowsAdapter;
    private MediaModel mMediaModel;
    private Context mContext;
    private static final int ACTION_WATCH_TRAILER = 1;

    private  BackgroundManager mBackgroundManager;
    private DisplayMetrics mMetrics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();
        mMediaModel = getActivity().getIntent().getParcelableExtra(MediaDetailsActivity.MEDIA);

        prepareBackgroundManager();
        buildDetails();
    }

    private void prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void buildDetails() {
         ClassPresenterSelector selector = new ClassPresenterSelector();
        FullWidthDetailsOverviewRowPresenter rowPresenter = new FullWidthDetailsOverviewRowPresenter(
                new MediaDetailsDescriptionPresenter(),
                new DetailsOverviewLogoPresenter());

        rowPresenter.setOnActionClickedListener(new OnActionClickedListener() {
            @Override
            public void onActionClicked(Action action) {
                if (action.getId() == ACTION_WATCH_TRAILER) {
                    Intent intent = new Intent(getActivity(), VideoActivity.class);
                    intent.putExtra(VideoActivity.VIDEO, mMediaModel);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), action.toString(), Toast.LENGTH_SHORT).show();
                }
            }

        });

        selector.addClassPresenter(DetailsOverviewRow.class, rowPresenter);
        selector.addClassPresenter(ListRow.class, new ListRowPresenter());
        mRowsAdapter = new ArrayObjectAdapter(selector);

        final DetailsOverviewRow detailsOverview = new DetailsOverviewRow(mMediaModel);

        Glide.with(mContext)
                .asBitmap()
                .load(mMediaModel.getImageUrl())
                .listener(new RequestListener<Bitmap>() {

                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        detailsOverview.setImageBitmap(mContext,resource);
                        return true;
                    }

                });

        updateBackground(mMediaModel.getImageUrl());

        SparseArrayObjectAdapter adapter = new SparseArrayObjectAdapter();
        if (!mMediaModel.getVideoUrl().isEmpty()) {
            adapter.set(ACTION_WATCH_TRAILER, new Action(ACTION_WATCH_TRAILER, "播放"));
        }
        detailsOverview.setActionsAdapter(adapter);
        mRowsAdapter.add(detailsOverview);

        setAdapter(mRowsAdapter);
    }

    private void updateBackground(String uri) {
        Glide.with(this)
                .asBitmap()
                .load(uri)
                .centerCrop()
                .into(new CustomTarget<Bitmap>(mMetrics.widthPixels, mMetrics.heightPixels) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        mBackgroundManager.setBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                }
     );
    }

    @Override
    public void onStop() {
        mBackgroundManager.release();
        super.onStop();
    }
}