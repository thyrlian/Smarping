package com.dreiri.smarping.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dreiri.smarping.R;
import com.dreiri.smarping.adapters.ItemAdapter;
import com.dreiri.smarping.exceptions.AlreadyExistsException;
import com.dreiri.smarping.exceptions.LocationServicesNotAvailableException;
import com.dreiri.smarping.exceptions.NullValueException;
import com.dreiri.smarping.models.List;
import com.dreiri.smarping.persistence.PersistenceManager;
import com.dreiri.smarping.services.LocationService;
import com.dreiri.smarping.utils.EditItemDialogListener;
import com.dreiri.smarping.utils.ResultCallback;
import com.dreiri.smarping.views.BackgroundContainer;
import com.dreiri.smarping.views.DrawableRightOnTouchListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ListActivity extends Activity implements EditItemDialogListener {

    public static final int REQUEST_SPEECH = 101;
    private static final int SWIPE_DURATION = 250;
    private static final int MOVE_DURATION = 150;
    private ListView listView;
    private EditText editTextNewItem;
    public List list = new List();
    public ItemAdapter itemAdapter;
    private Menu menu;
    private Location location = null;
    private boolean swiping = false;
    private boolean itemPressed = false;
    private HashMap<Long, Integer> itemIdTopMap = new HashMap<Long, Integer>();
    private BackgroundContainer backgroundContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        PersistenceManager persistenceManager = new PersistenceManager(this);
        list = persistenceManager.readList();
        listView = (ListView) findViewById(R.id.listView);
        itemAdapter = new ItemAdapter(this, list, onTouchListener);
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
        backgroundContainer = (BackgroundContainer) findViewById(R.id.listViewBackground);
        setLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLocation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.activity_list, menu);
        updateMenu();
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
            item.setVisible(false);
            PersistenceManager persistenceManager = new PersistenceManager(this);
            persistenceManager.saveList(list);
            break;
        default:
            break;
        }
        return true;
    }

    public void updateMenu() {
        int[] checkedIndexes = itemAdapter.getIndexesOfCheckedItems();
        MenuItem item = menu.findItem(R.id.action_delete);
        if (item.isVisible() && checkedIndexes.length < 1) {
            item.setVisible(false);
            this.invalidateOptionsMenu();
        } else if (!item.isVisible() && checkedIndexes.length >= 1) {
            item.setVisible(true);
            this.invalidateOptionsMenu();
        }
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
        } catch (NullValueException e) {
            Toast.makeText(this, R.string.toast_null_value, Toast.LENGTH_SHORT).show();
        } catch (AlreadyExistsException e) {
            Toast.makeText(this, R.string.toast_already_exists, Toast.LENGTH_SHORT).show();
        }
        itemAdapter.refreshWithNewData(list);
        PersistenceManager persistenceManager = new PersistenceManager(this);
        persistenceManager.saveList(list);
    }

    private void setLocation() {
        try {
            LocationService locationService = new LocationService(this, new ResultCallback<Location>() {
                @Override
                public void execute(Location location) {
                    ListActivity.this.location = location;
                }
            });
            locationService.connect();
        } catch (LocationServicesNotAvailableException e) {
            this.location = null;
        }
    }

    public Location getLocation() {
        return location;
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        float downX;
        private int swipeSlop = -1;

        @Override
        public boolean onTouch(final View view, MotionEvent motionEvent) {
            if (swipeSlop < 0) {
                swipeSlop = ViewConfiguration.get(ListActivity.this).getScaledTouchSlop();
            }
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (itemPressed) {
                        return false;
                    }
                    itemPressed = true;
                    downX = motionEvent.getX();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    view.setAlpha(1);
                    view.setTranslationX(0);
                    itemPressed = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    {
                        float x = motionEvent.getX() + view.getTranslationX();
                        float deltaX = x - downX;
                        float deltaXAbs = Math.abs(deltaX);
                        if (!swiping) {
                            if (deltaXAbs > swipeSlop) {
                                swiping = true;
                                listView.requestDisallowInterceptTouchEvent(true);
                                backgroundContainer.showBackground(view.getTop(), view.getHeight());
                            }
                        }
                        if (swiping) {
                            view.setTranslationX(x - downX);
                            view.setAlpha(1 - deltaXAbs / view.getWidth());
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    {
                        if (swiping) {
                            float x = motionEvent.getX() + view.getTranslationX();
                            float deltaX = x - downX;
                            float deltaXAbs = Math.abs(deltaX);
                            float fractionCovered;
                            float endX;
                            float endAlpha;
                            final boolean remove;
                            if (deltaXAbs > view.getWidth() / 4) {
                                fractionCovered = deltaXAbs / view.getWidth();
                                endX = deltaX < 0 ? -view.getWidth() : view.getWidth();
                                endAlpha = 0;
                                remove = true;
                            } else {
                                fractionCovered = 1 - (deltaXAbs / view.getWidth());
                                endX = 0;
                                endAlpha = 1;
                                remove = false;
                            }
                            long duration = (int) ((1 - fractionCovered) * SWIPE_DURATION);
                            listView.setEnabled(false);
                            view.animate().setDuration(duration).alpha(endAlpha).translationX(endX).withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    view.setAlpha(1);
                                    view.setTranslationX(0);
                                    if (remove) {
                                        animateRemoval(listView, view);
                                    } else {
                                        backgroundContainer.hideBackground();
                                        swiping = false;
                                        listView.setEnabled(true);
                                    }
                                }
                            });
                        }
                    }
                    itemPressed = false;
                    break;
                default:
                    return false;
            }
            return true;
        }
    };

    private void animateRemoval(final ListView listview, View viewToRemove) {
        int firstVisiblePosition = listview.getFirstVisiblePosition();
        for (int i = 0; i < listview.getChildCount(); ++i) {
            View child = listview.getChildAt(i);
            if (child != viewToRemove) {
                int position = firstVisiblePosition + i;
                long itemId = itemAdapter.getItemId(position);
                itemIdTopMap.put(itemId, child.getTop());
            }
        }
        int position = listView.getPositionForView(viewToRemove);
        itemAdapter.remove(itemAdapter.getItem(position));

        final ViewTreeObserver observer = listview.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                observer.removeOnPreDrawListener(this);
                boolean firstAnimation = true;
                int firstVisiblePosition = listview.getFirstVisiblePosition();
                for (int i = 0; i < listview.getChildCount(); ++i) {
                    final View child = listview.getChildAt(i);
                    int position = firstVisiblePosition + i;
                    long itemId = itemAdapter.getItemId(position);
                    Integer startTop = itemIdTopMap.get(itemId);
                    int top = child.getTop();
                    if (startTop != null) {
                        if (startTop != top) {
                            int delta = startTop - top;
                            child.setTranslationY(delta);
                            child.animate().setDuration(MOVE_DURATION).translationY(0);
                            if (firstAnimation) {
                                child.animate().withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        backgroundContainer.hideBackground();
                                        swiping = false;
                                        listView.setEnabled(true);
                                    }
                                });
                                firstAnimation = false;
                            }
                        }
                    } else {
                        int childHeight = child.getHeight() + listview.getDividerHeight();
                        startTop = top + (i > 0 ? childHeight : -childHeight);
                        int delta = startTop - top;
                        child.setTranslationY(delta);
                        child.animate().setDuration(MOVE_DURATION).translationY(0);
                        if (firstAnimation) {
                            child.animate().withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    backgroundContainer.hideBackground();
                                    swiping = false;
                                    listView.setEnabled(true);
                                }
                            });
                            firstAnimation = false;
                        }
                    }
                }
                itemIdTopMap.clear();
                return true;
            }
        });
    }

}