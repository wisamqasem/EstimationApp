package com.jdeco.estimationapp.ui.screens;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.TokenWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jdeco.estimationapp.R;
import com.jdeco.estimationapp.objects.CONSTANTS;
import com.jdeco.estimationapp.objects.Item;
import com.jdeco.estimationapp.objects.ServiceDetails;
import com.jdeco.estimationapp.operations.Database;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class serviceInfo extends AppCompatActivity {

    RequestQueue mRequestQueue;
    StringRequest mStringRequest;
    EditText serviceNoET ;
    TextView resultCode,service_id,oldMeterNo,oldMeterType,serviceStatusID,serviceAddress,old_meter_curr_reading,full_name,beneficiary_full_name
            ,IDCardNo,totalBalance,areaCode,categoryDescA,subAreaCode,customerType,winter_consumption,ava_consumption,summer_consumption,agreementStatus,
            propertyType,tarrifNo,tarrifNameA,tarrifCost,unpaidVouchers,unpaidVouchersCount,paidVouchers,paidVouchersCount,estimatedNextVoucherAMT,estimatedDaysForNextVoucher,
            nextExpectedVoucherDate,nextEstimatedChargeDate,smartMeterCurrentReading,smartMeterCurrentReadingDate,prepaymentBalance,isSmartMeter,isPrepaidMeter,transformerNo,
            polNo,serviceClassDesc,old_meter_curr_date ;
    Button serviceDetailsBtn;
    String serviceNo;
    ProgressDialog progress;
    Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_info2);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("تفاصيل الخدمات");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // Add back arrow in action bar
        }

        progress = new ProgressDialog(this);
        progress.setTitle(getResources().getString(R.string.please_wait));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        serviceNoET=(EditText) findViewById(R.id.serviceNoET);
        serviceNo=serviceNoET.getText().toString();

                service_id = (TextView)findViewById(R.id.service_id);
        oldMeterNo= (TextView)findViewById(R.id.oldMeterNo);
                oldMeterType= (TextView)findViewById(R.id.oldMeterType);
                        serviceStatusID= (TextView)findViewById(R.id.serviceStatusID);
        serviceAddress= (TextView)findViewById(R.id.serviceAddress);
                old_meter_curr_reading= (TextView)findViewById(R.id.old_meter_curr_reading);

                full_name= (TextView)findViewById(R.id.full_name);
        beneficiary_full_name= (TextView)findViewById(R.id.beneficiary_full_name);
                IDCardNo= (TextView)findViewById(R.id.IDCardNo);
                totalBalance= (TextView)findViewById(R.id.totalBalance);
                        areaCode= (TextView)findViewById(R.id.areaCode);
        categoryDescA= (TextView)findViewById(R.id.categoryDescA);
                subAreaCode= (TextView)findViewById(R.id.subAreaCode);
        customerType= (TextView)findViewById(R.id.customerType);
                winter_consumption= (TextView)findViewById(R.id.winter_consumption);
        ava_consumption= (TextView)findViewById(R.id.ava_consumption);
                summer_consumption= (TextView)findViewById(R.id.summer_consumption);
        agreementStatus= (TextView)findViewById(R.id.agreementStatus);
                propertyType= (TextView)findViewById(R.id.propertyType);
        tarrifNo= (TextView)findViewById(R.id.tarrifNo);
                tarrifNameA= (TextView)findViewById(R.id.tarrifNameA);
        tarrifCost= (TextView)findViewById(R.id.tarrifCost);
                unpaidVouchers= (TextView)findViewById(R.id.unpaidVouchers);
        unpaidVouchersCount= (TextView)findViewById(R.id.unpaidVouchersCount);
        serviceClassDesc = (TextView)findViewById(R.id.serviceClassDesc);
        old_meter_curr_date  = (TextView)findViewById(R.id.old_meter_curr_date);

        paidVouchers = (TextView)findViewById(R.id.paidVouchers);
        paidVouchersCount = (TextView)findViewById(R.id.paidVouchersCount);
        estimatedNextVoucherAMT = (TextView)findViewById(R.id.estimatedNextVoucherAMT);
        estimatedDaysForNextVoucher = (TextView)findViewById(R.id.estimatedDaysForNextVoucher);
        nextExpectedVoucherDate  = (TextView)findViewById(R.id.nextExpectedVoucherDate);
        nextEstimatedChargeDate  = (TextView)findViewById(R.id.nextEstimatedChargeDate);
        smartMeterCurrentReading   = (TextView)findViewById(R.id.smartMeterCurrentReading);
        smartMeterCurrentReadingDate  = (TextView)findViewById(R.id.smartMeterCurrentReadingDate);
        prepaymentBalance    = (TextView)findViewById(R.id.prepaymentBalance);
        isSmartMeter     = (TextView)findViewById(R.id.isSmartMeter);
        isPrepaidMeter  = (TextView)findViewById(R.id.isPrepaidMeter);
        transformerNo   = (TextView)findViewById(R.id.transformerNo);
        polNo  = (TextView)findViewById(R.id.polNo);
        serviceDetailsBtn = (Button)findViewById(R.id.serviceDetailsBtn);



        extras = getIntent().getExtras();
        if (extras != null) {

            serviceNo = extras.getString("serviceNo");
            serviceNoET.setText(serviceNo);
            getServiceDetails(serviceNo);
        }

        serviceDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serviceNoET.getText().toString().isEmpty() || serviceNoET.getText().toString().equalsIgnoreCase(" ")){
                    serviceNoET.requestFocus();
                    serviceNoET.setError(getString(R.string.fill_field));
                }
                else
                getServiceDetails(serviceNoET.getText().toString());

            }
        });



        /*
         * When you want that Keybord not shown untill user clicks on one of the EditText Field.
         */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


    }

    //to add the back button
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }



    public void getServiceDetails(String serviceNumber){
        ArrayList<ServiceDetails> serviceDetails = new ArrayList<>();
        progress.show();
        String SN=  arabicToDecimal(serviceNumber);
//RequestQueue initialized
            mRequestQueue = Volley.newRequestQueue(this);
            //String Request initialized
            mStringRequest = new StringRequest(Request.Method.POST, CONSTANTS.API_LINK, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.d("getServiceDetails", "Response: " + response);

                    //create json object
                    try {

                        //parse string to json
                      //  JSONObject itemsResultObject = new JSONObject(response);

                        //get items array according to items array object
                        JSONArray itemsJsonArr = new  JSONArray(response);
                        JSONObject itemsResultObject = new JSONObject(String.valueOf(itemsJsonArr.get(0)));
Log.d("getServiceDetails",":"+itemsResultObject);


                        service_id.setText( "رقم الخدمة :"+  itemsResultObject.getString("service_id"));
                        oldMeterNo.setText( "رقم العداد  :"+ itemsResultObject.getString("oldMeterNo"));
                        oldMeterType.setText( "تصنيف العداد  :"+  itemsResultObject.getString("oldMeterType"));
                        //   serviceStatusID.setText(  "تصنيف حالة الخدمة :"+itemsResultObject.getString("serviceStatusID"));
                        serviceAddress .setText(  "عنوان الخدمة :"+ itemsResultObject.getString("serviceAddress"));
                        old_meter_curr_reading.setText( "قراءة العداد  :"+   itemsResultObject.getString("old_meter_curr_reading"));
                        full_name.setText( "أسم المشترك :"+  itemsResultObject.getString("full_name"));
                        beneficiary_full_name.setText(  " أسم المستفيد :"+ itemsResultObject.getString("beneficiary_full_name"));
                        IDCardNo.setText(  "رقم الهوية :"+  itemsResultObject.getString("IDCardNo"));
                        totalBalance.setText(  "رصيد  :"+ itemsResultObject.getString("totalBalance"));
                        areaCode.setText( "كود المدينة :"+ itemsResultObject.getString("areaCode"));
                        categoryDescA.setText(  "نوع العداد :"+  itemsResultObject.getString("categoryDescA"));
                        subAreaCode.setText(  "كود المنطقة :"+ itemsResultObject.getString("subAreaCode"));
                        customerType.setText(  "نوع المشترك :"+  itemsResultObject.getString("customerType"));
                        winter_consumption.setText( "أستهلاك الشتاء :"+   itemsResultObject.getString("winter_consumption"));
                        ava_consumption .setText( "معدل الأستهلاك :"+  itemsResultObject.getString("ava_consumption"));
                        summer_consumption.setText( "أستهلاك الصيف :"+  itemsResultObject.getString("summer_consumption"));
                        agreementStatus .setText(  "حالة الخدمة :"+  itemsResultObject.getString("agreementStatus"));
                        propertyType.setText(  "نوع العقار :"+ itemsResultObject.getString("propertyType"));
                        tarrifNo.setText(  "رقم التعرفة :"+ itemsResultObject.getString("tarrifNo"));
                        tarrifNameA.setText(  "التعرفة :"+  itemsResultObject.getString("tarrifNameA"));
                        tarrifCost.setText(  "سعر التعرفة:"+  itemsResultObject.getString("tarrifCost"));
                        old_meter_curr_date.setText( "تاريخ أخر قراءة :"+  itemsResultObject.getString("old_meter_curr_date"));
                        serviceClassDesc.setText("قوة الربط :"+ itemsResultObject.getString("serviceClassDesc"));
                     //   unpaidVouchers.setText(  "فواتير غير مدفوعة :"+  itemsResultObject.getString("unpaidVouchers"));
                      //  unpaidVouchersCount.setText(  "عدد الفواتير غير مدفوعة :"+  itemsResultObject.getString("unpaidVouchersCount"));

                        //paidVouchers.setText(  "فواتير مدفوعة :"+  itemsResultObject.getString("paidVouchers"));
                        //paidVouchersCount.setText(   "عدد الفواتير المدفوعة :"+ itemsResultObject.getString("paidVouchersCount"));
                       // estimatedNextVoucherAMT.setText(  ":"+  itemsResultObject.getString("estimatedNextVoucherAMT"));
                       // estimatedDaysForNextVoucher.setText( ":"+   itemsResultObject.getString("estimatedDaysForNextVoucher"));
                       // nextExpectedVoucherDate.setText(  "تاريخ الفاتورة التالية المتوقع :"+  itemsResultObject.getString("nextExpectedVoucherDate"));
                       // nextEstimatedChargeDate.setText(  ":"+  itemsResultObject.getString("nextEstimatedChargeDate"));
                       // smartMeterCurrentReading.setText( "قراءة العداد الذكي :"+   itemsResultObject.getString("smartMeterCurrentReading"));
                       // smartMeterCurrentReadingDate.setText(  ":"+  itemsResultObject.getString("smartMeterCurrentReadingDate"));
                       // prepaymentBalance.setText(  "رصيد دفع مسبق :"+  itemsResultObject.getString("prepaymentBalance"));
                       // isSmartMeter.setText(  "عدد ذكي :"+  itemsResultObject.getString("isSmartMeter"));
                       // isPrepaidMeter.setText(  "عدد دفع مسبق :"+  itemsResultObject.getString("isPrepaidMeter"));
                       // transformerNo.setText(   "رقم المحول :"+ itemsResultObject.getString("transformerNo"));
                       // polNo.setText(  ":"+  itemsResultObject.getString("polNo"));





                        progress.dismiss();
                    } catch (Exception ex) {
                        progress.dismiss();
                        Log.d("error", ":" + ex);
                        ex.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.d("getItemsFromServer", "Error Login Request :" + error.toString());
                }

            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    //parameters
                    params.put("agreeNo", SN);
                    params.put("apiKey", CONSTANTS.API_KEY);
                    params.put("action", CONSTANTS.ACTION_SERVICE_DETAILS);

                    return params;
                }
            };

            mRequestQueue.add(mStringRequest);



    }


    // arabicNumbersToDecimal
    private String arabicToDecimal(String number) {
        char[] chars = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }


}