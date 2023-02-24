package company.com.to_do_list;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AddTaskViewModelFactory implements ViewModelProvider.Factory {

     Context mContext;
     String type;

    public AddTaskViewModelFactory(Context context, String workType){
        this.mContext = context;
        this.type = workType;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
       if (aClass.isAssignableFrom(AddTaskViewModel.class)) {
           return (T) new AddTaskViewModel(mContext,type) ;
        } else {
            throw new IllegalArgumentException("ViewModel Not Found");
        }

    }
}
