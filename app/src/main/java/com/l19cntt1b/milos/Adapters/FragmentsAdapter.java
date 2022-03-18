package com.l19cntt1b.milos.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.l19cntt1b.milos.Fragments.ChatsFragment;
import com.l19cntt1b.milos.Fragments.DiaryFragment;
import com.l19cntt1b.milos.Fragments.DiscoverFragment;
import com.l19cntt1b.milos.Fragments.IndividualFragment;
import com.l19cntt1b.milos.Fragments.PhoneBookFragment;

public class FragmentsAdapter extends FragmentPagerAdapter {
    int totaltabs=5;
    public FragmentsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public FragmentsAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:return new ChatsFragment();
            case 1:return new PhoneBookFragment();
            case 2:return new DiscoverFragment();
            case 3:return new DiaryFragment();
            case 4:return new IndividualFragment();
            default: return new ChatsFragment();
        }
    }

    @Override
    public int getCount() {
        return totaltabs;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
//        String title =null;
//        if (position==0)
//            title="Tin nhắn";
//        if (position==1)
//            title="Danh bạ";
//        if (position==2)
//            title="Khám phá";
//        if (position==3)
//            title="Nhật ký";
//        if (position==4)
//            title="Cá nhân";

        return null;
    }
}
