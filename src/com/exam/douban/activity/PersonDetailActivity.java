package com.exam.douban.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import com.exam.douban.entity.MovieData;
import com.exam.douban.entity.PersonData;
import com.exam.douban.entity.Properties;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class PersonDetailActivity extends Activity {
	private TextView Info;// ��ʾ Ӱ�˻�����Ϣ�� �ı��ؼ�
	private ImageView mImg;// ��ʾͼƬ��ͼƬ�ؼ�
	private LinearLayout lin_works;//
	// private List<MovieData> works = new ArrayList<MovieData>();

	// private Button button;// "���� "��ť
	private ProgressDialog proDialog;
	private PersonData person;
	private String url;// ��Ӱ�ľ���url
	private Util util = new Util();
	private Button btn_back;
	private Button btn_home;
	private TextView tv_wokes;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_detail);

		initView();
		initData();

		util.backClick(btn_back, btn_home, PersonDetailActivity.this);
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
		tv_wokes = (TextView) findViewById(R.id.tv_works);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_home = (Button) findViewById(R.id.btn_home);

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
		//���ص������ﶼû��birthday����ֶ�
		@Override
		public void handleMessage(Message message) {
			Info.setText(person.getName() + "\n" + person.getName_en() + "\n\n"
					+ "���գ�" +person.getBirthday()+ "\n" + "������" + person.getBorn_place());
			mImg.setImageBitmap(person.getImg());
			
			// ��̬����
			ArrayList<MovieData> works = (ArrayList<MovieData>)person.getWorks();
			System.out.println("works ---  "+works.toString());
			for (int i = 0; i < works.size(); i++) {
				String id = works.get(i).getId();
				Bitmap img = works.get(i).getImg();
				String name = works.get(i).getTitle();
				ViewGroup layout = showPersonOrMoive(id, img, name);
				lin_works.addView(layout);
				System.out.println("dongtaibuju----"+i);
			}
			
			tv_wokes.setVisibility(View.VISIBLE);
			proDialog.dismiss();
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
						DetailActivity.class);
				intent.putExtra("id", id);
				util.saveHistory(getApplicationContext(), Properties.HISTORY_NAME_MOVIE, id);
				startActivity(intent);
			}
		});
		lin.addView(iv, lp);// addView(view,params)params��Ӧview�Ĳ���

		TextView tv = new TextView(getApplicationContext());
		tv.setTextAppearance(getApplicationContext(),
				android.R.attr.textAppearanceLarge);
		tv.setWidth(155);
		tv.setText(text);
		// if(text.length()>4){
		// tv.setText(text.substring(0,4)+"...");
		// }
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
				Log.i("OUTPUT", "detail person parse completed");
				handler.sendMessage(new Message());
				Log.i("OUTPUT", "msg sent completed");
				

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/**
		 * ������Ӱ������Ϣ�����浽�б�
		 * 
		 * @param str
		 * @param works
		 *            Ӱ����Ʒ��������
		 */
		private void parseDetailInfo(String result) {

			// �����������Ӱ���ص����ݸ�ʽ�Ͷ�����д�Ĳ�һ������Ʒ��Ŀ��ͬ��subject�������飬
			person = new PersonData();
			try {
				JSONObject s = new JSONObject(result);

				person.setWorks(util.parseMovieData(s, "works","medium"));
				Log.i("OUTPUT", "works parse completly");

				JSONObject images1 = s.getJSONObject("avatars");// ͷ��
				person.setImg(util.downloadImg(images1.getString("medium")));
				// person.setBirthday(s.getString("birthday"));
				person.setName(s.getString("name"));
				person.setName_en(s.getString("name_en"));
				person.setGender(s.getString("gender"));
				person.setBorn_place(s.getString("born_place"));
				person.print();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
