package com.yunsoo.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunsoo.activity.R;
import com.yunsoo.activity.R.drawable;
import com.yunsoo.activity.R.id;
import com.yunsoo.activity.R.layout;

import java.security.KeyStore;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Frank zhou on 7/15/2015.
 */
public class LogisticActionAdapter extends BaseAdapter
{
    List<Map<Integer,String>> actions;

    LayoutInflater inflater;
    Activity activity;

    public LogisticActionAdapter(Activity activity) {
        this.activity=activity;
        this.inflater = activity.getLayoutInflater();
    }
    public void setActions(List<Map<Integer, String>> actions) {
        this.actions = actions;
    }

    @Override
    public int getCount() {
        return actions.size();
    }

    @Override
    public Object getItem(int i) {
        return actions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view=inflater.inflate(layout.action_item_layout_bottom,viewGroup,false);
            ViewHolder holder=new ViewHolder();
            holder.tv_action= (TextView) view.findViewById(id.tv_action_name);
            holder.iv_action= (ImageView) view.findViewById(id.iv_image);
            view.setTag(holder);
        }
        ViewHolder holder= (ViewHolder) view.getTag();

        int actionId=actions.get(i).keySet().iterator().next();
        String actionName=actions.get(i).get(actionId);

        holder.tv_action.setText(actionName+"扫描");

        switch (actionName){
            case "入库":
                holder.iv_action.setImageResource(drawable.ic_box_in);
                break;
            case "出库":
                holder.iv_action.setImageResource(drawable.ic_box_out);
                break;
        }

        return view;
    }

    private final static class ViewHolder {
        TextView tv_action;
        ImageView iv_action;
    }

}
