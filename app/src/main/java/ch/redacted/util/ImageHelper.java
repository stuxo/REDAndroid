package ch.redacted.util;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.lang.reflect.Field;

import ch.redacted.app.R;

/**
 * Created by sxo on 26/05/17.
 */

public class ImageHelper {

	public static String replaceImageLinks(String body) {
		//should remove from class= to end of onclick
		//this is a gross hack that needs to happen because the site is swapping these values because img is some site scrubbed image url
		String temp = body.replaceAll("src=\".*?\"", "");
		temp = temp.replaceAll("onclick=\".*?\"", "");
		temp = temp.replaceAll("class=\".*?\"", "");

		return temp.replace("alt=\"", "src=\"");
	}

    public static ImageView getRippy(SwipeRefreshLayout swipeRefreshContainer) {
		ImageView img = null;
		Field f;
		try {
			f = swipeRefreshContainer.getClass().getDeclaredField("mCircleView");
			f.setAccessible(true);
			img = (ImageView)f.get(swipeRefreshContainer);
			img.setImageResource(R.drawable.ic_rippy);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return img;
	}

	public static String getFirstImageLink(String body) {
		try {
			return body.substring(body.indexOf("alt=") + 5, body.indexOf("src=") - 2);
		} catch (IndexOutOfBoundsException e) {
			return "";
		}
	}

	public static void loadImage(Context context, String imageUrl, ImageView imageView, boolean centreCrop, boolean fitCentre) {

		if (centreCrop)  Glide.with(context).load(imageUrl).apply(new RequestOptions().centerCrop()).into(imageView);
		else if (fitCentre)  Glide.with(context).load(imageUrl).apply(new RequestOptions().fitCenter()).into(imageView);
		else Glide.with(context).load(imageUrl).into(imageView);
	}

	public static void loadImageRounded(Context context, String imageUrl, final ImageView imageView) {
		Glide.with(context).load(imageUrl).apply(new RequestOptions().centerCrop().circleCrop()).into(imageView);
	}

	public static void loadImageRounded(Context context, int resource, final ImageView imageView) {
		Glide.with(context).load(resource).apply(new RequestOptions().centerCrop().circleCrop()).into(imageView);
	}
}
