package com.ioter.uhfscan.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ioter.uhfscan.AppApplication;
import com.ioter.uhfscan.R;
import com.ioter.uhfscan.bean.BaseEpc;
import com.ioter.uhfscan.bean.EPC;
import com.ioter.uhfscan.common.util.ACache;
import com.ioter.uhfscan.common.util.ToastUtil;
import com.ioter.uhfscan.data.greendao.EpcModel;
import com.ioter.uhfscan.data.greendao.dao.EpcModelDao;
import com.ioter.uhfscan.ui.adapter.CheckAdapter;
import com.ioter.uhfscan.ui.dialog.BaseDialog;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckMessActivity extends NewBaseActivity {

    @BindView(R.id.lv_scan)
    ListView lvScan;
    @BindView(R.id.edt_saomiao)
    EditText edtSaomiao;
    @BindView(R.id.edt_yuqi)
    EditText edtYuqi;
    @BindView(R.id.edt_chayi)
    EditText edtChayi;
    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.bt_scan)
    Button btScan;
    private CheckAdapter checkAdapter = null;
    private ArrayList<EPC> epcList = new ArrayList<>();
    private ConcurrentHashMap<String, String> conMap = new ConcurrentHashMap<>();
    private String bar = null;
    private boolean check = false;
    private int a = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_mess);
        ButterKnife.bind(this);

        setTitle("盘点");
        checkAdapter = new CheckAdapter(this);
        lvScan.setAdapter(checkAdapter);
        Intent intent = getIntent();
        bar = intent.getStringExtra("barCode");
        check = intent.getBooleanExtra("check", false);

        initview();
        reHelp();
        conMap.clear();
    }

    //获取EPC群读数据
    @Override
    public void handleUi(BaseEpc baseEpc) {
        super.handleUi(baseEpc);
        if (conMap.containsKey(baseEpc._EPC)) {
            return;
        }
        for (int i = 0; i < epcList.size(); i++) {
            if (epcList.get(i).getEpc().equals(baseEpc._EPC)) {
                conMap.put(baseEpc._EPC, "");
                epcList.get(i).setState(true);
                if (check) {
                    epcList.remove(i);
                }
                break;
            }
        }

        checkAdapter.updateDatas(epcList);
        edtSaomiao.setText(conMap.size() + "");
        edtChayi.setText(conMap.size() - a + "");
    }

    private void getPressDialog(String content) {
        final BaseDialog baseDialog = new BaseDialog(this, 1);
        baseDialog.setHintTvValue(content);
        baseDialog.setConfrimBtnOnclick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseDialog.dismiss();
                Intent intent = new Intent();
                intent.putExtra("finish", true);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        baseDialog.setCancelBtnOnclick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseDialog.dismiss();
                Intent intent = new Intent();
                intent.putExtra("finish", false);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void initview() {
        String warehouse = ACache.get(AppApplication.getApplication()).getAsString("spinner");
        if (TextUtils.isEmpty(warehouse)){
            warehouse = "集美";
        }
        if (bar.equals("*")) {
            Query<EpcModel> nQuery = getEpcModelDao().queryBuilder()
                    .where(EpcModelDao.Properties.Warehouse.eq(warehouse))
                    .build();
            List<EpcModel> users = nQuery.list();
            if (users == null || users.size() == 0) {
                ToastUtil.toast("该仓库没有数据，请先入库");
                return;
            }
            Log.i("tagGreen", "当前数量：" + users.size());
            for (int i = 0; i < users.size(); i++) {
                EPC epc = new EPC();
                epc.setEpc(users.get(i).getEpc());
                epc.setState(false);
                epcList.add(epc);
            }
        } else {
            Query<EpcModel> nQuery = getEpcModelDao().queryBuilder()
                    .where(EpcModelDao.Properties.Warehouse.eq(warehouse))
                    .where(EpcModelDao.Properties.Casecode.eq(bar)).build();
            List<EpcModel> users = nQuery.list();
            Log.i("tagGreen", "当前数量：" + users.size());
            for (int i = 0; i < users.size(); i++) {
                EPC epc = new EPC();
                epc.setEpc(users.get(i).getEpc());
                epc.setState(false);
                epcList.add(epc);
            }
        }
        checkAdapter.updateDatas(epcList);
        edtYuqi.setText(epcList.size() + "");
        a = epcList.size();
    }

    @OnClick({R.id.bt_sure, R.id.btn_cancel,R.id.bt_scan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_sure:
                getPressDialog("盘点成功，是否扫描下一箱");
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.bt_scan:
                //读标签
                if (btScan.getText().toString().trim().equals("扫描")) {
                    //开始读标签
                    helper.startInventroy();
                    btScan.setText("停止");
                } else {
                    //停止读标签和停止感应模块
                    helper.stopInventroyAndGpio();
                    btScan.setText("扫描");
                }
                break;
        }
    }
}
