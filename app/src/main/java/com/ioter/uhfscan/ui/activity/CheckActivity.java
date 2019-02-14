package com.ioter.uhfscan.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.ioter.uhfscan.AppApplication;
import com.ioter.uhfscan.R;
import com.ioter.uhfscan.common.ScreenUtils;
import com.ioter.uhfscan.common.util.ACache;
import com.ioter.uhfscan.common.util.ToastUtil;
import com.ioter.uhfscan.data.greendao.EpcModel;
import com.ioter.uhfscan.data.greendao.dao.EpcModelDao;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckActivity extends NewBaseActivity {

    @BindView(R.id.tv_tick)
    TextView tvTick;
    @BindView(R.id.et_danhao)
    EditText etDanhao;
    @BindView(R.id.cb_check)
    CheckBox cbCheck;
    @BindView(R.id.cb_show)
    CheckBox cbShow;
    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    private static int RAC = 4;
    private boolean check = false;
    private boolean show = false;
    private ArrayList<Integer> scannedCodes = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        ButterKnife.bind(this);

        setTitle("盘点");

        cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check = true;
                } else {
                    check = false;
                }
            }
        });

        cbShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    show = true;
                    cbCheck.setVisibility(View.VISIBLE);
                } else {
                    show = false;
                    cbCheck.setVisibility(View.GONE);
                }
            }
        });
    }

    //获取扫描枪的扫描数据
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode!= KeyEvent.KEYCODE_ENTER){ //扫码枪以回车为结束
            scannedCodes.add(keyCode);
        }else{ //结束
            handleKeyCodes();
        }
        return super.onKeyDown(keyCode, event);
    }


    private void handleKeyCodes(){
        ScreenUtils fnString = new ScreenUtils();
        String result = "";
        boolean hasShift = false;
        for(int keyCode : scannedCodes){
            result += fnString.keyCodeToChar(keyCode, hasShift);
            hasShift = (keyCode == KeyEvent.KEYCODE_SHIFT_LEFT);
        }
        etDanhao.setText(result);
        //扫条码
        takeData();
        scannedCodes.clear();
    }


    private void takeData() {
        String barCode = etDanhao.getText().toString();
        String warehouse = ACache.get(AppApplication.getApplication()).getAsString("spinner");
        if (TextUtils.isEmpty(warehouse)){
            warehouse = "集美";
        }
        if (TextUtils.isEmpty(barCode)) {
            ToastUtil.toast("箱码不能为空，请扫描/输入盘点箱码");
            return;
        }
        if (show) {
            if (!barCode.equals("*")) {
                Query<EpcModel> nQuery = getEpcModelDao().queryBuilder()
                        .where(EpcModelDao.Properties.Warehouse.eq(warehouse))
                        .where(EpcModelDao.Properties.Casecode.eq(barCode)).build();
                List<EpcModel> users = nQuery.list();
                if (users == null || users.size() == 0) {
                    ToastUtil.toast("不存在该箱码，请确认盘点箱码");
                    return;
                }
                Intent intent = new Intent(CheckActivity.this, CheckMessActivity.class);
                intent.putExtra("barCode", barCode);
                intent.putExtra("check", check);
                startActivityForResult(intent, RAC);
            } else {
                Query<EpcModel> nQuery = getEpcModelDao().queryBuilder()
                        .where(EpcModelDao.Properties.Warehouse.eq(warehouse))
                        .build();
                List<EpcModel> users = nQuery.list();
                if (users == null || users.size() == 0) {
                    ToastUtil.toast("该仓库没有数据，请先入库");
                    return;
                }
                Intent intent = new Intent(CheckActivity.this, CheckMessActivity.class);
                intent.putExtra("barCode", barCode);
                intent.putExtra("check", check);
                startActivityForResult(intent, RAC);
            }
        } else {
            //查询数据详细
            Query<EpcModel> nQuery = getEpcModelDao().queryBuilder()
                    .where(EpcModelDao.Properties.Warehouse.eq(warehouse))
                    .build();
            List<EpcModel> users = nQuery.list();
            if (users == null || users.size() == 0) {
                ToastUtil.toast("该仓库没有数据，请先入库");
                return;
            }
            Intent intent = new Intent(CheckActivity.this, CheckDataActivity.class);
            intent.putExtra("barCode", barCode);
            intent.putExtra("check", check);
            startActivityForResult(intent, RAC);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RAC) {
            if (resultCode == RESULT_OK) {
                boolean a = data.getBooleanExtra("finish", true);
                if (a) {
                    etDanhao.setText("");
                } else {
                    finish();
                }
            }
        }
    }

    @OnClick({R.id.bt_sure, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_sure:
                takeData();
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }
}
