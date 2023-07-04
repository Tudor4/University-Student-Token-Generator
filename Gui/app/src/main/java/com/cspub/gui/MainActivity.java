package com.cspub.gui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText collectionName;
    private EditText ticker;
    private Button createCollectionButton;

    private EditText collectionName2;

    private EditText address;

    private Button createNFTButton;

    private EditText address2;

    private Button getNFTsButton;
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;

    private GetNFTsThread getNFTsThread;

    private List<String> imageUrls = new ArrayList<>();

    private List<String> textList = new ArrayList<>();

    private CreateCollectionButtonClickListener createCollectionButtonClickListener = new CreateCollectionButtonClickListener();

    private CreateNFTButtonClickListener createNFTButtonClickListener = new CreateNFTButtonClickListener();

    private CreateCollectionThread createCollectionThread;
    private CreateCollectionThread createNFTThread;

    public class CreateCollectionButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            try {
                String url = "http://192.168.0.124:8080/issue-nft";
                String body = "{\"name\":\"" + collectionName.getText().toString() + "\", \"ticker\":\"" + ticker.getText().toString() + "\"}";
                System.out.println(body);
                createCollectionThread = new CreateCollectionThread(url, body);
                createCollectionThread.start();
                String result = createCollectionThread.response;
                System.out.println(result);
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class CreateNFTButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            try {
                String url = "http://192.168.0.124:8080/mint-nft";
                String body = "{\"name\":\"" + collectionName2.getText().toString() + "\", \"sendToAddress\":\"" + address.getText().toString() + "\"}";
                System.out.println(body);
                createNFTThread = new CreateCollectionThread(url, body);
                createNFTThread.start();
                String result = createNFTThread.response;
                System.out.println(result);
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class getNFTsButtonClickListener implements View.OnClickListener {
        private List<String> textList;
        private TextAdapter textAdapter;

        public getNFTsButtonClickListener(List<String> textList, TextAdapter textAdapter) {
            this.textList = textList;
            this.textAdapter = textAdapter;
        }

        @Override
        public void onClick(View view) {
            try {
                textList.clear();
                String url = "https://testnet-api.multiversx.com/accounts/erd1yjm3vlm5mulfv0c7symn5e25a3te7fwhm5q24hcw22j4r04y0s4qs6gt5q/nfts";
                getNFTsThread = new GetNFTsThread(url);
                getNFTsThread.start();
                while(getNFTsThread.response == null) {}
                String result = getNFTsThread.response;
                System.out.println(result);
                JSONArray nfts = new JSONArray(result);
                for (int i = 0; i < nfts.length(); i++) {
                    JSONObject nft = nfts.getJSONObject(i);
                    textList.add("Colectie: " + nft.getString("name") + "  NFT: " + nft.getString("identifier"));
                    //JSONArray media = nft.getJSONArray("media");
                    //urls.add(media.getJSONObject(0).getString("url"));
                }
                //System.out.println(urls);
                textAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        collectionName = findViewById(R.id.collection_name);
        ticker = findViewById(R.id.ticker);
        createCollectionButton = findViewById(R.id.create_collection_button);
        createCollectionButton.setOnClickListener(createCollectionButtonClickListener);
        collectionName2 = findViewById(R.id.collection_name2);
        address = findViewById(R.id.address);
        createNFTButton = findViewById(R.id.create_nft_button);
        createNFTButton.setOnClickListener(createNFTButtonClickListener);
        recyclerView = findViewById(R.id.recyclerView);
        address2 = findViewById(R.id.address2);
        getNFTsButton = findViewById(R.id.get_nfts_button);
        TextAdapter textAdapter = new TextAdapter(this, textList);
        //imageAdapter = new ImageAdapter(this, imageUrls);
        getNFTsButton.setOnClickListener(new getNFTsButtonClickListener(textList, textAdapter));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(textAdapter);

    }

    @Override
    protected void onDestroy() {
        if (createCollectionThread != null) {
            createCollectionThread.stopThread();
        }
        createNFTThread.stopThread();
        getNFTsThread.stopThread();
        super.onDestroy();
    }
}