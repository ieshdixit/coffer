package elope.crazyones.com.coffer.serverapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import com.readystatesoftware.chuck.ChuckInterceptor;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import elope.crazyones.com.coffer.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class ApiClient {
    private static final String BASE_URL = "https://97036b70.ngrok.io/";
    private static Retrofit retrofit, retrofitAuth, retrofitVersion;
    private static SharedPreferences sharedPref;
    private static OkHttpClient mClient;

    private static String prevImageUrl = "";

    public static Retrofit getLoginClient(Context context) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(new OkHttpClient.Builder().addInterceptor(new ChuckInterceptor(context)).build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClientWithoutAuth(Context context) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(new OkHttpClient.Builder().addInterceptor(new ChuckInterceptor(context)).build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getAuthClient(final Context context) {
        if (retrofitAuth == null) {
            retrofitAuth = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getClient(context))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofitAuth;
    }

    public static Retrofit getVersionClient(final Context context) {
        if (retrofitVersion == null) {
            retrofitVersion = new Retrofit.Builder()
                    .baseUrl(BASE_URL.substring(0, BASE_URL.lastIndexOf("api/")))
                    .client(new OkHttpClient.Builder().addInterceptor(new ChuckInterceptor(context)).build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitVersion;
    }


    private static OkHttpClient getClient(final Context context) {
        if (mClient == null) {
            sharedPref = context.getSharedPreferences(
                    context.getString(R.string.preference_file_key), MODE_PRIVATE);
            OkHttpClient.Builder ok_builder = new OkHttpClient.Builder();
            ok_builder.connectTimeout(5, TimeUnit.MINUTES);
            ok_builder.readTimeout(5, TimeUnit.MINUTES);
            ok_builder.writeTimeout(5, TimeUnit.MINUTES);
            ok_builder.addInterceptor(chain -> {
                Request request = chain.request();
                String token_identifier = context.getString(R.string.token_identifier);
                Request.Builder newRequest = request.newBuilder()
                        .addHeader(
                                "authorization", "Token " +
                                        sharedPref.getString(token_identifier, "")
                        );//.addHeader(
                //        "UA", "an/" + getAndroidVersion() + " " + getAppVersion(context)
                //);
                return chain.proceed(newRequest.build());
            });
            ok_builder.addInterceptor(new ChuckInterceptor(context));
            mClient = ok_builder.build();
        }
        return mClient;
    }

    public static String getAppVersion(Context context) {
        String version;
        try {
            PackageInfo pInfo;
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(context, "Failed to get app version", Toast.LENGTH_SHORT).show();
            version = "0.0";
        }
        return version;
    }

    public static String getAndroidVersion() {
        StringBuilder builder = new StringBuilder();
        builder.append(Build.VERSION.RELEASE);

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException | IllegalAccessException | NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                return builder.toString();
            }
        }
        return "0.0";
    }
}
