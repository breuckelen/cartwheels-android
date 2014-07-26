package com.cartwheels;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.cartwheels.tasks.ReviewTask;
import com.cartwheels.tasks.ReviewTaskFragment;

public class ViewReviewFragment extends Fragment
										implements OnItemClickListener {

	private ListView reviews;
	private ReviewItem[] items;
	private ObjectCartListItem item;
	
	private FragmentManager fragmentManager;
	
	private int offset;
	private int limit;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		offset = 0;
		limit = 20;
		
		fragmentManager = getFragmentManager();
		Resources resources = getResources();
		String fragmentTag = resources.getString(R.string.review_task_fragment_string);
		int fragmentId = resources.getInteger(R.integer.review_task_fragment);
		ReviewTaskFragment fragment = (ReviewTaskFragment) fragmentManager.findFragmentByTag(fragmentTag);
		
		if (fragment != null) {
			fragment.setTargetFragment(this, fragmentId);
		} else {
			Log.d("fragment", "null");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
						Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		item = getArguments().getParcelable("ObjectCartListItem");
		
		String cartId = item.cartId;
		ReviewTask reviewTask = new ReviewTask(getActivity().getApplicationContext());
		SharedPreferences preferences = getActivity().getSharedPreferences("CurrentUser", Activity.MODE_PRIVATE);
		
		String email = preferences.getString("email", "");
		String auth_token = preferences.getString("AuthToken", "");
		
		reviewTask.put("email", email);
		reviewTask.put("AuthToken", auth_token);
		reviewTask.put("offset", "" + offset);
		reviewTask.put("limit", "" + limit);
		
		reviewTask.setCartId(cartId);
		
		ReviewTaskFragment fragment = new ReviewTaskFragment();
		fragment.setTask(reviewTask);
		reviewTask.setFragment(fragment);
		fragment.setShowsDialog(false);
		
		Resources resources = getResources();
		int fragmentId = resources.getInteger(R.integer.review_task_fragment);
		String fragmentTag = resources.getString(R.string.review_task_fragment_string);
		fragment.setTargetFragment(this, fragmentId);
		
		
		reviews = (ListView) inflater.inflate(R.layout.fragment_search, container, false);
		
		fragment.show(getFragmentManager(), fragmentTag);
		fragment.execute();
		reviews.setOnItemClickListener(this);
		
		return reviews;
	}

	public static ViewReviewFragment newInstance(Bundle arguments) {
		ViewReviewFragment fragment = new ViewReviewFragment();
		fragment.setArguments(arguments);
		return fragment;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//Resources resources = getResources();
		//int fragmentId = resources.getInteger(R.integer.review_task_fragment);
		if (data != null && data.hasExtra("ReviewItems") && resultCode == Activity.RESULT_OK) {
			items = (ReviewItem[]) data.getParcelableArrayExtra("ReviewItems");
			buildList(items);
			Log.d("onActivityResult ViewReviewFragment", "" + items);
		}
	}
	
	private void buildList(ReviewItem[] items) {
		ReviewItemAdapter adapter = new ReviewItemAdapter(getActivity(), R.layout.listview_review_row, items);
		
		if (reviews != null)
			reviews.setAdapter(adapter);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

	    menu.clear();
	    inflater.inflate(R.menu.view_cart, menu);
	    super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	            load();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void load() {
		String cartId = item.cartId;
		ReviewTask reviewTask = new ReviewTask(getActivity().getApplicationContext());
		SharedPreferences preferences = getActivity().getSharedPreferences("CurrentUser", Activity.MODE_PRIVATE);
		
		String email = preferences.getString("email", "");
		String auth_token = preferences.getString("AuthToken", "");
		
		reviewTask.put("email", email);
		reviewTask.put("AuthToken", auth_token);
		reviewTask.put("offset", "" + offset);
		reviewTask.put("limit", "" + limit);
		
		reviewTask.setCartId(cartId);
		
		ReviewTaskFragment fragment = new ReviewTaskFragment();
		fragment.setTask(reviewTask);
		reviewTask.setFragment(fragment);
		fragment.setShowsDialog(false);
		
		Resources resources = getResources();
		int fragmentId = resources.getInteger(R.integer.review_task_fragment);
		String fragmentTag = resources.getString(R.string.review_task_fragment_string);
		fragment.setTargetFragment(this, fragmentId);
		
		fragment.show(getFragmentManager(), fragmentTag);
		fragment.execute();
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
}
