package com.sureone;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

class SgfFragmentAdapter extends FragmentPagerAdapter  implements ViewPager.OnPageChangeListener{
    protected String[] CONTENT = new String[] { null,null};

    private int mCount = CONTENT.length;

    public SgfFragmentAdapter(FragmentManager fm) {
        super(fm);
		CONTENT[0] = GoApp.getInstance().getApplication().getString(R.string.localsgf);
		CONTENT[1] = GoApp.getInstance().getApplication().getString(R.string.remotesgf);		
    }
	
	ViewPager mPager;
	public void setViewPager(ViewPager pager){
		mPager=pager;
	}
	
	LocalSgfFragment mLocalFrag=null;
	RemoteSgfFragment mRemoteFrag=null;

    public BaseFragment mCurFragment=null;

    @Override
    public Fragment getItem(int position) {		
		int idx = position % CONTENT.length;		
		if(idx==0){

			mLocalFrag = LocalSgfFragment.newInstance(CONTENT[idx]);
			mLocalFrag.setViewPager(mPager);
            mCurFragment=mLocalFrag;
			return mLocalFrag;
		}
		if(idx==1){
			mRemoteFrag = RemoteSgfFragment.newInstance(CONTENT[idx]);
			mRemoteFrag.setViewPager(mPager);

			return mRemoteFrag;
		}
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
			if(mRemoteFrag!=null) mRemoteFrag.onHide();
			if(mLocalFrag!=null) mLocalFrag.onShow();
            mCurFragment=mLocalFrag;
		}
		if(position==1){
			if(mLocalFrag!=null) mLocalFrag.onHide();
			if(mRemoteFrag!=null) mRemoteFrag.onShow();
            mCurFragment=mRemoteFrag;
		}

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