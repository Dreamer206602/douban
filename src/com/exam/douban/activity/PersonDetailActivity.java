package com.exam.douban.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.exam.douban.entity.MovieData;
import com.exam.douban.entity.PersonData;
import com.exam.douban.util.Util;
import com.exam.douban_movie_get.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class PersonDetailActivity extends Activity {
	private TextView Info;// ��ʾ Ӱ�˻�����Ϣ�� �ı��ؼ�
	private ImageView mImg;// ��ʾͼƬ��ͼƬ�ؼ�
	private LinearLayout lin_works;// 

	// private Button button;// "���� "��ť
	private ProgressDialog proDialog;
	private PersonData person = new PersonData();
	private String url;// ��Ӱ�ľ���url
	private Util util = new Util();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_detail);
		initView();
		initData();
		// addListener();

		new Thread(new Load()).start();
		proDialog.show();
	}

	/**
	 * ��ʼ��view
	 */
	private void initView() {
		Info = (TextView) findViewById(R.id.tv_m);
		mImg = (ImageView) findViewById(R.id.img_m);
		lin_works = (LinearLayout) findViewById(R.id.lin_pserson_m);
		// button = (Button) findViewById(R.id.button);

		proDialog = new ProgressDialog(this);
		proDialog.setMessage("Loading...");
	}

	/**
	 * ��ʼ������(�õ�url)
	 */
	private void initData() {
		// Bundle bundle = getIntent().getExtras();
		Bundle extra = getIntent().getExtras();
		String id = extra.getString("id");
		url = "https://api.douban.com/v2/movie/celebrity/" + id;

	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message message) {
			Info.setText( person.getName()+ "\n" + person.getName_en() + "\n\n"
					+ person.getBirthday() + "\n" + person.getBorn_place());
			// imageView.setImageBitmap(bm);
			mImg.setImageBitmap(person.getImgLarge());
			//��̬����
			ArrayList<MovieData> works = (ArrayList<MovieData>) person.getWorks();
			for (int i = 0; i < works.size(); i++) {
				String id = works.get(i).getmId();
				Bitmap img = works.get(i).getmImgSmall();
				String name = works.get(i).getmTitle();
				ViewGroup layout = showPersonOrMoive(id, img, name);
				lin_works.addView(layout);
			}
		};
	};

	/**
	 * ��������-ͼƬ��LinearLayout����
	 * 
	 * @param id
	 *            ���ͼƬ����ת��url��Id
	 * @param img
	 *            Ҫ��ʾ��ͼƬ
	 * @param context
	 * @return
	 */
	public ViewGroup showPersonOrMoive(final String id, Bitmap img, String text) {

		LinearLayout lin = new LinearLayout(getApplicationContext());
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lin.setOrientation(LinearLayout.VERTICAL);

		ImageView iv = new ImageView(getApplicationContext());
		iv.setImageBitmap(img);
		iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						PersonDetailActivity.class);
				intent.putExtra("url", id);
				startActivity(intent);
			}
		});
		lin.addView(iv, lp);// addView(view,params)params��Ӧview�Ĳ���

		TextView tv = new TextView(getApplicationContext());
		tv.setTextAppearance(getApplicationContext(),
				android.R.attr.textAppearanceLarge);
		tv.setText(text);
		lin.addView(tv, lp);
		Log.i("OUTPUT", "�������");
		return lin;
	}

	/**
	 * @author �������ݣ����أ�
	 */
	private class Load implements Runnable {

		@Override
		public void run() {
			try {
				String result = util.download(url);
				// String result =
				// util.download("https://api.douban.com/v2/movie/subject/2049435");

				Log.i("OUTPUT", "detail personData download completed");
				Log.i("Download Data", result);
				
				parseDetailInfo(result);
				Log.i("OUTPUT", "detail parse completed");

				handler.sendMessage(new Message());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/**
		 * ������Ӱ������Ϣ�����浽�б�
		 * @param str
		 * @param works Ӱ����Ʒ��������
		 */
		private void parseDetailInfo(String str) {
			
			List<MovieData> works = new ArrayList<MovieData>();
			
			try {
				JSONObject s = new JSONObject(str);
				works = util.parseMovieData(s, "works");//Ӱ����Ʒ��������
				
				JSONObject images = s.getJSONObject("avatars");//ͷ��
				person.setImgLarge(util.downloadImg(images.getString("large")));
				person.setBirthday(s.getString("birthday"));
				person.setName(s.getString("name"));
				person.setName_en(s.getString("name_en"));
				person.setGender(s.getString("gender"));
				person.setBorn_place(s.getString("born_place"));
				person.setWorks(works);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		
	}
}
