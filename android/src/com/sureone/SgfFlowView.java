package com.sureone;
import android.widget.AdapterView;
import com.sureone.R;
import android.view.MenuItem;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.support.v4.view.ViewPager;
import com.viewpagerindicator.PageIndicator;
import android.widget.ImageView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import com.viewpagerindicator.TitlePageIndicator;
import android.app.Activity;
import android.view.View.OnClickListener;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class SgfFlowView extends FragmentActivity{


    SgfFragmentAdapter mAdapter;
    SgfViewPager mPager;
    PageIndicator mIndicator;
	
    GoApp mApp=null;
	GoController mController;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this); 
        mApp =  GoApp.getInstance();
        mController = mApp.getGoController();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sgf_flow_view);
		
        mAdapter = new SgfTitleFragmentAdapter(getSupportFragmentManager());

        mPager = (SgfViewPager)this.findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
		mAdapter.setViewPager(mPager);
		
        mIndicator = (TitlePageIndicator)this.findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);	
		
		mIndicator.setOnPageChangeListener(mAdapter);
		
		
    }
	
	@Override
	public void onResume(){
		super.onResume();

	}


    @Override
    public boolean onContextItemSelected(MenuItem item) {


        return mAdapter.mCurFragment.onContextItemSelected(item);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        mAdapter.mCurFragment.onCreateContextMenu(menu,v,menuInfo);

    }


}
