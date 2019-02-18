package com.ioter.uhfscan.ui.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.ioter.uhfscan.common.util.DataUtil;
import com.ioter.uhfscan.common.util.ToastUtil;
import com.ioter.uhfscan.data.greendao.EpcModel;
import com.ioter.uhfscan.data.greendao.dao.EpcModelDao;
import com.ioter.uhfscan.ui.adapter.RecevieScamadapter;
import com.ioter.uhfscan.ui.dialog.BaseDialog;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroundMessActivity extends NewBaseActivity {

    @BindView(R.id.et_biaoqian)
    EditText etBiaoqian;
    @BindView(R.id.lv_scan)
    ListView lvScan;
    @BindView(R.id.edt_saomiao)
    EditText edtSaomiao;
    @BindView(R.id.edt_chayi)
    EditText edtChayi;
    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.bt_scan)
    Button btScan;
    private ArrayList<EPC> epcList = new ArrayList<>();
    private RecevieScamadapter recevieScamadapter = null;
    private String bar = null;
    private int user=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ground_mess);
        ButterKnife.bind(this);

        setTitle("出库");
        reHelp();
        recevieScamadapter = new RecevieScamadapter(this);
        lvScan.setAdapter(recevieScamadapter);
        bar = getIntent().getStringExtra("barCode");

        String warehouse = ACache.get(AppApplication.getApplication()).getAsString("spinner");
        Query<EpcModel> nQuery = getEpcModelDao().queryBuilder()
                .where(EpcModelDao.Properties.Warehouse.eq(warehouse))
                .where(EpcModelDao.Properties.Casecode.eq(bar)).build();
        List<EpcModel> users = nQuery.list();
        if (users==null || users.size()==0){
            ToastUtil.toast("不存在该箱码，请确认出库箱码");
            return;
        }
        user = users.size();
        edtChayi.setText(users.size()+"");
    }

    //获取EPC群读数据
    @Override
    public void handleUi(BaseEpc baseEpc) {
        super.handleUi(baseEpc);
        etBiaoqian.setText(baseEpc._EPC);
        for (int i = 0; i < epcList.size(); i++) {
            if (epcList.get(i).getEpc().equals(baseEpc._EPC)) {
                return;
            }
        }

        EPC epc = new EPC();
        epc.setEpc(baseEpc._EPC);
        epcList.add(epc);
        recevieScamadapter.updateDatas(epcList);
        edtSaomiao.setText(epcList.size() + "");
        edtChayi.setText(user-epcList.size()+"");
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

    private void takeData() {
        String warehouse = ACache.get(AppApplication.getApplication()).getAsString("spinner");
        Query<EpcModel> nQuery = getEpcModelDao().queryBuilder()
                .where(EpcModelDao.Properties.Warehouse.eq(warehouse))
                .where(EpcModelDao.Properties.Casecode.eq(bar)).build();
        List<EpcModel> users = nQuery.list();
        if (users.size() != epcList.size()) {
            ToastUtil.toast("标签数不一致");
            return;
        }
        if (DataUtil.isListEqual(users, epcList)) {
            ToastUtil.toast("标签数不一致");
            return;
        }

        Log.i("tagGreen", "当前数量：" + users.size());
        for (int i = 0; i < users.size(); i++) {
            //删除对应数据
            deleteNote(users.get(i));
        }
        querydata();
        getPressDialog("出库成功，是否扫描下一箱");
    }

    @OnClick({R.id.bt_sure, R.id.btn_cancel,R.id.bt_scan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_sure:
                takeData();
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
