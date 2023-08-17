package com.example.shop;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class PageActualInfoAdapter extends FragmentStateAdapter {

    private int size;
    private ArrayList<Post> posts;
    public PageActualInfoAdapter(FragmentActivity fragmentActivity, ArrayList<Post> posts) {
        super(fragmentActivity);
        this.posts = posts;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {

        return PageActualInfoFragment.newInstance(position, posts);
    }
    @Override
    public int getItemCount() {

        this.size = posts.size();
        return size;
    }
    public int getSize() {return size;}
}
