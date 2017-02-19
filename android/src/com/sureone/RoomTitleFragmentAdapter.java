package com.sureone;

import android.support.v4.app.FragmentManager;

class RoomTitleFragmentAdapter extends RoomFragmentAdapter {
    public RoomTitleFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.CONTENT[position % (getCount())];
    }
}