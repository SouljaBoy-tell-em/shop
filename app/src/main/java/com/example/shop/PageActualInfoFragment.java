package com.example.shop;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PageActualInfoFragment extends Fragment {

    public static int pageNumber;
    private static ArrayList<Post> posts;
    public static PageActualInfoFragment newInstance(int page, ArrayList<Post> posts) {

        PageActualInfoFragment pageActualInfoFragment = new PageActualInfoFragment(posts);
        Bundle pageArg = new Bundle();
        pageArg.putInt("page_number", page);
        pageActualInfoFragment.setArguments(pageArg);

        return pageActualInfoFragment;
    }
    public PageActualInfoFragment(ArrayList<Post> posts) {this.posts = posts;}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments() != null ? getArguments().getInt("page_number") : 1;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.info_block_page, container, false);

        Post post = posts.get(pageNumber);
        TextView postText = view.findViewById(R.id.postText);
        ImageView postImage = view.findViewById(R.id.postImage);
        postText.setText(post.getPostText());
        Picasso.get().load(post.getResourceDrawable()).into(postImage);
        return view;
    }
}
