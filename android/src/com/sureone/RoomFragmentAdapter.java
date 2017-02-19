package com.sureone;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

class RoomFragmentAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener{
    protected String[] CONTENT = new String[2];

    private int mCount =CONTENT.length;;

    public BaseFragment mCurFragment=null;

	
    public RoomFragmentAdapter(FragmentManager fm) {
	    super(fm);
		CONTENT[0] = GoApp.getInstance().getApplication().getString(R.string.deskslist);
		CONTENT[1] = GoApp.getInstance().getApplication().getString(R.string.playerslist);
        // CONTENT[0] = GoApp.getInstance().getApplication().getString(R.string.grouplist);


    }
	
	ViewPager mPager;
	public void setViewPager(ViewPager pager){
		mPager=pager;
	}
	
	DeskListFragment mDeskFrag=null;
	PlayersListFragment mPlayerFrag=null;
    GroupListFragment mGroupFrag=null;

    @Override
    public Fragment getItem(int position) {		
		int idx = position % CONTENT.length;		
		if(idx==0){
			mDeskFrag = DeskListFragment.newInstance(CONTENT[idx]);
			mDeskFrag.setViewPager(mPager);
            mCurFragment=mDeskFrag;
			return mDeskFrag;
		}
		if(idx==1){
			mPlayerFrag = PlayersListFragment.newInstance(CONTENT[idx]);
			mPlayerFrag.setViewPager(mPager);
            mCurFragment=mPlayerFrag;
			return mPlayerFrag;
		}
        // if(idx==0){
        //     mGroupFrag = GroupListFragment.newInstance(CONTENT[idx]);
        //     mGroupFrag.setViewPager(mPager);
        //     mCurFragment=mGroupFrag;
        //     return mGroupFrag;
        // }
        return null;
    }
	
    @Override
    public void onPageScrollStateChanged(int state) {

    }
	
	@Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
	
		xHelper.log("onPageSelected="+position);
		if(position==0){
			if(mPlayerFrag!=null) mPlayerFrag.onHide();
            // if(mGroupFrag!=null) mGroupFrag.onHide();
            if(mDeskFrag!=null) mDeskFrag.onShow();
            mCurFragment=mDeskFrag;
		}
		if(position==1){
			if(mDeskFrag!=null) mDeskFrag.onHide();
            // if(mGroupFrag!=null) mGroupFrag.onHide();
			if(mPlayerFrag!=null) mPlayerFrag.onShow();
            mCurFragment=mPlayerFrag;
		}
        // if(position==0){
        //     if(mDeskFrag!=null) mDeskFrag.onHide();
        //     if(mPlayerFrag!=null) mPlayerFrag.onHide();
        //     if(mGroupFrag!=null) mGroupFrag.onShow();
        //     mCurFragment=mGroupFrag;
        // }

    }



	

    @Override
    public int getCount() {
		return mCount;        
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}