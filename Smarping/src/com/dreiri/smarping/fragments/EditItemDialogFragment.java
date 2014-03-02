
package com.dreiri.smarping.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dreiri.smarping.R;

public class EditItemDialogFragment extends DialogFragment {

    public static EditItemDialogFragment newInstance(String itemName) {
        EditItemDialogFragment dialogFragment = new EditItemDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putString("itemName", itemName);
        dialogFragment.setArguments(bundle);

        return dialogFragment;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String itemName = getArguments().getString("itemName");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_edit_item, null);
        final EditText editTextItemName = (EditText) view.findViewById(R.id.itemName);
        editTextItemName.setText(itemName);
        editTextItemName.setSelection(editTextItemName.length());

        builder.setView(view)
                .setPositiveButton(R.string.btn_pos, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Toast.makeText(getActivity(), editTextItemName.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.btn_neg, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });

        return builder.create();
    }
}
