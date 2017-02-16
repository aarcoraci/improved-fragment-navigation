package angel.drawertest.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import angel.drawertest.R;
import angel.drawertest.infrastructure.AppSection;

/**
 * Created by angel on 2/13/2017.
 */

public class FragmentInbox extends MyAppFragment {

    private Button inboxButton;

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

        inboxButton = (Button)view.findViewById(R.id.inbox_button);
        inboxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getMainActivity(), "I'm alive !", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
