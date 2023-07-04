package com.cspub.gui;

import org.jsoup.internal.StringUtil;

import java.io.IOException;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

public class GetNFTsThread extends Thread{
    String url;
    String response;

    public GetNFTsThread(String url) {
        this.url = url;
    }

    @Override
    public void run() {
        HttpClient httpClient = new DefaultHttpClient();

        try {
            System.out.println(url);
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpGetResponse = httpClient.execute(httpGet);
            HttpEntity httpGetEntity = httpGetResponse.getEntity();
            response = EntityUtils.toString(httpGetEntity);
            System.out.println(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void stopThread() {
        interrupt();
    }
}
