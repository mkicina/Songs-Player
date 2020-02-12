package com.example.kicinamartin.songsplayer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.youtube.player.YouTubeStandalonePlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private ListView listApps;
    private String feedXml = "https://www.youtube.com/feeds/videos.xml?playlist_id=PLMC9KNkIncKtPzgY-5rmhvj7fax8fdxoj";
    private String feedCachedXml = "NOTHING";
    public static final String STATE_XML = "feedXml";
    public String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listApps = findViewById(R.id.xmlListView);
        if (savedInstanceState != null){
            feedXml = savedInstanceState.getString(STATE_XML); // v pripade ze sme mali otvoreny playlist a otocime obrazovku, xml sa nam ulozi
        }
        if (feedXml != null) {
            downloadUrl(feedXml);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feeds_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.playlist1:
                feedXml =  "https://www.youtube.com/feeds/videos.xml?playlist_id=PLMC9KNkIncKtPzgY-5rmhvj7fax8fdxoj";
                break;
            case R.id.playlist2:
                feedXml = "https://www.youtube.com/feeds/videos.xml?playlist_id=RDQMQ4bFH3Mph80&";
                break;
            case R.id.playlist3:
                feedXml = "https://www.youtube.com/feeds/videos.xml?playlist_id=PLuUrokoVSxlfUJuJB_D8j_wsFR4exaEmy";
                break;
            case R.id.playlist4:
                feedXml = "https://www.youtube.com/feeds/videos.xml?playlist_id=PLK9Va2Rr8zavU6gh1P1krkusWY13R3qyp";
                break;
            case R.id.playlist5:
                feedXml = "https://www.youtube.com/feeds/videos.xml?playlist_id=PLHGyJr6bx0nkO1XXnLyp9yggqHbahP2J-";
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        downloadUrl(feedXml);
        return true;

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_XML,feedXml);  // v pripade ze sme mali otvoreny playlist a otocime obrazovku, xml sa nam ulozi
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onClick(View v) {
        DownloadData downloadData = new DownloadData();
        FeedAdapter feedAdapter1 = downloadData.getFeedAdapter();
        String url = feedAdapter1.getViewHolder(v).name.getContentDescription().toString();
        Intent intent = YouTubeStandalonePlayer.createVideoIntent(this,YoutubeActivity.GOOGLE_API_KEY,url,0,true,false);
        startActivity(intent);
    }

    private void downloadUrl(String feedUrl){
        if (!feedUrl.equalsIgnoreCase(feedCachedXml)){
            Log.d(TAG, "downloadUrl: Starting Async Task");
            DownloadData downloadData = new DownloadData();
            downloadData.execute(feedUrl);
            feedCachedXml = feedUrl;
            Log.d(TAG, "downloadUrl: Done");
        }else{
            Log.d(TAG, "downloadUrl: URL not changed");
        }
    }

    public class DownloadData extends AsyncTask<String,Void,String> {
        private static final String TAG = "DownloadData";
        ParseApplications parseApplications = new ParseApplications();
        FeedAdapter feedAdapter = new FeedAdapter(MainActivity.this,R.layout.list_record,parseApplications.getApplications());
        FeedAdapter getFeedAdapter(){
                return feedAdapter;
        }
        @Override
        public void onPostExecute(String s) {
            super.onPostExecute(s);
//            Log.d(TAG, "onPostExecute: parameter is: " + s);
            //parseApplications = new ParseApplications();
            parseApplications.parse(s);


            //feedAdapter = new FeedAdapter(MainActivity.this,R.layout.list_record,parseApplications.getApplications());

            listApps.setAdapter(feedAdapter);
            title=feedAdapter.getTitle();
            TextView textView = findViewById(R.id.title);
            textView.setText(title);

            /*ArrayAdapter<FeedEntry> arrayAdapter = new ArrayAdapter<>(
                    MainActivity.this,R.layout.list_item,parseApplications.getApplications());
            listApps.setAdapter(arrayAdapter);*/
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: starts with: " + strings[0]);
            String rssFeed = downloadXML(strings[0]);
            if (rssFeed == null){
                Log.e(TAG, "doInBackground: Error downloading");
            }
            return rssFeed;
        }
        private String downloadXML(String urlPath){
            StringBuilder xmlResult = new StringBuilder();

            try{
                URL url  = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "downloadXML: The response code was: " + response);
                /*InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);*/

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                int charsRead;
                char [] inputBuffer = new char[500];
                while(true){
                    charsRead = reader.read(inputBuffer);
                    if (charsRead < 0){
                        break;
                    }
                    if (charsRead > 0){
                        xmlResult.append(String.copyValueOf(inputBuffer,0,charsRead));
                    }
                }
                reader.close();
                return  xmlResult.toString();
            }catch (MalformedURLException e){
                Log.e(TAG, "downloadXML: Invalid URL " + e.getMessage());
            }catch (IOException e ){
                Log.e(TAG, "downloadXML: IO Exception reading data" + e.getMessage());
            }catch (SecurityException e){
                Log.e(TAG, "downloadXML: Need permission? " + e.getMessage());
            }
            return  null;
        }
    }


}
