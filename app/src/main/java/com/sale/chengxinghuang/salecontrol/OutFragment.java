package com.sale.chengxinghuang.salecontrol;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class OutFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "OutFragment";

    private Context mContext;
    private ListView mListView;
    private SimpleAdapter mAdapter;
    private List<Map<String, String>> mBeanList = new ArrayList<Map<String, String>>();

    public OutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mContext = getActivity();
        initBeanList();
        mAdapter = new SimpleAdapter(mContext, mBeanList, R.layout.fragment_main_list_item,
                new String[]{"title", "context"},
                new int[]{R.id.title, R.id.edit_context});

        mListView = view.findViewById(R.id.list);
        Button button = view.findViewById(R.id.ok);

        button.setOnClickListener(this);
        mListView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onClick(View view) {
        String[] context = new String[mAdapter.getCount()];
        for(int i = 0; i < mAdapter.getCount(); i++){
            RelativeLayout rl = (RelativeLayout) mListView.getChildAt(i);
            EditText contextEdit = rl.findViewById(R.id.edit_context);
            context[i] = contextEdit.getText().toString();
        }
        Log.d(TAG, "ID:" + context[0] + "; Number:" + context[1]
                + "; Price:" + context[2] + "; Total_Price:" + context[3] + "; Manufacturer:" + context[4]);

        try{
            if(context[0].isEmpty()){
                showAlertDialog(R.string.id_empty);
            }else if(context[1].isEmpty()){
                showAlertDialog(R.string.number_empty);
            }else if(Integer.parseInt(context[1]) <= 0){
                showAlertDialog(R.string.number_error);
            }else {
                ((MainActivity)mContext).updateTables(context[0], context[1], SQLiteUtils.TYPE_OUT, context[2], context[3], context[4], null, null);
            }
        }catch (NumberFormatException e){
            showAlertDialog(R.string.should_number);
        }
    }

    private void showAlertDialog(int message){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.attention);
        builder.setMessage(message);
        builder.setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    private void initBeanList(){
        mBeanList.clear();

        Map<String, String> map = new HashMap<String, String>();
        map.put("title", mContext.getString(R.string.id));
        map.put("context", "");
        mBeanList.add(map);

        map = new HashMap<String, String>();
        map.put("title", mContext.getString(R.string.number));
        map.put("context", "");
        mBeanList.add(map);

        map = new HashMap<String, String>();
        map.put("title", mContext.getString(R.string.customer));
        map.put("context", "");
        mBeanList.add(map);

        map = new HashMap<String, String>();
        map.put("title", mContext.getString(R.string.agent));
        map.put("context", "");
        mBeanList.add(map);

        map = new HashMap<String, String>();
        map.put("title", mContext.getString(R.string.price));
        map.put("context", "");
        mBeanList.add(map);

        map = new HashMap<String, String>();
        map.put("title", mContext.getString(R.string.total_price));
        map.put("context", "");
        mBeanList.add(map);
    }
}
