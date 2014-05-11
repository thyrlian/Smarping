package com.dreiri.smarping.activities;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dreiri.smarping.R;
import com.dreiri.smarping.adapters.ItemAdapter;
import com.dreiri.smarping.exceptions.AlreadyExists;
import com.dreiri.smarping.exceptions.NullValue;
import com.dreiri.smarping.models.List;
import com.dreiri.smarping.persistence.PersistenceManager;
import com.dreiri.smarping.utils.EditItemDialogListener;
import com.dreiri.smarping.views.DrawableRightOnTouchListener;

public class ListActivity extends Activity implements EditItemDialogListener {

    public static final int REQUEST_SPEECH = 101;
    private ListView listView;
    private EditText editTextNewItem;
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
        editTextNewItem = (EditText) findViewById(R.id.editTextNewItem);
        editTextNewItem.setOnTouchListener(new DrawableRightOnTouchListener(editTextNewItem) {
            @Override
            public boolean onDrawableTouch(MotionEvent event) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                startActivityForResult(intent, REQUEST_SPEECH);
                return false;
            }
        });
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
            int[] checkedIndexes = itemAdapter.getIndexesOfCheckedItems();
            list.remove(checkedIndexes);
            itemAdapter.refreshWithNewDataAndResetCheckBoxes(list);
            PersistenceManager persistenceManager = new PersistenceManager(this);
            persistenceManager.saveList(list);
            break;
        default:
            break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SPEECH) {
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (text.size() > 0) {
                    editTextNewItem.setText(text.get(0));
                    editTextNewItem.setSelection(editTextNewItem.getText().length());
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInputFromWindow(listView.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                }
            }
        }
    }

    public void scrollToTop() {
        listView.smoothScrollToPosition(0);
    }

    @Override
    public void onFinishEditDialog(int position, String text) {
        try {
            list.modify(position, text);
        } catch (NullValue e) {
            Toast.makeText(this, R.string.toast_null_value, Toast.LENGTH_SHORT).show();
        } catch (AlreadyExists e) {
            Toast.makeText(this, R.string.toast_already_exists, Toast.LENGTH_SHORT).show();
        }
        itemAdapter.refreshWithNewData(list);
        PersistenceManager persistenceManager = new PersistenceManager(this);
        persistenceManager.saveList(list);
    }

}
