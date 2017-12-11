/**
 * @author fapin
 * create at 2017-09-08 11:40:10
 */

package com.fapin.helper;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fapin.classtable.R;

public class DataAdapterForClassConfigClass extends BaseAdapter {
    private List<Map<String, String>> data;

    private LayoutInflater layoutInflater;

    public DataAdapterForClassConfigClass(Context context, List<Map<String, String>> data) {
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    /**
     * 组件集合，对应list.xml中的控件
     * 
     * @author Administrator
     */
    public final class Components {
        public TextView classNameTextView;

        public TextView teacherNameTextView;

    }

    @Override
    public int getCount() {
        return data.size();
    }

    /**
     * 获得某一位置的数据
     */
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    /**
     * 获得唯一标识
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Components components = null;
        if (convertView == null) {
            components = new Components();
            // 获得组件，实例化组件
            convertView = layoutInflater.inflate(R.layout.list_classconfig, null);
            components.classNameTextView = (TextView) convertView
                    .findViewById(R.id.textview_classconfig_class);
            components.teacherNameTextView = (TextView) convertView
                    .findViewById(R.id.textview_classconfig_teacher);
            convertView.setTag(components);
        } else {
            components = (Components) convertView.getTag();
        }
        // 绑定数据
        components.classNameTextView.setText((String) data.get(position).get("classname"));
        components.teacherNameTextView.setText((String) data.get(position).get("teacher"));
        return convertView;
    }
}
