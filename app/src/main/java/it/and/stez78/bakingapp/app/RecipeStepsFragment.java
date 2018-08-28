package it.and.stez78.bakingapp.app;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.and.stez78.bakingapp.R;
import it.and.stez78.bakingapp.app.adapter.ElementStepAdapter;
import it.and.stez78.bakingapp.app.adapter.OnStepClickListener;
import it.and.stez78.bakingapp.model.Step;

public class RecipeStepsFragment extends Fragment {

    @BindView(R.id.fragment_recipe_steps_rw)
    RecyclerView stepsRecyclerView;

    @BindView(R.id.fragment_recipe_steps_title)
    TextView title;

    private List<Step> steps = new ArrayList<>();

    private ElementStepAdapter stepAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public RecipeStepsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);
        ButterKnife.bind(this, rootView);
        layoutManager = new LinearLayoutManager(getActivity());
        stepsRecyclerView.setLayoutManager(layoutManager);
        stepAdapter = new ElementStepAdapter(getContext(), steps, (OnStepClickListener) getActivity());
        stepsRecyclerView.setAdapter(stepAdapter);
        Resources res = getResources();
        String text = String.format(res.getString(R.string.step_counter), steps.size());
        title.setText(text);
        return rootView;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public void setStepActive(Step s) {
        for (Step step : steps) {
            if (step.getStepId().equals(s.getStepId())) {
                step.setActive(true);
            } else {
                step.setActive(false);
            }
        }
        stepAdapter.notifyDataSetChanged();
    }
}
