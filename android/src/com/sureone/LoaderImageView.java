package com.sureone;

import java.io.IOException;
import java.net.MalformedURLException;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import us.xdroid.util.RemoteResource;
import us.xdroid.util.ResourceManager;
import android.graphics.BitmapFactory;

/**
 * Free for anyone to use, just say thanks and share :-)
 * @author Blundell
 *
 */
public class LoaderImageView extends LinearLayout {

    private static final int COMPLETE = 0;
	private static final int DL_COMPLETE = 1;
    private static final int FAILED = 2;

    private Context mContext;
    private Drawable mDrawable;
    private ProgressBar mSpinner;
    private ImageView mImage;



    /**
     * This is used when creating the view in XML
     * To have an image load in XML use the tag 'image="http://developer.android.com/images/dialog_buttons.png"'
     * Replacing the url with your desired image
     * Once you have instantiated the XML view you can call
     * setImageDrawable(url) to change the image
     * @param context
     * @param attrSet
     */
    public LoaderImageView(final Context context, final AttributeSet attrSet) {
        super(context, attrSet);
        final String url = attrSet.getAttributeValue(null, "image");
		final String resid = attrSet.getAttributeValue(null, "resid");
        if(url != null) {
            instantiate(context, url);			
        } if(resid !=null){
			int id = Integer.parseInt(resid);
			instantiate(context, id);
		}else{
			instantiate(context, null);
		}
		
		
    }

    /**
     * This is used when creating the view programatically
     * Once you have instantiated the view you can call
     * setImageDrawable(url) to change the image
     * @param context the Activity context
     * @param imageUrl the Image URL you wish to load
     */
    public LoaderImageView(final Context context, final String imageUrl) {
        super(context);
        instantiate(context, imageUrl);
    }
	
    /**
     * This is used when creating the view programatically
     * Once you have instantiated the view you can call
     * setImageDrawable(url) to change the image
     * @param context the Activity context
     * @param imageUrl the Image URL you wish to load
     */
    public LoaderImageView(final Context context, final int resid) {
        super(context);
        instantiate(context, resid);
    }
	
	int mResId = 0;
	
	public int getResourceId(){
		return mResId;
	}
	public void setResourceId(final int resid){	
		
		mResId=resid;
		ResourceManager rm = ResourceManager.getInstance();
		if(rm==null) return;
		
		mResource = rm.getResourceById(resid);
		
		if(mResource==null) return;	
		
		if(mResource.getPath()!=null){
			setImageFile(mResource.getPath());
		}else{
		    if(mResource.getUrl() != null) {
				xHelper.log("to download resource from "+mResource.getUrl());
				downloadResource();
			}
		}			
	}
	
	public void reloadResource(){	
		

		xHelper.log("reloadResource");
		ResourceManager rm = ResourceManager.getInstance();
		if(rm==null) return;
		
		mResource = rm.getResourceById(mResId);
		
		if(mResource==null) return;	
		
		if(mResource.getPath()!=null){
			setImageFile(mResource.getPath());
		}else{
		    if(mResource.getUrl() != null) {
				xHelper.log("to download resource from "+mResource.getUrl());
				downloadResource();
			}
		}			
	}	
	
    /**
     *  First time loading of the LoaderImageView
     *  Sets up the LayoutParams of the view, you can change these to
     *  get the required effects you want
     */
    private void instantiate(final Context context, final String imageUrl) {
        mContext = context;

        mImage = new ImageView(mContext);
        mImage.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        //mImage.setLayoutParams(new LayoutParams(96,96));

        mSpinner = new ProgressBar(mContext);
        mSpinner.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        mSpinner.setIndeterminate(true);

        addView(mSpinner);
        addView(mImage);

        if(imageUrl != null) {
            setImageDrawable(imageUrl);
        }
    }
	
	
    /**
     *  First time loading of the LoaderImageView
     *  Sets up the LayoutParams of the view, you can change these to
     *  get the required effects you want
     */
    private void instantiate(final Context context, final int resid) {
        mContext = context;
		mResId=resid;
		xHelper.log("load resource ="+resid);
        mImage = new ImageView(mContext);
        mImage.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        //mImage.setLayoutParams(new LayoutParams(96,96));

        mSpinner = new ProgressBar(mContext);
        mSpinner.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        mSpinner.setIndeterminate(true);

        addView(mSpinner);
        addView(mImage);
		
		ResourceManager rm = ResourceManager.getInstance();
		if(rm==null) return;
		
		mResource = rm.getResourceById(resid);
		
		if(mResource==null) return;
		
		if(mResource.getPath()!=null){
			setImageFile(mResource.getPath());
		}else{
		    if(mResource.getUrl() != null) {
				xHelper.log("to download resource from "+mResource.getUrl());
				downloadResource();
			}
		}		
    }	
	
	RemoteResource mResource=null;
	
	
	public void setImageFile(String fn){
		try{
			
			mImage.setImageBitmap(BitmapFactory.decodeFile(fn));
			mImage.setVisibility(View.VISIBLE);
			mSpinner.setVisibility(View.GONE);	

			xHelper.log("load resource end "+fn);			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
    /**
     * Set's the view's drawable, this uses the internet to retrieve the image
     * don't forget to add the correct permissions to your manifest
     * @param imageUrl the url of the image you wish to load
     */
    public void downloadResource() {
        mDrawable = null;
        mSpinner.setVisibility(View.VISIBLE);
        mImage.setVisibility(View.GONE);
        new Thread() {
            public void run() {
              	ResourceManager rm = ResourceManager.getInstance();
				rm.downloadResource(mResource);                    
                imageLoadedHandler.sendEmptyMessage(DL_COMPLETE);
              
            };
        } .start();
    }
    public void setLocalResource(int resId) {
        mImage.setVisibility(View.VISIBLE);
        mImage.setImageResource(resId);
        mSpinner.setVisibility(View.GONE);

    }

    /**
     * Set's the view's drawable, this uses the internet to retrieve the image
     * don't forget to add the correct permissions to your manifest
     * @param imageUrl the url of the image you wish to load
     */
    public void setImageDrawable(final String imageUrl) {
        mDrawable = null;
        mSpinner.setVisibility(View.VISIBLE);
        mImage.setVisibility(View.GONE);
        new Thread() {
            public void run() {
                try {
                    mDrawable = getDrawableFromUrl(imageUrl);
                    imageLoadedHandler.sendEmptyMessage(COMPLETE);
                } catch (MalformedURLException e) {
                    imageLoadedHandler.sendEmptyMessage(FAILED);
                } catch (IOException e) {
                    imageLoadedHandler.sendEmptyMessage(FAILED);
                }
            };
        } .start();
    }

    /**
     * Callback that is received once the image has been downloaded
     */
    private final Handler imageLoadedHandler = new Handler(new Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
            case COMPLETE:
                mImage.setImageDrawable(mDrawable);
                mImage.setVisibility(View.VISIBLE);
                mSpinner.setVisibility(View.GONE);
                break;
            case DL_COMPLETE:
				if(mResource.getPath()!=null)
					setImageFile(mResource.getPath());
                break;				
            case FAILED:
            default:
                // Could change image here to a 'failed' image
                // otherwise will just keep on spinning
                break;
            }
            return true;
        }
    });

    /**
     * Pass in an image url to get a drawable object
     * @return a drawable object
     * @throws IOException
     * @throws MalformedURLException
     */
    private static Drawable getDrawableFromUrl(final String url) throws IOException, MalformedURLException {
        return Drawable.createFromStream(((java.io.InputStream)new java.net.URL(url).getContent()), "name");
    }

}
