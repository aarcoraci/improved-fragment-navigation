package angel.drawertest.infrastructure;

import angel.drawertest.fragments.FragmentSent;
import angel.drawertest.fragments.FragmentInbox;
import angel.drawertest.fragments.FragmentItem;
import angel.drawertest.fragments.FragmentTrash;
import angel.drawertest.fragments.MyAppFragment;

/**
 * Created by angel on 2/13/2017.
 */

public class MyAppFragmentFactory {

    public static MyAppFragment getFragment(AppSection section) {
        switch (section) {
            case INBOX:
                return new FragmentInbox();
            case TRASH:
                return new FragmentTrash();
            case SENT:
                return new FragmentSent();
            case ITEMS:
                return new FragmentItem();
            default:
                return null;
        }
    }
}
