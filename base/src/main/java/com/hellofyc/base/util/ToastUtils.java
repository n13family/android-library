/*
 *  Copyright (C) 2012-2015 Jason Fang ( ifangyucun@gmail.com )
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.hellofyc.base.util;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Toast Tool
 * Create on 2014年12月6日 下午12:12:47
 *
 * @author Jason Fang
 */
public final class ToastUtils {
	static final boolean DEBUG = false;
	
	private static final int TYPE_DEFAULT			 = 1;
	private static final int TYPE_SIMPLE_TEXT_CENTER = 2;
	
	private static Toast mToast;
	private static Toast mCustomToast;

    public static void showDefault(Fragment fragment, CharSequence text) {
        if (fragment == null) return;
        showDefault(fragment.getActivity(), text);
    }

	public static void showDefault(Context context, CharSequence text) {
		showDefault(context, text, false);
	}
	
	public static void showDefault(final Context context, final CharSequence text, final boolean isLongTime) {
		showInMainThread(context, text, isLongTime, TYPE_DEFAULT);
	}
	
	static void doShowDefault(Context context, CharSequence text, boolean isLongTime) {
		if (context == null) {
			throw new IllegalArgumentException("context can not be null");
		}
		
		if (text == null) text = "";
		
		if(mToast == null) {
			mToast = Toast.makeText(context, text, isLongTime ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
			mToast.setDuration(isLongTime ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
		}
		mToast.show();
	}

    public static void show(Fragment fragment, CharSequence text) {
        if (fragment == null) return;
        show(fragment.getActivity(), text);
    }
	
	public static void show(final Context context, final CharSequence text) {
		show(context, text, false);
	}

	public static void show(final Context context, final CharSequence text, final boolean isLongTime) {
		showInMainThread(context, text, isLongTime, TYPE_SIMPLE_TEXT_CENTER);
	}

	static void doShow(@NonNull Context context, CharSequence text, boolean isLongTime) {
		if (text == null) text = "";
		
		if(mToast == null) {
			mCustomToast = new Toast(context);
			TextView textView = new TextView(context);
			mCustomToast.setView(textView);
			textView.setText(text);
		} else {
			((TextView)mCustomToast.getView()).setText(text);
		}
		mCustomToast.setDuration(isLongTime ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
		mCustomToast.setGravity(Gravity.CENTER, 0, 0);
		mCustomToast.show();
	}
	
	static void showInMainThread(final Context context, final CharSequence text, final boolean isLongTime, final int type) {
		if (Looper.myLooper() != Looper.getMainLooper()) {
			if (context instanceof Activity) {
				((Activity) context).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						chooseToInvoke(context, text, isLongTime, type);
					}
					
				});
			}
			return;
		}
		chooseToInvoke(context, text, isLongTime, type);
	}
	
	static void chooseToInvoke(final Context context, final CharSequence text, final boolean isLongTime, final int type) {
		switch (type) {
		case TYPE_DEFAULT:
			doShowDefault(context, text, isLongTime);
			break;
		case TYPE_SIMPLE_TEXT_CENTER:
			doShow(context, text, isLongTime);
			break;
		}
	}
	
	private ToastUtils() {/*Do not new me*/}
}