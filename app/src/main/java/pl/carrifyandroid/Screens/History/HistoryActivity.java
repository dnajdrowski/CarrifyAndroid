package pl.carrifyandroid.Screens.History;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.carrifyandroid.App;
import pl.carrifyandroid.Models.Rent;
import pl.carrifyandroid.R;

public class HistoryActivity extends AppCompatActivity implements HistoryAdapter.ItemClickListener {

    @BindView(R.id.historyRecycler)
    RecyclerView historyRecycler;
    @BindView(R.id.historyNoRents)
    TextView historyNoRents;

    @Inject
    HistoryManager historyManager;

    HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        App.component.inject(this);
        ButterKnife.bind(this);
        initRecyclerView();
        historyManager.getRentHistoryList();
    }

    @OnClick({R.id.historyBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.historyBack:
                finish();
                break;
        }
    }

    public void initRecyclerView() {
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        historyRecycler.setLayoutManager(horizontalLayoutManager);
        historyAdapter = new HistoryAdapter(this);
        historyAdapter.setClickListener(this);
        historyRecycler.setAdapter(historyAdapter);
        historyRecycler.setHasFixedSize(true);
        historyRecycler.setItemAnimator(null);
    }

    public void showRentList(List<Rent> body) {
        if (body.size() > 0) {
            historyAdapter.clear();
            historyAdapter.setList(body);
            historyNoRents.setVisibility(View.GONE);
        } else
            historyNoRents.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        historyManager.onAttach(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        historyManager.onStop();
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
