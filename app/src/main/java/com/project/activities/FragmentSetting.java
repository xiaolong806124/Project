package com.project.activities;

import java.io.ByteArrayOutputStream;

import com.project.R;
import com.project.dao.UserDAO;
import com.project.entities.User;
import com.project.util.UserUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentSetting extends Fragment {
	View lySetting;
	User user;
	Bitmap bitmap;
	ImageView portrait;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 1. find the user from phone.
		String userName = getContext().getSharedPreferences("user", Context.MODE_PRIVATE).getString("username", null);
		user = new UserDAO(getContext()).findFromPhone(userName);

		if (user == null) {
			return;
		}

		// 2. initialize portrait showed by image view in this fragment.
		if (user.getPortrait() != null) {
			// ---------------------------->>>????!!!!
			bitmap = BitmapFactory.decodeByteArray(user.getPortrait(), 0, user.getPortrait().length);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		lySetting = (LinearLayout) inflater.inflate(R.layout.tab_setting, container, false);

		return lySetting;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	
		// 1. initialize this fragment view.
		initView();
	}

	/**
	 * set portrait. http://www.cnblogs.com/evilfei/p/3923495.html
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 2:
			if (data != null) {
				Uri uri = data.getData();
				startImageAction(uri, 100, 100, 3, true);
			}
			break;
		case 3:
			saveCropAvator(data);
			break;
		default:
			break;
		}
	}

	private void initView() {
		// 1. get all the controllers form layout
		TextView cuser = (TextView) lySetting.findViewById(R.id.id_txt_cur_user);
		Button btnBlueTeeth = (Button) lySetting.findViewById(R.id.btnBlueTeeth);
		Button exit = (Button) lySetting.findViewById(R.id.id_exit);

		portrait = (ImageView) lySetting.findViewById(R.id.id_img_portrait);
		
		// 2. set click listener for some controllers.
		portrait.setOnClickListener(new PortraitClick());
		btnBlueTeeth.setOnClickListener(new BlueTeethClick());
		exit.setOnClickListener(new ExitClick());

		// 3. if phone has the user, we have to show them in this view.
		if (user != null) {
			cuser.setText(user.getName());
			if (user.getPortrait() != null) {
				portrait.setImageBitmap(bitmap);
			}
		}
	}

	private void startImageAction(Uri uri, int outputX, int outputY, int requestCode, boolean isCrop) {
		Intent intent = null;
		if (isCrop) {
			intent = new Intent("com.android.camera.action.CROP");
		} else {
			intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		}
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", true);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, requestCode);
	}

	private void saveCropAvator(Intent data) {
		if (data == null)
			return;
		Bundle extras = data.getExtras();
		if (extras != null) {
			bitmap = extras.getParcelable("data");
			Log.i("life", "avatar - bitmap = " + bitmap);
			if (bitmap != null) {
				bitmap = toRoundCorner(bitmap, 10);// 调用圆角处理方法
				if (bitmap != null && bitmap.isRecycled()) {
					bitmap.recycle();
				}

				// save this change, need to update this user.
				new Thread(new Runnable() {
					public void run() {
						if (user != null) {
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
							byte[] bytes = baos.toByteArray();
							
							// 3. update these info to the server and phone.
							String res = new UserUtil(getContext()).update("portrait", bytes);
							
							// 4. error, show message.
							if (res.equals("WRONG")) {
								Toast.makeText(getContext(), getResources().getString(R.string.str_update_error),
										Toast.LENGTH_SHORT).show();
							}
						}
					}
				}).start();
				portrait.setImageBitmap(bitmap);
			}
		}
	}

	private Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	class ExitClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// could not be auto-logined.
			Editor edit = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE).edit();
			edit.putBoolean("auto", false);
			edit.commit();

			Intent intent = new Intent();
			intent.setClass(getContext(), ActivityLogin.class);
			getContext().startActivity(intent);
			// filish mainactivity. I don't know until search on internet.
			getActivity().finish();
		}

	}

	/**
	 * select portrait
	 * 
	 * @author zwl
	 *
	 */
	class PortraitClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_PICK, null);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			startActivityForResult(intent, 2);
		}
	}

	/**
	 * click Button Blue teeth to find a device.
	 * 
	 * @author zwl
	 *
	 */
	class BlueTeethClick implements OnClickListener {

		@Override
		public void onClick(View v) {

		}
	}

}
