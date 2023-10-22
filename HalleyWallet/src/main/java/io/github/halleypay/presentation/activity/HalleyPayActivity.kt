package io.github.halleypay.presentation.activity

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Matrix
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.card.MaterialCardView
import io.github.halleypay.R
import io.github.halleypay.core.ui.BaseActivity
import io.github.halleypay.core.utils.HorizontalMarginItemDecoration
import io.github.halleypay.domain.entity.CardModel
import io.github.halleypay.presentation.adapter.RcvCardsBankAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs


class HalleyPayActivity : BaseActivity() {

    private lateinit var rcvCardsBankAdapter: RcvCardsBankAdapter

    private val durationAnimation by lazy {
        resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
    }

    private lateinit var imageSecond: ImageView
    private lateinit var imvPhone: ImageView
    private lateinit var imvTickDown: ImageView
    private lateinit var cvStatusPayment: MaterialCardView
    private lateinit var clScreen1: ConstraintLayout
    private lateinit var clScreen2: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_halley_pay)

        initToolbar()
        initTextView()
        initImageView()
        initLayouts()
        setupCarouselForPortrait()
//        initAnim()
//        animRotate()
    }

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitleTextAppearance(this@HalleyPayActivity, R.style.toolbarStyle)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = getString(R.string.select_card)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun initTextView() {
        findViewById<TextView>(R.id.lblSelectCard).typeface =
            auxiliaryFunctionsManager.getTypefaceNormal(this)
        findViewById<TextView>(R.id.lblDescription).typeface =
            auxiliaryFunctionsManager.getTypefaceNormal(this)
    }

    private fun initImageView() {
        imageSecond = findViewById(R.id.imageSecond)
        imvPhone = findViewById(R.id.imvPhone)
        imvTickDown = findViewById(R.id.imvTickDown)
    }

    private fun initLayouts() {
        clScreen1 = findViewById(R.id.clScreen1)
        clScreen2 = findViewById(R.id.clScreen2)
        cvStatusPayment = findViewById(R.id.cvStatusPayment)
    }

    @SuppressLint("SuspiciousIndentation")
    private fun setupCarouselForPortrait() {
        val viewPagerCard = findViewById<ViewPager2>(R.id.viewPagerCard)
        rcvCardsBankAdapter = RcvCardsBankAdapter(this)
        viewPagerCard.offscreenPageLimit = 1
        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx =
            resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)

        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx

        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            page.scaleY = 1 - 0.25f * abs(position)
            page.alpha = 0.50f + (1 - abs(position))
        }
        viewPagerCard.setPageTransformer(pageTransformer)
        val itemDecoration = HorizontalMarginItemDecoration(
            this, R.dimen.viewpager_current_item_horizontal_margin,
            R.dimen.viewpager_current_item_horizontal_margin
        )
        viewPagerCard.addItemDecoration(itemDecoration)
        viewPagerCard.adapter = rcvCardsBankAdapter

        val cardsBank = mutableListOf(CardModel(1), CardModel(2), CardModel(3))
        rcvCardsBankAdapter.submitList(cardsBank)

        rcvCardsBankAdapter.setOnItemClickListener(object :
            RcvCardsBankAdapter.OnCardClickListener {
            override fun onClick(cardModel: CardModel, position: Int) {
                changeScreen(position)
            }
        })

