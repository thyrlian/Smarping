package com.dreiri.smarping.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.dreiri.smarping.R;
import com.dreiri.smarping.utils.EditItemDialogListener;

public class EditItemDialogFragment extends DialogFragment {

    private EditText editTextItemName;
    private int itemPos;
    private String itemName;

    public static EditItemDialogFragment newInstance(int position, String itemName) {
        EditItemDialogFragment dialogFragment = new EditItemDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("itemPos", position);
        bundle.putString("itemName", itemName);
        dialogFragment.setArguments(bundle);

        return dialogFragment;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        itemPos = arguments.getInt("itemPos");
        itemName = arguments.getString("itemName");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_edit_item, null);
        editTextItemName = (EditText) view.findViewById(R.id.itemName);
        editTextItemName.setText(itemName);
        editTextItemName.setSelection(editTextItemName.length());
        editTextItemName.requestFocus();

        builder.setView(view);

        builder.setPositiveButton(R.string.btn_pos, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateItem();
            }
        });

        builder.setNegativeButton(R.string.btn_neg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER)) {
                    updateItem();
                    dismiss();
                    getActivity().getCurrentFocus().clearFocus();
                    return true;
                } else {
                    return false;
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return dialog;
    }

    private void updateItem() {
        String newItemName = editTextItemName.getText().toString();
        if (!newItemName.equals(itemName)) {
            EditItemDialogListener listener = (EditItemDialogListener) getActivity();
            listener.onFinishEditDialog(itemPos, newItemName);
        }
    }

}
