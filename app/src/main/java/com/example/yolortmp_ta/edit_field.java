package com.example.yolortmp_ta;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class edit_field extends AppCompatDialogFragment {
    private EditText RTMPinputEdit;
    private edit_field_listerner listener;

    public interface edit_field_listerner{
        void applyText(String RTMPurl);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_field,null);

        builder.setView(view).setTitle("Edit").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String RTMPurl = RTMPinputEdit.getText().toString();
                listener.applyText(RTMPurl);

            }
        });

        RTMPinputEdit = view.findViewById(R.id.RTMPinputEdit);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (edit_field_listerner) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement edit_field_listener");
        }
    }
}
