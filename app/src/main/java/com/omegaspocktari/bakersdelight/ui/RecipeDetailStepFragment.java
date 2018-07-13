package com.omegaspocktari.bakersdelight.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.omegaspocktari.bakersdelight.R;
import com.omegaspocktari.bakersdelight.data.RecipeSteps;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ${Michael} on 7/6/2017.
 */

//TODO: Add VideoEXO PLAYER MAX VIDEO PLAY SEE
public class RecipeDetailStepFragment extends Fragment {

    //Logging Tag
    private static final String LOG_TAG = RecipeDetailStepFragment.class.getSimpleName();
    //Integer Constants
    private static final int CURRENT_STEP_MOVE_BACKWARDS = 1;
    private static final int CURRENT_STEP_MOVE_FORWARDS = 1;
    //Bandwidth Meter
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    //Views for Layout
    @BindView(R.id.tv_step_short_description)
    TextView mStepShortDescription;
    @BindView(R.id.tv_step_description)
    TextView mStepDescription;
    @BindView(R.id.btn_step_previous)
    Button mPreviousStepButton;
    @BindView(R.id.btn_step_next)
    Button mNextStepButton;
    @BindView(R.id.fl_button_container)
    FrameLayout mButtonContainer;
    @BindView(R.id.exo_player_view)
    SimpleExoPlayerView mExoPlayerView;
    @BindView(R.id.fl_exo_player_view_container)
    FrameLayout mExoPlayerContainer;
    @BindView(R.id.fl_step_description_container)
    FrameLayout mStepDescriptionContainer;

    View mRootView;

    // Exo Player for the exo player view
    private SimpleExoPlayer mExoPlayer;
    //RecipeBase Objects
    private RecipeSteps mRecipeStep;
    private List<RecipeSteps> mRecipeStepList;
    //Tablet Layout Tracker
    private boolean mTwoPane;
    //DetailStepFragment Initialized Tracker
    private boolean mInitialized = false;
    // Exo Player Window
    private int mCurrentWindow = 0;
    private long mPlaybackPosition = 0;
    // Exo Player timestamp

