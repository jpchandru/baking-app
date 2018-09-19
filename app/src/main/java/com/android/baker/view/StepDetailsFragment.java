package com.android.baker.view;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.baker.R;
import com.android.baker.model.Recipe;
import com.android.baker.model.RecipeDetailsViewModel;
import com.android.baker.model.RecipeStep;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;


public class StepDetailsFragment extends Fragment {
    private static final String TAG = StepDetailsFragment.class.getSimpleName();
    private static final String PLAYER_STATE = "player-state";

    private Recipe mRecipe;
    private ArrayList<RecipeStep> mStepsList;
    private RecipeStep mStep;
    private int stepId;
    private String mDetailedDescription;
    private SimpleExoPlayer player;
    private long playerPosition;
    private boolean isPlayerReady;
    private String videoUrlString;
    private int mStepPosition;
    private boolean hasNoVideoUrl;
    @BindView(R.id.tv_step_details)
    TextView descriptionTextVIew;
    @BindView(R.id.playerView)
    PlayerView playerView;
    @BindView(R.id.bt_previous_step)
    Button previousButton;
    @BindView(R.id.bt_next_step)
    Button nextButton;
    @BindDrawable(R.drawable.no_video_available)
    Drawable noVideoImage;

    public StepDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // video status check
            if (savedInstanceState.containsKey(PLAYER_STATE)) {
                playerPosition = savedInstanceState.getLong(PLAYER_STATE);
                mStepPosition = savedInstanceState.getInt("Step-Id");
                isPlayerReady = savedInstanceState.getBoolean("isPlaying");
                Log.d(TAG, "The saved instance of the video is :"
                        + String.valueOf(playerPosition) + " for step : "
                        + String.valueOf(mStepPosition));
            } else {
                playerPosition = C.TIME_UNSET;
            }

        }
        Bundle recipeBundle = this.getArguments();
        if (recipeBundle != null) {
            mRecipe = recipeBundle.getParcelable(MainActivity.RECIPE_KEY);
        } else {
            RecipeDetailsViewModel model = ViewModelProviders
                    .of(getActivity()).get(RecipeDetailsViewModel.class);
            mRecipe = model.getRecipe();
            stepId = model.getmRecipeStep();
        }


        String recipe = mRecipe.getName();
        Log.d(TAG, "The recipe name is: " + recipe);
        mStepsList = mRecipe.getRecipeStepList();

        mStep = mStepsList.get(stepId);

        mDetailedDescription = mStepsList.get(stepId).getDetailedDescription();
        descriptionTextVIew.setText(mDetailedDescription);
        descriptionTextVIew.setMovementMethod(new ScrollingMovementMethod());
        if (mStep.getVideoUrl().isEmpty()) {
            hasNoVideoUrl = true;
        }
        initializePlayer(Uri.parse(mStep.getVideoUrl()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_details, container, false);
        ButterKnife.bind(this, view);
        togglePreviousButton();
        previousButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (stepId > 0) {
                    stepId--;
                    mStep = mStepsList.get(stepId);
                    videoUrlString = mStep.getVideoUrl();
                    playerPosition = C.TIME_UNSET;
                    mDetailedDescription = mStepsList.get(stepId).getDetailedDescription();
                    descriptionTextVIew.setText(mDetailedDescription);
                    checkStepVideoUrl(videoUrlString);
                    initializePlayer(Uri.parse(videoUrlString));
                    togglePreviousButton();
                    toggleNextButton();
                }
            }
        });
        toggleNextButton();
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stepId < mStepsList.size() - 1) {
                    stepId++;
                    mStep = mStepsList.get(stepId);
                    videoUrlString = mStep.getVideoUrl();
                    playerPosition = C.TIME_UNSET;
                    mDetailedDescription = mStepsList.get(stepId).getDetailedDescription();
                    descriptionTextVIew.setText(mDetailedDescription);
                    checkStepVideoUrl(videoUrlString);
                    initializePlayer(Uri.parse(videoUrlString));
                    toggleNextButton();
                    togglePreviousButton();
                }
            }
        });
        return view;
    }

    private void toggleNextButton() {
        if (mStepsList != null && mStepsList.size() > 0) {
            if (stepId == mStepsList.size() - 1) nextButton.setEnabled(false);
            else if (stepId < mStepsList.size() - 1) nextButton.setEnabled(true);

        }
    }

    private void togglePreviousButton() {
        if (stepId == 0) previousButton.setEnabled(false);
        else if (stepId > 0) previousButton.setEnabled(true);
    }

    private void initializePlayer(Uri uri) {
        if (player == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            playerView.setPlayer(player);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(
                    new DefaultHttpDataSourceFactory("BakingApp")).createMediaSource(uri);

            if (hasNoVideoUrl) {
                Log.d(TAG, "No video available");
                playerView.setForeground(noVideoImage);
            } else {
                playerView.setForeground(null);
            }

            player.prepare(mediaSource);

            // maintain video play status
            if (playerPosition != C.TIME_UNSET) {
                player.seekTo(playerPosition);
            }

            player.setPlayWhenReady(true);
        }
    }

    public void releasePlayer() {
        player.stop();
        player.release();
        player = null;
    }

    public void checkStepVideoUrl(String videoUrl) {
        if (videoUrl.isEmpty()) {
            hasNoVideoUrl = true;
        } else {
            hasNoVideoUrl = false;
        }
        if (player != null) {
            releasePlayer();
        } else {
            hasNoVideoUrl = false;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (player != null) {
            player.stop();
            player.release();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (player != null) {
            player.stop();
            player.release();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            // video position inorder to maintain post screen rotation
            playerPosition = player.getCurrentPosition();
            isPlayerReady = player.getPlayWhenReady();
            player.stop();
            player.release();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            if (videoUrlString != null) {
                initializePlayer(Uri.parse(videoUrlString));
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (player != null) {
            outState.putLong(PLAYER_STATE, playerPosition);
            outState.putBoolean("isPlaying", isPlayerReady);
            outState.putInt("Step-Id", mStep.getId());
        }

    }
}
