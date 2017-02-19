package com.sureone.igs;

import com.sureone.*;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.CallLog;
import android.provider.Contacts;
import android.provider.CallLog.Calls;
import android.provider.Contacts.Intents.UI;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.TabHost;
import android.view.Window;
import android.view.WindowManager;

/**
 * The dialer activity that has one tab with the virtual 12key dialer,
 * and another tab with recent calls in it. This is the container and the tabs
 * are embedded using intents.
 */
public class IgsBrowser extends TabActivity implements TabHost.OnTabChangeListener {
    void LogLog(String s) {
        xHelper.log("igs","IgsBrowser: "+s);
    }
    private static final int TAB_INDEX_PLAYERS = 0;
    private static final int TAB_INDEX_GAMES = 1;
    private static final int TAB_INDEX_FRIENDS = 2;

    static final String EXTRA_IGNORE_STATE = "ignore-state";

    private TabHost mTabHost;
    private String mFilterText;
    private Uri mDialUri;

    GoApp mApp=null;
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        mApp =  GoApp.getInstance();
        mController = mApp.getIgsController();
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.igsbrowser);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mTabHost = getTabHost();
        mTabHost.setOnTabChangedListener(this);

        // Setup the tabs
        setupPlayersTab();
        setupGamesTab();
        setupFriendsTab();
    }

    @Override
    protected void onPause() {
        super.onPause();
        int currentTabIndex = mTabHost.getCurrentTab();
    }

    private void setupFriendsTab() {
        // Force the class since overriding tab entries doesn't work
        Intent intent = new Intent();
        intent.setClass(this, IgsFriendListView.class);

        mTabHost.addTab(mTabHost.newTabSpec("Friends")
                        .setIndicator(getString(R.string.friendsTab),
                                      getResources().getDrawable(R.drawable.ic_tab_friends))
                        .setContent(intent));
    }

    private void setupPlayersTab() {
        Intent intent = new Intent();
        intent.setClass(this, IgsPlayerListView.class);

        mTabHost.addTab(mTabHost.newTabSpec("Players")
                        .setIndicator(getString(R.string.playersTab),
                                      getResources().getDrawable(R.drawable.ic_tab_players))
                        .setContent(intent));
    }

    private void setupGamesTab() {
        Intent intent = new Intent();
        intent.setClass(this, IgsGameListView.class);
        mTabHost.addTab(mTabHost.newTabSpec("games")
                        .setIndicator(getText(R.string.gamesTab),
                                      getResources().getDrawable(R.drawable.ic_tab_games))
                        .setContent(intent));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Handle BACK
        if (keyCode == KeyEvent.KEYCODE_BACK && isTaskRoot()) {
            // Instead of stopping, simply push this to the back of the stack.
            // This is only done when running at the top of the stack;
            // otherwise, we have been launched by someone else so need to
            // allow the user to go back to the caller.
            moveTaskToBack(false);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    IgsController mController=null;
    /** {@inheritDoc} */
    public void onTabChanged(String tabId) {
        // Because we're using Activities as our tab children, we trigger
        // onWindowFocusChanged() to let them know when they're active.  This may
        // seem to duplicate the purpose of onResume(), but it's needed because
        // onResume() can't reliably check if a keyguard is active.
        Activity activity = getLocalActivityManager().getActivity(tabId);
        if (activity != null) {
	    if(tabId.compareTo("games")==0) mController.listGames();
	    if(tabId.compareTo("Players")==0) mController.listPlayers("1d-9d o");
            activity.onWindowFocusChanged(true);
        }
    }
}
