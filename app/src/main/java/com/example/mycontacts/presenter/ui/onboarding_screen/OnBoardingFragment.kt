package com.example.mycontacts.presenter.ui.onboarding_screen

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.mycontacts.R
import com.example.mycontacts.databinding.FragmentOnboardingBinding
import com.example.mycontacts.presenter.contract.Action
import com.example.mycontacts.presenter.contract.HasCustomActionToolbar
import com.example.mycontacts.presenter.ui.onboarding_screen.adapter.CarouselVPAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class OnBoardingFragment : Fragment(), HasCustomActionToolbar {

    private lateinit var binding: FragmentOnboardingBinding

    private lateinit var adapter: CarouselVPAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CarouselVPAdapter(resources.getStringArray(R.array.onboarding_texts), ::toNextItem)

        setupCarousel()
    }

    private fun setupCarousel() {
        with(binding.carousel) {
            adapter = this@OnBoardingFragment.adapter

            offscreenPageLimit = 3

            val compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.addTransformer(MarginPageTransformer((20 * Resources.getSystem().displayMetrics.density).toInt()))
            compositePageTransformer.addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = (0.80f + r * 0.20f)
            }
            setPageTransformer(compositePageTransformer)
        }
    }

    private fun toNextItem() {
        if (binding.carousel.currentItem != (adapter.itemCount) - 1) binding.carousel.setCurrentItem(
            ++binding.carousel.currentItem, true
        )
    }

    override fun getCustomActionsList(): List<Action> =
        listOf(Action(R.drawable.ic_next_white, R.string.next) {
            if (binding.carousel.currentItem == adapter.itemCount - 1) findNavController().navigate(
                R.id.action_onBoardingFragment_to_tabsFragment
            )
            else toNextItem()
        })

}