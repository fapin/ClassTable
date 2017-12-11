/**
 * @author fapin
 * create at 2017-09-08 11:40:10
 */

package com.fapin.helper;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fapin.classtable.R;

public class ModifyClassDialog extends Dialog {
    public ModifyClassDialog(Context context) {
        super(context);
    }

    public ModifyClassDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;

        private String title;

        private String positiveButtonText;

        private String negativeButtonText;

        private View contentView;

        private View mLayoutView;

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
        public ModifyClassDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final ModifyClassDialog dialog = new ModifyClassDialog(context, R.style.Dialog);
            mLayoutView = inflater.inflate(R.layout.dialog_modify_class, null);
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
                ((LinearLayout) mLayoutView.findViewById(R.id.linearlayout_time_config))
                        .removeAllViews();
                ((LinearLayout) mLayoutView.findViewById(R.id.linearlayout_time_config)).addView(
                        contentView, new LayoutParams(LayoutParams.FILL_PARENT,
                                LayoutParams.FILL_PARENT));
            }
            dialog.setContentView(mLayoutView);
            return dialog;
        }
    }
}
