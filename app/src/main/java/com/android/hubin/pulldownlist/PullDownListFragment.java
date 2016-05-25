package com.android.hubin.pulldownlist;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;

/**
 * 说明：pull down list
 *
 * @author hubin
 * @Date 2015-4-1
 */
public class PullDownListFragment extends Fragment
{
    private static final String TAG = PullDownListFragment.class.getSimpleName();
    private ListView callListView;
    private PullDownListAdapter listAdapter;
    private View layoutView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        layoutView = inflater.inflate(getLayoutView(), null);
        initComponentViews(layoutView);
        return layoutView;
    }

    public int getLayoutView()
    {
        return R.layout.fragment_pull_down_list;
    }

    public void initComponentViews(View view)
    {
        callListView = (ListView) view.findViewById(R.id.call_list);
        listAdapter = new PullDownListAdapter(getActivity());
        listAdapter.setListView(callListView);
        callListView.setAdapter(listAdapter);
        callListView.setLayoutAnimation(getListViewAnimationController());
        listAdapter.updateList();
    }

    /**
     * ListView animation
     *
     * @return
     */
    public static LayoutAnimationController getListViewAnimationController()
    {
        try
        {
            int duration = 300;
            AnimationSet set = new AnimationSet(true);
            Animation animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setDuration(duration);
            set.addAnimation(animation);
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f);
            animation.setDuration(duration);
            set.addAnimation(animation);
            LayoutAnimationController controller = new LayoutAnimationController(set, 0.1f);
            controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
            return controller;
        }
        catch (Exception e)
        {
        }
        return null;
    }

}
