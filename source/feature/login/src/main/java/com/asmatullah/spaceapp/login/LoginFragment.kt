package com.asmatullah.spaceapp.login

import com.asmatullah.spaceapp.common.core.ui.mvvm.BaseMvvmFragment
import com.asmatullah.spaceapp.common.core.util.Constants.FEATURE_MIN_VALUE
import com.asmatullah.spaceapp.common.core.util.Constants.FEATURE_TOTAL_MAX_VALUE
import com.asmatullah.spaceapp.common.uikit.widget.CustomSeekbar
import com.asmatullah.spaceapp.login.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class LoginFragment :
    BaseMvvmFragment<LoginViewModel, FragmentLoginBinding>(R.layout.fragment_login) {

    override val viewModel: LoginViewModel by viewModel { parametersOf(requireActivity() as LoginContract.Navigator) }

    override fun configureUI() {
        onActivityBackPressed()
        updateTotalScore()
        initSeekBars()
        setupViewModel()
    }

    private fun setupViewModel() {
        super.setupViewModel(viewModel)
    }

    private fun initSeekBars() {
        seekBarStrength.setOnSeekBarChangeListener { _, _ ->
            seekBarStrength.verifySeekBars(
                seekBarSpeed,
                seekBarCapacity
            )
        }
        seekBarSpeed.setOnSeekBarChangeListener { _, _ ->
            seekBarSpeed.verifySeekBars(
                seekBarCapacity,
                seekBarStrength
            )
        }
        seekBarCapacity.setOnSeekBarChangeListener { _, _ ->
            seekBarCapacity.verifySeekBars(
                seekBarStrength,
                seekBarSpeed
            )
        }
    }

    private fun CustomSeekbar.verifySeekBars(seekBar1: CustomSeekbar, seekBar2: CustomSeekbar) {
        var sum = getProgress() + seekBar1.getProgress() + seekBar2.getProgress()
        while (sum > FEATURE_TOTAL_MAX_VALUE) {
            if (seekBar1.getProgress() > FEATURE_MIN_VALUE)
                seekBar1.increaseProgressBy(-1)
            else if (seekBar2.getProgress() > FEATURE_MIN_VALUE)
                seekBar2.increaseProgressBy(-1)
            sum--;
        }
        updateTotalScore()
    }

    private fun updateTotalScore() {
        val sum =
            seekBarSpeed.getProgress() + seekBarStrength.getProgress() + seekBarCapacity.getProgress()
        tvScoreNumber.text = getString(R.string.formatted_out_of, sum, FEATURE_TOTAL_MAX_VALUE)
    }
}