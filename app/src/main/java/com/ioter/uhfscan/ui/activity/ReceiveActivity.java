package com.ioter.uhfscan.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.ioter.uhfscan.R;
import com.ioter.uhfscan.common.ScreenUtils;
import com.ioter.uhfscan.common.util.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReceiveActivity extends NewBaseActivity {

    @BindView(R.id.tv_tick)
    TextView tvTick;
    @BindView(R.id.et_danhao)
    EditText etDanhao;
    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    private static int RAG = 1;
    private ArrayList<Integer> scannedCodes = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        ButterKnife.bind(this);

        setTitle("入库");
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
        String barCode = etDanhao.getText().toString();
        if (TextUtils.isEmpty(barCode)) {
            ToastUtil.toast("箱码不能为空，请扫描/输入入库箱码");
            return;
        }
        Intent intent = new Intent(ReceiveActivity.this, ReceiveMessActivity.class);
        intent.putExtra("barCode", barCode);
        startActivityForResult(intent,RAG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RAG){
            if (resultCode == RESULT_OK){
                boolean a = data.getBooleanExtra("finish",true);
                if (a){
                    etDanhao.setText("");
                    querydata();
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
