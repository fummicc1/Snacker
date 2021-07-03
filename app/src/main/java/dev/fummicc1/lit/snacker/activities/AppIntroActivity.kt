package dev.fummicc1.lit.snacker.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import dev.fummicc1.lit.snacker.R
import kotlinx.android.synthetic.main.fragment_add_snack.*

class AppIntroActivity : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addSlide(AppIntroFragment.newInstance(
            title = "STEP0. Snackerとは",
            description = "Snackerは積読をたくさんできるアプリです",
            imageDrawable = R.drawable.walkthrough_first_page,
            backgroundColor = resources.getColor(R.color.brown)
        ))

        addSlide(AppIntroFragment.newInstance(
            title = "STEP1. 記事を追加登録する",
            description = "Snackerはアプリ内に限らず、Google Chromeから共有機能を通して記事を登録することもできます",
            imageDrawable = R.drawable.walkthrough_second_page,
            backgroundColor = resources.getColor(R.color.brown)
        ))

        addSlide(AppIntroFragment.newInstance(
            title = "STEP2. 記事を読む・再発見する",
            description = "アプリ内のブラウザで検索をすると、保存済みの関連記事が発見されます",
            imageDrawable = R.drawable.walkthrough_third_page,
            backgroundColor = resources.getColor(R.color.brown)
        ))
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        saveCompleteWalkthourghState()
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        saveCompleteWalkthourghState()
    }

    private fun saveCompleteWalkthourghState() {
        val prefs = getSharedPreferences("snaker_prefs", MODE_PRIVATE)
        prefs.edit {
            putBoolean("show_walkthrough", false)
            commit()
            finish()
        }
    }
}