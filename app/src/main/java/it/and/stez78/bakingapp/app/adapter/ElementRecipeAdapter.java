package it.and.stez78.bakingapp.app.adapter;

import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import it.and.stez78.bakingapp.R;
import it.and.stez78.bakingapp.model.Recipe;
import it.and.stez78.bakingapp.repository.RecipesRepository;

public class ElementRecipeAdapter extends RecyclerView.Adapter<ElementRecipeAdapter.ViewHolder> {

    private static final String TAG = "ElRecipeAdapter";

    private List<Recipe> dataSet;
    private OnRecipeClickListener recipeClickListener;

    private Picasso p;
    private RecipesRepository recipesRepository;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardContainer;
        private TextView title;
        private ImageView img;

        public ViewHolder(View v) {
            super(v);
            cardContainer = v.findViewById(R.id.list_element_recipe_card_container);
            title = v.findViewById(R.id.list_element_recipe_title);
            img = v.findViewById(R.id.list_element_recipe_image);
        }

        public CardView getCardContainer() {
            return cardContainer;
        }

        public TextView getTitle() {
            return title;
        }

        public ImageView getImg() {
            return img;
        }

    }

    public ElementRecipeAdapter(List<Recipe> dataSet, Picasso picasso, RecipesRepository recipesRepository,
                                OnRecipeClickListener recipeClickListener) {
        this.dataSet = dataSet;
        this.p = picasso;
        this.recipesRepository = recipesRepository;
        this.recipeClickListener = recipeClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_element_recipe, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Recipe el = dataSet.get(position);
        viewHolder.getTitle().setText(el.getName());

        recipesRepository.getThumbUrlFromRecipeId(el.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(thumbUrl -> {
                    p.load(Uri.parse(thumbUrl))
                            .placeholder(R.drawable.cakeslice)
                            .into(viewHolder.getImg());
                });

        viewHolder.getCardContainer().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeClickListener.OnRecipeClick(el);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
