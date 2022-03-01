package com.devdavi.whatsapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.devdavi.whatsapp.fragments.ContatosFragment
import com.devdavi.whatsapp.fragments.ConversasFragment

class FragmentAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> ContatosFragment()
            else -> ConversasFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}