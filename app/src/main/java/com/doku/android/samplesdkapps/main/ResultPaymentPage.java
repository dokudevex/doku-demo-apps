package com.doku.android.samplesdkapps.main;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.doku.android.samplesdkapps.R;
import com.doku.android.samplesdkapps.model.MandiriVaResponse;
import com.doku.android.samplesdkapps.utils.Utils;
import com.doku.android.sdk.utils.PaymentChannel;
import com.google.gson.Gson;

/**
 * Created by dedyeirawan on 22,May,2020
 */
public class ResultPaymentPage extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resultpaymentpage_activity);

        Toolbar toolbarPaymentpageActivity = findViewById(R.id.toolbar_paymentresult_activity);
        toolbarPaymentpageActivity.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24px));
        toolbarPaymentpageActivity.setTitle("");
        setSupportActionBar(toolbarPaymentpageActivity);
        toolbarPaymentpageActivity.setNavigationOnClickListener(v -> finish());

        TextView textviewPaymentresultOrderid = findViewById(com.doku.android.sdk.R.id.textview_paymentresult_orderid);
        TextView textviewPaymentresultTotalrp = findViewById(com.doku.android.sdk.R.id.textview_paymentresult_totalrp);
        TextView textviewPaymentresultChannel = findViewById(com.doku.android.sdk.R.id.textview_paymentresult_channel);
        TextView textviewPaymentresultVirtualnumber = findViewById(com.doku.android.sdk.R.id.textview_paymentresult_virtualnumber);
        ImageView imageviewPaymentresultChannelnumber = findViewById(com.doku.android.sdk.R.id.imageview_paymentresult_channelnumber);

        Button buttonPaymentpageChange = findViewById(com.doku.android.sdk.R.id.button_paymentpage_change);
        buttonPaymentpageChange.setOnClickListener(view -> {
            finish();
        });

        TextView textviewPaymentresultCopy = findViewById(com.doku.android.sdk.R.id.textview_paymentresult_copy);
        textviewPaymentresultCopy.setOnClickListener(view -> {
            ClipboardManager cManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData cData = ClipData.newPlainText("text", textviewPaymentresultVirtualnumber.getText().toString());
            cManager.setPrimaryClip(cData);
            Toast.makeText(getApplicationContext(), "Copy data berhasil",Toast.LENGTH_SHORT).show();
        });

        Bundle extras = getIntent().getExtras();
        if(extras !=null)
        {
            String dataResponse = extras.getString("dataResponse");
            String amount = extras.getString("amount");
            Integer channelId = extras.getInt("channelId");
            Gson gson = new Gson();
            MandiriVaResponse mandiriVaResponse = gson.fromJson(dataResponse , MandiriVaResponse.class);
            textviewPaymentresultOrderid.setText("Order ID "+ mandiriVaResponse.getOrder().getInvoiceNumber());
            textviewPaymentresultTotalrp.setText(Utils.formatCurrencyRupiah(Double.parseDouble(amount)));
            textviewPaymentresultVirtualnumber.setText(mandiriVaResponse.getVirtualAccountInfo().getVirtualAccountNumber());

            if (channelId == PaymentChannel.VA_MANDIRI.getValue()) {
                imageviewPaymentresultChannelnumber.setBackgroundResource(com.doku.android.sdk.R.drawable.logo_mandiri);
                textviewPaymentresultChannel.setText(getResources().getString(R.string.bank_mandiri));

            } else if (channelId == PaymentChannel.VA_SYARIAH_MANDIRI.getValue()) {
                imageviewPaymentresultChannelnumber.setBackgroundResource(com.doku.android.sdk.R.drawable.logo_mandiri_syariah);
                textviewPaymentresultChannel.setText(getResources().getString(R.string.bank_mandiri_syariah));
            }
        }
    }
}
