package dev.fummicc1.lit.snacker.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import dev.fummicc1.lit.snacker.R
import dev.fummicc1.lit.snacker.activities.AppIntroActivity
import dev.fummicc1.lit.snacker.activities.ManageTagActivity
import dev.fummicc1.lit.snacker.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding.apply {
            settingTagInclude.settingItemTextView.text = "タグの管理"
            settingTagInclude.settingItemView.setOnClickListener {
                Intent(requireContext(), ManageTagActivity::class.java).apply {
                    requireActivity().startActivity(this)
                }
            }

            checkWalkthroughInclude.settingItemTextView.text = "使い方"
            checkWalkthroughInclude.settingItemView.setOnClickListener {
                Intent(requireContext(), AppIntroActivity::class.java).apply {
                    requireActivity().startActivity(this)
                }
            }

            manageSnackLimitationInclude.settingItemTextView.text = "未読ボックスの管理"
            manageSnackLimitationInclude.settingItemView.setOnClickListener {
                val editText = EditText(requireContext())
                editText.inputType = InputType.TYPE_CLASS_NUMBER
                AlertDialog.Builder(requireContext())
                    .setTitle("数字を入力してください")
                    .setMessage("未読ボックスに表示される記事の個数を設定できます")
                    .setView(editText)
                    .setPositiveButton("決定", { dialog, which ->
                        try {
                            val count = Integer.parseInt(editText.text.toString())
                            val prefs = requireActivity().getSharedPreferences("snacker_shared_preferences", Context.MODE_PRIVATE)
                            prefs.edit().apply {
                                putInt("snack_display_limit_count", count)
                                apply()
                            }
                        } catch (e: Exception) {
                            Log.e("SettingsFragment", e.toString())
                        }
                    })
                    .setNegativeButton("キャンセル", { _, _ -> })
                    .show()
            }
        }
        return binding.root
    }
}