package com.example.mycontacts.ui.onboarding_screen

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.mycontacts.R
import com.example.mycontacts.databinding.FragmentOnboardingBinding
import com.example.mycontacts.ui.details.Action
import com.example.mycontacts.ui.details.HasCustomActionToolbar
import com.example.mycontacts.ui.details.HasCustomTitleToolbar
import com.example.mycontacts.ui.details.HasNotBottomNavigationBar
import com.example.mycontacts.ui.navigation.navigator
import com.example.mycontacts.ui.onboarding_screen.utils.CarouselVPAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class OnBoardingFragment : Fragment(), HasCustomTitleToolbar, HasCustomActionToolbar, HasNotBottomNavigationBar {

    private lateinit var binding: FragmentOnboardingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val demoData = arrayListOf(
            getString(R.string.onboarding1),
            getString(R.string.onboarding2),
            getString(R.string.onboarding3),
            getString(R.string.onboarding4),
            getString(R.string.onboarding5),
        )

        setupCarousel(binding.carousel, demoData)
    }

    private fun setupCarousel(carousel: ViewPager2, data: ArrayList<String>) {
        carousel.adapter = CarouselVPAdapter(data)

        carousel.offscreenPageLimit = 3

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer((20 * Resources.getSystem().displayMetrics.density).toInt()))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = (0.80f + r * 0.20f)
        }
        carousel.setPageTransformer(compositePageTransformer)
    }

    override fun getCustomAction(): Action {
        return Action(R.drawable.ic_next_white, R.string.next) {
            with(binding.carousel) {
                if (currentItem ==  (adapter?.itemCount ?: 0) - 1)
                    navigator().launchContactListScreen()
                else
                    setCurrentItem(++currentItem, true)
            }
        }
    }

    override fun getTitle(): Int = R.string.onboarding_title
}