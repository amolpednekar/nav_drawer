package com.example.simeon.nav_drawer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 19/09/2015.
 */
public class RssMainActivity extends AppCompatActivity {
    static final int DIALOG_ERROR_CONNECTION = 1;
    private SwipeRefreshLayout swipeContainer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isOnline(this)) {
            showDialog(DIALOG_ERROR_CONNECTION); //display no connection dialog
        } else {
            if (savedInstanceState == null) {
                setContentView(R.layout.rss_main);
            }

            addRssFragment();
        }
    }

    public boolean isOnline(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if (ni != null && ni.isConnected())
            return true;
        else
            return false;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch (id) {
            case DIALOG_ERROR_CONNECTION:
                AlertDialog.Builder errorDialog = new AlertDialog.Builder(this);
                errorDialog.setTitle("Error");
                errorDialog.setMessage("No internet connection.");
                errorDialog.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog errorAlert = errorDialog.create();
                return errorAlert;

            default:
                break;
        }
        return dialog;
    }


    public void addRssFragment() {
        rss_fragment fragment = new rss_fragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.add(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("fragment_added", true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.rss_sub_main, menu);
        return true;
    }

    void SetRSS()
    {
        final CharSequence sources[] = new CharSequence[] {"Personal Finance - Top Stories", "Times Of India - Finance"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a feed");
        builder.setItems(sources, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (sources[which] == "Personal Finance - Top Stories") {
                    Toast.makeText(getApplicationContext(), "Showing Top Stories", Toast.LENGTH_LONG).show();
                    rss_service.RSS_LINK="http://feeds.marketwatch.com/marketwatch/pf/";
                    ref();
                } else if (sources[which] == "Times Of India - Finance") {
                    Toast.makeText(getApplicationContext(), "Showing Finance News", Toast.LENGTH_LONG).show();
                    rss_service.RSS_LINK="http://timesofindia.feedsportal.com/c/33039/f/533919/index.rss";
                    ref();
                }
            }
        });
        builder.show();
    }

    public void ref()
    {
        setContentView(R.layout.rss_main);
        addRssFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.change_feed) {

            //RssService.RSS_LINK = "";
            SetRSS();
            return true;
        }
        if(id==R.id.refresh_rss)
        {
            ref();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

