/**
 * @author fapin
 * create at 2017-09-08 11:40:10
 */

package com.fapin.helper;

import java.util.List;
import java.util.Map;

import com.fapin.classtable.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DataAdapterForWeeklyConfigClass extends BaseAdapter {
    private List<Map<String, String>> data;

    private LayoutInflater layoutInflater;

    public DataAdapterForWeeklyConfigClass(Context context, List<Map<String, String>> data) {
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    /**
     * 组件集合，对应list.xml中的控件
     * 
     * @author Administrator
     */
    public final class Components {
        public TextView timeTextView;

        public TextView curriculumDetailsTextView;

        public TextView addressTextView;
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
            convertView = layoutInflater.inflate(R.layout.list_weeklyconfig, null);
            components.timeTextView = (TextView) convertView
                    .findViewById(R.id.textview_weeklyconfig_time);
            components.curriculumDetailsTextView = (TextView) convertView
                    .findViewById(R.id.textview_weeklyconfig_curriculum_details);
            components.addressTextView = (TextView) convertView
                    .findViewById(R.id.textview_weeklyconfig_address);
            convertView.setTag(components);
        } else {
            components = (Components) convertView.getTag();
        }
        // 绑定数据
        components.timeTextView.setText((String) data.get(position).get("time"));
        components.curriculumDetailsTextView.setText((String) data.get(position).get(
                "curriculumDetails"));
        components.addressTextView.setText((String) data.get(position).get("address"));
        return convertView;
    }
}
