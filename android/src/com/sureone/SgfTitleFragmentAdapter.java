package com.sureone;

import android.support.v4.app.FragmentManager;

class SgfTitleFragmentAdapter extends SgfFragmentAdapter {
    public SgfTitleFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.CONTENT[position % (getCount())];
    }
}