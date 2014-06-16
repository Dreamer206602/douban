package com.exam.douban.activity;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
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
import android.widget.LinearLayout;
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
	private LinearLayout linD1;//���ݵ��ĸ���ʾλ
	private LinearLayout linD2;
	private LinearLayout linD3;
	private LinearLayout linD4;
	private LinearLayout linC1;//��Ա���ĸ���ʾλ
	private LinearLayout linC2;
	private LinearLayout linC3;
	private LinearLayout linC4;

	// private Button button;// "���� "��ť
	private List<MovieData> moiveList; // ��Ӱ������Ϣ�ķ���LIST
	private List<CastData> castList;
	private List<CastData> directorList;
	private ProgressDialog proDialog;
	private String url;// ��Ӱ�ľ���url

	private MovieData movie = new MovieData();// ��Ӱ��Ϣ������ʵ��

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		initView();
		initData();
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
			mImg.setImageBitmap(movie.getmImgLarge());
			
			

			proDialog.dismiss();

		};
	};

	/**
	 * ��ʼ��view
	 */
	private void initView() {
		mInfo = (TextView) findViewById(R.id.tv_m);
		mImg = (ImageView) findViewById(R.id.img_m);
		dImg = (ImageView) findViewById(R.id.im_d1);
		cImg = (ImageView) findViewById(R.id.im_c1);
		linD1 = (LinearLayout) findViewById(R.id.lin_d1);
		linD2 = (LinearLayout) findViewById(R.id.lin_d2);
		linD3 = (LinearLayout) findViewById(R.id.lin_d3);
		linD4 = (LinearLayout) findViewById(R.id.lin_d4);
		linC1 = (LinearLayout) findViewById(R.id.lin_c1);
		linC2 = (LinearLayout) findViewById(R.id.lin_c2);
		linC3 = (LinearLayout) findViewById(R.id.lin_c3);
		linC4 = (LinearLayout) findViewById(R.id.lin_c4);
		
		
		

		cName = (TextView) findViewById(R.id.tv_c1);
		dName = (TextView) findViewById(R.id.tv_d1);
		// button = (Button) findViewById(R.id.button);

		proDialog = new ProgressDialog(this);
		proDialog.setMessage("Loading...");
	}

	/**
	 * ��ʼ������
	 */
	private void initData() {
		// Bundle bundle = getIntent().getExtras();
		Bundle extra = getIntent().getExtras();
		String id = extra.getString("id");

		url = "https://api.douban.com/v2/movie/subject/" + id;

	}

	/**
	 * @author �������ݣ����أ�
	 */
	private class LoadData implements Runnable {

		@Override
		public void run() {
			try {
				String result = Util.download(url);
				Log.i("OUTPUT", "detail download completed");
				Log.i("Download Data", result);
				parseDetailInfo(result);
				Log.i("OUTPUT", "detail parse completed");

				handler.sendMessage(new Message());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void parseDetailInfo(String str) {
			

			// TODO Auto-generated method stub
			try {
				JSONObject s = new JSONObject(str);
				castList = parsePersonArray(s, "casts");
				directorList = parsePersonArray(s, "directors");

				movie.setmYear(s.getString("year"));
				JSONObject rating = s.getJSONObject("rating");
				movie.setmRating(rating.getString("average"));// ��ʾ��������

				JSONObject images = s.getJSONObject("images");
				movie.setmImgLarge(Util.downloadImg(images.getString("large")));
				movie.setmTitle(s.getString("title"));
				JSONArray genres = s.getJSONArray("genres");
				StringBuffer buffer = new StringBuffer();
				for (int j = 0; j < genres.length(); j++) {
					if(j==genres.length()-1)
						buffer = buffer.append(genres.getString(j));
					else
						buffer = buffer.append(genres.getString(j)+" / ");
						
				}
				movie.setmTag(buffer.toString());
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/**
		 * ��������Ա�͵��ݵ���Ϣ����Ϊ��������Ϣ��ʽ��һ����
		 * @param s
		 * @param title
		 * @return
		 */
		public List<CastData> parsePersonArray(JSONObject s, String title) {
			List<CastData> avatars = new ArrayList<CastData>();
			JSONArray dir;
			try {
				dir = s.getJSONArray(title);
				for (int j = 0; j < dir.length(); j++) {
					JSONObject d = dir.getJSONObject(j);
					CastData cast = new CastData();
					cast.setName(d.getString("name"));
					JSONObject a = d.getJSONObject("avatars");// ��Աͷ��
					cast.setImg(Util.downloadImg(a.getString("medium")));
					cast.setId(d.getString("id"));// ��Աid
					avatars.add(cast);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return avatars;
		}
	}
}
