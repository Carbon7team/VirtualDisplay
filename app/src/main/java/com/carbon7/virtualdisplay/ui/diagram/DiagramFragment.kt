package com.carbon7.virtualdisplay.ui.diagram

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.allViews
import androidx.fragment.app.viewModels
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.databinding.FragmentDiagramBinding
import com.carbon7.virtualdisplay.ui.UpsDataVisualizerFragment
import com.carbon7.virtualdisplay.ui.diagram.DiagramViewModel.State
import com.carbon7.virtualdisplay.ui.diagram.DiagramViewModel.OutputState

class DiagramFragment : UpsDataVisualizerFragment() {

    override val viewModel: DiagramViewModel by viewModels()
    private var _binding: FragmentDiagramBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiagramBinding.inflate(inflater, container, false)
        setupView()
        return binding.root

    }

    private fun stateToColor(state: State): Int{
        return when(state){
            State.ON -> R.color.green_s
            State.OFF -> R.color.grey_s
            State.PREV -> R.color.yellow_s
            State.CRITIC -> R.color.red_s
            State.SYMBOL -> R.color.dark_grey_s
            State.ENABLE-> R.color.black_s
            State.DISABLE -> R.color.black_grey_s
        }
    }

    private fun outputStateToColor(state: OutputState): Int{
        return when(state){
            OutputState.ON_INV -> R.color.green_s
            OutputState.ON_ECO -> R.color.green_s
            OutputState.OFF -> R.color.grey_s
            OutputState.PREV_BAT -> R.color.yellow_s
            OutputState.PREV_BYP -> R.color.yellow_s
        }
    }

    private fun setupView(){

        //recInput
        viewModel.recInputState.observe(viewLifecycleOwner) {
            binding.recInput.setColorFilter(stateToColor(it))
        }
        //recState
        viewModel.recState.observe(viewLifecycleOwner){
            binding.rec.setImageResource(
                when(it){
                    State.CRITIC->R.drawable.rec_critic
                    State.PREV->R.drawable.rec_prev
                    State.SYMBOL->R.drawable.rec_normal
                    else -> R.drawable.rec_normal //TODO: Gestire errore
                }
            )
        }
        //dcInput
        viewModel.dcInputState.observe(viewLifecycleOwner) {
            binding.dcInput.setColorFilter(stateToColor(it))
        }
        //dcBus
        viewModel.dcBusState.observe(viewLifecycleOwner) {
            binding.dcBus.setColorFilter(stateToColor(it))
        }
        //dcOutput
        viewModel.dcOutputState.observe(viewLifecycleOwner) {
            binding.dcOutput.setColorFilter(stateToColor(it))
        }
        //invinput
        viewModel.invInputState.observe(viewLifecycleOwner) {
            binding.invInput.setColorFilter(stateToColor(it))
        }
        //inv
        viewModel.invState.observe(viewLifecycleOwner){
            binding.inv.setImageResource(
                when(it){
                    State.CRITIC->R.drawable.inv_critic
                    State.PREV->R.drawable.inv_prev
                    State.SYMBOL->R.drawable.inv_normal
                    else -> R.drawable.inv_normal //TODO: Gestire errore
                }
            )
        }
        //invOutput
        viewModel.invOutputState.observe(viewLifecycleOwner) {
            binding.invOutput.allViews.forEach { view ->
                (view as ImageView).setColorFilter(stateToColor(it))
            }
        }
        //Output
        viewModel.outputState.observe(viewLifecycleOwner) {
            binding.output!!.allViews.forEach { view ->
                (view as ImageView).setColorFilter(outputStateToColor(it))
            }

        }
        //bypInput
        viewModel.bypInputState.observe(viewLifecycleOwner) {
            binding.bypInput.setColorFilter(stateToColor(it))
        }
        //byp
        viewModel.bypState.observe(viewLifecycleOwner){
            binding.byp.setImageResource(
                when(it){
                    State.CRITIC->R.drawable.byp_critic
                    State.PREV->R.drawable.byp_prev
                    State.SYMBOL->R.drawable.byp_normal
                    else -> R.drawable.byp_normal //TODO: Gestire errore
                }
            )
        }
        //bypOutput
        viewModel.bypOutputState.observe(viewLifecycleOwner) {
            binding.bypOutput.allViews.forEach { view ->
                (view as ImageView).setColorFilter(stateToColor(it))
            }
        }

        //loadStatus
        viewModel.loadStatus.observe(viewLifecycleOwner){
            binding.load.setImageResource(
                when(it){
                    State.SYMBOL->R.drawable.byp_normal
                    else -> R.drawable.byp_normal //TODO: Gestire errore
                }
            )
        }

        //loadLevel
        viewModel.loadValue.observe(viewLifecycleOwner) {
            binding.loadDescription!!.text = it
        }

        //loadValueStatus
        viewModel.loadValueStatus.observe(viewLifecycleOwner){
            binding.load!!.setImageResource(
                when (it) {
                    5 -> R.drawable.l5
                    10 -> R.drawable.l10
                    15 -> R.drawable.l15
                    20 -> R.drawable.l20
                    25 -> R.drawable.l25
                    30 -> R.drawable.l30
                    35 -> R.drawable.l35
                    40 -> R.drawable.l40
                    45 -> R.drawable.l45
                    50 -> R.drawable.l50
                    55 -> R.drawable.l55
                    60 -> R.drawable.l65
                    65 -> R.drawable.l65
                    70 -> R.drawable.l70
                    75 -> R.drawable.l75
                    80 -> R.drawable.l80
                    85 -> R.drawable.l85
                    90 -> R.drawable.l90
                    95 -> R.drawable.l95
                    100 -> R.drawable.l100
                    105 -> R.drawable.l105
                    110 -> R.drawable.l110
                    115 -> R.drawable.l115
                    120 -> R.drawable.l120
                    else -> R.drawable.l100 //TODO: Gestire errore
                }
            )
        }

        //battState
        viewModel.batteryState.observe(viewLifecycleOwner){
            binding.batt.setImageResource(
                when(it){
                    State.CRITIC->R.drawable.bat_critic
                    State.PREV->R.drawable.bat_prev
                    State.SYMBOL->R.drawable.bat_normal
                    else -> R.drawable.bat_normal //TODO: Gestire errore
                }
            )
        }

        //battStatus
        viewModel.batteryStatus.observe(viewLifecycleOwner){
                when (it){
                    DiagramViewModel.BatteryStatus.DISCHARGE->{
                        binding.battStatus!!.setImageResource(R.drawable.discharge)
                        binding.battStatus!!.visibility = View.VISIBLE
                    }
                    DiagramViewModel.BatteryStatus.CHARGE->{
                        binding.battStatus!!.setImageResource(R.drawable.charge)
                        binding.battStatus!!.visibility = View.VISIBLE
                    }
                    else -> binding.battStatus!!.visibility = View.VISIBLE
                }
        }

        //batteryLevel
        viewModel.batteryLevelStatus.observe(viewLifecycleOwner){
            binding.battLevel!!.setImageResource(
                when (it) {
                    5 -> R.drawable.b5
                    10 -> R.drawable.b10
                    15 -> R.drawable.b15
                    20 -> R.drawable.b20
                    25 -> R.drawable.b25
                    30 -> R.drawable.b30
                    35 -> R.drawable.b35
                    40 -> R.drawable.b40
                    45 -> R.drawable.b45
                    50 -> R.drawable.b50
                    55 -> R.drawable.b55
                    60 -> R.drawable.b60
                    65 -> R.drawable.b65
                    70 -> R.drawable.b65
                    75 -> R.drawable.b75
                    80 -> R.drawable.b80
                    85 -> R.drawable.b85
                    90 -> R.drawable.b90
                    95 -> R.drawable.b95
                    100 -> R.drawable.b100
                    else -> R.drawable.b100 //TODO: Gestire errore
                }
            )
        }

        //battDescription
        viewModel.batteryDescription.observe(viewLifecycleOwner) {
            binding.batteryDescription!!.text = it
        }
    }

}