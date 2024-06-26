
package com.jacky.launcher.main;

import static androidx.leanback.app.BrowseSupportFragment.HEADERS_ENABLED;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.BrowseSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.multidex.MultiDex;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jacky.launcher.R;
import com.jacky.launcher.app.AppCardPresenter;
import com.jacky.launcher.app.AppDataManage;
import com.jacky.launcher.app.AppModel;
import com.jacky.launcher.detail.MediaDetailsActivity;
import com.jacky.launcher.detail.MediaModel;
import com.jacky.launcher.function.FunctionCardPresenter;
import com.jacky.launcher.function.FunctionModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {
    private BrowseSupportFragment mBrowserSupportFragment;
    private ArrayObjectAdapter rowsAdapter;
    private BackgroundManager mBackgroundManager;
    private DisplayMetrics mMetrics;
    private Context mContext;

    @Override
    protected void attachBaseContext(Context context){
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        //mBrowseFragment = (BrowseFragment) getFragmentManager().findFragmentById(R.id.browse_fragment
        mBrowserSupportFragment = (BrowseSupportFragment) getSupportFragmentManager().findFragmentById(R.id.browse_fragment);
        //getSupportFragmentManager().beginTransaction().add(R.id.browse_fragment,mBrowserSupportFragment).commitAllowingStateLoss();

        mBrowserSupportFragment.setHeadersState(HEADERS_ENABLED);
        mBrowserSupportFragment.setTitle(getString(R.string.app_name));

        prepareBackgroundManager();
        buildRowsAdapter();
    }

    private void prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(this);
        mBackgroundManager.attach(this.getWindow());
        mMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void buildRowsAdapter() {
        rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        addPhotoRow();
        addVideoRow();
        addAppRow();
        addFunctionRow();

        mBrowserSupportFragment.setAdapter(rowsAdapter);
        mBrowserSupportFragment.setOnItemViewClickedListener(new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                if (item instanceof MediaModel mediaModel) {
                    Intent intent = new Intent(mContext, MediaDetailsActivity.class);
                    intent.putExtra(MediaDetailsActivity.MEDIA, mediaModel);

                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            (Activity) mContext,
                            ((ImageCardView) itemViewHolder.view).getMainImageView(),
                            MediaDetailsActivity.SHARED_ELEMENT_NAME).toBundle();
                    startActivity(intent, bundle);
                } else if (item instanceof AppModel appBean) {
                    Intent launchIntent = mContext.getPackageManager().getLaunchIntentForPackage(
                            appBean.getPackageName());
                    if (launchIntent != null) {
                        mContext.startActivity(launchIntent);
                    }
                } else if (item instanceof FunctionModel functionModel) {
                    Intent intent = functionModel.getIntent();
                    if (intent != null) {
                        startActivity(intent);
                    }
                }
            }

        });
        mBrowserSupportFragment.setOnItemViewSelectedListener(new OnItemViewSelectedListener() {
            //@Override
            public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                if (item instanceof MediaModel mediaModel) {

                    int width = mMetrics.widthPixels;
                    int height = mMetrics.heightPixels;

                    Glide.with(mContext)
                            .asBitmap()
                            .load(mediaModel.getImageUrl())
                            .centerCrop()
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                   int height = resource.getHeight();
                                   int width = resource.getWidth();
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }
                            });
                } else {
                    mBackgroundManager.setBitmap(null);
                }
            }
        });
    }

    private void addPhotoRow() {
        String headerName = getResources().getString(R.string.app_header_photo_name);
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new ImgCardPresenter());

        for (MediaModel mediaModel : MediaModel.getPhotoModels()) {
            listRowAdapter.add(mediaModel);
        }
        HeaderItem header = new HeaderItem(0, headerName);
        rowsAdapter.add(new ListRow(header, listRowAdapter));
    }

    private void addVideoRow() {
        String headerName = getResources().getString(R.string.app_header_video_name);
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new ImgCardPresenter());
        for (MediaModel mediaModel : MediaModel.getVideoModels()) {
            listRowAdapter.add(mediaModel);
        }
        HeaderItem header = new HeaderItem(0, headerName);
        rowsAdapter.add(new ListRow(header, listRowAdapter));
    }

    private void addAppRow() {
        String headerName = getResources().getString(R.string.app_header_app_name);
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new AppCardPresenter());

        ArrayList<AppModel> appDataList = new AppDataManage(mContext).getLaunchAppList();
        int cardCount = appDataList.size();

        for (int i = 0; i < cardCount; i++) {
            listRowAdapter.add(appDataList.get(i));
        }
        HeaderItem header = new HeaderItem(0, headerName);
        rowsAdapter.add(new ListRow(header, listRowAdapter));
    }

    private void addFunctionRow() {
        String headerName = getResources().getString(R.string.app_header_function_name);
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new FunctionCardPresenter());
        List<FunctionModel> functionModels = FunctionModel.getFunctionList(mContext);
        int cardCount = functionModels.size();
        for (int i = 0; i < cardCount; i++) {
            listRowAdapter.add(functionModels.get(i));
        }
        HeaderItem header = new HeaderItem(0, headerName);
        rowsAdapter.add(new ListRow(header, listRowAdapter));
    }
}
