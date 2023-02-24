package company.com.to_do_list;

import company.com.to_do_list.database.NotesDetails;

public interface onFragmentInteractionListener {
    void onListFragmentInteraction(NotesDetails item);

    void onTaskCompleted(NotesDetails item);
}
