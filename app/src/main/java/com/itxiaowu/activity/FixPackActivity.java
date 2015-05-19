package com.itxiaowu.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.itxiaowu.adapter.ProductInPackageAdapter;
import com.itxiaowu.fileOpreation.PackDetailFileRead;
import com.itxiaowu.utils.StringUtils;
import com.itxiaowu.view.TitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FixPackActivity extends Activity {
    private TitleBar titleBar;
    private EditText et_fix_barcode;
    private List<String> productCodes;
    private ProductInPackageAdapter adapter;
    private ListView lv_fix_products;
    private EditText et_get_packCode;
    private EditText et_get_productCode;
    private AlertDialog.Builder builder;
    private int originalSize;
    PackDetailFileRead fileRead;
    private File fixedFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_pack);

        getActionBar().hide();
        titleBar=(TitleBar) findViewById(R.id.fix_title_bar);
        titleBar.setMode(TitleBar.TitleBarMode.BOTH_BUTTONS);
        titleBar.setDisplayAsBack(true);
        titleBar.setTitle("修改包装");
        titleBar.setRightButtonText("完成");
        try {
            titleBar.setOnRightButtonClickedListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                       fixedFile=fileRead.getFixedFile();


                }
            });
        } catch (Exception e) {
            finish();
            e.printStackTrace();
        }

        productCodes=new ArrayList<String>();
        adapter=new ProductInPackageAdapter(FixPackActivity.this,getResources());
        adapter.setProductIdList(productCodes);
        lv_fix_products= (ListView) findViewById(R.id.lv_fix_products);
        lv_fix_products.setAdapter(adapter);
        et_fix_barcode= (EditText) findViewById(R.id.et_fix_barcode);
        et_get_packCode= (EditText) findViewById(R.id.et_get_packCode);
        et_get_productCode= (EditText) findViewById(R.id.et_get_productCode);
        et_get_packCode.requestFocus();
        bindPackBarcodeChanged();
        bindProductBarcodeChanged();
        bindListViewEvent();
    }



    private void bindListViewEvent(){
        lv_fix_products.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                final int po=position;
                builder=new AlertDialog.Builder(FixPackActivity.this);

                builder.setPositiveButton("删除",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        productCodes.remove(po);
                        adapter.notifyDataSetChanged();

                    }
                })
                 .create().show();


                return true;
            }

        });
    }

    private void bindProductBarcodeChanged() {
        et_get_productCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(productCodes.size()<originalSize){
                    String string = new StringBuilder(s).toString();
                    string=StringUtils.getLastString(StringUtils.replaceBlank(string));
                    productCodes.add(string);

                }

            }
        });
    }

    private void bindPackBarcodeChanged() {
        et_get_packCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = new StringBuilder(s).toString();
                string=StringUtils.getLastString(StringUtils.replaceBlank(string));
                et_fix_barcode.setText(string);
                productCodes.clear();
                et_get_packCode.clearFocus();
                et_get_productCode.requestFocus();
//                adapter.notifyDataSetChanged();

                try {
                    fileRead=new PackDetailFileRead("Pack_");
                    productCodes.addAll(fileRead.getProductsByPackCode(string));
                    originalSize=productCodes.size();
                    adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "找不到该包装码", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM , 0, 0);
                    toast.show();
                }
            }
        });
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_fix_pack, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
