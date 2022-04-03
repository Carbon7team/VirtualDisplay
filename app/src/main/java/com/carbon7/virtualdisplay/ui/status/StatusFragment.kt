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
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.databinding.FragmentStatusBinding
import com.carbon7.virtualdisplay.model.Status

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


    private val rotateOpen: Animation by lazy {AnimationUtils.loadAnimation(context,R.anim.rotate_open_anim)}
    private val rotateClose: Animation by lazy {AnimationUtils.loadAnimation(context,R.anim.rotate_close_anim)}
    private val fromBottom: Animation by lazy {AnimationUtils.loadAnimation(context,R.anim.from_bottom_anim)}
    private val toBottom: Animation by lazy {AnimationUtils.loadAnimation(context,R.anim.to_botton_anim)}
    private var fabOpened : Boolean = false;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(StatusViewModel::class.java)

        viewModel.status.observe(viewLifecycleOwner,{
            Log.d("MyApp","UPDATE")
            Log.d("MyApp",it.toString())


            val recyclerViewState = binding.listStatus.layoutManager?.onSaveInstanceState()
            binding.listStatus.adapter = StatusAdapter(it)
            binding.listStatus.layoutManager?.onRestoreInstanceState(recyclerViewState)
        })



        _binding = FragmentStatusBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btn1.setOnClickListener { viewModel.addS000(true)}
        binding.btn2.setOnClickListener { viewModel.addS001(true)}
        binding.btn3.setOnClickListener { viewModel.addS002(true)}
        binding.btn4.setOnClickListener { viewModel.addS000(false)}
        binding.btn5.setOnClickListener { viewModel.addS001(false)}
        binding.btn6.setOnClickListener { viewModel.addS002(false)}

        binding.fab1.subFab.setOnClickListener { Toast.makeText(context,"FAB1",Toast.LENGTH_SHORT).show()}
        binding.fab2.subFab.setOnClickListener { Toast.makeText(context,"FAB2",Toast.LENGTH_SHORT).show()}
        binding.fab3.subFab.setOnClickListener { Toast.makeText(context,"FAB3",Toast.LENGTH_SHORT).show()}

        binding.fabMain.setOnClickListener{
            if(!fabOpened){
                binding.fab1.all.visibility = View.VISIBLE
                binding.fab2.all.visibility = View.VISIBLE
                binding.fab3.all.visibility = View.VISIBLE

                binding.fab1.all.startAnimation(fromBottom)
                binding.fab2.all.startAnimation(fromBottom)
                binding.fab3.all.startAnimation(fromBottom)

                binding.fabMain.startAnimation(rotateOpen)
            }else{
                binding.fab1.all.visibility = View.INVISIBLE
                binding.fab2.all.visibility = View.INVISIBLE
                binding.fab3.all.visibility = View.INVISIBLE

                binding.fab1.all.startAnimation(toBottom)
                binding.fab2.all.startAnimation(toBottom)
                binding.fab3.all.startAnimation(toBottom)

                binding.fabMain.startAnimation(rotateClose)

            }
            fabOpened=!fabOpened
        }


        return root
    }

}