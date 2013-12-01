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
import com.dreiri.smarping.models.Item;
import com.dreiri.smarping.models.List;

public class ListActivity extends Activity {
    
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        List list = new List();
        list.add(new Item("鸡蛋"));
        list.add(new Item("牛奶"));
        list.add(new Item("面包"));
        list.add(new Item("美女"));
        list.add(new Item("手机"));
        list.add(new Item("漫画"));
        list.add(new Item("饮料"));
        list.add(new Item("酒水"));
        list.add(new Item("寿司"));
        list.add(new Item("篮球"));
        list.add(new Item("电脑"));
        list.add(new Item("手机"));
        list.add(new Item("家电"));
        list.add(new Item("极客装备"));
        list.add(new Item("天"));
        list.add(new Item("地"));
        list.add(new Item("人"));
        list.add(new Item("和"));
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ItemAdapter(this, list));
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                listView.setSelectionAfterHeaderView();
                listView.smoothScrollToPosition(0);
                break;
            case R.id.action_add:
                Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
            }
            return true;
    }

}