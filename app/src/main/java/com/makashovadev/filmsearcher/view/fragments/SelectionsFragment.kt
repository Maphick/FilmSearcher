package com.makashovadev.filmsearcher.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makashovadev.filmsearcher.utils.AnimationHelper
import com.makashovadev.filmsearcher.databinding.FragmentCollectionsBinding

class SelectionsFragment : Fragment() {

    private lateinit var selectionsBinding: FragmentCollectionsBinding

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
        selectionsBinding = FragmentCollectionsBinding.inflate(layoutInflater)
        val view = selectionsBinding.root
        AnimationHelper.performFragmentCircularRevealAnimation(selectionsBinding.root, requireActivity(), 1)
        return view
    }


}