//        findViewById<CircleIndicator3>(R.id.indicator).setViewPager(viewPagerCard)
        viewPagerCard.currentItem = 1
    }

    private fun changeScreen(position: Int) {

        val transition: Transition = Fade()

        transition.duration = durationAnimation
        transition.addTarget(clScreen1)
        transition.addTarget(clScreen2)

        TransitionManager.beginDelayedTransition(clScreen1.parent as ViewGroup, transition)
        TransitionManager.beginDelayedTransition(clScreen2.parent as ViewGroup, transition)
        clScreen1.visibility = View.INVISIBLE
        clScreen2.visibility = View.VISIBLE
        supportActionBar?.title = getString(R.string.connect_to_pos)

        lifecycleScope.launch {
            delay(durationAnimation)
            visibleImagePhone()
            delay(2000)
//            invisibleImagePhone()
//            delay(durationAnimation)
//            visibleImageDown()
        }

        when (position) {
            0 -> {
                imageSecond.setImageResource(R.mipmap.card1)
            }

            1 -> {
                imageSecond.setImageResource(R.mipmap.card2)
            }

            2 -> {
                imageSecond.setImageResource(R.mipmap.card3)
            }
        }
    }

    private fun visibleImagePhone() {
        val transitionSlide: Transition = Slide()
        transitionSlide.duration = durationAnimation
        transitionSlide.addTarget(imvPhone)
        TransitionManager.beginDelayedTransition(
            imvPhone.parent as ViewGroup,
            transitionSlide
        )
        imvPhone.visibility = View.VISIBLE
        cvStatusPayment.strokeColor = ContextCompat.getColor(
            this@HalleyPayActivity,
            R.color.red_color
        )
    }

    private fun invisibleImagePhone() {
        val transitionSlide: Transition = Slide()
        transitionSlide.duration = durationAnimation
        transitionSlide.addTarget(imvPhone)
        TransitionManager.beginDelayedTransition(
            imvPhone.parent as ViewGroup,
            transitionSlide
        )
        imvPhone.visibility = View.INVISIBLE
    }

    private fun visibleImageDown() {
        val transitionSlide: Transition = Slide(Gravity.TOP)
        transitionSlide.duration = durationAnimation
        transitionSlide.addTarget(imvTickDown)
        TransitionManager.beginDelayedTransition(
            imvTickDown.parent as ViewGroup,
            transitionSlide
        )
        imvTickDown.visibility = View.VISIBLE
        cvStatusPayment.strokeColor = ContextCompat.getColor(
            this@HalleyPayActivity,
            R.color.color_green
        )
    }

    private fun invisibleImageDown() {
        val transitionSlide: Transition = Slide(Gravity.TOP)
        transitionSlide.duration = durationAnimation
        transitionSlide.addTarget(imvTickDown)
        TransitionManager.beginDelayedTransition(
            imvTickDown.parent as ViewGroup,
            transitionSlide
        )
        imvTickDown.visibility = View.INVISIBLE
    }

    private fun initAnim() {
        val imageAnim = findViewById<ImageView>(R.id.imageAnim)
        val dWidth = imageAnim.drawable.intrinsicWidth
        val dHeight = imageAnim.drawable.intrinsicHeight

        val vWidth = imageAnim.measuredWidth
        val vHeight = imageAnim.measuredHeight
//
//        val imageMatrix = Matrix()
////            .apply {
////            setTranslate(
////                round((vWidth - dWidth) * 0.5f),
////                round((vHeight - dHeight) * 0.5f)
////            )
////            setSkew(1f, 0f, dWidth / 2f, dHeight / 2f)
////        }
//
//        imageMatrix.postSkew(1f, 0f, dWidth / 2f, dHeight / 2f)
        val matrix = Matrix()

        val skewAnimator =
            ValueAnimator.ofFloat(
                0f,
                -0.3f,
                0f
            )

        skewAnimator.duration = 3000

        skewAnimator.addUpdateListener { animation ->
            val skewAmount = animation.animatedValue as Float
            matrix.reset()
            val imageCenterX: Float = (imageAnim.width / 2).toFloat()
            val imageCenterY: Float = (imageAnim.height / 2).toFloat()
            matrix.postTranslate(-imageCenterX, -imageCenterY)
            matrix.postSkew(skewAmount, 0F)
            matrix.postTranslate(imageCenterX, imageCenterY)
            imageAnim.imageMatrix = matrix
        }
        skewAnimator.start()
    }

    private fun animRotate() {
        val imvCard = findViewById<ImageView>(R.id.imageAnim)
        val rotationAnimator = ObjectAnimator.ofFloat(
            imvCard,
            "rotation",
            0f,
            360f
        )
        rotationAnimator.duration = 1000
        rotationAnimator.start()
    }
}