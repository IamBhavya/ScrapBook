package com.example.bhavya.myapplication;

import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
public class TopListActivity
        extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AnimationAlphaTimer(new TopListActivity());
    }

    private class AnimationAlphaTimer
            extends TimerTask
            implements Animation.AnimationListener
    {
        TopListActivity topList;
        Vector<BitmapDrawable> images;
        int count = 0;

        private String[] FilePathStrings;
        private String[] FileNameStrings;
        private File[] listFile;


        ImageView im=(ImageView) topList.findViewById(R.id.slide_1);//topList.getApplicationContext());
        ImageView im1=(ImageView) topList.findViewById(R.id.slide_1);//topList.getApplicationContext());

        public AnimationAlphaTimer(TopListActivity _topList)
        {
            this.topList = _topList;

            this.images = new Vector<BitmapDrawable>();

            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "Columbus");
            if (!file.exists()) {
                System.out.println("no such directory");

            }
            listFile = file.listFiles();
// Create a String array for FilePathStrings
            FilePathStrings = new String[listFile.length];
// Create a String array for FileNameStrings
            FileNameStrings = new String[listFile.length];

try {
    for (int i = 0; i < listFile.length; i++) {
        FilePathStrings[i] = listFile[i].getAbsolutePath();

        // Get the name image file
        FileNameStrings[i] = listFile[i].getName();
        // LOAD IMAGES HERE

        Uri contentURI = Uri.parse(FileNameStrings[i]);
        ContentResolver cr = getContentResolver();
        InputStream in = cr.openInputStream(contentURI);
        BitmapFactory.Options options = new BitmapFactory.Options();
                      options.inSampleSize=8;
        //
        Bitmap bm=BitmapFactory.decodeStream(in, null, options);
        images.set(i, new BitmapDrawable(this.topList.getApplicationContext().getResources(), bm));
//                       Bitmap thumb = BitmapFactory.decodeStream(in,null,options);
    }
}catch(Exception e){}
            if (this.images.size() > 0) {
                im.setBackgroundDrawable(this.images.get(1));
                //R.id.frame.setBackgroundDrawable(this.images.get(0));

                if (this.images.size() > 1) {
                            im1.setBackgroundDrawable(this.images.get(1));
                    //this.topList.slide_1.setBackgroundDrawable(this.images.get(1));
                }
            }

            this.count = 1;
        }

        public void launch()
        {
            if (this.images.size() >= 2) {
                (new Timer(false)).schedule(this, 100);
            }
        }

        @Override
        public void run()
        {
            this.doit();
            this.cancel();
        }

        private void doit()
        {
            if ((this.count % 2) == 0) {
                AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
                animation.setStartOffset(3000);
                animation.setDuration(3000);
                animation.setFillAfter(true);
                animation.setAnimationListener(this);

                im.startAnimation(animation);
            } else {
                AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
                animation.setStartOffset(3000);
                animation.setDuration(3000);
                animation.setFillAfter(true);
                animation.setAnimationListener(this);

                im1.startAnimation(animation);
            }
        }

        public void onAnimationEnd(Animation animation)
        {
            if ((this.count % 2) == 0) {
                im1.setBackgroundDrawable(
                        this.images.get((this.count + 1) % (this.images.size()))
                );
            } else {
                im.setBackgroundDrawable(
                        this.images.get((this.count + 1) % (this.images.size()))
                );
            }

            this.count++;
            this.doit();
        }

        public void onAnimationRepeat(Animation animation)
        {
        }
        public void onAnimationStart(Animation animation)
        {
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        (new AnimationAlphaTimer(this)).launch();
    }
}