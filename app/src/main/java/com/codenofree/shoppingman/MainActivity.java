package com.codenofree.shoppingman;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.codenofree.shoppingman.model.pdModel;
import com.codenofree.shoppingman.view.BottomTabView;
import com.codenofree.shoppingman.view.PopupShow;
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
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Uri uri;
    private RecyclerView pdList;
    private BottomTabView webFilterView;
    private ImageView searchButton;
    private PdAdapter pdAdapter;
    private List<pdModel> data = new ArrayList<>();
    private String searchWord = "杯子";
    private int showNum = 3;
    private EditText searchInput;
    private ImageView filterButton;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private int[] FILTER_IMAGE = {R.drawable.search, R.drawable.search, R.drawable.search, R.drawable.search, R.drawable.search};
    private int[] FTLTER_TEXT = {R.string.tianmao, R.string.jingdong, R.string.dangdang, R.string.mogu, R.string.guomei};

    private boolean isShowFilter = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webFilterView = (BottomTabView)findViewById(R.id.web_filter);
        filterButton = (ImageView) findViewById(R.id.filter_button);
        searchButton = (ImageView) findViewById(R.id.search_icon);
        searchInput = (EditText) findViewById(R.id.search_input);
        pdList = (RecyclerView)findViewById(R.id.pdList);
        webFilterView.setResource(FILTER_IMAGE, null, FTLTER_TEXT, R.color.colorAccent, 4);
        pdAdapter = new PdAdapter(this, data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        pdList.setLayoutManager(layoutManager);
        pdList.addItemDecoration(new MyDecoration(this,MyDecoration.VERTICAL_LIST));
        pdList.setAdapter(pdAdapter);
        initEvent();
    }

    public void initEvent(){
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShowFilter){
                    PopupShow.hide(getBaseContext(), webFilterView, 50,50, 300, "top");
                    isShowFilter = false;
                }else {
                    PopupShow.show(getBaseContext(), webFilterView, 50,50, 300, "top");
                    isShowFilter = true;
                }
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShowFilter){
                    PopupShow.hide(getBaseContext(), webFilterView, 50,50, 300, "top");
                    isShowFilter = false;
                }
                data.clear();
                searchWord = searchInput.getText().toString();
                List<Integer> filters = webFilterView.getSelected();
                for(int i=0;i<filters.size();i++){
                    switch (filters.get(i)){
                        case 0:
                            getTianmao();
                            break;
                        case 1:
                            getJD();
                            break;
                        case 2:
                            getDangDang();
                            break;
                        case 3:
                            getMoguJie();
                            break;
                        case 4:
                            getGuomei();
                            break;
                    }
                }
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
                } catch (IndexOutOfBoundsException ex){
                    return;
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

    public void getMoguJie() {
        final Request request = new Request.Builder()
                .url("http://list.mogujie.com/search?_version=8193&ratio=3%3A4&cKey=43&sort=pop&page=1&q=" + searchWord + "&minPrice=&maxPrice=&ppath=&cpc_offset=&_=1524669117445")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(final Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray items = jsonObject.getJSONObject("result").getJSONObject("wall").getJSONArray("docs");
                    int itemNum = 0;
                    while (itemNum < showNum) {
                        JSONObject item1 = items.getJSONObject(itemNum);
                        String title = item1.getString("title");
                        String price = item1.getString("price");
                        String img = item1.getString("img");
                        String href = item1.getString("link");
                        data.add(new pdModel(title, price, img, href, "蘑菇街"));
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
    public void getSuning(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("https://search.suning.com/杯子/")
                            .get();

                    Elements items = document.getElementById("filter-results").getElementsByClass("product");
//                    int itemNum = 0;
//                    while(itemNum<showNum){
//                        String title = items.get(itemNum)
//                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getYHD(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("https://search.suning.com/杯子/")
                            .get();

                    Elements items = document.getElementById("filter-results").getElementsByClass("product");
//                    int itemNum = 0;
//                    while(itemNum<showNum){
//                        String title = items.get(itemNum)
//                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getGuomei(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("https://search.gome.com.cn/search?question="
                            +searchWord+"&searchType=goods")
                            .get();

                    Elements items = document.getElementById("product-box").getElementsByTag("li");

                    int itemNum = 0;
                    while (itemNum<showNum){
                        Element item = items.get(itemNum).getElementsByClass("item-tab-warp").get(0).getElementsByClass("item-pic").get(0);
                        String title = item.getElementsByTag("a").get(0).attr("title");
                        String image = "https:"+item.getElementsByTag("a").get(0).getElementsByTag("img").attr("gome-src");
                        String href = "https:"+item.getElementsByTag("a").get(0).attr("href");
                        String price = "--";
                        data.add(new pdModel(title,price,image,href,"国美在线"));
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
                } catch (IndexOutOfBoundsException ex){
                    return;
                }
            }
        }).start();
    }

    public void getDangDang(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("http://search.dangdang.com/?key="+searchWord+"&act=input")
                            .get();

                    Elements items = document.getElementById("12810").getElementsByTag("li");
                    if(items.size()==0){
                        return;
                    }
                    int itemNum = 0;
                    while(itemNum<showNum){
                        Element item = items.get(itemNum);
                        String title = item.getElementsByTag("a").attr("title");
                        String href = item.getElementsByTag("a").attr("href");
                        String img = item.getElementsByTag("a").get(0).getElementsByTag("img").get(0).attr("src");
                        Elements priceItem = item.getElementsByTag("p").get(0).getElementsByTag("span");
                        String price;
                        if(priceItem.size()==0){
                            price = item.getElementsByClass("price").get(0).getElementsByClass("search_now_price").get(0).text();
                            if(itemNum!=0){
                                img = item.getElementsByTag("a").get(0).getElementsByTag("img").get(0).attr("data-original");
                            }
                        }else {
                           price = priceItem.get(0).text();

                        }
                        data.add(new pdModel(title,price,img,href,"当当网"));
                        itemNum++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (IndexOutOfBoundsException ex){
                    return;
                }
            }
        }).start();
    }
}
