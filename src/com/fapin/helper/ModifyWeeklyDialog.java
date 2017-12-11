/**
 * @author fapin
 * create at 2017-09-08 11:40:10
 */

package com.fapin.helper;

import java.util.ArrayList;
import java.util.List;

import com.fapin.classtable.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class ModifyWeeklyDialog extends Dialog {
    public ModifyWeeklyDialog(Context context) {
        super(context);
    }

    public ModifyWeeklyDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;

        private String title;

        private String positiveButtonText;

        private String negativeButtonText;

        private View contentView;

        private View mLayoutView;

        private int classId;

        private String className;

        private String classTeacher;

        private String address;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getClassId() {
            return classId;
        }

        public void setClassId(int classId) {
            this.classId = classId;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getClassTeacher() {
            return classTeacher;
        }

        public void setClassTeacher(String classTeacher) {
            this.classTeacher = classTeacher;
        }

        private DialogInterface.OnClickListener positiveButtonClickListener;

        private DialogInterface.OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * Set the Dialog title from resource
         * 
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         * 
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public String getEditTextById(int resId) {
            String result = null;
            EditText editText = null;
            if (null != mLayoutView) {
                editText = (EditText) mLayoutView.findViewById(resId);
                result = editText.getText().toString();
            }
            return result;
        }

        public Spinner getSpinner(int resId) {
            Spinner spinner = null;
            if (null != mLayoutView) {
                spinner = (Spinner) mLayoutView.findViewById(resId);
            }
            return spinner;
        }

        /**
         * Set the positive button resource and it's listener
         * 
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context.getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) context.getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        @SuppressLint("InflateParams")
        @SuppressWarnings("deprecation")
        public ModifyWeeklyDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final ModifyWeeklyDialog dialog = new ModifyWeeklyDialog(context, R.style.Dialog);
            mLayoutView = inflater.inflate(R.layout.dialog_modify_weekly, null);
            dialog.addContentView(mLayoutView, new LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
            // set the dialog title
            ((TextView) mLayoutView.findViewById(R.id.title)).setText(title);
            // set the confirm button
            if (positiveButtonText != null) {
                ((Button) mLayoutView.findViewById(R.id.positiveButton))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button) mLayoutView.findViewById(R.id.positiveButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                mLayoutView.findViewById(R.id.positiveButton).setVisibility(View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                ((Button) mLayoutView.findViewById(R.id.negativeButton))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) mLayoutView.findViewById(R.id.negativeButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                mLayoutView.findViewById(R.id.negativeButton).setVisibility(View.GONE);
            }
            // set the content message
            if (contentView != null) {
                // add the contentView to the dialog body
                ((LinearLayout) mLayoutView.findViewById(R.id.linearlayout_weekly_config))
                        .removeAllViews();
                ((LinearLayout) mLayoutView.findViewById(R.id.linearlayout_weekly_config)).addView(
                        contentView, new LayoutParams(LayoutParams.FILL_PARENT,
                                LayoutParams.FILL_PARENT));
            }
            Spinner spinner = (Spinner) mLayoutView.findViewById(R.id.spinner_class_details);
            setAdapterToSpinner(spinner);
            dialog.setContentView(mLayoutView);
            return dialog;
        }

        private void setAdapterToSpinner(final Spinner spinner) {
            Log.d("hhp", "12");
            DatabaseHelper databaseHelper = new DatabaseHelper(this.context);
            Cursor cursor = null;
            if (databaseHelper != null) {
                Log.d("hhp", "13");
                List<String> listToAdapter = new ArrayList<String>();
                List<ClassDetailsList> classListToAdapter = new ArrayList<ClassDetailsList>();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.context,
                        android.R.layout.simple_spinner_item, listToAdapter);
                ArrayAdapter<ClassDetailsList> classAdapter = new ArrayAdapter<ClassDetailsList>(
                        this.context, android.R.layout.simple_spinner_item, classListToAdapter);
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                cursor = databaseHelper.queryAllData(DatabaseHelper.CLASSTABLE);
                int classId = 0;
                String classname = "";
                String teacher = "";
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        classId = cursor.getInt(cursor.getColumnIndex("classid"));
                        classname = cursor.getString(cursor.getColumnIndex("classname"));
                        teacher = cursor.getString(cursor.getColumnIndex("teacher"));
                        // listToAdapter.add(classId + ":" + classname + "-" +
                        // teacher);
                        classListToAdapter.add(new ClassDetailsList(classId, classname, teacher));
                    }
                }
                // spinner.setAdapter(adapter);
                spinner.setAdapter(classAdapter);
                spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position,
                            long id) {
                        // TODO Auto-generated method stub
                        // ArrayAdapter<ClassDetailsList> currentView =
                        // (ArrayAdapter<ClassDetailsList>) adapterView
                        // .getAdapter();
                        // Log.d("hhp", currentView.getItem(position));
                        Log.d("hhp", "onItemSelected");
                        ClassDetailsList classDetailsList = (ClassDetailsList) spinner
                                .getSelectedItem();
                        Log.d("hhp", "classDetailsList: id = " + classDetailsList.getClassId()
                                + " | class = " + classDetailsList.getClassName() + " | teacher = "
                                + classDetailsList.getClassTeacher());
                        setClassId(classDetailsList.getClassId());
                        setClassName(classDetailsList.getClassName());
                        setClassTeacher(classDetailsList.getClassTeacher());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                        Log.d("hhp", "NothingSelected");
                    }
                });
            }
        }
    }

}
