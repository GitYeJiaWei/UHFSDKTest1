package com.ioter.uhfscan.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.ioter.uhfscan.AppApplication;
import com.ioter.uhfscan.R;
import com.ioter.uhfscan.bean.Spin;
import com.ioter.uhfscan.common.util.ACache;
import com.ioter.uhfscan.common.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends NewBaseActivity implements SeekBar.OnSeekBarChangeListener {
    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.sp_cangku)
    Spinner spCangku;
    @BindView(R.id.tv_tick)
    TextView tvTick;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.seekBar1)
    SeekBar seekBar1;
    @BindView(R.id.tvShow1)
    TextView tvShow1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.seekBar2)
    SeekBar seekBar2;
    @BindView(R.id.tvShow2)
    TextView tvShow2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.seekBar3)
    SeekBar seekBar3;
    @BindView(R.id.tvShow3)
    TextView tvShow3;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.seekBar4)
    SeekBar seekBar4;
    @BindView(R.id.tvShow4)
    TextView tvShow4;
    @BindView(R.id.et_warehouse)
    EditText etWarehouse;
    @BindView(R.id.btn_add)
    Button btnAdds;
    @BindView(R.id.btn_del)
    Button btnDels;
    @BindView(R.id.btn_sure)
    TextView btnSures;
    @BindView(R.id.li_warehouse)
    LinearLayout liWarehouse;
    private ArrayList<Spin> spinnerList;
    private boolean stage = true;
    private String selected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        setTitle("设置");
        initview();

    }

    private void setSpinner(){
        spinnerList = (ArrayList<Spin>) ACache.get(AppApplication.getApplication()).getAsObject("spinnerList");
        if (spinnerList==null || spinnerList.size()==0){
            spinnerList = new ArrayList<>();
            Spin spin = new Spin();
            spin.setName("集美");
            spinnerList.add(spin);
            ACache.get(AppApplication.getApplication()).put("spinnerList",spinnerList);
        }
        /*
         * 动态添显示下来菜单的选项，可以动态添加元素
         */
        List<String> list = new ArrayList<>();
        for (int i = 0; i < spinnerList.size(); i++) {
            list.add(spinnerList.get(i).getName());
        }
        /*
         * 第二个参数是显示的布局
         * 第三个参数是在布局显示的位置id
         * 第四个参数是将要显示的数据
         */
        ArrayAdapter adapter2 = new ArrayAdapter(this, R.layout.item, R.id.text_item, list);
        spCangku.setAdapter(adapter2);

        selected = ACache.get(AppApplication.getApplication()).getAsString("spinner");
        if (TextUtils.isEmpty(selected)) {
            selected = list.get(0);
            spCangku.setSelection(0, true);
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (selected.equals(list.get(i))) {
                    //设置默认值
                    spCangku.setSelection(i, true);
                    selected = list.get(i);
                }
            }
        }

        spCangku.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //将选择的元素显示出来
                selected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initview() {
        String key1 = ACache.get(AppApplication.getApplication()).getAsString("key1");
        if (TextUtils.isEmpty(key1)) {
            key1 = "15";
        }
        tvShow1.setText(key1);
        seekBar1.setProgress(Integer.valueOf(key1) - 5);

        String key2 = ACache.get(AppApplication.getApplication()).getAsString("key2");
        if (TextUtils.isEmpty(key2)) {
            key2 = "15";
        }
        tvShow2.setText(key2);
        seekBar2.setProgress(Integer.valueOf(key2) - 5);

        String key3 = ACache.get(AppApplication.getApplication()).getAsString("key3");
        if (TextUtils.isEmpty(key3)) {
            key3 = "15";
        }
        tvShow3.setText(key3);
        seekBar3.setProgress(Integer.valueOf(key3) - 5);

        String key4 = ACache.get(AppApplication.getApplication()).getAsString("key4");
        if (TextUtils.isEmpty(key4)) {
            key4 = "15";
        }
        tvShow4.setText(key4);
        seekBar4.setProgress(Integer.valueOf(key4) - 5);

        seekBar2.setOnSeekBarChangeListener(this);
        seekBar1.setOnSeekBarChangeListener(this);
        seekBar3.setOnSeekBarChangeListener(this);
        seekBar4.setOnSeekBarChangeListener(this);

        setSpinner();
    }


    @OnClick({R.id.bt_sure, R.id.btn_cancel, R.id.btn_add, R.id.btn_del, R.id.btn_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_sure:
                String key1 = tvShow1.getText().toString();
                ACache.get(AppApplication.getApplication()).put("key1", key1);
                String key2 = tvShow2.getText().toString();
                ACache.get(AppApplication.getApplication()).put("key2", key2);
                String key3 = tvShow3.getText().toString();
                ACache.get(AppApplication.getApplication()).put("key3", key3);
                String key4 = tvShow4.getText().toString();
                ACache.get(AppApplication.getApplication()).put("key4", key4);

                ACache.get(AppApplication.getApplication()).put("spinner", selected);
                ACache.get(AppApplication.getApplication()).put("spinnerList",spinnerList);

                finish();
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_add:
                if (liWarehouse.getVisibility()==View.VISIBLE){
                    liWarehouse.setVisibility(View.GONE);
                }else {
                    liWarehouse.setVisibility(View.VISIBLE);
                }
                stage = true;
                break;
            case R.id.btn_del:
                if (liWarehouse.getVisibility()==View.VISIBLE){
                    liWarehouse.setVisibility(View.GONE);
                }else {
                    liWarehouse.setVisibility(View.VISIBLE);
                    etWarehouse.setText(selected);
                }
                stage = false;
                break;
            case R.id.btn_sure:
                if (stage){
                    String name = etWarehouse.getText().toString();
                    if (TextUtils.isEmpty(name)){
                        ToastUtil.toast("请输入仓库名");
                    }else {
                        Spin spin = new Spin();
                        spin.setName(name);
                        spinnerList.add(spin);
                        ACache.get(AppApplication.getApplication()).put("spinner", name);
                        ACache.get(AppApplication.getApplication()).put("spinnerList",spinnerList);
                        liWarehouse.setVisibility(View.GONE);
                        setSpinner();
                    }
                }else {
                    String name = etWarehouse.getText().toString();
                    if (TextUtils.isEmpty(name)){
                        ToastUtil.toast("请输入仓库名");
                    }else {
                        for (int i = 0; i < spinnerList.size(); i++) {
                            if (spinnerList.get(i).getName().equals(name)){
                                spinnerList.remove(spinnerList.get(i));
                            }
                        }
                        ACache.get(AppApplication.getApplication()).put("spinnerList",spinnerList);
                        liWarehouse.setVisibility(View.GONE);
                        setSpinner();
                    }
                }
                break;
        }
    }

    //拖动中
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seekBar1:
                tvShow1.setText(progress + 5 + "");
                break;
            case R.id.seekBar2:
                tvShow2.setText(progress + 5 + "");
                break;
            case R.id.seekBar3:
                tvShow3.setText(progress + 5 + "");
                break;
            case R.id.seekBar4:
                tvShow4.setText(progress + 5 + "");
                break;
        }
    }

    //开始拖动
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    //停止拖动
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
