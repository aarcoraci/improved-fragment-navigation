package angel.drawertest.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import angel.drawertest.R;
import angel.drawertest.infrastructure.AppSection;

/**
 * Created by angel on 2/13/2017.
 */

public class FragmentInbox extends MyAppFragment {

    public FragmentInbox(){
        this.isRootSection = true;
        this.appSection = AppSection.INBOX;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);

        getMainActivity().getSupportActionBar().setTitle("Inbox");

        return view;
    }
}