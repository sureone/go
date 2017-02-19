package com.sureone;

import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created with IntelliJ IDEA.
 * User: sureone
 * Date: 7/6/13
 * Time: 8:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class BaseFragment extends Fragment {

    public boolean onContextItemSelected(MenuItem item) {


        return true;
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

    }

}
