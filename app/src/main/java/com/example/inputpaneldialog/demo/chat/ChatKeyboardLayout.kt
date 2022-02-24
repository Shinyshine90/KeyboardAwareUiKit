package com.example.inputpaneldialog.demo.chat

import android.content.Context
import android.util.ArrayMap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.candy.keyboard_aware.KeyboardAwareLayout
import com.candy.keyboard_aware.entity.KeyboardBottomUi
import com.candy.keyboard_aware.entity.PanelUi
import com.candy.keyboard_aware.entity.createAdjustKeyboardPanel
import com.candy.keyboard_aware.entity.createExactlyHeightPanel
import com.example.inputpaneldialog.R
import com.example.inputpaneldialog.utils.dp2px
import com.example.inputpaneldialog.widget.LayoutAwareRecyclerView

class ChatKeyboardLayout constructor(context: Context, attrs: AttributeSet? = null) :
    KeyboardAwareLayout(context, attrs) {

    lateinit var emojiPanel: RecyclerView

    lateinit var functionPanel: View

    init {
        handleRecyclerViewScroll()
    }

    override fun createContentUi(): View = inflateView(R.layout.layout_chat_recycler_view)

    override fun createKeyboardBottomUi(): KeyboardBottomUi {

        emojiPanel = inflateView(R.layout.layout_panel_emoji) as RecyclerView

        functionPanel = inflateView(R.layout.layout_panel_function)

        val bottomPanelBg = ContextCompat.getColor(context, R.color.white_light)

        val bottomBar = inflateView(R.layout.layout_dialog_bottom_bar)

        val bottomPanelRegistrations = ArrayMap<Int, PanelUi>()

        bottomPanelRegistrations[R.id.btn_emoji] = createExactlyHeightPanel(
            emojiPanel, context.dp2px(360f)
        )

        bottomPanelRegistrations[R.id.btn_func] =
            createAdjustKeyboardPanel(functionPanel)

        return KeyboardBottomUi(bottomBar, bottomPanelBg, bottomPanelRegistrations)
    }


    private fun handleRecyclerViewScroll() {
        val awareRecyclerView: LayoutAwareRecyclerView = findViewById(R.id.rv_chat)
        registerBottomUiHeightChanged {
            awareRecyclerView.handleLayoutChange(it)
        }
    }

    private fun inflateView(id: Int) = LayoutInflater.from(context).inflate(id, null)

}