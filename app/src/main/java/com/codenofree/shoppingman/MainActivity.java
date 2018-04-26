package com.codenofree.shoppingman;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codenofree.shoppingman.model.pdModel;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Uri uri;
    private RecyclerView pdList;
    private PdAdapter pdAdapter;
    private List<pdModel> data = new ArrayList<>();
    private String searchWord;
    private int showNum = 5;
    private EditText searchInput;
    private TextView searchButton;
    private OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchButton = (TextView) findViewById(R.id.search_button);
        searchInput = (EditText) findViewById(R.id.search_input);
        pdList = (RecyclerView)findViewById(R.id.pdList);
        pdAdapter = new PdAdapter(this, data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        pdList.setLayoutManager(layoutManager);
        pdList.addItemDecoration(new MyDecoration(this,MyDecoration.VERTICAL_LIST));
        pdList.setAdapter(pdAdapter);
        initEvent();
    }

    public void initEvent(){
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.clear();
                searchWord = searchInput.getText().toString();
                getJD();
                getTianmao();
                getMoguJie();
            }
        });
    }

    public void getJD(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("https://search.jd.com/Search?keyword="+searchWord+"&enc=utf-8")
                            .get();
                    int itemNum = 0;
                    while (itemNum<showNum){
                        Element item = document.getElementsByClass("gl-item").get(itemNum);
                        final String title = item.getElementsByClass("p-img")
                                .get(0).getElementsByTag("a").attr("title");
                        final String price = item.getElementsByClass("p-price")
                                .get(0).getElementsByTag("i").text();
                        final String image = "http:"+ item.getElementsByClass("p-img").get(0)
                                .getElementsByTag("a").get(0).getElementsByTag("img").attr("src");
                        final String href = "http:"+item.getElementsByClass("p-img")
                                .get(0).getElementsByTag("a").attr("href");
                        uri = Uri.parse(href);
                        data.add(new pdModel(title,price,image,href,"JD"));
                        itemNum++;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getTianmao(){
        final Request request = new Request.Builder()
                .url("https://list.tmall.com/m/search_items.htm?page_size=20&page_no=1&q="+searchWord)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("thg",e.getMessage());

            }

            @Override
            public void onResponse(final Response response) throws IOException {

                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray items = jsonObject.getJSONArray("item");
                    int itemNum = 0;
                    while (itemNum<showNum){
                        JSONObject item1 = items.getJSONObject(itemNum);
                        String title = item1.getString("title");
                        String price = item1.getString("price");
                        String img = "http:"+item1.getString("img");
                        String href = "http:"+item1.getString("url");
                        data.add(new pdModel(title,price,img,href,"Timao"));
                        itemNum++;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdAdapter.notifyDataSetChanged();

                    }
                });
            }
        });
    }

    public void getMoguJie(){
        final Request request = new Request.Builder()
                .url("http://list.mogujie.com/search?_version=8193&ratio=3%3A4&cKey=43&sort=pop&page=1&q="+searchWord+"&minPrice=&maxPrice=&ppath=&cpc_offset=&_=1524669117445")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("thg",e.getMessage());

            }

            @Override
            public void onResponse(final Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray items = jsonObject.getJSONObject("result").getJSONObject("wall").getJSONArray("docs");
                    int itemNum = 0;
                    while (itemNum<showNum){
                        JSONObject item1 = items.getJSONObject(itemNum);
                        String title = item1.getString("title");
                        String price = item1.getString("price");
                        String img = item1.getString("img");
                        String href = item1.getString("link");
                        data.add(new pdModel(title,price,img,href,"蘑菇街"));
                        itemNum++;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdAdapter.notifyDataSetChanged();

                    }
                });
            }
        });
    }
}
