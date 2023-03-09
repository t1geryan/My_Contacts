package com.example.mycontacts.ui.onboarding_screen

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.mycontacts.databinding.FragmentOnboardingBinding
import com.example.mycontacts.ui.onboarding_screen.utils.CarouselRVAdapter
import kotlin.math.abs

// todo
// hide bottom navigation bar
// by creating HasNotBottomNavigationBar interface
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
            "Приложение My Contacts позволит вам хранить список своих контактов в удобном виде",
            "Вы можете добавлять контакты вручную или синхронизировать со списком ваших контактов на телефоне",
            "Вы можете добавлять контакты в избранные",
            "Вы можете легко осуществить звонок, нажав на номер контакта",
            "В разделе Me вы можете посмотреть статистику о себе",
        )

        binding.carousel.adapter = CarouselRVAdapter(demoData)

        binding.carousel.apply {
            clipChildren = false  // No clipping the left and right items
            clipToPadding = false  // Show the viewpager in full width without clipping the padding
            offscreenPageLimit = 3  // Render the left and right items
        }

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer((20 * Resources.getSystem().displayMetrics.density).toInt()))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = (0.80f + r * 0.20f)
        }
        binding.carousel.setPageTransformer(compositePageTransformer)
    }
}