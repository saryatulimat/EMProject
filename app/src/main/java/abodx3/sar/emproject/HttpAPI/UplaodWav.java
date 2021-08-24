package abodx3.sar.emproject.HttpAPI;

import java.io.File;

import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.MultipartReader;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class UplaodWav extends OkHttpClient {
    Request request;


    public UplaodWav inituploadFile(String serverURL, File file) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file"
                        , file.getName()
                        , RequestBody.create(file, MediaType.parse("audio/wav")))
                .build();
        request = new Request.Builder()
                .url(serverURL)
                .post(requestBody)
                .build();
        return this;
    }

    public UplaodWav exec(Callback c) {
        this.newCall(request).enqueue(c);
        return this;
    }


}
