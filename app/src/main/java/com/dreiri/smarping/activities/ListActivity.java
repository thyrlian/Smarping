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
import android.view.ViewPropertyAnimator;
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
import com.dreiri.smarping.fragments.EditItemDialogFragment;
import com.dreiri.smarping.models.Item;
import com.dreiri.smarping.models.List;
import com.dreiri.smarping.persistence.PersistenceManager;
import com.dreiri.smarping.services.LocationService;
import com.dreiri.smarping.utils.EditItemDialogListener;
import com.dreiri.smarping.utils.MethodsOnAndroidVersionsUnification;
import com.dreiri.smarping.utils.ResultCallback;
import com.dreiri.smarping.utils.SimpleCallback;
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
        private float downX;
        private int swipeSlop = -1;
        private long previousClickTime;
        private int clickCount = 0;
        private static final int DOUBLE_CLICK_DELAY = 300;
        private boolean deleted = false;

        @Override
        public boolean onTouch(final View view, MotionEvent motionEvent) {
            final View viewParent = (View) view.getParent();
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
                    previousClickTime = System.currentTimeMillis();
                    clickCount++;
                    break;
                case MotionEvent.ACTION_CANCEL:
                    viewParent.setAlpha(1);
                    viewParent.setTranslationX(0);
                    itemPressed = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    {
                        float x = motionEvent.getX() + viewParent.getTranslationX();
                        float deltaX = x - downX;
                        float deltaXAbs = Math.abs(deltaX);
                        if (!swiping) {
                            if (deltaXAbs > swipeSlop) {
                                swiping = true;
                                listView.requestDisallowInterceptTouchEvent(true);
                                backgroundContainer.showBackground(viewParent.getTop(), viewParent.getHeight());
                            }
                        }
                        if (swiping) {
                            viewParent.setTranslationX(x - downX);
                            viewParent.setAlpha(1 - deltaXAbs / viewParent.getWidth());
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    {
                        if (swiping) {
                            float x = motionEvent.getX() + viewParent.getTranslationX();
                            float deltaX = x - downX;
                            float deltaXAbs = Math.abs(deltaX);
                            float fractionCovered;
                            float endX;
                            float endAlpha;
                            final boolean remove;
                            if (deltaXAbs > viewParent.getWidth() / 4) {
                                fractionCovered = deltaXAbs / viewParent.getWidth();
                                endX = deltaX < 0 ? -viewParent.getWidth() : viewParent.getWidth();
                                endAlpha = 0;
                                remove = true;
                                deleted = true;
                            } else {
                                fractionCovered = 1 - (deltaXAbs / viewParent.getWidth());
                                endX = 0;
                                endAlpha = 1;
                                remove = false;
                            }
                            long duration = Math.abs((int) ((1 - fractionCovered) * SWIPE_DURATION));
                            listView.setEnabled(false);
                            ViewPropertyAnimator viewPropertyAnimator = viewParent.animate().setDuration(duration).alpha(endAlpha).translationX(endX);
                            MethodsOnAndroidVersionsUnification.setEndActionAfterAnimation(viewPropertyAnimator, new SimpleCallback() {
                                @Override
                                public void execute() {
                                    viewParent.setAlpha(1);
                                    viewParent.setTranslationX(0);
                                    if (remove) {
                                        animateRemoval(listView, viewParent);
                                    } else {
                                        backgroundContainer.hideBackground();
                                        swiping = false;
                                        listView.setEnabled(true);
                                    }
                                }
                            });
                        }
                    }
                    if (!deleted) {
                        if (clickCount == 2 && System.currentTimeMillis() - previousClickTime < DOUBLE_CLICK_DELAY) {
                            int position = listView.getPositionForView(view);
                            Item item = (Item) itemAdapter.getItem(position);
                            EditItemDialogFragment editItemDialogFragment = EditItemDialogFragment.newInstance(position, item.name);
                            editItemDialogFragment.show(ListActivity.this.getFragmentManager(), "item");
                            clickCount = 0;
                        } else if (clickCount > 2) {
                            clickCount = 0;
                        }
                    }
                    deleted = false;
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
                                MethodsOnAndroidVersionsUnification.setEndActionAfterAnimation(child.animate(), new SimpleCallback() {
                                    @Override
                                    public void execute() {
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
                            MethodsOnAndroidVersionsUnification.setEndActionAfterAnimation(child.animate(), new SimpleCallback() {
                                @Override
                                public void execute() {
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