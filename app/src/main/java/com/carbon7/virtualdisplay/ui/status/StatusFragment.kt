package com.carbon7.virtualdisplay.ui.status

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.databinding.FragmentStatusBinding

class StatusFragment : Fragment() {

    companion object {
        fun newInstance() = StatusFragment()
    }

    private lateinit var viewModel: StatusViewModel
    private var _binding: FragmentStatusBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding
        get() = _binding!!


    private val rotateOpen: Animation by lazy {AnimationUtils.loadAnimation(context, R.anim.rotate_open_anim)}
    private val rotateClose: Animation by lazy {AnimationUtils.loadAnimation(context,R.anim.rotate_close_anim)}
    private val fromBottom: Animation by lazy {AnimationUtils.loadAnimation(context,R.anim.from_bottom_anim)}
    private val toBottom: Animation by lazy {AnimationUtils.loadAnimation(context,R.anim.to_botton_anim)}
    private var fabOpened : Boolean = false;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(StatusViewModel::class.java)

        viewModel.currentStatus.observe(viewLifecycleOwner,{
            val recyclerViewState = binding.listStatus.layoutManager?.onSaveInstanceState()
            binding.listStatus.adapter = StatusAdapter(it)
            binding.listStatus.layoutManager?.onRestoreInstanceState(recyclerViewState)
        })


        _binding = FragmentStatusBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*binding.btn1.setOnClickListener { viewModel.addS000(true)}
        binding.btn2.setOnClickListener { viewModel.addS001(true)}
        binding.btn3.setOnClickListener { viewModel.addS002(true)}
        binding.btn4.setOnClickListener { viewModel.addS000(false)}
        binding.btn5.setOnClickListener { viewModel.addS001(false)}
        binding.btn6.setOnClickListener { viewModel.addS002(false)}*/

        binding.filterAllStatus.subFab.setOnClickListener {
            viewModel.filterAllStatus()
            closeFab()
        }
        binding.filterActiveStatus.subFab.setOnClickListener {
            viewModel.filterActiveStatus()
            closeFab()
        }
        binding.filterInactiveStatus.subFab.setOnClickListener {
            viewModel.filterInctiveStatus()
            closeFab()
        }

        binding.fabMain.setOnClickListener{
            if(!fabOpened)
                openFab()
            else
                closeFab()
        }


        return root
    }

    fun openFab(){
        binding.filterAllStatus.all.visibility = View.VISIBLE
        binding.filterActiveStatus.all.visibility = View.VISIBLE
        binding.filterInactiveStatus.all.visibility = View.VISIBLE

        binding.filterAllStatus.all.startAnimation(fromBottom)
        binding.filterActiveStatus.all.startAnimation(fromBottom)
        binding.filterInactiveStatus.all.startAnimation(fromBottom)

        binding.fabMain.startAnimation(rotateOpen)

        fabOpened=true
    }
    fun closeFab(){
        binding.filterAllStatus.all.visibility = View.INVISIBLE
        binding.filterActiveStatus.all.visibility = View.INVISIBLE
        binding.filterInactiveStatus.all.visibility = View.INVISIBLE

        binding.filterAllStatus.all.startAnimation(toBottom)
        binding.filterActiveStatus.all.startAnimation(toBottom)
        binding.filterInactiveStatus.all.startAnimation(toBottom)

        binding.fabMain.startAnimation(rotateClose)

        fabOpened=false
    }


}