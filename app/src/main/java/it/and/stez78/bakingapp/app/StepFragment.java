package it.and.stez78.bakingapp.app;

import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.and.stez78.bakingapp.R;
import it.and.stez78.bakingapp.app.viewmodel.SharedRecipeViewModel;
import it.and.stez78.bakingapp.app.viewmodel.ViewModelFactory;
import it.and.stez78.bakingapp.di.Injectable;
import it.and.stez78.bakingapp.model.Step;

public class StepFragment extends Fragment implements Injectable {

    private static final String STEP_KEY = "step";
    private static final String PLAYER_POSITION_KEY = "playerPosition";

    @BindView(R.id.fragment_step_description)
    TextView description;

    @BindView(R.id.fragment_step_no_video_imageview)
    ImageView noVideo;

    @BindView(R.id.fragment_step_player)
    SimpleExoPlayerView simpleExoPlayerView;

    private SimpleExoPlayer exoPlayer;

    @Inject
    ViewModelFactory viewModelFactory;
    private SharedRecipeViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, rootView);
        viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(SharedRecipeViewModel.class);
        viewModel.getCurrentStepLiveData().observe(this, index -> {
            if (exoPlayer != null) {
                int position = viewModel.getPrevStepIndex() == null ? viewModel.getCurrentStepIndex() : viewModel.getPrevStepIndex();
                releasePlayerAndSavePosition(position);
                exoPlayer = null;
            }
            initializeUI(viewModel.getSteps().get(index));
        });
        return rootView;
    }

    private void initializeUI(Step step) {
        description.setText(step.getDescription());
        initializePlayer(step.getVideoURL());
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayerAndSavePosition();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayerAndSavePosition();
    }

    private void initializePlayer(String videoUrl) {
        if (videoUrl.isEmpty()) {
            simpleExoPlayerView.setVisibility(View.INVISIBLE);
            noVideo.setVisibility(View.VISIBLE);
        } else if (exoPlayer == null) {
            Uri mediaUri = Uri.parse(videoUrl);
            simpleExoPlayerView.setVisibility(View.VISIBLE);
            noVideo.setVisibility(View.INVISIBLE);
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(exoPlayer);

            String userAgent = Util.getUserAgent(getContext(), "BakingAppApplication");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            exoPlayer.prepare(mediaSource);
            if (viewModel.getPlayerPosition() != C.TIME_UNSET) {
                exoPlayer.seekTo(viewModel.getPlayerWindow(), viewModel.getPlayerPosition());
            }
            exoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayerAndSavePosition() {
        if (exoPlayer != null) {
            viewModel.setPlayerWindow(exoPlayer.getCurrentWindowIndex());
            viewModel.setPlayerPosition(exoPlayer.getCurrentPosition());
            exoPlayer.release();
        }
    }

    private void releasePlayerAndSavePosition(int stepIndex) {
        if (exoPlayer != null) {
            viewModel.setPlayerWindow(stepIndex, exoPlayer.getCurrentWindowIndex());
            viewModel.setPlayerPosition(stepIndex, exoPlayer.getCurrentPosition());
            exoPlayer.release();
        }
    }
}
