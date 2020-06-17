package com.doku.android.samplesdkapps.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.doku.android.samplesdkapps.R;
import com.doku.android.samplesdkapps.adapter.ExpandableRecyclerAdapter;
import com.doku.android.samplesdkapps.adapter.PaymentPageAdapter;
import com.doku.android.samplesdkapps.channel.PaymentChannel;
import com.doku.android.samplesdkapps.prefs.Preferences;
import com.doku.android.samplesdkapps.utils.ClickListener;
import com.doku.android.samplesdkapps.utils.RecyclerTouchListener;
import com.doku.android.samplesdkapps.utils.Utils;
import com.doku.android.sdk.main.DokuPayVa;
import java.security.NoSuchAlgorithmException;

/**
 * Created by dedyeirawan on 22,May,2020
 */
public class PaymentPage extends AppCompatActivity implements SettingPage.SettingDialogListener {
    private static final String TAG = "payment_page_app";
    private String noInvoice = "";
    private String amount = "";
    private TextView textviewPaymentpageOrderid;
    private Integer channelCode = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paymentpage_activity);

        Toolbar toolbarPaymentpageActivity = findViewById(R.id.toolbar_paymentpage_activity);
        toolbarPaymentpageActivity.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24px));
        toolbarPaymentpageActivity.setTitle("");
        setSupportActionBar(toolbarPaymentpageActivity);
        toolbarPaymentpageActivity.setNavigationOnClickListener(v -> finish());

        RecyclerView recyclerviewPaymentpageMain = findViewById(R.id.recyclerview_paymentpage_main);
        PaymentPageAdapter paymentPageAdapter = new PaymentPageAdapter(PaymentPage.this);
        paymentPageAdapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
        recyclerviewPaymentpageMain.setLayoutManager(new LinearLayoutManager(PaymentPage.this));
        recyclerviewPaymentpageMain.setAdapter(paymentPageAdapter);

        TextView toolbarPaymentpageSet = findViewById(R.id.toolbar_paymentpage_set);
        toolbarPaymentpageSet.setOnClickListener(view -> {
            FragmentManager fm = getSupportFragmentManager();
            SettingPage settingPage = new SettingPage();
            settingPage.setParent(this);
            settingPage.show(fm, "setting_page");
        });

        Button buttonPaymentpageDetails = findViewById(R.id.button_paymentpage_details);
        buttonPaymentpageDetails.setOnClickListener(view -> {
            DetailPayment addPhotoBottomDialogFragment = DetailPayment.newInstance();
            addPhotoBottomDialogFragment.show(getSupportFragmentManager(), DetailPayment.TAG);
        });

        recyclerviewPaymentpageMain.addOnItemTouchListener(new RecyclerTouchListener(PaymentPage.this, recyclerviewPaymentpageMain, new ClickListener() {
            @Override
            public void onClick(View view, int position) {/*not implemented*/}

            @Override
            public void onLongClick(View view, int position) {/*not implemented*/}
        }));

        textviewPaymentpageOrderid = findViewById(R.id.textview_paymentpage_orderid);
        refreshInvoice();

        amount    = "10000";
        TextView textviewPaymentpageTotalrp = findViewById(R.id.textview_paymentpage_totalrp);
        textviewPaymentpageTotalrp.setText(Utils.formatCurrencyRupiah(Double.parseDouble(amount)));

        paymentPageAdapter.SetOnItemClickListener((view, childTitle) -> {
            if (!Preferences.getClientId(PaymentPage.this).equalsIgnoreCase("")) {

                String bankMandiri = getResources().getString(R.string.bank)+" "+getResources().getString(R.string.bank_mandiri);
                String bankMandiriSyariah = getResources().getString(R.string.bank)+" "+getResources().getString(R.string.bank_mandiri_syariah);

                if(childTitle.trim().equalsIgnoreCase(bankMandiri)) {
                    channelCode = PaymentChannel.VA_MANDIRI.getValue();
                } else if(childTitle.trim().equalsIgnoreCase(bankMandiriSyariah)) {
                    channelCode = PaymentChannel.VA_SYARIAH_MANDIRI.getValue();
                }

                String email = "demosdk@doku.com";
                String name = "demosdk";
                String expiredTime = "60";
                String reusableStatus = "false";

                String dataWords =  Preferences.getClientId(PaymentPage.this)
                                    + email
                                    + name
                                    + amount
                                    + noInvoice
                                    + expiredTime
                                    + reusableStatus
                                    + Preferences.getSharedKey(PaymentPage.this);

                try {
                    String Words = Utils.SHA256(dataWords);
                    new DokuPayVa.Builder(this)
                            .clientId(Preferences.getClientId(PaymentPage.this))
                            .customerEmail(email)
                            .customerName(name)
                            .dataAmount(amount)
                            .dataWords(Words)
                            .expiredTime(60)
                            .invoiceNumber(noInvoice)
                            .isProduction(Preferences.getEnvironmentServer(PaymentPage.this))
                            .paymentChannel(channelCode)
                            .reusableStatus(reusableStatus)
                            .usePageResult(Preferences.getUsePageResult(PaymentPage.this))
                            .connect();
                } catch (NoSuchAlgorithmException e) {
                    Log.i(TAG, e.getMessage());
                }
            } else {
                Toast.makeText(getApplicationContext(),"Please Setting Credential !",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onFinishEditDialog(String clientId, String sharedKey, Boolean isProduction, Boolean activeResultPage) {
        Preferences.setClientId(PaymentPage.this, clientId);
        Preferences.setSharedKey(PaymentPage.this, sharedKey);
        Preferences.setEnvironmentServer(PaymentPage.this, isProduction);
        Preferences.setUsePageResult(PaymentPage.this, activeResultPage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String dataResponse = data.getStringExtra("dataResponse");
            Boolean usePageResult = Preferences.getUsePageResult(PaymentPage.this);
            if (usePageResult) {
                Log.i(TAG, dataResponse);
            } else {
                Intent intent = new Intent(getBaseContext(), ResultPaymentPage.class);
                intent.putExtra("dataResponse", dataResponse);
                intent.putExtra("amount", amount);
                intent.putExtra("channelId", channelCode);
                startActivity(intent);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void refreshInvoice() {
        noInvoice = "DEMOSDK-"+ Utils.generateInvoiceId();
        textviewPaymentpageOrderid.setText(getResources().getString(R.string.order_id) +" "+noInvoice);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshInvoice();
    }
}
