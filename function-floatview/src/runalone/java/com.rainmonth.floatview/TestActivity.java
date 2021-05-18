package com.rainmonth.floatview;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainmonth.utils.DensityUtils;
import com.rainmonth.utils.log.LogUtils;

public class TestActivity extends AppCompatActivity {

    private static int spanCount = 6;
    private RecyclerView rvWordCard;
    private BookCollectionWordCardDialogAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floatview_activity_test);

        rvWordCard = findViewById(R.id.rv_word_card);

        GridLayoutManager manager = new GridLayoutManager(this, spanCount);
        rvWordCard.setLayoutManager(manager);
        mAdapter = new BookCollectionWordCardDialogAdapter(this);
        rvWordCard.setItemAnimator(null);
        rvWordCard.setAdapter(mAdapter);

    }


    static class BookCollectionWordCardDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context context;
        private float wordCardSize;

        public BookCollectionWordCardDialogAdapter(Context context) {
            this.context = context;
            this.wordCardSize = (DensityUtils.getScreenWidth(context) - 2 * DensityUtils.dip2px(context, 22)) * 1.0f / spanCount;
            LogUtils.d("Randy", "wordCardSize: " + wordCardSize);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View rootView = LayoutInflater.from(context).inflate(R.layout.floatview_view_holder_book_collection_word_card_item, parent, false);
            KaDaWordCardView wordCardView = rootView.findViewById(R.id.word_card_view);
            wordCardView.getLayoutParams().width = (int) wordCardSize;
            wordCardView.getLayoutParams().height = (int) wordCardSize;

            WordCardViewHolder holder = new WordCardViewHolder(rootView);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof WordCardViewHolder) {
                // todo
            }
        }

        @Override
        public int getItemCount() {
            // todo
            return 50;
        }
    }

    static class WordCardViewHolder extends RecyclerView.ViewHolder {
        KaDaWordCardView wordCardView;

        public WordCardViewHolder(@NonNull View itemView) {
            super(itemView);
            wordCardView = itemView.findViewById(R.id.word_card_view);

        }
    }
}
