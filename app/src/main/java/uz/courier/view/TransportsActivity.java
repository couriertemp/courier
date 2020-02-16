package uz.courier.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import uz.courier.R;

import uz.courier.adapters.TransportsAdapter;

import uz.courier.databinding.ActivityTransportsBinding;

import uz.courier.models.Transport;
import uz.courier.viewModel.TransportsViewModel;

import java.util.ArrayList;

public class TransportsActivity extends AppCompatActivity {

    private TransportsViewModel transportsViewModel;

    private ActivityTransportsBinding binding;

    private RecyclerView transportsView;

    private TransportsAdapter adapter;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);

        transportsViewModel = ViewModelProviders.of(this).get(TransportsViewModel.class);

        binding = DataBindingUtil.setContentView(TransportsActivity.this, R.layout.activity_transports);
        binding.setLifecycleOwner(this);
        binding.setTransportsViewModel(transportsViewModel);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        transportsView = findViewById(R.id.transportsView);

        transportsViewModel.getTransports();
        transportsViewModel.getMutableTransports().observe(this, transportListUpdateObserver);

    }

    Observer<ArrayList<Transport>> transportListUpdateObserver = new Observer<ArrayList<Transport>>() {
        @Override
        public void onChanged(@Nullable ArrayList<Transport> transports) {
            adapter = new TransportsAdapter(transports);
            transportsView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            transportsView.setAdapter(adapter);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        transportsViewModel.getMutableTransports().observe(this, transportListUpdateObserver);

    }
}
