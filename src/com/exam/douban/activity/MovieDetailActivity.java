package com.exam.douban.activity;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.exam.douban.entity.PersonData;
import com.exam.douban.entity.MovieData;
import com.exam.douban.entity.Properties;
import com.exam.douban.loader.DetailLoader;
import com.exam.douban.loader.ImgLoader;
import com.exam.douban.util.Util;
import com.exam.douban_movie_get.R;

import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * 
 * @author ������ϸ��Ϣ��ʾ��ҳ��
 * 
 */

public class MovieDetailActivity extends Activity {

	private TextView mInfo;// ��ʾ ���� ���ݵ���Ϣ�� �ı��ؼ�
	private ImageView mImg;// ��ʾͼƬ��ͼƬ�ؼ�
	private LinearLayout lin_director;// ���ݵ��ĸ���ʾλ
	private LinearLayout lin_cast;// ��Ա���ĸ���ʾλ
	private Button btn_back;
	private Button btn_home;
	private TextView tv_dir;
	private TextView tv_cast;
	private ProgressDialog proDialog;
	private String url;// ��Ӱ�ľ���url
	private Util util = new Util();
	private DetailLoader detailLoader;
	private ImgLoader imgLoader;
	private MovieData movie;// ��Ӱ��Ϣ������ʵ��

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		initView();
		initData();
		util.backClick(btn_back, btn_home, MovieDetailActivity.this);

		new Thread(new LoadData()).start();
		proDialog.show();
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message message) {

			switch (message.arg1) {
			case 0:
				proDialog.dismiss();
				Toast.makeText(getApplicationContext(), "����ʧ��",
						Toast.LENGTH_SHORT).show();
				// ((Activity) getApplicationContext()).finish();
				finish();
				break;

			case 1:
				mInfo.setText(movie.getTitle() + "\n" + movie.getRating()
						+ "\n" + movie.getYear() + "\n\n" + movie.getTag());
				// ���س�ͼƬ��Ԥ��ͼƬ
				mImg.setImageResource(R.drawable.img_medium);
				// �첽���غ���
				// imgLoader.displayImg(movie.getImgUrl(),mImg);
				imgLoader = new ImgLoader(MovieDetailActivity.this,
						PersonDetailActivity.class, movie.getDirList());
				imgLoader.displayImg(movie.getImgUrl(), mImg);
				// ��̬����LinearLayout���֣���ʾ����ͷ�������
				imgLoader.loadLayout(lin_director);

				imgLoader = new ImgLoader(MovieDetailActivity.this,
						PersonDetailActivity.class, movie.getCastList());
				imgLoader.loadLayout(lin_cast);

				tv_cast.setVisibility(View.VISIBLE);
				tv_dir.setVisibility(View.VISIBLE);
				proDialog.dismiss();
				break;
			}

		}
	};

	// Handler h = new Handler() {
	// @Override
	// public void handleMessage(Message msg) {
	// super.handleMessage(msg);
	// Bitmap bm = (Bitmap) msg.obj;
	// int id = msg.arg1;
	// ImageView iv = (ImageView) findViewById(id);
	// iv.setImageBitmap(bm);
	// }
	// };

	/**
	 * ��ʼ��view
	 */
	private void initView() {
		mInfo = (TextView) findViewById(R.id.tv_m);
		mImg = (ImageView) findViewById(R.id.img_m);
		lin_director = (LinearLayout) findViewById(R.id.lin_director);
		lin_cast = (LinearLayout) findViewById(R.id.lin_cast);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_home = (Button) findViewById(R.id.btn_home);
		tv_cast = (TextView) findViewById(R.id.tv_cast);
		tv_dir = (TextView) findViewById(R.id.tv_dir);

		proDialog = new ProgressDialog(this);
		// proDialog.setMessage("Loading...");
	}

	/**
	 * ��ʼ������(�õ�url)
	 */
	private void initData() {
		Bundle extra = getIntent().getExtras();
		String id = extra.getString("id");
		url = "https://api.douban.com/v2/movie/subject/" + id;
		// url = "https://api.douban.com/v2/movie/subject/1764796";
		// System.out.println("id----"+id);
		detailLoader = new DetailLoader();

	}

	/**
	 * @author �������ݣ����أ�
	 */
	private class LoadData implements Runnable {

		@Override
		public void run() {
			try {
				movie = null;
				movie = detailLoader.loadDetailInfo(url);
				Message msg = new Message();
				if (movie == null) {
					msg.arg1 = 0;
					handler.sendMessage(msg);
				}else{
				msg.arg1 = 1;
				handler.sendMessage(msg);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