    //Button onClick methods
    @OnClick(R.id.btn_step_previous)
    public void previousStep() {
        //Current step id
        int currentStep = mRecipeStep.getID();
        Log.d(LOG_TAG, "btn_step_previous: " + currentStep);

        //If the current step is not the first load the next step
        if (currentStep > 0) {
            //Get previous step and setup details
            mRecipeStep = mRecipeStepList.get(currentStep - CURRENT_STEP_MOVE_BACKWARDS);
            Log.d(LOG_TAG, "btn_step_previous: if TRUE: " + currentStep);
            setupDetailStep();

            //  Make the exo player null to ready for the next steps details
            releasePlayer();

            //  Create the ExoPlayer if video data is present and hide the view if not
            if (mRecipeStep.mVideoURL != null && !mRecipeStep.mVideoURL.isEmpty()) {
                Log.d(LOG_TAG, "Video URL present" + mRecipeStep.mVideoURL);

                // Make the ExoPlayer initialize and reappear in the layout and leave enough room for both views
                mExoPlayerContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,5f));
                mStepDescriptionContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,  0, 4f));
                mExoPlayerView.setVisibility(View.VISIBLE);
                initializeExoPlayer(mRecipeStep.mVideoURL);

            } else if (mRecipeStep.mVideoURL.isEmpty() || mRecipeStep.mVideoURL == null) {
                Log.d(LOG_TAG, "Video URL not present");

                // Make the ExoPlayer release and disappear from the layout and have step details take up the extra space
                mExoPlayerContainer.setLayoutParams(new LinearLayout.LayoutParams(0, 0,0f));
                mStepDescriptionContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,  0, 9f));
                mExoPlayerView.setVisibility(View.GONE);
                releasePlayer();
            }
        } else {
            //Inform user that this is the first step
            Toast.makeText(getContext(), "This is the first step", Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.btn_step_next)
    public void nextStep() {
        //Current step id
        int currentStep = mRecipeStep.getID();
        Log.d(LOG_TAG, "btn_step_next: " + currentStep + " | " + mRecipeStepList.size());

        //If the current step is not the last load the next step
        if (currentStep < mRecipeStepList.size() - 1) {
            //Get next step and setup details
            mRecipeStep = mRecipeStepList.get(currentStep + CURRENT_STEP_MOVE_FORWARDS);
            Log.d(LOG_TAG, "btn_step_next: if TRUE: " + currentStep);
            setupDetailStep();

            //  Make the exo player null to ready for the next steps details
            releasePlayer();

            //  Create the ExoPlayer if video data is present and hide the view if not
            if (mRecipeStep.mVideoURL != null && !mRecipeStep.mVideoURL.isEmpty()) {
                Log.d(LOG_TAG, "Video URL present" + mRecipeStep.mVideoURL);

                // Make the ExoPlayer initialize and reappear in the layout and leave enough room for both views
                mExoPlayerContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,5f));
                mStepDescriptionContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,  0, 4f));
                mExoPlayerView.setVisibility(View.VISIBLE);
                initializeExoPlayer(mRecipeStep.mVideoURL);


            } else if (mRecipeStep.mVideoURL.isEmpty() || mRecipeStep.mVideoURL == null) {
                Log.d(LOG_TAG, "Video URL not present");

                // Make the ExoPlayer release and disappear from the layout and have step details take up the extra space
                mExoPlayerContainer.setLayoutParams(new LinearLayout.LayoutParams(0, 0,0f));
                mStepDescriptionContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,  0, 9f));
                mExoPlayerView.setVisibility(View.GONE);
                releasePlayer();
            }
        } else {
            //Inform user this is the last step
            Toast.makeText(getContext(), "This is the last step", Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Acquire the layout we wish to use and bind views
        mRootView = inflater.inflate(R.layout.recipe_detail_step_fragment, container, false);

        //Catch touch events so the touch doesn't go through and invoke the fragment prior
        mRootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        ButterKnife.bind(this, mRootView);

        if (savedInstanceState == null) {
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                mRecipeStep = Parcels.unwrap(bundle.getParcelable(getString(R.string.recipe_step_key)));
                mRecipeStepList = Parcels.unwrap(bundle.getParcelable(getString(R.string.recipe_step_list_key)));
                mTwoPane = bundle.getBoolean(getString(R.string.tablet_layout_key));
                setupDetailStep();
            }
        } else if (savedInstanceState != null) {
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                mRecipeStep = Parcels.unwrap(bundle.getParcelable(getString(R.string.recipe_step_key)));
                mRecipeStepList = Parcels.unwrap(bundle.getParcelable(getString(R.string.recipe_step_list_key)));
                mTwoPane = bundle.getBoolean(getString(R.string.tablet_layout_key));
                mInitialized = bundle.getBoolean(getString(R.string.detail_step_initialized));
                mPlaybackPosition = bundle.getLong(getString(R.string.exo_player_video_position));
                mCurrentWindow = bundle.getInt(getString(R.string.exo_player_video_window));
                setupDetailStep();
            }
        }

        if (mTwoPane && !mInitialized) {
            mButtonContainer.setVisibility(View.GONE);
        }

        // Check to see if ExoPlayer is needed
        //  Create the ExoPlayer if video data is present and hide the view if not

        if (mRecipeStep.mVideoURL != null && !mRecipeStep.mVideoURL.isEmpty()) {
            Log.d(LOG_TAG, "Video URL present" + mRecipeStep.mVideoURL);

            // Make the ExoPlayer initialize and reappear in the layout and leave enough room for both views
            mExoPlayerContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,5f));
            mStepDescriptionContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,  0, 4f));
            mExoPlayerView.setVisibility(View.VISIBLE);
            initializeExoPlayer(mRecipeStep.mVideoURL);

        } else if (mRecipeStep.mVideoURL.isEmpty() || mRecipeStep.mVideoURL == null) {
            Log.d(LOG_TAG, "Video URL not present");

            // Make the ExoPlayer release and disappear from the layout and have step details take up the extra space
            mExoPlayerContainer.setLayoutParams(new LinearLayout.LayoutParams(0, 0,0f));
            mStepDescriptionContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,  0, 9f));
            mExoPlayerView.setVisibility(View.GONE);
            releasePlayer();
        }
        Log.d(LOG_TAG, "Quack: " + mRecipeStep.mVideoURL);

        return mRootView;
    }

    private void initializeExoPlayer(String url) {
        if (mExoPlayer == null) {
            // Create an ExoPlayer instance
            TrackSelection.Factory adaptiveTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(adaptiveTrackSelectionFactory),
                    new DefaultLoadControl()
            );
            mExoPlayerView.setPlayer(mExoPlayer);

            // Prep the media source
            String userAgent = Util.getUserAgent(getContext(), "BakersDelight");
            MediaSource mediaSource = buildMediaSource(url, userAgent);

            mExoPlayer.setPlayWhenReady(true);
            mExoPlayer.seekTo(mCurrentWindow, mPlaybackPosition);

            mExoPlayer.prepare(mediaSource);
        }

    }

    private MediaSource buildMediaSource(String url, String userAgent) {
        DataSource.Factory manifestDataSourceFactory =
                new DefaultHttpDataSourceFactory(userAgent);

        return new ExtractorMediaSource(Uri.parse(url), new DefaultDataSourceFactory(
                (getActivity()), userAgent), new DefaultExtractorsFactory(), null, null);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mPlaybackPosition = mExoPlayer.getCurrentPosition();
            mCurrentWindow = mExoPlayer.getCurrentWindowIndex();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    /**
     * Called when the Fragment is no longer resumed.  This is generally
     * tied to {@link Activity#onPause() Activity.onPause} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    /**
     * Called when the Fragment is visible to the user.  This is generally
     * tied to {@link Activity#onStart() Activity.onStart} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStart() {
        super.onStart();
        if (mRecipeStep.mVideoURL != null && mExoPlayer == null) {
            initializeExoPlayer(mRecipeStep.mVideoURL);
        }
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();
        if (mRecipeStep.mVideoURL != null && mExoPlayer == null) {
            initializeExoPlayer(mRecipeStep.mVideoURL);
        }
    }

    /**
     * Called when the Fragment is no longer started.  This is generally
     * tied to {@link Activity#onStop() Activity.onStop} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void setupDetailStep() {

        //Set proper step info
        mStepShortDescription.setText(mRecipeStep.getShortDescription());
        mStepDescription.setText(mRecipeStep.getDescription());
        if (mRecipeStep.getID() == 0) {
            mStepDescription.setVisibility(View.INVISIBLE);
        } else {
            mStepDescription.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Store user's current step location
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.recipe_step_key), Parcels.wrap(mRecipeStep));
        outState.putParcelable(getString(R.string.recipe_step_list_key), Parcels.wrap(mRecipeStepList));
        outState.putBoolean(getString(R.string.detail_step_initialized), mInitialized);
        outState.putBoolean(getString(R.string.tablet_layout_key), mTwoPane);
        outState.putLong(getString(R.string.exo_player_video_position), mPlaybackPosition);
        outState.putInt(getString(R.string.exo_player_video_window), mCurrentWindow);

    }

    /**
     * Restore user's instance state and the current step the user is on if fragment is reloaded
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            Log.d(LOG_TAG, "Am I run here? WHERE EXPECTED?");
            mRecipeStep = Parcels.unwrap(savedInstanceState.getParcelable(getString(R.string.recipe_step_key)));
            mRecipeStepList = Parcels.unwrap(savedInstanceState.getParcelable(getString(R.string.recipe_step_list_key)));
            mInitialized = savedInstanceState.getBoolean(getString(R.string.detail_step_initialized));
            setupDetailStep();
        }
    }
}
