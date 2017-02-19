package com.sureone.match;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.*;
import android.widget.*;
import com.sureone.*;
import com.sureone.base.BaseActivity;
import com.sureone.model.Group;
import us.xdroid.util.MapHelper;
import us.xdroid.util.xUtil;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sureone
 * Date: 9/14/13
 * Time: 11:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class MatchHomeView extends BaseActivity{

    Long matchId;

    ListView mGrid=null;

    AppsAdapter mAppsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matchhomeview);
        setMyTitle(R.string.matchlist);


        mGrid = (ListView) findViewById(R.id.matchList);
        mAppsAdapter = new AppsAdapter(this);
        mGrid.setAdapter(mAppsAdapter);
        mGrid.setOnItemClickListener(mAppsAdapter);
        mGrid.setOnItemLongClickListener(mAppsAdapter);
        mGrid.setOnScrollListener(mAppsAdapter);
        mGrid.setCacheColorHint(0);




    }

    @Override
    public void onStart() {
        super.onStart();    //To change body of overridden methods use File | Settings | File Templates.
        getController().webListSelfMatch();

    }

    public class AppsAdapter extends BaseAdapter implements AdapterView.OnItemClickListener,
            AdapterView.OnItemLongClickListener,AbsListView.OnScrollListener {
        private LayoutInflater mInflater;
        public AppsAdapter(Context context) {
            // Cache the LayoutInflate to avoid asking for a new one each time.
            mInflater = LayoutInflater.from(context);
        }



        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.self_match_item, null);
                // Creates a ViewHolder and store references to the two children views
                // we want to bind data to.
                holder = new ViewHolder();
                holder.txtGroupName=(TextView) convertView.findViewById(R.id.txtMatchName);
                holder.txtStatus=(TextView) convertView.findViewById(R.id.txtMatchStatus);

                holder.icon = (LoaderImageView) convertView.findViewById(R.id.ivIcon);
                holder.lvIcon=(LinearLayout)convertView.findViewById(R.id.lvIcon);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Map map = mController.getSelfMatchByIndex(position);
            if(map==null) return convertView;
            String s = MapHelper.s(map,"title");
            holder.txtGroupName.setText(s);

            holder.icon.setLocalResource(R.drawable.self_match_icon);



            return convertView;

        }



        public void onItemClick(AdapterView parent, View v, int position, long id) {
            ViewHolder holder = (ViewHolder) v.getTag();

            onClickMatch(position);
        }

        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            ViewHolder holder = (ViewHolder) view.getTag();
            return true;
        }

        public final int getCount() {
            int cnt = mController.getNumberOfMatches();
            return cnt;
        }

        public final Object getItem(int position) {
            return null;
        }

        public final long getItemId(int position) {
            return position;
        }
        public class ViewHolder {
            TextView txtGroupName;
            TextView txtStatus;

            LoaderImageView icon;
            LinearLayout lvIcon;
            int id;
        }


        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // TODO Auto-generated method stub
        }
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();

    }




    int matchIndex;

    void onClickMatch(int pos) {
        matchIndex=pos;
        Map map = mController.getSelfMatchByIndex(pos);
        if(map==null) return;

        Intent intent =  new Intent(this, MatchViewActivity.class);

        intent.putExtra("match-index", matchIndex);


        startActivity(intent);


    }


    public void showOptionsMenu(View v){

        openOptionsMenu();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        menu.clear();
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.matchhomeoptions,menu);
        return super.onPrepareOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //respond to menu item selection

        switch (item.getItemId()) {
            case R.id.newSelfMatch:

                Intent intent =  new Intent(this,NewMatchView.class);
                startActivity(intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }






    void toGoActivity() {
        Intent intent = new Intent(this,com.sureone.GoActivity.class);
        intent.putExtra("view","normal");
        intent.putExtra("fullscreen","false");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        this.startActivity(intent);
        ((Activity)this).finish();
    }


    private void onLoadSelfMatch(Map map){


        Long resultCode = MapHelper.l(map,"CODE");

        if(resultCode.equals(200L)==true){
            getModel().addSelfMatches(map);
            mAppsAdapter.notifyDataSetChanged();
        }


    }


    protected void processHandlerMessage(Message msg){
        switch (msg.what){
            case GoController.WEB_LIST_SELF_MATCH:

                if(msg.arg2==0){

                    onLoadSelfMatch((Map)msg.obj) ;
                }

                break;
        }

    }
}
