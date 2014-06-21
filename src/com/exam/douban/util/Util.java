package com.exam.douban.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.exam.douban.activity.DetailActivity;
import com.exam.douban.activity.HistoryActivity;
import com.exam.douban.activity.MainActivity;
import com.exam.douban.activity.PersonDetailActivity;
import com.exam.douban.entity.MovieData;
import com.exam.douban.entity.PersonData;
import com.exam.douban_movie_get.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * ������
 */
public class Util {
	

	/**
	 * ���������������Ŀ
	 * 
	 * @param context
	 * @param type
	 * @param movieInfo
	 */
	public void saveHistory(Context context, String type, String movieInfo) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				type, Activity.MODE_APPEND);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(movieInfo, movieInfo);
		Log.i("movieInfo", movieInfo);

		if (editor.commit())
			Log.i("MainActivity", "��ʷ��¼�ɹ�");

		// ��ȡ��ʷ��¼
	}

	
	

	

	/**
	 * �������ذ�ť�ļ�������
	 * 
	 * @param back
	 * @param home
	 * @param context
	 */
	public void backClick(Button back, Button home, final Context context) {
		// TODO Auto-generated method stub
		home.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				context.startActivity(new Intent(context, MainActivity.class));
				((Activity) context).finish();
			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((Activity) context).finish();
			}
		});

	}

	/**
	 * �򶹰귢�����󣬷����ַ�������
	 * 
	 * @param urlstr
	 * @return
	 * @throws IOException
	 */
	public String download(String urlstr) throws IOException {

		URL url = new URL(urlstr);
		System.out.println(urlstr);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setReadTimeout(3000);
		connection.setRequestMethod("GET");

		String line;
		// connection.getInputStream()���Ƿ���������������,��Ȼ��������Ӧ�ò��󣬻���ʹ�û����ȡ��
		InputStreamReader isr = new InputStreamReader(connection.getInputStream(), "UTF-8");
		if(isr != null){
			BufferedReader buffer = new BufferedReader(isr);
			Log.i("Response Code", connection.getResponseCode() + "");
			StringBuffer sBuffer = new StringBuffer();
			while ((line = buffer.readLine()) != null) {
				sBuffer.append(line);
			}
			return sBuffer.toString();
		}else
			return "ERROR";
	}
	
	/**
	 * �����������صĵ�Ӱ��Ŀ��Ϣ����棩
	 * @param s
	 * @param str
	 * @param imgType
	 *            ���صĵ�Ӱ�ߴ�
	 * @return
	 */
	public List<MovieData> parseMovieData(JSONObject s, String str,
			String imgType) {

		List<MovieData> list = new ArrayList<MovieData>();

		try {
			JSONArray total = s.getJSONArray(str);
			for (int i = 0; i < total.length(); i++) {
				MovieData movie = new MovieData();
				JSONObject m;
				if (str.equals("works")) {
					JSONObject mov = total.getJSONObject(i);
					m = mov.getJSONObject("subject");
				} else
					m = total.getJSONObject(i);
				movie.setTitle(m.getString("title"));
				movie.setId(m.getString("id"));
				movie.setYear(m.getString("year"));

				JSONObject rating = m.getJSONObject("rating");
				movie.setRating(rating.getString("average"));// ��ʾ��������

				JSONObject images = m.getJSONObject("images");
				movie.setImgUrl(images.getString(imgType));
				list.add(movie);
				movie.print();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}


}
