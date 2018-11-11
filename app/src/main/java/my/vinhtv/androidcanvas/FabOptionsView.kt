package my.vinhtv.androidcanvas

import android.content.Context
import android.graphics.drawable.Animatable
import android.support.design.widget.FloatingActionButton
import android.transition.ChangeBounds
import android.transition.ChangeTransform
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton

class FabOptionsView: FrameLayout, View.OnClickListener {

    private val mFab: FloatingActionButton
    private val mButtonContainer: ViewGroup
    private val mBackground: View

    private var isOpen: Boolean = false

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, -1)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.faboptions_layout, this, true)
        mButtonContainer = findViewById(R.id.faboptions_button_container)
        mBackground = findViewById(R.id.faboptions_background)
        mFab = findViewById(R.id.faboptions_fab)
        animateButtons(false)
        mFab.setOnClickListener {
            toggle()
        }

        val count = mButtonContainer.childCount-1
        val separatorPos = count/2
        for(i in 0..count) {
            if (i == separatorPos) continue
            mButtonContainer.getChildAt(i).setOnClickListener(this)
        }
    }

    override fun onClick(view: View) {
        val drawable = (view as ImageButton).drawable
        if(drawable is Animatable) drawable.start()
    }

    private fun toggle() {
        isOpen = !isOpen
        val state = if (isOpen) 1 else -1
        val stateSet = intArrayOf(android.R.attr.state_checked * state)
        mFab.setImageState(stateSet, true)
        val transition = if(isOpen) OpenMorphTransition(mButtonContainer) else CloseMorphTransition(mButtonContainer)
        TransitionManager.beginDelayedTransition(this, transition)
        animateBackground(isOpen)
        animateButtons(isOpen)
    }

    private fun animateBackground(isOpening: Boolean) {
        val layoutParams = mBackground.layoutParams
        layoutParams.width = if(isOpening) mButtonContainer.width else 0
        mBackground.layoutParams = layoutParams
    }

    private fun animateButtons(isOpening: Boolean) {
        val count = mButtonContainer.childCount-1
        val separatorPos = count/2
        for(i in 0..count) {
            if(i == separatorPos) continue
            val button = mButtonContainer.getChildAt(i)
            button.scaleX = if(isOpening) 1.0f else 0.0f
            button.scaleY = if(isOpening) 1.0f else 0.0f
        }
    }

    private class OpenMorphTransition(viewGroup: ViewGroup): TransitionSet() {
        init {
            excludeChildren(R.id.faboptions_fab, true)
            val changeBounds = ChangeBounds()
            addTransition(changeBounds)

            val changeTransform = ChangeTransform()
            for (i in 0 until viewGroup.childCount) changeTransform.addTarget(viewGroup.getChildAt(i))
            addTransition(changeTransform)
            ordering = TransitionSet.ORDERING_SEQUENTIAL
        }
    }

    private class CloseMorphTransition(viewGroup: ViewGroup): TransitionSet() {
        init {
            excludeChildren(R.id.faboptions_fab, true)
            val changeBounds = ChangeBounds()
            addTransition(changeBounds)

            val changeTransform = ChangeTransform()
            for (i in 0 until viewGroup.childCount) changeTransform.addTarget(viewGroup.getChildAt(i))
            addTransition(changeTransform)
            ordering = TransitionSet.ORDERING_TOGETHER
        }
    }
}