package com.exam.douban.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.exam.douban.activity.DetailActivity;
import com.exam.douban.activity.HistoryActivity;
import com.exam.douban.activity.PersonDetailActivity;
import com.exam.douban.entity.MovieData;
import com.exam.douban.entity.PersonData;
import com.exam.douban_movie_get.R;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Layout;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
/**
 * ������
 */
public class Util {

	/**
	 * ����Intent��ת�ᱨ����֪��ν��
	 * 
	 * @param id
	 * @param img
	 * @param text
	 * @param context
	 * @return
	 */
	public ViewGroup showPersonOrMoive(final String id, Bitmap img,
			String text, final Context context) {

		LinearLayout lin = new LinearLayout(context.getApplicationContext());
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lin.setOrientation(LinearLayout.VERTICAL);

		ImageView iv = new ImageView(context.getApplicationContext());
		iv.setImageBitmap(img);
		iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context.getApplicationContext(),
						PersonDetailActivity.class);
				intent.putExtra("url", id);
				context.startActivity(intent);
			}
		});
		lin.addView(iv, lp);// addView(view,params)params��Ӧview�Ĳ���

		TextView tv = new TextView(context.getApplicationContext());
		tv.setTextAppearance(context.getApplicationContext(),
				android.R.attr.textAppearanceLarge);
		tv.setText(text);
		lin.addView(tv, lp);
		Log.i("OUTPUT", "�������");
		return lin;
	}

	public String download(String urlstr) {
		StringBuffer sBuffer = new StringBuffer();
		try {
			URL url = new URL(urlstr);
			System.out.println(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setReadTimeout(3000);
			connection.setRequestMethod("GET");

			String line;
			InputStreamReader isr = new InputStreamReader(
					connection.getInputStream(), "UTF-8");
			BufferedReader buffer = new BufferedReader(isr);
			System.out.println("response code---"
					+ connection.getResponseCode());
			while ((line = buffer.readLine()) != null) {
				sBuffer.append(line);
			}
			// isr.close();
			connection.disconnect();
		} catch (Exception e) {
			Log.i("OUT PUT", "download error");
			e.printStackTrace();

		}
		return sBuffer.toString();
	}

	/**
	 * ����ͼƬurl������bit
	 * 
	 * @param bmurl
	 * @return ͼƬ����
	 */
	public Bitmap downloadImg(String bmurl) {
		Bitmap bm = null;
		InputStream is = null;
		BufferedInputStream bis = null;
		try {
			URL url = new URL(bmurl);
			URLConnection connection = url.openConnection();
			bis = new BufferedInputStream(connection.getInputStream());
			bm = BitmapFactory.decodeStream(bis);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bis != null)
					bis.close();
				if (is != null)
					is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return bm;
	}

	/**
	 * ��������Ա�͵��ݵ���Ϣ����Ϊ��������Ϣ��ʽ��һ����
	 * 
	 * @param s
	 * @param title
	 * @return List<PersonData>
	 */
	public List<PersonData> parsePersonArray(JSONObject s, String title) {
		List<PersonData> avatars = new ArrayList<PersonData>();
		JSONArray dir;
		try {
			dir = s.getJSONArray(title);
			for (int j = 0; j < dir.length(); j++) {
				JSONObject d = dir.getJSONObject(j);
				PersonData cast = new PersonData();
				cast.setName(d.getString("name"));
				JSONObject a = d.getJSONObject("avatars");// ��Աͷ��
				cast.setImg(downloadImg(a.getString("medium")));
				cast.setId(d.getString("id"));// ��Աid
				avatars.add(cast);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return avatars;
	}
	/**
	 * ������Ӱ��Ŀ��Ϣ����棩
	 * @param s
	 * @param str
	 * @return 
	 */
	public List<MovieData> parseMovieData(JSONObject s, String str) {

		List<MovieData> list = new ArrayList<MovieData>();

		try {
			JSONArray total = s.getJSONArray(str);
			for (int i = 0; i < total.length(); i++) {
				MovieData movie = new MovieData();
				JSONObject m = total.getJSONObject(i);
				movie.setmTitle(m.getString("title"));
				movie.setmId(m.getString("id"));
				movie.setmYear(m.getString("year"));

				JSONObject rating = m.getJSONObject("rating");
				movie.setmRating(rating.getString("average"));// ��ʾ��������

				JSONObject images = m.getJSONObject("images");
				movie.setmImgSmall(downloadImg(images.getString("small")));
				list.add(movie);
				movie.print();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * ����jsonarray���ݵ�string
	 * 
	 * @param arr
	 * @return �ַ���
	 */
	// public String parseJSONArraytoString(JSONArray arr) {
	// StringBuffer str = new StringBuffer();
	//
	// for (int i = 0; i < arr.length(); i++) {
	// try {
	// str = str.append(arr.getString(i)).append(" ");
	// Log.i("parse Json line", arr.getString(i));
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// return str.toString();
	// }
	/**
	 * �����ݷŵ�Movie���ݽṹ��
	 * 
	 * @param str
	 *            json��ʽ���ַ���
	 * @return MovieData
	 */

}
