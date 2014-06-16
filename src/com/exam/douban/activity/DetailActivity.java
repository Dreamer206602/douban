package com.exam.douban.activity;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.exam.douban.util.Util;
import com.exam.douban_movie_get.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author ������ϸ��Ϣ��ʾ��ҳ��
 * 
 */

public class DetailActivity extends Activity {

	private TextView mInfo;// ��ʾ ���� ���ݵ���Ϣ�� �ı��ؼ�
	private ImageView mImg;// ��ʾͼƬ��ͼƬ�ؼ�
	private ImageView dImg;// ��ʾ������Ƭ
	private ImageView cImg;// ��ʾ��Ա��Ƭ
	private TextView cName;// ��Ա����
	private TextView dName;// ��������

	// private Button button;// "���� "��ť
	private List<MovieData> moiveList; // ��Ӱ������Ϣ�ķ���LIST
	private ProgressDialog proDialog;
	private String url;// ��Ӱ�ľ���url
	private Util util = new Util();

	private MovieData movie = new MovieData();// ��Ӱ��Ϣ������ʵ��

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		System.out.println("111111");
		initView();
		System.out.println("22222222");
		initData();
		System.out.println("333333333");
		// addListener();

		new Thread(new LoadData()).start();
		proDialog.show();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			mInfo.setText(movie.getmTitle() + "\n" + movie.getmRating() + "\n"
					+ movie.getmYear() + "\n\n" + movie.getmTag());
			// imageView.setImageBitmap(bm);
			mImg.setImageBitmap(movie.getmImgMedium());
			
			proDialog.dismiss();

		};
	};
	/**
	 * ��ʼ��view
	 */
	private void initView() {
		mInfo = (TextView) findViewById(R.id.tv_m);
		mImg = (ImageView) findViewById(R.id.img_m);
		dImg = (ImageView) findViewById(R.id.img_d);
		cImg = (ImageView) findViewById(R.id.im_cast);
		
		cName = (TextView) findViewById(R.id.tv_c_name);
		dName = (TextView) findViewById(R.id.tv_d_name);
		// button = (Button) findViewById(R.id.button);

		proDialog = new ProgressDialog(this);
		proDialog.setMessage("Loading...");
	}
	/**
	 * ��ʼ������
	 */
	private void initData() {
//		Bundle bundle = getIntent().getExtras();
//		String id = bundle.getString("id");

//		url = "https://api.douban.com/v2/movie/subject/" + id;

		// DebugUtil.error(url);
		

		// imageUrl = bundle.getString("imageurl");

	}
	/**
	 * 
	 * @author �������ݣ����أ�
	 *
	 */
	class LoadData implements Runnable {

		@Override
		public void run() {
			try {
				String result = util.download("https://api.douban.com/v2/movie/subject/1764796");
				Log.i("OUTPUT","detail download completed");
				Log.i("Download Data",result);
				parseDetailInfo(result);
				Log.i("OUTPUT","detail parse completed");
				
				handler.sendMessage(new Message());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	private void parseDetailInfo(String str) {
		// TODO Auto-generated method stub
//		try {
//			JSONObject s = new JSONObject(str);
//			JSONArray total = s.getJSONArray("subjects");
//			for (int i = 0; i < total.length(); i++) {
//				JSONObject m = total.getJSONObject(i);
//				movie.setmTitle(m.getString("title"));
//				movie.setmId(m.getString("id"));
//				movie.setmYear(m.getString("year"));
//
//				JSONObject rating = m.getJSONObject("rating");
//				movie.setmRating(rating.getString("average"));// ��ʾ��������
//				moiveList.add(movie);
//				movie.print();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}
