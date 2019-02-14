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
import com.ioter.uhfscan.bean.CaseCode;
import com.ioter.uhfscan.bean.EPC;
import com.ioter.uhfscan.common.util.ACache;
import com.ioter.uhfscan.common.util.ToastUtil;
import com.ioter.uhfscan.data.greendao.EpcModel;
import com.ioter.uhfscan.data.greendao.dao.EpcModelDao;
import com.ioter.uhfscan.ui.adapter.CheckDataAdapter;
import com.ioter.uhfscan.ui.dialog.BaseDialog;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckDataActivity extends NewBaseActivity {

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
    private ArrayList<EPC> epcList = new ArrayList<>();
    private ConcurrentHashMap<String, CaseCode> map = new ConcurrentHashMap<>();
    private String bar = null;
    private boolean check = false;
    private CheckDataAdapter checkDataAdapter = null;
    private HashMap<String, String> map1 = new HashMap<>();
    private int a = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_data);
        ButterKnife.bind(this);

        setTitle("盘点");
        checkDataAdapter = new CheckDataAdapter(this);
        lvScan.setAdapter(checkDataAdapter);
        Intent intent = getIntent();
        bar = intent.getStringExtra("barCode");
        check = intent.getBooleanExtra("check", false);

        initview();
        reHelp();
    }

    //获取EPC群读数据
    @Override
    public void handleUi(BaseEpc baseEpc) {
        super.handleUi(baseEpc);
        if (map == null || map.size() == 0) {
            return;
        }
        if (map1.containsKey(baseEpc._EPC)) {
            return;
        }

        epcList.clear();
        map1.put(baseEpc._EPC, "");
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            if (map.get(key)._map.containsKey(baseEpc._EPC)) {
                map.get(key)._map.remove(baseEpc._EPC);
                map.get(key)._map.put(baseEpc._EPC, true);
            }
        }

        Iterator it = map.keySet().iterator();
        int saomiao = 0;
        while (it.hasNext()) {
            EPC epc = new EPC();
            String key = (String) it.next();
            int size = map.get(key)._map.size();
            int rel = 0;
            Iterator ite = map.get(key)._map.keySet().iterator();
            while (ite.hasNext()) {
                String key1 = (String) ite.next();
                if (map.get(key)._map.get(key1)) {
                    rel++;
                }
            }
            epc.setEpc(key);
            epc.setSize(size);
            epc.setRel(rel);
            epcList.add(epc);
            saomiao += rel;
        }
        checkDataAdapter.updateDatas(epcList);
        edtSaomiao.setText(saomiao + "");
        edtChayi.setText(saomiao - a + "");
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
        if (TextUtils.isEmpty(warehouse)) {
            warehouse = "集美";
        }
        List<EpcModel> users;
        if (bar.equals("*")) {
            Query<EpcModel> nQuery = getEpcModelDao().queryBuilder()
                    .where(EpcModelDao.Properties.Warehouse.eq(warehouse))
                    .build();
            users = nQuery.list();
            if (users == null || users.size() == 0) {
                ToastUtil.toast("该仓库没有数据，请先入库");
                return;
            }
        } else {
            Query<EpcModel> nQuery = getEpcModelDao().queryBuilder()
                    .where(EpcModelDao.Properties.Warehouse.eq(warehouse))
                    .where(EpcModelDao.Properties.Casecode.eq(bar)).build();
            users = nQuery.list();
        }
        Log.i("tagGreen", "当前数量：" + users.size());
        for (int i = 0; i < users.size(); i++) {
            String code = users.get(i).getCasecode();
            if (!map.containsKey(code)) {
                CaseCode caseCode = new CaseCode();
                caseCode._map.put(users.get(i).getEpc(), false);
                map.put(code, caseCode);
            } else {
                map.get(code)._map.put(users.get(i).getEpc(), false);
            }
        }
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            EPC epc = new EPC();
            String key = (String) it.next();
            int size = map.get(key)._map.size();
            epc.setEpc(key);
            epc.setSize(size);
            epc.setRel(0);
            epcList.add(epc);
        }
        checkDataAdapter.updateDatas(epcList);
        edtYuqi.setText(users.size() + "");
        a = users.size();
    }

    @OnClick({R.id.bt_sure, R.id.btn_cancel, R.id.bt_scan})
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
