package com.ioter.uhfscan.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
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

public class GroundActivity extends NewBaseActivity {

    @BindView(R.id.tv_tick)
    TextView tvTick;
    @BindView(R.id.et_danhao)
    EditText etDanhao;
    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    private static int RAB = 2;
    private ArrayList<Integer> scannedCodes = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ground);
        ButterKnife.bind(this);

        setTitle("出库");
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
        etDanhao.setText(result.toUpperCase());
        //扫条码
        takeData();
        scannedCodes.clear();
    }

    private void takeData() {
        String warehouse = ACache.get(AppApplication.getApplication()).getAsString("spinner");
        String barCode = etDanhao.getText().toString();
        if (TextUtils.isEmpty(barCode)) {
            ToastUtil.toast("箱码不能为空，请扫描/输入出库箱码");
            return;
        }
        Query<EpcModel> nQuery = getEpcModelDao().queryBuilder()
                .where(EpcModelDao.Properties.Warehouse.eq(warehouse))
                .where(EpcModelDao.Properties.Casecode.eq(barCode)).build();
        List<EpcModel> users = nQuery.list();
        if (users==null || users.size()==0){
            ToastUtil.toast("不存在该箱码，请确认出库箱码");
            return;
        }
        Intent intent = new Intent(GroundActivity.this, GroundMessActivity.class);
        intent.putExtra("barCode", barCode);
        startActivityForResult(intent,RAB);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RAB){
            if (resultCode == RESULT_OK){
                boolean a = data.getBooleanExtra("finish",true);
                if (a){
                    etDanhao.setText("");
                }else {
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
