package dev.fummicc1.lit.snacker.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dev.fummicc1.lit.snacker.R
import dev.fummicc1.lit.snacker.activities.AddSnackActivity
import dev.fummicc1.lit.snacker.databinding.FragmentAddSnackBinding
import dev.fummicc1.lit.snacker.databinding.ViewSnackTagChipBinding
import dev.fummicc1.lit.snacker.models.AddSnackPreset
import dev.fummicc1.lit.snacker.models.SnackTagKindPresentable
import dev.fummicc1.lit.snacker.viewmodels.AddSnackViewModel
import kotlinx.android.synthetic.main.fragment_add_snack.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class AddSnackFragment : Fragment(), CoroutineScope {

    private val viewModel: AddSnackViewModel by viewModels()
    private lateinit var binding: FragmentAddSnackBinding

    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddSnackBinding.inflate(layoutInflater, container, false)

        // Binding
        binding.apply {
            urlEditText.addTextChangedListener { url ->
                viewModel.updateURL(url.toString())
            }

            titleEditText.addTextChangedListener { title ->
                viewModel.updateTitle(title.toString())
            }

            firstLevelButton.setOnClickListener {
                handleInterestLevelSelection(1)
                viewModel.updateLevel(1)
            }

            secondLevelButton.setOnClickListener {
                handleInterestLevelSelection(2)
                viewModel.updateLevel(2)
            }

            thirdLevelButton.setOnClickListener {
                handleInterestLevelSelection(3)
                viewModel.updateLevel(3)
            }

            fourthLevelButton.setOnClickListener {
                handleInterestLevelSelection(4)
                viewModel.updateLevel(4)
            }

            fifthLevelButton.setOnClickListener {
                handleInterestLevelSelection(5)
                viewModel.updateLevel(5)
            }

            val addSnackPreset =
                requireActivity().intent.getParcelableExtra<AddSnackPreset>(AddSnackActivity.addSnackPresetField)
            if (addSnackPreset == null) {
                prepareInitialState()
            } else {
                injectSnackPreset(addSnackPreset)
            }

            // Handle Intent
            val intent = requireActivity().intent
            when {
                intent.action == Intent.ACTION_SEND -> {
                    if ("text/plain" == intent.type) {
                        val url = intent.getStringExtra(Intent.EXTRA_TEXT)
                        url?.let {
                            val preset = viewModel.getAddSnackPresetFromUrl(it)
                            injectSnackPreset(preset)
                        }
                    }
                }
            }

            addChipButton.setOnClickListener {
                val editText = EditText(requireContext()).apply {
                    maxLines = 1
                }
                AlertDialog.Builder(requireContext())
                    .setTitle("タグの追加")
                    .setMessage("タグの名前を入力")
                    .setView(editText)
                    .setPositiveButton("完了", { dialog, which ->
                        val name = editText.text.toString()
                        viewModel.addSnackTag(name)
                    })
                    .setNegativeButton("キャンセル", { _, _ -> })
                    .show()
            }

            snackTagChipGroupToAdd.setOnCheckedChangeListener { group, checkedId ->
                viewModel.selectSnackTagPresentableId(checkedId)
            }
        }

        // ViewModel
        viewModel.completeAddition.observe(viewLifecycleOwner, Observer {
            showCompleteMessage()
        })
        viewModel.inputError.observe(viewLifecycleOwner, Observer { error ->
            when (error.type) {
                AddSnackViewModel.InputError.InputType.URL -> {
                    binding.urlEditText.error = error.message
                }
                AddSnackViewModel.InputError.InputType.TITLE -> {
                    binding.titleEditText.error = error.message
                }
                AddSnackViewModel.InputError.InputType.INTERESTLEVEL -> {
                    binding.priorityLevelTextView.error = error.message
                }
            }
        })
        viewModel.availableTags.observe(viewLifecycleOwner, Observer { chips ->
            snackTagChipGroupToAdd.children.mapNotNull {
                it as? Chip
            }.map { it.id }.let { seq ->
                chips.filter { seq.contains(it.id).not() }
            }.apply {
                forEach {
                    addChipToGroup(it)
                }
            }
        })

        // initial setup
        configureToolbar()

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.create_snack_button -> {
                viewModel.createSnack()
                return true
            }
            android.R.id.home -> {
                requireActivity().finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addChipToGroup(snackTagKindPresentable: SnackTagKindPresentable) {
        ViewSnackTagChipBinding.inflate(layoutInflater, null, false).apply {
            this.tagName = snackTagKindPresentable.name
            (root as? Chip)?.let { chip ->
                chip.id = snackTagKindPresentable.id
                chip.isCheckable = true
                chip.isChecked = false
                chip.isCloseIconVisible = false
                chip.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        viewModel.selectSnackTagPresentableId(snackTagKindPresentable.id)
                    } else {
                        viewModel.deselectTag(snackTagKindPresentable)
                    }
                }
            }
        }.apply {
            binding.snackTagChipGroupToAdd.addView(this.root)
        }
    }

    private fun showCompleteMessage() {
        Snackbar.make(requireContext(), binding.root, "登録完了", Snackbar.LENGTH_SHORT).show()
        requireActivity().finish()
    }

    private fun configureToolbar() {
        setHasOptionsMenu(true)
    }

    private fun prepareInitialState() {
        binding.urlEditText.setText("")
        binding.titleEditText.setText("")
        handleInterestLevelSelection(0)
    }

    private fun handleInterestLevelSelection(level: Int) {
        val buttons: List<ImageButton> = listOf(
            binding.firstLevelButton,
            binding.secondLevelButton,
            binding.thirdLevelButton,
            binding.fourthLevelButton,
            binding.fifthLevelButton
        )

        for (index in 0 until buttons.size) {
            if (level > index) {
                buttons[index].alpha = 1f
            } else {
                buttons[index].alpha = 0.3f
            }
        }
    }

    fun injectSnackPreset(preset: AddSnackPreset) {
        binding.apply {
            urlEditText.setText(preset.url)
            titleEditText.setText(preset.title)
            viewModel.updateLevel(preset.priority)
            handleInterestLevelSelection(preset.priority)
        }
    }
}