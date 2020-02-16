package uz.courier.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import uz.courier.R;
import uz.courier.adapters.DocumentsAdapter;
import uz.courier.databinding.ActivityDocumentsBinding;
import uz.courier.models.Document;
import uz.courier.viewModel.DocumentsViewModel;

import java.util.ArrayList;

public class DocumentsActivity extends AppCompatActivity {

    private DocumentsViewModel documentsViewModel;

    private ActivityDocumentsBinding binding;

    private RecyclerView documentsView;

    private DocumentsAdapter adapter;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);

        documentsViewModel = ViewModelProviders.of(this).get(DocumentsViewModel.class);

        binding = DataBindingUtil.setContentView(DocumentsActivity.this, R.layout.activity_documents);
        binding.setLifecycleOwner(this);
        binding.setDocumentsViewModel(documentsViewModel);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        documentsView = findViewById(R.id.documentsView);

        documentsViewModel.getMutableDocuments().observe(this, documentListUpdateObserver);

    }

    Observer<ArrayList<Document>> documentListUpdateObserver = new Observer<ArrayList<Document>>() {
        @Override
        public void onChanged(@Nullable ArrayList<Document> documents) {
            adapter = new DocumentsAdapter(documents);
            documentsView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            documentsView.setAdapter(adapter);
        }

    };

    @Override
    protected void onResume() {
        super.onResume();
        documentsViewModel.getMutableDocuments().observe(this, documentListUpdateObserver);
    }
}
