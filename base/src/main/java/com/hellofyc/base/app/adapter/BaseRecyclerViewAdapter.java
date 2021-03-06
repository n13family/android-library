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

package com.hellofyc.base.app.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hellofyc.base.app.ResourcesValue;
import com.hellofyc.base.app.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Base RecyclerView Adapter
 * Create on 2014年12月3日 下午2:18:26
 *
 * @author Jason Fang
 */
abstract public class BaseRecyclerViewAdapter<T, VH extends BaseRecyclerViewAdapter.ViewHolder> extends RecyclerView.Adapter<VH>
        implements ResourcesValue {

    private BaseActivity mActivity;
	private Context mContext;
	private LayoutInflater mInflater;
	private List<T> mDataList = new ArrayList<>();
    private OnRecyclerViewItemClickListener mRecyclerViewItemClickListener;
	
	public BaseRecyclerViewAdapter(Context context, List<T> data) {
		mContext = context;
		mInflater = LayoutInflater.from(context);

		if (data != null) {
			mDataList.addAll(data);
		}

        if (context instanceof BaseActivity) {
            mActivity = (BaseActivity) context;
        }
	}
	
	abstract public VH onCreateItemViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType);
	abstract public void onBindItemViewHolder(VH itemHolder, int position, int viewType);
	
	@Override
	public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
		return onCreateItemViewHolder(mInflater, parent, viewType);
	}
	
	@Override
	public final void onBindViewHolder(final VH holder, final int position) {
		onBindItemViewHolder(holder, position, getItemViewType(position));
        holder.itemView.setOnClickListener(new ItemClickListener(this, holder, position));
    }

	@Override
	public int getItemCount() {
		return mDataList.size();
	}
	
	public T getItem(int position) {
        if (position < 0 || position >= mDataList.size()) {
            return null;
        }
		return mDataList.get(position);
	}

    public List<T> getItems() {
        return mDataList;
    }
	
	@Override
	public int getItemViewType(int position) {
		return super.getItemViewType(position);
	}
	
	@Override
	public void onViewRecycled(VH holder) {
		super.onViewRecycled(holder);
	}

	public void clear() {
		mDataList.clear();
		notifyDataSetChanged();
	}

    public void updateItems(List<T> newItems) {
        mDataList.clear();
        if (newItems != null && newItems.size() > 0) {
            mDataList.addAll(newItems);
        }
        notifyDataSetChanged();
    }

	/**
	 * add item
	 * 
	 * @param itemData data
	 */
	public void addItem(T itemData) {
        if (itemData == null) return;
		addItem(getItemCount(), itemData);
	}
	
	/**
	 * insert item at position
	 * 
	 * @param position position
	 * @param itemData data
	 */
	public void addItem(int position, T itemData) {
		mDataList.add(position, itemData);
		notifyItemInserted(position);
	}
	
	/**
	 * remove item
	 * @param position position
	 */
	public void removeItem(@IntRange(from = 0, to = Integer.MAX_VALUE) int position) {
        if (position >= mDataList.size()) {
            return;
        }
		mDataList.remove(position);
		notifyItemRemoved(position);
	}
	
	/**
	 * insert items
	 * @param itemDataList list
	 */
	public void addItems(List<T> itemDataList) {
        if (itemDataList == null || itemDataList.size() == 0) return;
		addItems(getItemCount(), itemDataList);
	}
	
	/**
	 * insert items at position
	 * 
	 * @param positionStart start
	 * @param items items
	 */
	public void addItems(int positionStart, List<T> items) {
        if (items == null || items.size() <= 0) return;
        if (positionStart > mDataList.size()) {
            throw new IllegalArgumentException("positionStart cannot rather than list size!");
        }

		mDataList.addAll(positionStart, items);
		notifyItemRangeInserted(positionStart, items.size());
	}
	
	/**
	 * modify item
	 * 
	 * @param position position
	 * @param newItem newItem
	 */
	public void updateItem(int position, T newItem) {
        if (newItem == null || position < 0 || position >= mDataList.size()) {
            return;
        }
		mDataList.add(position, newItem);
		notifyItemChanged(position);
	}

	/**
	 * remove items
	 * @param items items
	 */
	public void removeItems(List<T> items) {
        if (items == null || items.size() <= 0) return;

        if (mDataList.containsAll(items)) {
            mDataList.removeAll(items);
            notifyItemRangeRemoved(0, items.size());
        }
	}
	
	/**
	 * item moved
	 * @param fromPosition from position
	 * @param toPosition to position
	 */
	public void moveItem(int fromPosition, int toPosition) {
        if (fromPosition <0 || toPosition < 0 || fromPosition == toPosition) return;

		T tempValue = mDataList.get(fromPosition);
		mDataList.set(fromPosition, mDataList.get(toPosition));
		mDataList.set(toPosition, tempValue);
		notifyItemInserted(toPosition);
		notifyItemRemoved(fromPosition);
	}

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
        mRecyclerViewItemClickListener = listener;
    }

    public Context getContext() {
        return mContext;
    }

    public BaseActivity getActivity() {
        return mActivity;
    }

    @Override
    public Resources getResources() {
        return mContext.getResources();
    }

    @Override
    public String getString(@StringRes int id) {
        return getResources().getString(id);
    }

    @Override
    public String getString(@StringRes int id, Object... args) {
        return getResources().getString(id, args);
    }

    @Override
    public int getColor(@ColorRes int id) {
        return ContextCompat.getColor(mContext, id);
    }

    @Override
    public int getColor(@ColorRes int id, @Nullable Resources.Theme theme) {
        return ResourcesCompat.getColor(getResources(), id, theme);
    }

    @Override
    public Drawable getDrawable(@DrawableRes int id) {
        return ContextCompat.getDrawable(mContext, id);
    }

    @Override
    public Drawable getDrawable(@DrawableRes int id, Resources.Theme theme) {
        return ResourcesCompat.getDrawable(getResources(), id, theme);
    }

    @Override
    public ColorStateList getColorStateList(@ColorRes int id) {
        return ContextCompat.getColorStateList(mContext, id);
    }

    @Override
    public ColorStateList getColorStateList(@ColorRes int id, @Nullable Resources.Theme theme) {
        return ResourcesCompat.getColorStateList(getResources(), id, theme);
    }

    @Override
    public Drawable getDrawableForDensity(@DrawableRes int id, int density, @Nullable Resources.Theme theme) {
        return ResourcesCompat.getDrawableForDensity(getResources(), id, density, theme);
    }

    static class ItemClickListener implements View.OnClickListener {

        BaseRecyclerViewAdapter mAdapter;
        RecyclerView.ViewHolder mHolder;
        int mPosition;

        public ItemClickListener(BaseRecyclerViewAdapter adapter, RecyclerView.ViewHolder holder, int position) {
            mAdapter = adapter;
            mHolder = holder;
            mPosition = position;
        }

        @Override
        public void onClick(View v) {
            if (mAdapter.mRecyclerViewItemClickListener != null) {
                mAdapter.mRecyclerViewItemClickListener.onRecyclerViewItemClick(mHolder, mPosition);
            }
        }
    }

	public static abstract class ViewHolder extends RecyclerView.ViewHolder {

		public ViewHolder(View itemView) {
			super(itemView);
		}

		public void setText(@IdRes int id, CharSequence text) {
			TextView textView = (TextView)itemView.findViewById(id);
			if (textView != null) {
				textView.setText(text);
			}
		}

		public void setImage(@IdRes int id, Bitmap bitmap) {
			ImageView imageView = (ImageView)itemView.findViewById(id);
			if (imageView != null) {
				imageView.setImageBitmap(bitmap);
			}
		}
	}

    public interface OnRecyclerViewItemClickListener {
        void onRecyclerViewItemClick(RecyclerView.ViewHolder absItemViewHolder, int position);
    }

}

