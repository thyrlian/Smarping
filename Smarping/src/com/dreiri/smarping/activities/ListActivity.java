package com.dreiri.smarping.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.dreiri.smarping.R;
import com.dreiri.smarping.adapters.ItemAdapter;
import com.dreiri.smarping.models.List;
import com.dreiri.smarping.persistence.PersistenceManager;

public class ListActivity extends Activity {
    
    private ListView listView;
    public List list = new List();
    public ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        PersistenceManager persistenceManager = new PersistenceManager(this);
        list = persistenceManager.readList();
        listView = (ListView) findViewById(R.id.listView);
        itemAdapter = new ItemAdapter(this, list);
        listView.setAdapter(itemAdapter);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                scrollToTop();
                break;
            case R.id.action_delete:
                Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
            }
            return true;
    }

    public void scrollToTop() {
        listView.smoothScrollToPosition(0);
    }

}