package com.makashovadev.filmsearcher.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makashovadev.filmsearcher.utils.AnimationHelper
import com.makashovadev.filmsearcher.databinding.FragmentWatchLaterBinding

class WatchLaterFragment : Fragment()
{
    private lateinit var watchLaterBinding: FragmentWatchLaterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View? = Init()
        return view
    }


    //  инициализация вьюшек в этом фрагменте
    fun Init(): View? {
        watchLaterBinding = FragmentWatchLaterBinding.inflate(layoutInflater)
        val view = watchLaterBinding.root
        AnimationHelper.performFragmentCircularRevealAnimation(watchLaterBinding.root, requireActivity(), 3)
        return view
    }

}