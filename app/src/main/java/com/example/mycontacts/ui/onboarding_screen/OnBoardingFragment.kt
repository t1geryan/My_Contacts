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
import com.example.mycontacts.ui.onboarding_screen.utils.CarouselRVAdapter
import kotlin.math.abs

class OnBoardingFragment : Fragment() {

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
        carousel.adapter = CarouselRVAdapter(data)

        carousel.offscreenPageLimit = 3

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer((20 * Resources.getSystem().displayMetrics.density).toInt()))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = (0.80f + r * 0.20f)
        }
        carousel.setPageTransformer(compositePageTransformer)
    }
}